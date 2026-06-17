package src.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import src.model.Emprestimo;
import src.model.Livro;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.componentes.CartaoEstatistica;
import src.view.utils.EstiloUI;


public class PainelDashBoard extends JPanel {

    private Biblioteca biblioteca;

    // Cartões de estatística
    private CartaoEstatistica cartaoLivros;
    private CartaoEstatistica cartaoUtilizadores;
    private CartaoEstatistica cartaoEmprestimos;
    private CartaoEstatistica cartaoAtrasados;

    // Tabela de empréstimos ativos
    private DefaultTableModel modeloTabela;

    public PainelDashBoard(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        // ── Cabeçalho ──────────────────────────────────────────
        add(new Cabecalho("Dashboard  —  Visão Geral"), BorderLayout.NORTH);

        // ── Área central com scroll ─────────────────────────────
        JPanel centro = new JPanel(new BorderLayout(15, 15));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cartões
        JPanel painelCartoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelCartoes.setOpaque(false);

        cartaoLivros       = new CartaoEstatistica("Total de Livros",     "—", EstiloUI.COR_DESTAQUE);
        cartaoUtilizadores = new CartaoEstatistica("Utilizadores",        "—", EstiloUI.COR_SUCESSO);
        cartaoEmprestimos  = new CartaoEstatistica("Empréstimos Ativos",  "—", EstiloUI.COR_AVISO);
        cartaoAtrasados    = new CartaoEstatistica("Em Atraso",           "—", EstiloUI.COR_PERIGO);

        painelCartoes.add(cartaoLivros);
        painelCartoes.add(cartaoUtilizadores);
        painelCartoes.add(cartaoEmprestimos);
        painelCartoes.add(cartaoAtrasados);

        centro.add(painelCartoes, BorderLayout.NORTH);

        // Tabela de empréstimos ativos
        String[] colunas = {"ID", "Livro", "Utilizador", "Data Empréstimo", "Devolução Prevista", "Estado"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modeloTabela);
        tabela.setFont(EstiloUI.FONTE_CORPO);
        tabela.getTableHeader().setFont(EstiloUI.FONTE_SUBTITULO);
        tabela.setRowHeight(28);
        tabela.setGridColor(EstiloUI.COR_BORDA);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                "Empréstimos em Aberto"));
        centro.add(scroll, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);

        // Carrega os dados reais
        atualizarDados();
    }

    
    public void atualizarDados() {
        biblioteca.recarregarDados();

        // Contagens nas listas ligadas
        int totalLivros = contarLista(biblioteca.getLivros().getHead());
        int totalUsers  = contarLista(biblioteca.getUtilizadores().getHead());

        int emprestimosAtivos = 0;
        int atrasados         = 0;
        java.time.LocalDate hoje = java.time.LocalDate.now();

        modeloTabela.setRowCount(0);
        No<Emprestimo> noEmp = biblioteca.getEmprestimos().getHead();
        while (noEmp != null) {
            Emprestimo emp = noEmp.getDado();
            if (emp.getDataDevolucaoReal() == null) {   // ainda em aberto
                emprestimosAtivos++;
                boolean atrasado = emp.getDataDevolucaoPrevista() != null
                        && emp.getDataDevolucaoPrevista().isBefore(hoje);
                if (atrasado) atrasados++;

                modeloTabela.addRow(new Object[]{
                    emp.getId(),
                    emp.getLivro().getTitulo(),
                    emp.getUtilizador().getNome(),
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucaoPrevista(),
                    atrasado ? "⚠ Atrasado" : "✔ OK"
                });
            }
            noEmp = noEmp.getProximo();
        }

        cartaoLivros.setValor(String.valueOf(totalLivros));
        cartaoUtilizadores.setValor(String.valueOf(totalUsers));
        cartaoEmprestimos.setValor(String.valueOf(emprestimosAtivos));
        cartaoAtrasados.setValor(String.valueOf(atrasados));
    }

    // Conta elementos de uma ListaLigada percorrendo os nós
    private <T> int contarLista(No<T> head) {
        int count = 0;
        while (head != null) { count++; head = head.getProximo(); }
        return count;
    }
}
