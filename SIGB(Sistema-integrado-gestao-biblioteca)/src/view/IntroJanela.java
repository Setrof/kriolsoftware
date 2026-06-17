package src.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import src.service.Biblioteca;

public class IntroJanela extends JFrame {

    private Timer timer;
    private int progresso = 0;

    public IntroJanela(Biblioteca biblioteca, Object[] lock) {
        setUndecorated(true);
        setSize(420, 250);
        setLocationRelativeTo(null);

        JPanel fundo = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 41, 59), getWidth(), getHeight(), new Color(59, 130, 246)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JLabel lblTitulo = new JLabel("SIGB", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 50, 420, 45);
        fundo.add(lblTitulo);

        JLabel lblSub = new JLabel("Sistema Integrado de Gestão de Biblioteca", JLabel.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(191, 219, 254));
        lblSub.setBounds(0, 100, 420, 20);
        fundo.add(lblSub);

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setBounds(80, 170, 260, 4);
        barra.setBackground(new Color(51, 65, 85));
        barra.setForeground(Color.WHITE);
        barra.setBorderPainted(false);
        barra.setStringPainted(false);
        fundo.add(barra);

        setContentPane(fundo);

        timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progresso += 2;
                barra.setValue(progresso);
                if (progresso >= 100) {
                    timer.stop();
                    dispose();
                    try {
                        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ignored) {}
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JanelaPrincipal janela = new JanelaPrincipal(biblioteca);
                        janela.setVisible(true);
                        janela.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent ev) {
                                synchronized (lock) {
                                    lock[0] = Boolean.TRUE;
                                    lock.notifyAll();
                                }
                            }
                        });
                    });
                }
            }
        });
        timer.start();
    }
}
