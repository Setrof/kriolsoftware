package src.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import src.service.Biblioteca;
import src.view.componentes.MenuLateral;

public class JanelaPrincipal extends JFrame {

    private JPanel painelConteudo;
    private CardLayout cardLayout;

    private Biblioteca biblioteca;

    public JanelaPrincipal(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;

        setTitle("SIGB - Sistema Integrado de Gestão de Biblioteca");
        setSize(1300, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        MenuLateral menu = new MenuLateral(this);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(new PainelDashBoard(biblioteca),    "dashboard");
        painelConteudo.add(new PainelLivros(biblioteca),       "livros");
        painelConteudo.add(new PainelUtilizadores(biblioteca), "utilizadores");
        painelConteudo.add(new PainelEmprestimo(biblioteca),   "emprestimos");
        painelConteudo.add(new PainelDevolucoes(biblioteca),   "devolucoes");
        painelConteudo.add(new PainelHistorico(biblioteca),    "historico");

        add(menu, BorderLayout.WEST);
        add(painelConteudo, BorderLayout.CENTER);

        cardLayout.show(painelConteudo, "dashboard");
    }

    public void mostrarPainel(String nomePainel) {
        cardLayout.show(painelConteudo, nomePainel);

        if ("historico".equals(nomePainel)) {
            for (java.awt.Component c : painelConteudo.getComponents()) {
                if (c instanceof PainelHistorico) {
                    ((PainelHistorico) c).atualizarDados();
                    break;
                }
            }
        } else if ("dashboard".equals(nomePainel)) {
            for (java.awt.Component c : painelConteudo.getComponents()) {
                if (c instanceof PainelDashBoard) {
                    ((PainelDashBoard) c).atualizarDados();
                    break;
                }
            }
        }
    }
}
