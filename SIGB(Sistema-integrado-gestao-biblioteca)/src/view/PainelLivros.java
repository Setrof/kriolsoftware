package src.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import src.model.Livro;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;

public class PainelLivros extends JPanel {

    private Biblioteca biblioteca;
    private DefaultTableModel modeloTabela;
    private JTextField txtISBN, txtTitulo, txtAutor, txtGenero, txtEditora, txtAno;
    private boolean ordemAZ = true;

    public PainelLivros(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        add(new Cabecalho("Gestão do Catálogo de Livros"), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_MENU);
        abas.addTab("Consultar Catálogo", criarPainelConsulta());
        abas.addTab("Adicionar Novo Livro", criarPainelRegisto());
        add(abas, BorderLayout.CENTER);

        carregarLivros();
    }

    private JPanel criarPainelConsulta() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setOpaque(false);

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnOrdenar = new JButton("A-Z");
        estilizarBotao(btnAtualizar, EstiloUI.COR_SUCESSO);
        estilizarBotao(btnEliminar, EstiloUI.COR_PERIGO);
        estilizarBotao(btnOrdenar, EstiloUI.COR_DESTAQUE);
        
        topo.add(btnAtualizar);
        topo.add(btnEliminar);
        topo.add(btnOrdenar);

        btnAtualizar.addActionListener(e -> {
            biblioteca.recarregarDados();
            carregarLivros();
        });

        btnOrdenar.addActionListener(e -> {
            ordemAZ = !ordemAZ;
            btnOrdenar.setText(ordemAZ ? "A-Z" : "Z-A");
            carregarLivrosOrdenados();
        });

        String[] colunas = {"ISBN", "Título", "Autor", "Género", "Editora", "Ano", "Estado"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modeloTabela);
        tabela.setFont(EstiloUI.FONTE_CORPO);
        tabela.getTableHeader().setFont(EstiloUI.FONTE_SUBTITULO);
        tabela.setRowHeight(26);
        tabela.setGridColor(EstiloUI.COR_BORDA);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnEliminar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um livro na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String isbn = (String) modeloTabela.getValueAt(linha, 0);
            String titulo = (String) modeloTabela.getValueAt(linha, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this, "Eliminar o livro \"" + titulo + "\"?\nEsta ação não pode ser desfeita.", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (biblioteca.eliminarLivro(isbn)) {
                    JOptionPane.showMessageDialog(this, "Livro removido com sucesso!");
                    carregarLivros();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao eliminar. Certifique-se de que o livro não está emprestado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return painel;
    }

    private void carregarLivros() {
        modeloTabela.setRowCount(0);
        No<Livro> atual = biblioteca.getLivros().getHead();
        while (atual != null) {
            Livro l = atual.getDado();
            String estado = l.getEmprestado() ? "Emprestado" : "Disponível";
            modeloTabela.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getEditora(), l.getAnoPublicacao(), estado
            });
            atual = atual.getProximo();
        }
    }

    private void carregarLivrosOrdenados() {
        modeloTabela.setRowCount(0);
        List<Livro> lista = new ArrayList<>();
        No<Livro> atual = biblioteca.getLivros().getHead();
        while (atual != null) {
            lista.add(atual.getDado());
            atual = atual.getProximo();
        }
        if (ordemAZ) {
            Collections.sort(lista, Comparator.comparing(l -> l.getTitulo().toLowerCase()));
        } else {
            Collections.sort(lista, (l1, l2) -> l2.getTitulo().toLowerCase().compareTo(l1.getTitulo().toLowerCase()));
        }
        for (Livro l : lista) {
            String estado = l.getEmprestado() ? "Emprestado" : "Disponível";
            modeloTabela.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getEditora(), l.getAnoPublicacao(), estado
            });
        }
    }

    private JPanel criarPainelRegisto() {
        JPanel externo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        externo.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        JPanel painel = new JPanel();
        painel.setBackground(EstiloUI.COR_CARTAO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                new EmptyBorder(25, 35, 25, 35)));

        GroupLayout layout = new GroupLayout(painel);
        painel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblISBN    = new JLabel("ISBN:");
        JLabel lblTitulo  = new JLabel("Título:");
        JLabel lblAutor   = new JLabel("Autor:");
        JLabel lblGenero  = new JLabel("Género:");
        JLabel lblEditora = new JLabel("Editora:");
        JLabel lblAno     = new JLabel("Ano:");

        for (JLabel l : new JLabel[]{lblISBN, lblTitulo, lblAutor, lblGenero, lblEditora, lblAno}) {
            l.setFont(EstiloUI.FONTE_CORPO);
        }

        txtISBN    = new JTextField(25);
        txtTitulo  = new JTextField(25);
        txtAutor   = new JTextField(25);
        txtGenero  = new JTextField(25);
        txtEditora = new JTextField(25);
        txtAno     = new JTextField(25);

        for (JTextField tf : new JTextField[]{txtISBN, txtTitulo, txtAutor, txtGenero, txtEditora, txtAno}) {
            tf.setFont(EstiloUI.FONTE_CORPO);
            tf.setBackground(java.awt.Color.WHITE);
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        JButton btnGuardar = new JButton("Guardar Livro");
        JButton btnLimpar  = new JButton("Limpar");
        estilizarBotao(btnGuardar, EstiloUI.COR_DESTAQUE);
        estilizarBotao(btnLimpar,  EstiloUI.COR_TEXTO_SECUNDARIO);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblISBN).addComponent(lblTitulo).addComponent(lblAutor)
                .addComponent(lblGenero).addComponent(lblEditora).addComponent(lblAno))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtISBN).addComponent(txtTitulo).addComponent(txtAutor)
                .addComponent(txtGenero).addComponent(txtEditora).addComponent(txtAno)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnGuardar).addComponent(btnLimpar)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblISBN).addComponent(txtISBN))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblTitulo).addComponent(txtTitulo))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblAutor).addComponent(txtAutor))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGenero).addComponent(txtGenero))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblEditora).addComponent(txtEditora))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblAno).addComponent(txtAno))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnGuardar).addComponent(btnLimpar))
        );

        btnGuardar.addActionListener(e -> {
            String isbn    = txtISBN.getText().trim();
            String titulo  = txtTitulo.getText().trim();
            String autor   = txtAutor.getText().trim();
            String genero  = txtGenero.getText().trim();
            String editora = txtEditora.getText().trim();
            String anoStr  = txtAno.getText().trim();

            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN, Título e Autor são obrigatórios!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int ano = 0;
            try { 
                if(!anoStr.isEmpty()) ano = Integer.parseInt(anoStr); 
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Livro novoLivro = new Livro(isbn, titulo, autor, genero, editora, ano, false);
            
            if (biblioteca.cadastrarLivro(novoLivro)) {
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                limparFormulario();
                carregarLivros();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao guardar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> limparFormulario());

        externo.add(painel);
        return externo;
    }

    private void limparFormulario() {
        txtISBN.setText(""); txtTitulo.setText(""); txtAutor.setText("");
        txtGenero.setText(""); txtEditora.setText(""); txtAno.setText("");
        txtISBN.requestFocus();
    }

    private void estilizarBotao(JButton btn, java.awt.Color cor) {
        btn.setFont(EstiloUI.FONTE_CORPO);
        btn.setBackground(cor);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }
}
