package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import src.model.Emprestimo;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;

public class PainelTransacoes extends JPanel {

    private Biblioteca biblioteca;

    // Componentes da Aba 1 (Empréstimo)
    private JTextField txtIdUsuarioEmp;
    private JTextField txtIsbnLivroEmp;
    private JButton btnConfirmarEmp;

    // Componentes da Aba 2 (Consulta/Prazos)
    private JTable tabelaEmprestimos;
    private DefaultTableModel modeloTabela;

    // Componentes da Aba 3 (Devolução)
    private JTextField txtIsbnDev;
    private JButton btnBuscarDev;
    private JButton btnConfirmarDev;
    private JTextPane txtAreaStatusDev;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelTransacoes(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout());

        add(new Cabecalho("Transações"), BorderLayout.NORTH);

        // Criar o sistema de 3 abas para Transações
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Arial", Font.PLAIN, 14));

        Font fonteLabels = new Font("Arial", Font.PLAIN, 14);
        Font fonteCampos = new Font("Arial", Font.PLAIN, 14);

        // -------------------------------------------------------------------------
        // ABA 1: EFETUAR EMPRÉSTIMO
        // -------------------------------------------------------------------------
        JPanel abaEmprestimo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 80));
        
        JPanel formEmp = new JPanel(new GridLayout(3, 2, 10, 20));
        formEmp.setPreferredSize(new Dimension(500, 180));
        formEmp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                " Nova Requisição ", 0, 0, new Font("Arial", Font.BOLD, 14)
        ));

        JLabel lblIdUser = new JLabel("  ID do Utilizador:");
        lblIdUser.setFont(fonteLabels);
        txtIdUsuarioEmp = new JTextField();
        txtIdUsuarioEmp.setFont(fonteCampos);

        JLabel lblIsbnLivro = new JLabel("  ISBN do Livro:");
        lblIsbnLivro.setFont(fonteLabels);
        txtIsbnLivroEmp = new JTextField();
        txtIsbnLivroEmp.setFont(fonteCampos);

        btnConfirmarEmp = new JButton("Confirmar Empréstimo");
        btnConfirmarEmp.setFont(new Font("Arial", Font.BOLD, 14));
        JButton btnLimparEmp = new JButton("Limpar");
        btnLimparEmp.setFont(fonteLabels);

        formEmp.add(lblIdUser);
        formEmp.add(txtIdUsuarioEmp);
        formEmp.add(lblIsbnLivro);
        formEmp.add(txtIsbnLivroEmp);
        formEmp.add(btnConfirmarEmp);
        formEmp.add(btnLimparEmp);

        abaEmprestimo.add(formEmp);

        // -------------------------------------------------------------------------
        // ABA 2: CONSULTAR EMPRÉSTIMOS (Prazos e Atrasos)
        // -------------------------------------------------------------------------
        JPanel abaConsulta = new JPanel(new BorderLayout());
        abaConsulta.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] colunas = {"ID Utilizador", "ISBN", "Data Empréstimo", "Data Limite", "Estado"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaEmprestimos = new JTable(modeloTabela);
        tabelaEmprestimos.setFont(fonteCampos);
        tabelaEmprestimos.setRowHeight(25);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaEmprestimos);
        abaConsulta.add(scrollTabela, BorderLayout.CENTER);

        // -------------------------------------------------------------------------
        // ABA 3: DEVOLUÇÕES
        // -------------------------------------------------------------------------
        JPanel abaDevolucao = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        
        JPanel painelDev = new JPanel(new BorderLayout(10, 15));
        painelDev.setPreferredSize(new Dimension(550, 320));
        painelDev.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                " Retorno de Exemplares ", 0, 0, new Font("Arial", Font.BOLD, 14)
        ));

        // Sub-painel superior de busca
        JPanel topoDev = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topoDev.add(new JLabel("ISBN do Livro:"));
        txtIsbnDev = new JTextField(15);
        btnBuscarDev = new JButton("Buscar Empréstimo");
        topoDev.add(txtIsbnDev);
        topoDev.add(btnBuscarDev);
        painelDev.add(topoDev, BorderLayout.NORTH);

        // Centro: Feedback visual
        txtAreaStatusDev = new JTextPane();
        txtAreaStatusDev.setEditable(false);
        txtAreaStatusDev.setText("\n  Insira o ISBN acima e clique em 'Buscar Empréstimo' para validar os dados da devolução...");
        txtAreaStatusDev.setBackground(new Color(245, 245, 245));
        JScrollPane scrollStatus = new JScrollPane(txtAreaStatusDev);
        painelDev.add(scrollStatus, BorderLayout.CENTER);

        // Botão de confirmação no fundo
        btnConfirmarDev = new JButton("Confirmar Devolução e Atualizar Stock");
        btnConfirmarDev.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmarDev.setEnabled(false);
        painelDev.add(btnConfirmarDev, BorderLayout.SOUTH);

        abaDevolucao.add(painelDev);

        // -------------------------------------------------------------------------
        // Adicionar tudo ao painel de abas
        // -------------------------------------------------------------------------
        abas.addTab("Efetuar Empréstimo", abaEmprestimo);
        abas.addTab("Consultar Empréstimos", abaConsulta);
        abas.addTab("Devoluções", abaDevolucao);

        add(abas, BorderLayout.CENTER);

        // Carregar dados reais na tabela
        carregarTabela();

        // ── Ações dos botões ──

        btnConfirmarEmp.addActionListener(e -> {
            String isbn = txtIsbnLivroEmp.getText().trim();
            String idStr = txtIdUsuarioEmp.getText().trim();
            if (isbn.isEmpty() || idStr.isEmpty()) return;
            try {
                int id = Integer.parseInt(idStr);
                String resultado = biblioteca.realizarEmprestimo(isbn, id);
                javax.swing.JOptionPane.showMessageDialog(this, resultado);
                txtIdUsuarioEmp.setText("");
                txtIsbnLivroEmp.setText("");
                carregarTabela();
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "ID inválido!");
            }
        });

        btnLimparEmp.addActionListener(e -> {
            txtIdUsuarioEmp.setText("");
            txtIsbnLivroEmp.setText("");
        });

        btnBuscarDev.addActionListener(e -> {
            String isbn = txtIsbnDev.getText().trim();
            if (isbn.isEmpty()) return;
            No<Emprestimo> no = biblioteca.getEmprestimos().getHead();
            Emprestimo encontrado = null;
            while (no != null) {
                Emprestimo emp = no.getDado();
                if (emp.getLivro().getIsbn().equalsIgnoreCase(isbn) && emp.getDataDevolucaoReal() == null) {
                    encontrado = emp;
                    break;
                }
                no = no.getProximo();
            }
            if (encontrado != null) {
                txtAreaStatusDev.setText("  Livro: " + encontrado.getLivro().getTitulo()
                    + "\n  Utilizador: " + encontrado.getUtilizador().getNome()
                    + "\n  Devolução Prevista: " + encontrado.getDataDevolucaoPrevista().format(FMT));
                btnConfirmarDev.setEnabled(true);
            } else {
                txtAreaStatusDev.setText("  Nenhum empréstimo ativo encontrado para este ISBN.");
                btnConfirmarDev.setEnabled(false);
            }
        });

        btnConfirmarDev.addActionListener(e -> {
            String isbn = txtIsbnDev.getText().trim();
            String resultado = biblioteca.devolverLivro(isbn);
            javax.swing.JOptionPane.showMessageDialog(this, resultado);
            txtIsbnDev.setText("");
            txtAreaStatusDev.setText("\n  Insira o ISBN acima e clique em 'Buscar Empréstimo' para validar os dados da devolução...");
            btnConfirmarDev.setEnabled(false);
            carregarTabela();
        });
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        LocalDate hoje = LocalDate.now();
        No<Emprestimo> no = biblioteca.getEmprestimos().getHead();
        while (no != null) {
            Emprestimo emp = no.getDado();
            String estado = emp.getDataDevolucaoReal() == null
                ? (emp.getDataDevolucaoPrevista().isBefore(hoje) ? "ATENÇÃO: Atrasado" : "Regular")
                : "Devolvido";
            modeloTabela.addRow(new Object[]{
                emp.getUtilizador().getId(),
                emp.getLivro().getIsbn(),
                emp.getDataEmprestimo().format(FMT),
                emp.getDataDevolucaoPrevista().format(FMT),
                estado
            });
            no = no.getProximo();
        }
    }

    public void atualizarDados() {
        biblioteca.recarregarDados();
        carregarTabela();
    }
}
