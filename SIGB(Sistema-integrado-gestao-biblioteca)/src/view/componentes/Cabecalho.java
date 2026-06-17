package src.view.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import src.view.utils.EstiloUI;

public class Cabecalho extends JPanel {

    private JLabel lblTitulo;
    private JLabel lblData;
    private Timer timer;
    private static final DateTimeFormatter FORMATO_DATA_HORA = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");

    public Cabecalho(String tituloPainel) {
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COR_CARTAO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstiloUI.COR_BORDA),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        lblTitulo = new JLabel(tituloPainel);
        lblTitulo.setFont(EstiloUI.FONTE_TITULO);
        lblTitulo.setForeground(EstiloUI.COR_TEXTO_PRINCIPAL);
        add(lblTitulo, BorderLayout.WEST);

        lblData = new JLabel();
        lblData.setFont(EstiloUI.FONTE_PEQUENA);
        lblData.setForeground(EstiloUI.COR_TEXTO_SECUNDARIO);

        JPanel painelDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelDireita.setOpaque(false);
        painelDireita.add(lblData);
        add(painelDireita, BorderLayout.EAST);

        // Timer que dispara a cada 1000ms (1 segundo)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarDataHora();
            }
        });
        timer.start();
        atualizarDataHora(); // exibe imediatamente sem esperar 1s
    }

    private void atualizarDataHora() {
        lblData.setText(LocalDateTime.now().format(FORMATO_DATA_HORA));
    }

    /**
     * Para o timer quando o componente não estiver mais em uso,
     * evitando vazamento de memória.
     */
    public void pararTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        pararTimer(); // para automaticamente ao remover da tela
    }
}