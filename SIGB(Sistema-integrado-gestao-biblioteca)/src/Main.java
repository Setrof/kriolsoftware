package src;

import java.util.Scanner;
import javax.swing.SwingUtilities;
import src.Database.conexaoBD;
import src.model.Livro;
import src.service.Biblioteca;
import src.structures.ListaLigada;
import src.structures.No;
import src.view.IntroJanela;

/**
 * Classe principal do Sistema Integrado de Gestão de Biblioteca (SIGB).
 * Responsável por gerir o ciclo de vida da aplicação, orquestrar a inicialização
 * da base de dados, e alternar entre as interfaces de Consola e Gráfica (GUI).
 * * @author Danilo Oliveira and Lenardo do Rosario
 * @version 0.2.1.2
 */
public class Main {

    /**
     * Ponto de entrada principal da aplicação.
     * * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        // 1. Inicialização Crítica: Garante a criação do arquivo .db e das tabelas DDL
        // antes que qualquer componente de serviço tente aceder ao repositório.
        conexaoBD.inicializarBaseDeDados();

        // 2. Inicialização dos recursos de input de consola
        Scanner scanner = new Scanner(System.in);

        // 3. Instanciação do Core Business Service (Partilhado entre Consola e GUI)
        Biblioteca biblioteca = new Biblioteca();

        System.out.println("=================================================");
        System.out.println("   SIGB - SISTEMA INTEGRADO DE GESTÃO DE BIBLIOTECA   ");
        System.out.println("            TESTE DE SISTEMA (CONSOLA)           ");
        System.out.println("=================================================");
 /* 
        // 4. Seed Data: Alimenta a base de dados com registos de teste caso esteja vazia
        if (biblioteca.getLivros().getHead() == null) {
            System.out.println("\n[Info] A base de dados está vazia. A criar dados de teste...");
            biblioteca.cadastrarLivro(new Livro("111", "Estruturas de Dados em Java", "Emanuel Vieira",  "Engenharia", "UniMindelo", 2024, false));
            biblioteca.cadastrarLivro(new Livro("222", "Introducao ao SQLite",        "Danilo Oliveira", "Tecnologia", "KriolTech",  2026, false));
            biblioteca.cadastrarUtilizador(new Utilizador(0, "Manuel Silva", "Praia",   "CNI12345", "manuel@email.com", "9912345"));
            biblioteca.cadastrarUtilizador(new Utilizador(0, "Maria Moniz",  "Mindelo", "CNI67890", "maria@email.com",  "9954321"));
        }
//*/
        // Menu de controlo iterativo via consola
        int opcao = 0;
        do {
            System.out.println("\n--- MENU DE OPERAÇÕES ---");
            System.out.println("1. Listar Todos os Livros (Como estão na DB)");
            System.out.println("2. Listar Livros Ordenados por Título (Bubble Sort)");
            System.out.println("3. Pesquisar Livro (Busca Linear)");
            System.out.println("4. Realizar Empréstimo (Testar Fila de Espera)");
            System.out.println("5. Devolver Livro");
            System.out.println("6. Ver Histórico de Operações (Pilha)");
            System.out.println("0. Abrir Interface Gráfica");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.out.println("Por favor, introduza um número válido.");
                scanner.nextLine(); // Descarta a entrada inválida
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.println("\n--- LISTA DE LIVROS (RAM / DB) ---");
                    imprimirListaLivros(biblioteca.getLivros());
                    break;

                case 2:
                    System.out.println("\n--- LIVROS ORDENADOS POR TÍTULO (A-Z) ---");
                    imprimirListaLivros(biblioteca.obterLivrosOrdenadosPorTitulo());
                    break;

                case 3:
                    System.out.print("\nDigite o título ou autor para pesquisar: ");
                    String termo = scanner.nextLine();
                    ListaLigada<Livro> resultados = biblioteca.pesquisarLivros(termo);
                    System.out.println("\n--- RESULTADOS DA BUSCA ---");
                    imprimirListaLivros(resultados);
                    break;

                case 4:
                    System.out.print("\nDigite o ISBN do livro: ");
                    String isbnEmp = scanner.nextLine();
                    System.out.print("Digite o ID do Utilizador: ");
                    int idUser = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("\n[Resultado]: " + biblioteca.realizarEmprestimo(isbnEmp, idUser));
                    break;

                case 5:
                    System.out.print("\nDigite o ISBN do livro a devolver: ");
                    String isbnDev = scanner.nextLine();
                    System.out.println("\n[Resultado]: " + biblioteca.devolverLivro(isbnDev));
                    break;

                case 6:
                    System.out.println("\n--- HISTÓRICO RECENTE (PILHA) ---");
                    if (biblioteca.getHistorico().estaVazia()) {
                        System.out.println("Nenhuma operação registada.");
                    } else {
                        // Varre a estrutura LIFO (Pilha) a partir do topo (elemento mais recente)
                        No<String> no = biblioteca.getHistorico().getTopo();
                        int i = 1;
                        while (no != null) {
                            System.out.println(i++ + ". " + no.getDado());
                            no = no.getProximo();
                        }
                    }
                    break;

                case 0:
                    System.out.println("\nA abrir a Interface Gráfica...");
                    abrirGUI(biblioteca);
                    System.out.println("Interface gráfica encerrada. A terminar o programa.");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        scanner.close();
    }

    /**
     * Inicializa a Interface Gráfica (Swing) de forma segura na Event Dispatch Thread (EDT)
     * e bloqueia a execução da Consola até que a janela principal seja fechada.
     * * @param biblioteca Instância partilhada do serviço de controlo.
     */
    private static void abrirGUI(Biblioteca biblioteca) {
        final Object[] lock = new Object[1];

        // Agenda a renderização da interface na thread correta do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            IntroJanela intro = new IntroJanela(biblioteca, lock);
            intro.setVisible(true);
        });

        // Mecanismo de Sincronização: Bloqueia a thread da consola até que a GUI mude o estado do lock
        synchronized (lock) {
            while (lock[0] == null) {
                try { 
                    lock.wait(); 
                } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                    break; 
                }
            }
        }
    }

    /**
     * Helper Method para percorrer e imprimir os nós de uma Lista Ligada de Livros.
     * * @param lista Instância da estrutura customizada ListaLigada contendo os livros.
     */
    private static void imprimirListaLivros(ListaLigada<Livro> lista) {
        No<Livro> atual = lista.getHead();
        if (atual == null) {
            System.out.println("Nenhum livro encontrado nesta lista.");
            return;
        }
        
        // Iteração linear sobre a estrutura de dados encadeada
        while (atual != null) {
            Livro l = atual.getDado();
            String status = l.getEmprestado() ? "[EMPRESTADO]" : "[DISPONÍVEL]";
            System.out.println("- ISBN: " + l.getIsbn() + " | " + l.getTitulo()
                    + " (" + l.getAutor() + ") - " + status);
            atual = atual.getProximo();
        }
    }
}