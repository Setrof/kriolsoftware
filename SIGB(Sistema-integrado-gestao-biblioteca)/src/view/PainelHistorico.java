package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import src.model.Emprestimo;
import src.model.Livro;
import src.model.Utilizador;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;

public class PainelHistorico extends JPanel {

    private Biblioteca biblioteca;

    private DefaultTableModel modeloEmprestimos;
    private DefaultTableModel modeloLivros;
    private DefaultTableModel modeloUtilizadores;
    private DefaultTableModel modeloPilha;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelHistorico(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout());

        add(new Cabecalho("Histórico"), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Arial", Font.PLAIN, 14));

        // -------------------------------------------------------------------------
        // ABA 1: HISTÓRICO DE EMPRÉSTIMOS
        // -------------------------------------------------------------------------
        JPanel abaEmprestimos = new JPanel(new BorderLayout());
        abaEmprestimos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colEmp = {"ID", "Livro", "Utilizador", "Empréstimo", "Data Limite", "Devolução Real", "Estado"};
        modeloEmprestimos = new DefaultTableModel(colEmp, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaEmp = new JTable(modeloEmprestimos);
        tabelaEmp.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEmp.setRowHeight(25);
        tabelaEmp.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollEmp = new JScrollPane(tabelaEmp);
        abaEmprestimos.add(scrollEmp, BorderLayout.CENTER);

        // Botão limpar em baixo
        JPanel rodapeEmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimparEmp = new JButton("Limpar Histórico de Empréstimos");
        btnLimparEmp.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimparEmp.setForeground(Color.WHITE);
        btnLimparEmp.setBackground(EstiloUI.COR_PERIGO);
        btnLimparEmp.setOpaque(true);
        btnLimparEmp.setBorderPainted(false);
        btnLimparEmp.setFocusPainted(false);
        btnLimparEmp.addActionListener(e -> {
            modeloEmprestimos.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Histórico de empréstimos limpo.");
        });
        rodapeEmp.add(btnLimparEmp);
        abaEmprestimos.add(rodapeEmp, BorderLayout.SOUTH);

        // -------------------------------------------------------------------------
        // ABA 2: HISTÓRICO DE LIVROS
        // -------------------------------------------------------------------------
        JPanel abaLivros = new JPanel(new BorderLayout());
        abaLivros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colLiv = {"ISBN", "Título", "Autor", "Género", "Editora", "Ano", "Estado"};
        modeloLivros = new DefaultTableModel(colLiv, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaLiv = new JTable(modeloLivros);
        tabelaLiv.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaLiv.setRowHeight(25);
        tabelaLiv.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollLiv = new JScrollPane(tabelaLiv);
        abaLivros.add(scrollLiv, BorderLayout.CENTER);

        JPanel rodapeLiv = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimparLiv = new JButton("Limpar Histórico de Livros");
        btnLimparLiv.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimparLiv.setForeground(Color.WHITE);
        btnLimparLiv.setBackground(EstiloUI.COR_PERIGO);
        btnLimparLiv.setOpaque(true);
        btnLimparLiv.setBorderPainted(false);
        btnLimparLiv.setFocusPainted(false);
        btnLimparLiv.addActionListener(e -> {
            modeloLivros.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Histórico de livros limpo.");
        });
        rodapeLiv.add(btnLimparLiv);
        abaLivros.add(rodapeLiv, BorderLayout.SOUTH);

        // -------------------------------------------------------------------------
        // ABA 3: HISTÓRICO DE UTILIZADORES
        // -------------------------------------------------------------------------
        JPanel abaUtilizadores = new JPanel(new BorderLayout());
        abaUtilizadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colUsu = {"ID", "Nome", "Morada", "CNI", "Email", "Telefone"};
        modeloUtilizadores = new DefaultTableModel(colUsu, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaUsu = new JTable(modeloUtilizadores);
        tabelaUsu.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaUsu.setRowHeight(25);
        tabelaUsu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollUsu = new JScrollPane(tabelaUsu);
        abaUtilizadores.add(scrollUsu, BorderLayout.CENTER);

        JPanel rodapeUsu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimparUsu = new JButton("Limpar Histórico de Utilizadores");
        btnLimparUsu.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimparUsu.setForeground(Color.WHITE);
        btnLimparUsu.setBackground(EstiloUI.COR_PERIGO);
        btnLimparUsu.setOpaque(true);
        btnLimparUsu.setBorderPainted(false);
        btnLimparUsu.setFocusPainted(false);
        btnLimparUsu.addActionListener(e -> {
            modeloUtilizadores.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Histórico de utilizadores limpo.");
        });
        rodapeUsu.add(btnLimparUsu);
        abaUtilizadores.add(rodapeUsu, BorderLayout.SOUTH);

        // -------------------------------------------------------------------------
        // ABA 4: OPERAÇÕES (PILHA)
        // -------------------------------------------------------------------------
        JPanel abaPilha = new JPanel(new BorderLayout());
        abaPilha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colPilha = {"#", "Operação Registada"};
        modeloPilha = new DefaultTableModel(colPilha, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaPilha = new JTable(modeloPilha);
        tabelaPilha.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaPilha.setRowHeight(25);
        tabelaPilha.getColumnModel().getColumn(0).setMaxWidth(40);
        tabelaPilha.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPilha = new JScrollPane(tabelaPilha);
        abaPilha.add(scrollPilha, BorderLayout.CENTER);

        JPanel rodapePilha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimparPilha = new JButton("Limpiar Pilha de Operações");
        btnLimparPilha.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimparPilha.setForeground(Color.WHITE);
        btnLimparPilha.setBackground(EstiloUI.COR_PERIGO);
        btnLimparPilha.setOpaque(true);
        btnLimparPilha.setBorderPainted(false);
        btnLimparPilha.setFocusPainted(false);
        btnLimparPilha.addActionListener(e -> {
            modeloPilha.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Pilha de operações limpa.");
        });
        rodapePilha.add(btnLimparPilha);
        abaPilha.add(rodapePilha, BorderLayout.SOUTH);

        // -------------------------------------------------------------------------
        // Adicionar abas
        // -------------------------------------------------------------------------
        abas.addTab("Empréstimos", abaEmprestimos);
        abas.addTab("Livros", abaLivros);
        abas.addTab("Utilizadores", abaUtilizadores);
        abas.addTab("Operações (Pilha)", abaPilha);

        add(abas, BorderLayout.CENTER);

        carregarTudo();
    }

    private void carregarTudo() {
        carregarEmprestimos();
        carregarLivros();
        carregarUtilizadores();
        carregarPilha();
    }

    private void carregarEmprestimos() {
        modeloEmprestimos.setRowCount(0);
        LocalDate hoje = LocalDate.now();
        No<Emprestimo> no = biblioteca.getEmprestimos().getHead();
        while (no != null) {
            Emprestimo emp = no.getDado();
            String devReal = emp.getDataDevolucaoReal() != null ? emp.getDataDevolucaoReal().format(FMT) : "—";
            String estado;
            if (emp.getDataDevolucaoReal() != null) {
                estado = emp.getDataDevolucaoReal().isAfter(emp.getDataDevolucaoPrevista()) ? "Devolvido (Atrasado)" : "Devolvido";
            } else if (emp.getDataDevolucaoPrevista().isBefore(hoje)) {
                estado = "ATENÇÃO: Atrasado";
            } else {
                estado = "Regular";
            }
            modeloEmprestimos.addRow(new Object[]{
                emp.getId(), emp.getLivro().getTitulo(), emp.getUtilizador().getNome(),
                emp.getDataEmprestimo().format(FMT), emp.getDataDevolucaoPrevista().format(FMT),
                devReal, estado
            });
            no = no.getProximo();
        }
    }

    private void carregarLivros() {
        modeloLivros.setRowCount(0);
        No<Livro> no = biblioteca.getLivros().getHead();
        while (no != null) {
            Livro l = no.getDado();
            String estado = l.getEmprestado() ? "Emprestado" : "Disponível";
            modeloLivros.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getEditora(), l.getAnoPublicacao(), estado
            });
            no = no.getProximo();
        }
    }

    private void carregarUtilizadores() {
        modeloUtilizadores.setRowCount(0);
        No<Utilizador> no = biblioteca.getUtilizadores().getHead();
        while (no != null) {
            Utilizador u = no.getDado();
            modeloUtilizadores.addRow(new Object[]{
                u.getId(), u.getNome(), u.getMorada(), u.getCNI(), u.getEmail(), u.getTelefone()
            });
            no = no.getProximo();
        }
    }

    private void carregarPilha() {
        modeloPilha.setRowCount(0);
        No<String> no = biblioteca.getHistorico().getTopo();
        int i = 1;
        while (no != null) {
            modeloPilha.addRow(new Object[]{ i++, no.getDado() });
            no = no.getProximo();
        }
    }

    public void atualizarDados() {
        biblioteca.recarregarDados();
        carregarTudo();
    }
}
