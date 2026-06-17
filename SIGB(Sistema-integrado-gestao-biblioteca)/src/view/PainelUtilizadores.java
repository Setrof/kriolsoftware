package src.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import src.model.Utilizador;
import src.service.Biblioteca;
import src.structures.No;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;

public class PainelUtilizadores extends JPanel {

    private Biblioteca biblioteca;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome, txtMorada, txtCNI, txtEmail, txtTelefone;
    private boolean ordemAZ = true;

    public PainelUtilizadores(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        add(new Cabecalho("Gestão de Utilizadores"), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_MENU);
        abas.addTab("Consultar Utilizadores", criarPainelConsulta());
        abas.addTab("Adicionar Utilizador",   criarFormulario());
        add(abas, BorderLayout.CENTER);

        carregarUtilizadores();
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
            carregarUtilizadores();
        });

        btnOrdenar.addActionListener(e -> {
            ordemAZ = !ordemAZ;
            btnOrdenar.setText(ordemAZ ? "A-Z" : "Z-A");
            carregarUtilizadoresOrdenados();
        });

        String[] colunas = {"ID", "Nome", "Morada", "CNI", "Email", "Telefone"};
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
                JOptionPane.showMessageDialog(this, "Selecione um utilizador na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) modeloTabela.getValueAt(linha, 0);
            String nome = (String) modeloTabela.getValueAt(linha, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this, "Eliminar o utilizador \"" + nome + "\"?\nEsta ação não pode ser desfeita.", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (biblioteca.eliminarUtilizador(id)) {
                    JOptionPane.showMessageDialog(this, "Utilizador removido com sucesso!");
                    carregarUtilizadores();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao eliminar. Remova empréstimos ativos vinculados primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return painel;
    }

    private void carregarUtilizadores() {
        modeloTabela.setRowCount(0);
        No<Utilizador> atual = biblioteca.getUtilizadores().getHead();
        while (atual != null) {
            Utilizador u = atual.getDado();
            modeloTabela.addRow(new Object[]{
                u.getId(), u.getNome(), u.getMorada(), u.getCNI(), u.getEmail(), u.getTelefone()
            });
            atual = atual.getProximo();
        }
    }

    private void carregarUtilizadoresOrdenados() {
        modeloTabela.setRowCount(0);
        List<Utilizador> lista = new ArrayList<>();
        No<Utilizador> atual = biblioteca.getUtilizadores().getHead();
        while (atual != null) {
            lista.add(atual.getDado());
            atual = atual.getProximo();
        }
        if (ordemAZ) {
            Collections.sort(lista, Comparator.comparing(u -> u.getNome().toLowerCase()));
        } else {
            Collections.sort(lista, (u1, u2) -> u2.getNome().toLowerCase().compareTo(u1.getNome().toLowerCase()));
        }
        for (Utilizador u : lista) {
            modeloTabela.addRow(new Object[]{
                u.getId(), u.getNome(), u.getMorada(), u.getCNI(), u.getEmail(), u.getTelefone()
            });
        }
    }

    private JPanel criarFormulario() {
        JPanel externo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        externo.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        JPanel painel = new JPanel();
        painel.setBackground(EstiloUI.COR_CARTAO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                new EmptyBorder(30, 40, 30, 40)));

        GroupLayout layout = new GroupLayout(painel);
        painel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblNome     = new JLabel("Nome:");
        JLabel lblMorada   = new JLabel("Morada:");
        JLabel lblCNI      = new JLabel("CNI:");
        JLabel lblEmail    = new JLabel("Email:");
        JLabel lblTelefone = new JLabel("Telefone:");

        for (JLabel l : new JLabel[]{lblNome, lblMorada, lblCNI, lblEmail, lblTelefone}) {
            l.setFont(EstiloUI.FONTE_CORPO);
        }

        txtNome     = new JTextField(25);
        txtMorada   = new JTextField(25);
        txtCNI      = new JTextField(25);
        txtEmail    = new JTextField(25);
        txtTelefone = new JTextField(25);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpar  = new JButton("Limpar");
        estilizarBotao(btnGuardar, EstiloUI.COR_DESTAQUE);
        estilizarBotao(btnLimpar,  EstiloUI.COR_TEXTO_SECUNDARIO);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblNome).addComponent(lblMorada).addComponent(lblCNI)
                .addComponent(lblEmail).addComponent(lblTelefone))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtNome).addComponent(txtMorada).addComponent(txtCNI)
                .addComponent(txtEmail).addComponent(txtTelefone)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnGuardar).addComponent(btnLimpar)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNome).addComponent(txtNome))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMorada).addComponent(txtMorada))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblCNI).addComponent(txtCNI))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEmail).addComponent(txtEmail))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTelefone).addComponent(txtTelefone))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnGuardar).addComponent(btnLimpar))
        );

        btnGuardar.addActionListener(e -> {
            String nome     = txtNome.getText().trim();
            String morada   = txtMorada.getText().trim();
            String cni      = txtCNI.getText().trim();
            String email    = txtEmail.getText().trim();
            String telefone = txtTelefone.getText().trim();

            if (nome.isEmpty() || cni.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, CNI e Email são obrigatórios!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Utilizador novoUser = new Utilizador(0, nome, morada, cni, email, telefone);
            if (biblioteca.cadastrarUtilizador(novoUser)) {
                JOptionPane.showMessageDialog(this, "Utilizador registado com sucesso!");
                txtNome.setText(""); txtMorada.setText(""); txtCNI.setText("");
                txtEmail.setText(""); txtTelefone.setText("");
                carregarUtilizadores();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao guardar utilizador.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            txtNome.setText(""); txtMorada.setText(""); txtCNI.setText("");
            txtEmail.setText(""); txtTelefone.setText("");
        });

        externo.add(painel);
        return externo;
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
