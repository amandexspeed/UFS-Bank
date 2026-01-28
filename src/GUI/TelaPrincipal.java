package GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;
    
    JButton botaoCaixa;
    JButton botaoRH;
    JButton botaoRecepcao;
    
    public TelaPrincipal() {
        setSize(1280, 720);
        setLayout(null);
        setBackground(new Color(245, 245, 250)); // Cor de fundo suave
        
        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 150);
        headerPanel.setBackground(new Color(41, 128, 185)); // Azul profissional
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Sistema Bank Itabaiana", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 30, 1280, 60);
        headerPanel.add(titulo);
        
        JLabel subtitulo = new JLabel("Sistema de Gerenciamento Bancário", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(new Color(236, 240, 241));
        subtitulo.setBounds(0, 90, 1280, 30);
        headerPanel.add(subtitulo);
        
        // === ÁREA DE BOTÕES ===
        JLabel lblSelecione = new JLabel("Selecione o setor:", SwingConstants.CENTER);
        lblSelecione.setFont(new Font("Arial", Font.BOLD, 22));
        lblSelecione.setForeground(new Color(52, 73, 94));
        lblSelecione.setBounds(0, 180, 1280, 40);
        add(lblSelecione);
        
        // Botão RH
        botaoRH = criarBotaoEstilizado("RH", "Recursos Humanos", new Color(46, 204, 113));
        botaoRH.setBounds(190, 270, 280, 180);
        add(botaoRH);
        
        // Botão Caixa
        botaoCaixa = criarBotaoEstilizado("Caixa", "Atendimento ao Cliente", new Color(52, 152, 219));
        botaoCaixa.setBounds(500, 270, 280, 180);
        add(botaoCaixa);
        
        // Botão Recepção
        botaoRecepcao = criarBotaoEstilizado("Recepção", "Cadastro de Clientes", new Color(155, 89, 182));
        botaoRecepcao.setBounds(810, 270, 280, 180);
        add(botaoRecepcao);
        
        // === RODAPÉ ===
        JLabel rodape = new JLabel("© 2026 Sistema Bank Itabaiana - Todos os direitos reservados", SwingConstants.CENTER);
        rodape.setFont(new Font("Arial", Font.PLAIN, 12));
        rodape.setForeground(new Color(127, 140, 141));
        rodape.setBounds(0, 660, 1280, 30);
        add(rodape);
    }
    
    /**
     * Cria um botão estilizado com título e descrição
     */
    private JButton criarBotaoEstilizado(String titulo, String descricao, Color cor) {
        JButton botao = new JButton();
        botao.setLayout(null);
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createLineBorder(cor.darker(), 2));
        botao.setFocusPainted(false);
        botao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Adiciona efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });
        
        // Título do botão
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 40, 280, 50);
        botao.add(lblTitulo);
        
        // Descrição do botão
        JLabel lblDescricao = new JLabel(descricao, SwingConstants.CENTER);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescricao.setForeground(new Color(236, 240, 241));
        lblDescricao.setBounds(0, 100, 280, 30);
        botao.add(lblDescricao);
        
        return botao;
    }
    
    public JButton getBotaoRH() {
        return botaoRH;
    }
    
    public JButton getBotaoCaixa() {
        return botaoCaixa;
    }

    public JButton getBotaoRecepcao() {
        return botaoRecepcao;
    }
}