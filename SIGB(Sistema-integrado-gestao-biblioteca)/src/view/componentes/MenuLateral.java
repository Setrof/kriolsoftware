package src.view.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import src.view.JanelaPrincipal;
import src.view.utils.EstiloUI;

/**
 * Menu de navegação lateral.
 * Cada botão chama janelaPrincipal.mostrarPainel(nome) para trocar o painel central.
 */
public class MenuLateral extends JPanel {

    private JanelaPrincipal janelaPrincipal;

    public MenuLateral(JanelaPrincipal janelaPrincipal) {
        this.janelaPrincipal = janelaPrincipal;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(EstiloUI.COR_MENU_LATERAL);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo / título do sistema
        JLabel lblSistema = new JLabel("SIGB");
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setAlignmentX(CENTER_ALIGNMENT);
        add(lblSistema);



      JLabel lblSub = new JLabel("Sistema Integrado  De Gestão de Biblioteca");
        lblSub.setFont(new Font("Tohoma", Font.BOLD, 9));
        lblSub.setForeground(new Color(148, 163, 184));
        lblSub.setAlignmentX(CENTER_ALIGNMENT);
        add(lblSub);

        add(Box.createVerticalStrut(35));

        // Botões de navegação
        add(criarBotao("Dashboard",    "dashboard"));
        add(Box.createVerticalStrut(8));
        add(criarBotao("Livros",        "livros"));
        add(Box.createVerticalStrut(8));
        add(criarBotao("Utilizadores",  "utilizadores"));
        add(Box.createVerticalStrut(8));
        add(criarBotao("Empréstimos",   "emprestimos"));
        add(Box.createVerticalStrut(8));
        add(criarBotao("Devoluções",    "devolucoes"));
        add(Box.createVerticalStrut(8));
        add(criarBotao("Histórico",     "historico"));

        add(Box.createVerticalGlue());
    }

    private JButton criarBotao(String texto, String nomePainel) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(EstiloUI.COR_MENU_TEXTO);
        btn.setBackground(EstiloUI.COR_MENU_BOTAO);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setAlignmentX(CENTER_ALIGNMENT);


        btn.setBorder(BorderFactory.createEmptyBorder(35, 15,35, 15));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(EstiloUI.COR_MENU_BOTAO_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(EstiloUI.COR_MENU_BOTAO);
            }
        });

        btn.addActionListener(e -> janelaPrincipal.mostrarPainel(nomePainel));
        return btn;
    }
}
