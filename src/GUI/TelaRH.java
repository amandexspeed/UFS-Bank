package GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaRH extends JPanel {

    private static final long serialVersionUID = 1L;
    
    JButton botaoCadastrar;
    JButton botaoListar;
    JButton botaoVoltar;
    JButton botaoExcluir;
    
    public TelaRH() {
        setSize(1280, 720);
        setLayout(null);
        setBackground(new Color(245, 245, 250));
        
        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(46, 204, 113)); // Verde para RH
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Recursos Humanos", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);
        
        // === ÁREA DE CONTEÚDO ===
        JLabel lblSelecione = new JLabel("Gerenciamento de Funcionários", SwingConstants.CENTER);
        lblSelecione.setFont(new Font("Arial", Font.BOLD, 24));
        lblSelecione.setForeground(new Color(52, 73, 94));
        lblSelecione.setBounds(0, 150, 1280, 40);
        add(lblSelecione);
        
        // === GRID DE BOTÕES (2x2) ===
        
        // Linha 1
        botaoCadastrar = criarBotaoEstilizado(
            "Cadastrar", 
            "Adicionar novos funcionários", 
            new Color(52, 152, 219),
            "+"
        );
        botaoCadastrar.setBounds(240, 230, 380, 160);
        add(botaoCadastrar);
        
        botaoListar = criarBotaoEstilizado(
            "Listar", 
            "Ver todos os funcionários", 
            new Color(155, 89, 182),
            "📋"
        );
        botaoListar.setBounds(660, 230, 380, 160);
        add(botaoListar);
        
        // Linha 2
        botaoExcluir = criarBotaoEstilizado(
            "Excluir", 
            "Remover funcionários", 
            new Color(231, 76, 60),
            "🗑"
        );
        botaoExcluir.setBounds(240, 420, 380, 160);
        add(botaoExcluir);
        
        // Botão Voltar (menor, no canto)
        botaoVoltar = new JButton("← Voltar ao Menu");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 640, 250, 50);
        botaoVoltar.setBackground(new Color(149, 165, 166));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
        botaoVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoVoltar);
        
        // Efeito hover botão voltar
        botaoVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(149, 165, 166));
            }
        });
        
        // === PAINEL DE INFORMAÇÕES ===
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(660, 420, 380, 160);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        infoPanel.setLayout(null);
        add(infoPanel);
        
        JLabel lblInfo = new JLabel("ℹ️ Informações", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 20));
        lblInfo.setForeground(new Color(52, 73, 94));
        lblInfo.setBounds(0, 10, 340, 30);
        infoPanel.add(lblInfo);
        
        JLabel lblTexto1 = new JLabel("• Cadastre caixas e gerentes");
        lblTexto1.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTexto1.setForeground(new Color(52, 73, 94));
        lblTexto1.setBounds(20, 50, 300, 25);
        infoPanel.add(lblTexto1);
        
        JLabel lblTexto2 = new JLabel("• Visualize todos cadastrados");
        lblTexto2.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTexto2.setForeground(new Color(52, 73, 94));
        lblTexto2.setBounds(20, 75, 300, 25);
        infoPanel.add(lblTexto2);
        
        JLabel lblTexto3 = new JLabel("• Remova funcionários inativos");
        lblTexto3.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTexto3.setForeground(new Color(52, 73, 94));
        lblTexto3.setBounds(20, 100, 300, 25);
        infoPanel.add(lblTexto3);
    }
    
    /**
     * Cria um botão estilizado com ícone, título e descrição
     */
    private JButton criarBotaoEstilizado(String titulo, String descricao, Color cor, String icone) {
        JButton botao = new JButton();
        botao.setLayout(null);
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker(), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
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
        
        // Ícone
        JLabel lblIcone = new JLabel(icone, SwingConstants.CENTER);
        lblIcone.setFont(new Font("Arial", Font.BOLD, 48));
        lblIcone.setForeground(Color.WHITE);
        lblIcone.setBounds(0, 20, 380, 50);
        botao.add(lblIcone);
        
        // Título do botão
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 75, 380, 40);
        botao.add(lblTitulo);
        
        // Descrição do botão
        JLabel lblDescricao = new JLabel(descricao, SwingConstants.CENTER);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescricao.setForeground(new Color(236, 240, 241));
        lblDescricao.setBounds(0, 120, 380, 25);
        botao.add(lblDescricao);
        
        return botao;
    }
    
    public JButton getBotaoCadastrar() {
        return botaoCadastrar;
    }
    
    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
    
    public JButton getBotaoListar() {
        return botaoListar;
    }
    
    public JButton getBotaoExcluir() {
        return botaoExcluir;
    }
}