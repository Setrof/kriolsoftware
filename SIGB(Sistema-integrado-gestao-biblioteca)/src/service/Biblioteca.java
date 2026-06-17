package src.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import src.Database.EmprestimoDAO;
import src.Database.LivroDAO;
import src.Database.UtilizadorDAO;
import src.model.Emprestimo;
import src.model.Livro;
import src.model.Utilizador;
import src.structures.Fila;
import src.structures.ListaLigada;
import src.structures.No;
import src.structures.Pilha;

public class Biblioteca {

     private ListaLigada<Livro> livros;
     private ListaLigada<Utilizador> utilizadores;
     private ListaLigada<Emprestimo> emprestimos;

     private Fila<String> filaDeEsperaLivros; 
     private Pilha<String> historicoOperacoes; 

     private LivroDAO livroDAO;
     private UtilizadorDAO utilizadorDAO;
     private EmprestimoDAO emprestimoDAO;
     
     private DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd-MM-yyyy");

     public Biblioteca() {
        this.livroDAO = new LivroDAO();
        this.utilizadorDAO = new UtilizadorDAO();
        this.emprestimoDAO = new EmprestimoDAO();

        this.filaDeEsperaLivros = new Fila<>();
        this.historicoOperacoes = new Pilha<>();

        recarregarDados();
        historicoOperacoes.empilhar("Sistema e tabelas sincronizados.");
     }

     public final void recarregarDados(){
        this.livros = livroDAO.listarTodos();
        this.utilizadores = utilizadorDAO.listarTodos();
        this.emprestimos = emprestimoDAO.listarTodos();
     }
     
     public boolean cadastrarLivro(Livro livro) {
        if (livroDAO.inserir(livro)) {
            recarregarDados();
            historicoOperacoes.empilhar("Livro cadastrado: " + livro.getTitulo());
            return true;
        }
        return false;
    }

    public boolean cadastrarUtilizador(Utilizador user) {
        if (utilizadorDAO.inserir(user)) {
            recarregarDados();
            historicoOperacoes.empilhar("Utilizador cadastrado: " + user.getNome());
            return true;
        }
        return false;
    }

    public boolean eliminarLivro(String isbn) {
        if (livroDAO.remover(isbn)) {
            recarregarDados();
            historicoOperacoes.empilhar("Livro removido: ISBN " + isbn);
            return true;
        }
        return false;
    }

    public boolean eliminarUtilizador(int id) {
        if (utilizadorDAO.remover(id)) {
            recarregarDados();
            historicoOperacoes.empilhar("Utilizador removido: ID " + id);
            return true;
        }
        return false;
    }

    public ListaLigada<Livro> getLivros() { return livros; }
    public ListaLigada<Utilizador> getUtilizadores() { return utilizadores; }
    public ListaLigada<Emprestimo> getEmprestimos() { return emprestimos; }
    public Fila<String> getFilaEspera() { return filaDeEsperaLivros; }
    public Pilha<String> getHistorico() { return historicoOperacoes; }

    // 1. MÉTODO DE COMPATIBILIDADE (Sobrecarga para o Main / Consola com 2 argumentos)
    public String realizarEmprestimo(String isbn, int idUtilizador) {
        return realizarEmprestimo(isbn, idUtilizador, "");
    }

    // 2. MÉTODO PRINCIPAL (Para a Interface Gráfica com 3 argumentos)
    public String realizarEmprestimo(String isbn, int idUtilizador, String dataPrevistaStr) {
        Livro livroAlvo = null;
        Utilizador userAlvo = null;

        No<Livro> atualLivro = livros.getHead();
        while (atualLivro != null) {
            if (atualLivro.getDado().getIsbn().equals(isbn)) {
                livroAlvo = atualLivro.getDado();
                break;
            }
            atualLivro = atualLivro.getProximo();
        }

        No<Utilizador> bookmarks = utilizadores.getHead();
        while (bookmarks != null) {
            if (bookmarks.getDado().getId() == idUtilizador) {
                userAlvo = bookmarks.getDado();
                break;
            }
            bookmarks = bookmarks.getProximo();
        }

        if (livroAlvo == null) return "Erro: Livro não encontrado!";
        if (userAlvo == null) return "Erro: Utilizador não encontrado!";

        if (livroAlvo.getEmprestado()) {
            String mensagemEspera = "User: " + userAlvo.getNome() + " (ID: " + idUtilizador + ") aguarda por: " + livroAlvo.getTitulo();
            filaDeEsperaLivros.enfileirar(mensagemEspera);
            return "O livro já está emprestado! Adicionado à Fila de Espera.";
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista;

        // VALIDAÇÃO DA DATA PERSONALIZADA
        if (dataPrevistaStr != null && !dataPrevistaStr.trim().isEmpty()) {
            try {
                dataPrevista = LocalDate.parse(dataPrevistaStr.trim(), formatadorData);
                if (dataPrevista.isBefore(hoje)) {
                    return "Erro: A data de devolução não pode ser anterior a hoje!";
                }
            } catch (DateTimeParseException e) {
                return "Erro: Formato de data inválido! Use o padrão dd-MM-yyyy (Ex: 25-06-2026).";
            }
        } else {
            dataPrevista = hoje.plusDays(15); // Padrão automático se ficar em branco
        }

        Emprestimo novoEmprestimo = new Emprestimo(0, livroAlvo, userAlvo, hoje, dataPrevista, null);

        if (emprestimoDAO.inserir(novoEmprestimo)) {
            livroAlvo.setEmprestado(true);
            livroDAO.atualizar(livroAlvo);
            recarregarDados(); 
            return "Empréstimo registado! Devolução até: " + dataPrevista.format(formatadorData);
        }
        return "Erro ao gravar o empréstimo.";
    }

    public String devolverLivro(String isbn) {
        No<Emprestimo> atualEmp = emprestimos.getHead();
        Emprestimo emprestimoAtivo = null;

        while (atualEmp != null) {
            Emprestimo emp = atualEmp.getDado();
            if (emp.getLivro().getIsbn().equals(isbn) && emp.getDataDevolucaoReal() == null) {
                emprestimoAtivo = emp;
                break;
            }
            atualEmp = atualEmp.getProximo();
        }

        if (emprestimoAtivo == null) return "Erro: Nenhum empréstimo ativo para este livro.";

        LocalDate hoje = LocalDate.now();
        if (emprestimoDAO.registarDevolucao(emprestimoAtivo.getId(), hoje)) {
            Livro livro = emprestimoAtivo.getLivro();
            livro.setEmprestado(false);
            livroDAO.atualizar(livro);

            long diasAtraso = ChronoUnit.DAYS.between(emprestimoAtivo.getDataDevolucaoPrevista(), hoje);
            double multa = 0;
            String msgMulta = "";

            if (diasAtraso > 0) {
                multa = diasAtraso * 100.0;
                msgMulta = String.format("\n[⚠️ MULTA] %d dias de atraso. Total: %.2f CVE.", diasAtraso, multa);
            }

            recarregarDados();
            return "Livro '" + livro.getTitulo() + "' devolvido!" + msgMulta;
        }
        return "Erro ao registar devolução.";
    }

    public ListaLigada<Livro> pesquisarLivros(String termo) {
        ListaLigada<Livro> resultado = new ListaLigada<>();
        String termoM = termo.toLowerCase();
        No<Livro> atual = livros.getHead();
        while (atual != null) {
            Livro l = atual.getDado();
            if (l.getTitulo().toLowerCase().contains(termoM) || l.getAutor().toLowerCase().contains(termoM)) {
                resultado.adicionar(l);
            }
            atual = atual.getProximo();
        }
        return resultado;
    }

    public ListaLigada<Livro> obterLivrosOrdenadosPorTitulo() {
        ListaLigada<Livro> listaOrdenada = livroDAO.listarTodos();
        if (listaOrdenada.getHead() == null || listaOrdenada.getHead().getProximo() == null) return listaOrdenada;
        boolean trocou;
        do {
            trocou = false;
            No<Livro> atual = listaOrdenada.getHead();
            while (atual != null && atual.getProximo() != null) {
                Livro livroAtual = atual.getDado();
                // CORREÇÃO DEFINITIVA: Removido o lixo de digitação "Brandon =" que quebrava o compilador
                Livro proximoLivro = atual.getProximo().getDado();
                if (livroAtual.getTitulo().compareToIgnoreCase(proximoLivro.getTitulo()) > 0) {
                    atual.setDado(proximoLivro);
                    atual.getProximo().setDado(livroAtual);
                    trocou = true;
                }
                atual = atual.getProximo();
            }
        } while (trocou);
        return listaOrdenada;
    }
}