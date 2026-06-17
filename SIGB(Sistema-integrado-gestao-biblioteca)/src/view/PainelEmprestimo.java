package src.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.service.Biblioteca;
import src.view.componentes.Cabecalho;
import src.view.utils.EstiloUI;

public class PainelEmprestimo extends JPanel {

    private Biblioteca biblioteca;
    private JTextField txtISBN;
    private JTextField txtIDUtilizador;
    private JTextField txtDataDevolucao; // Novo Campo de Data Opcional

    public PainelEmprestimo(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        add(new Cabecalho("Registar Novo Empréstimo"), BorderLayout.NORTH);

        JPanel externo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        externo.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        JPanel painelForm = new JPanel();
        painelForm.setBackground(EstiloUI.COR_CARTAO);
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA),
                new EmptyBorder(35, 45, 35, 45)));

        GroupLayout layout = new GroupLayout(painelForm);
        painelForm.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblISBN = new JLabel("ISBN do Livro:");
        JLabel lblIDUser = new JLabel("ID do Utilizador:");
        JLabel lblData = new JLabel("Data Devolução (Opcional):");
        JLabel lblDica = new JLabel("Formato: dd-mm-yyyy. Se vazio, assume 15 dias automaticamente.");
        
        lblISBN.setFont(EstiloUI.FONTE_CORPO);
        lblIDUser.setFont(EstiloUI.FONTE_CORPO);
        lblData.setFont(EstiloUI.FONTE_CORPO);
        lblDica.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 11));
        lblDica.setForeground(EstiloUI.COR_TEXTO_SECUNDARIO);

        txtISBN = new JTextField(25);
        txtIDUtilizador = new JTextField(25);
        txtDataDevolucao = new JTextField(25);
        
        txtISBN.setFont(EstiloUI.FONTE_CORPO);
        txtIDUtilizador.setFont(EstiloUI.FONTE_CORPO);
        txtDataDevolucao.setFont(EstiloUI.FONTE_CORPO);

        JButton btnSubmeter = new JButton("Confirmar Empréstimo");
        JButton btnLimpar = new JButton("Limpar");
        estilizarBotao(btnSubmeter, EstiloUI.COR_DESTAQUE);
        estilizarBotao(btnLimpar, EstiloUI.COR_TEXTO_SECUNDARIO);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblISBN).addComponent(lblIDUser).addComponent(lblData))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtISBN).addComponent(txtIDUtilizador).addComponent(txtDataDevolucao).addComponent(lblDica)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnSubmeter).addComponent(btnLimpar)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblISBN).addComponent(txtISBN))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblIDUser).addComponent(txtIDUtilizador))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblData).addComponent(txtDataDevolucao))
            .addComponent(lblDica)
            .addGap(15)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnSubmeter).addComponent(btnLimpar))
        );

        btnSubmeter.addActionListener(e -> {
            String isbn = txtISBN.getText().trim();
            String idStr = txtIDUtilizador.getText().trim();
            String dataStr = txtDataDevolucao.getText().trim();

            if (isbn.isEmpty() || idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o ISBN e o ID do Utilizador!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idUtilizador;
            try {
                idUtilizador = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O ID do Utilizador tem de ser um número válido!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Executa passando o parâmetro da data digitada
            String resultado = biblioteca.realizarEmprestimo(isbn, idUtilizador, dataStr);
            
            if (resultado.startsWith("Erro")) {
                JOptionPane.showMessageDialog(this, resultado, "Erro no Processamento", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtISBN.setText("");
                txtIDUtilizador.setText("");
                txtDataDevolucao.setText("");
            }
        });

        btnLimpar.addActionListener(e -> {
            txtISBN.setText("");
            txtIDUtilizador.setText("");
            txtDataDevolucao.setText("");
        });

        externo.add(painelForm);
        add(externo, BorderLayout.CENTER);
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