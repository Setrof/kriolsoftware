package src.view.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import src.view.utils.EstiloUI;

/**
 * Componente reutilizável para exibir uma estatística no Dashboard
 */
public class CartaoEstatistica extends JPanel {

    private JLabel lblValor;
    private JLabel lblTitulo;

    public CartaoEstatistica(String titulo, String valor, Color corDestaque) {
        setLayout(new BorderLayout(5, 5));
        setBackground(EstiloUI.COR_CARTAO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA, 1, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));
        setPreferredSize(new Dimension(200, 110));

        // Barra de cor no topo
        JPanel barraTop = new JPanel();
        barraTop.setBackground(corDestaque);
        barraTop.setPreferredSize(new Dimension(0, 5));
        add(barraTop, BorderLayout.NORTH);

        // Valor grande
        lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(EstiloUI.FONTE_TITULO);
        lblValor.setForeground(corDestaque);
        add(lblValor, BorderLayout.CENTER);

        // Título descritivo em baixo
        lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(EstiloUI.FONTE_CORPO);
        lblTitulo.setForeground(EstiloUI.COR_TEXTO_SECUNDARIO);
        add(lblTitulo, BorderLayout.SOUTH);
    }

    /** Atualiza o valor exibido no cartão (chamado pela view após carregar dados). */
    public void setValor(String novoValor) {
        lblValor.setText(novoValor);
    }
}
