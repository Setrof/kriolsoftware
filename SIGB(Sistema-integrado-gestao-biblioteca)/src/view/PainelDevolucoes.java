package src.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import src.model.Emprestimo;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;


public class PainelDevolucoes extends JPanel {

    private Biblioteca biblioteca;
    private DefaultTableModel modeloAtivos;

    private JTextField txtISBN;

    public PainelDevolucoes(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        add(new Cabecalho("Registo de Devoluções"), BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(15, 15));
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(20, 20, 20, 20));

        corpo.add(criarFormulario(),   BorderLayout.NORTH);
        corpo.add(criarTabelaAtivos(), BorderLayout.CENTER);

        add(corpo, BorderLayout.CENTER);

        carregarDados();
    }

    // ─────────────────── FORMULÁRIO ─────────────────────────────

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        card.setBackground(EstiloUI.COR_CARTAO);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                new EmptyBorder(10, 15, 10, 15)));

        JLabel lbl = new JLabel("ISBN do Livro a Devolver:");
        lbl.setFont(EstiloUI.FONTE_CORPO);

        txtISBN = new JTextField(20);
        txtISBN.setFont(EstiloUI.FONTE_CORPO);

        JButton btnProcurar  = new JButton("Procurar");
        JButton btnDevolver  = new JButton("Receber Livro");

        btnProcurar.setFont(EstiloUI.FONTE_CORPO);
        btnDevolver.setFont(EstiloUI.FONTE_CORPO);
        btnDevolver.setBackground(EstiloUI.COR_SUCESSO);
        btnDevolver.setForeground(java.awt.Color.WHITE);
        btnDevolver.setOpaque(true);
        btnDevolver.setBorderPainted(false);
        btnDevolver.setFocusPainted(false);

        card.add(lbl);
        card.add(txtISBN);
        card.add(btnProcurar);
        card.add(btnDevolver);

        // Filtrar tabela pelo ISBN pesquisado
        btnProcurar.addActionListener(e -> {
            String termo = txtISBN.getText().trim().toLowerCase();
            modeloAtivos.setRowCount(0);
            No<Emprestimo> atual = biblioteca.getEmprestimos().getHead();
            while (atual != null) {
                Emprestimo emp = atual.getDado();
                if (emp.getDataDevolucaoReal() == null &&
                    (termo.isEmpty() || emp.getLivro().getIsbn().toLowerCase().contains(termo))) {
                    modeloAtivos.addRow(new Object[]{
                        emp.getId(),
                        emp.getLivro().getIsbn(),
                        emp.getLivro().getTitulo(),
                        emp.getUtilizador().getNome(),
                        emp.getDataDevolucaoPrevista()
                    });
                }
                atual = atual.getProximo();
            }
        });

        btnDevolver.addActionListener(e -> {
            String isbn = txtISBN.getText().trim();
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Introduza o ISBN do livro a devolver.", "Validação",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            String resultado = biblioteca.devolverLivro(isbn);

            if (resultado.startsWith("Erro")) {
                JOptionPane.showMessageDialog(this, resultado, "Erro",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Devolução Registada",
                    JOptionPane.INFORMATION_MESSAGE);
                txtISBN.setText("");
                carregarDados();
            }
        });

        return card;
    }

    // ─────────────────── TABELA EMPRÉSTIMOS ATIVOS ───────────────

    private JScrollPane criarTabelaAtivos() {
        String[] colunas = {"ID", "ISBN", "Livro", "Utilizador", "Prev. Devolução"};
        modeloAtivos = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modeloAtivos);
        tabela.setFont(EstiloUI.FONTE_CORPO);
        tabela.getTableHeader().setFont(EstiloUI.FONTE_SUBTITULO);
        tabela.setRowHeight(26);
        tabela.setGridColor(EstiloUI.COR_BORDA);

        // Clique numa linha preenche o campo ISBN
        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                txtISBN.setText((String) modeloAtivos.getValueAt(tabela.getSelectedRow(), 1));
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                "Empréstimos em Aberto"));
        return scroll;
    }

    private void carregarDados() {
        modeloAtivos.setRowCount(0);
        No<Emprestimo> atual = biblioteca.getEmprestimos().getHead();
        while (atual != null) {
            Emprestimo emp = atual.getDado();
            if (emp.getDataDevolucaoReal() == null) {
                modeloAtivos.addRow(new Object[]{
                    emp.getId(),
                    emp.getLivro().getIsbn(),
                    emp.getLivro().getTitulo(),
                    emp.getUtilizador().getNome(),
                    emp.getDataDevolucaoPrevista()
                });
            }
            atual = atual.getProximo();
        }
    }
}
