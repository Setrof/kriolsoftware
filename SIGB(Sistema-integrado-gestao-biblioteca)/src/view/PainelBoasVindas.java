package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.service.Biblioteca;
import src.view.utils.EstiloUI;

public class PainelBoasVindas extends JPanel {

    public PainelBoasVindas(Biblioteca biblioteca) {
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);

        // Banner com gradiente
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 41, 59), getWidth(), getHeight(), new Color(59, 130, 246)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        int hora = java.time.LocalTime.now().getHour();
        String saudacao = hora < 12 ? "Bom dia!" : hora < 18 ? "Boa tarde!" : "Boa noite!";

        JLabel lblSaudacao = new JLabel(saudacao);
        lblSaudacao.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblSaudacao.setForeground(Color.WHITE);
        lblSaudacao.setAlignmentX(LEFT_ALIGNMENT);
        banner.add(lblSaudacao);

        JLabel lblSub = new JLabel("Bem-vindo ao SIGB — Sistema Integrado de Gestão de Biblioteca");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(191, 219, 254));
        lblSub.setAlignmentX(LEFT_ALIGNMENT);
        banner.add(lblSub);

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));
        JLabel lblData = new JLabel(data);
        lblData.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblData.setForeground(new Color(148, 163, 184));
        lblData.setAlignmentX(LEFT_ALIGNMENT);
        banner.add(lblData);

        add(banner, BorderLayout.NORTH);

        // Centro: atalhos rápidos
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblAcoes = new JLabel("Ações Rápidas");
        lblAcoes.setFont(EstiloUI.FONTE_SUBTITULO);
        lblAcoes.setForeground(EstiloUI.COR_TEXTO_SECUNDARIO);
        centro.add(lblAcoes);
        centro.add(Box.createVerticalStrut(12));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        botoes.setBackground(EstiloUI.COR_FUNDO_PRINCIPAL);
        botoes.add(criarAtalho("Dashboard", "dashboard", EstiloUI.COR_DESTAQUE));
        botoes.add(criarAtalho("Livros", "livros", EstiloUI.COR_SUCESSO));
        botoes.add(criarAtalho("Utilizadores", "utilizadores", new Color(139, 92, 246)));
        botoes.add(criarAtalho("Empréstimos", "emprestimos", EstiloUI.COR_AVISO));
        botoes.add(criarAtalho("Devoluções", "devolucoes", EstiloUI.COR_PERIGO));
        botoes.add(criarAtalho("Transações", "transacoes", new Color(236, 72, 153)));
        centro.add(botoes);

        add(centro, BorderLayout.CENTER);
    }

    private JButton criarAtalho(String texto, String painel, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(EstiloUI.FONTE_CORPO);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new java.awt.Dimension(130, 38));
        btn.addActionListener(e -> {
            java.awt.Container p = getParent();
            while (p != null && !(p instanceof JanelaPrincipal)) p = p.getParent();
            if (p != null) ((JanelaPrincipal) p).mostrarPainel(painel);
        });
        return btn;
    }
}
