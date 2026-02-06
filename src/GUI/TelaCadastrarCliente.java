package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Modelos.ModelosPessoa.Cliente;
import Utilitarios.Excecao;

public class TelaCadastrarCliente extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField campoNome;
    private JTextField campoCPF;
    private JButton botaoVoltar;
    private JButton botaoCadastrar;
    private JButton botaoVisualizarFilas; // NOVO BOTÃO
    private Boolean prioridade = false;

    public TelaCadastrarCliente() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);
        
        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Cadastro de Clientes", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);
        
        // === FORMULÁRIO ===
        JPanel formularioPanel = new JPanel();
        formularioPanel.setBounds(290, 160, 700, 450);
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 3),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        formularioPanel.setLayout(null);
        add(formularioPanel);
        
        // Campo Nome
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        lblNome.setForeground(new Color(52, 73, 94));
        lblNome.setBounds(30, 30, 200, 30);
        formularioPanel.add(lblNome);
        
        campoNome = new JTextField();
        campoNome.setFont(new Font("Arial", Font.PLAIN, 18));
        campoNome.setBounds(30, 65, 620, 40);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formularioPanel.add(campoNome);
        
        // Campo CPF
        JLabel lblCPF = new JLabel("CPF (apenas números):");
        lblCPF.setFont(new Font("Arial", Font.BOLD, 18));
        lblCPF.setForeground(new Color(52, 73, 94));
        lblCPF.setBounds(30, 125, 250, 30);
        formularioPanel.add(lblCPF);
        
        campoCPF = new JTextField();
        campoCPF.setFont(new Font("Arial", Font.PLAIN, 18));
        campoCPF.setBounds(30, 160, 300, 40);
        campoCPF.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formularioPanel.add(campoCPF);
        
        campoCPF.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (campoCPF.getText().length() >= 11) {
                    e.consume();
                }
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        
        // Tipo de Atendimento
        JLabel lblTipoAtendimento = new JLabel("Tipo de Atendimento:");
        lblTipoAtendimento.setFont(new Font("Arial", Font.BOLD, 18));
        lblTipoAtendimento.setForeground(new Color(52, 73, 94));
        lblTipoAtendimento.setBounds(360, 125, 250, 30);
        formularioPanel.add(lblTipoAtendimento);
        
        String[] opcoes = {"Caixa", "Gerente"};
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setBounds(360, 160, 290, 40);
        comboBox.setBackground(Color.WHITE);
        formularioPanel.add(comboBox);
        
        // Prioridade
        JLabel lblPrioridade = new JLabel("Cliente Prioritário?");
        lblPrioridade.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrioridade.setForeground(new Color(52, 73, 94));
        lblPrioridade.setBounds(30, 225, 300, 30);
        formularioPanel.add(lblPrioridade);
        
        JLabel lblInfo = new JLabel("(Idoso, Gestante, Deficiente, etc.)");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(127, 140, 141));
        lblInfo.setBounds(30, 248, 300, 20);
        formularioPanel.add(lblInfo);
        
        JRadioButton radioSim = new JRadioButton("Sim");
        radioSim.setFont(new Font("Arial", Font.PLAIN, 16));
        radioSim.setBounds(30, 280, 100, 30);
        radioSim.setBackground(Color.WHITE);
        formularioPanel.add(radioSim);
        
        JRadioButton radioNao = new JRadioButton("Não");
        radioNao.setFont(new Font("Arial", Font.PLAIN, 16));
        radioNao.setBounds(150, 280, 100, 30);
        radioNao.setBackground(Color.WHITE);
        radioNao.setSelected(true);
        formularioPanel.add(radioNao);
        
        ButtonGroup grupoPrioridade = new ButtonGroup();
        grupoPrioridade.add(radioSim);
        grupoPrioridade.add(radioNao);
        
        radioSim.addActionListener(e -> prioridade = true);
        radioNao.addActionListener(e -> prioridade = false);
        
        // Botão Cadastrar
        botaoCadastrar = new JButton("CADASTRAR CLIENTE");
        botaoCadastrar.setFont(new Font("Arial", Font.BOLD, 18));
        botaoCadastrar.setBounds(180, 360, 320, 60);
        botaoCadastrar.setBackground(new Color(46, 204, 113));
        botaoCadastrar.setForeground(Color.WHITE);
        botaoCadastrar.setFocusPainted(false);
        botaoCadastrar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        botaoCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        formularioPanel.add(botaoCadastrar);
        
        // Efeito hover
        botaoCadastrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoCadastrar.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoCadastrar.setBackground(new Color(46, 204, 113));
            }
        });
        
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = campoNome.getText().trim();
                String cpf = campoCPF.getText().trim();
                String escolha = (String) comboBox.getSelectedItem();
                
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, preencha o nome do cliente.", 
                        "Campo Obrigatório", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (cpf.length() != 11) {
                    JOptionPane.showMessageDialog(null, 
                        "CPF deve ter exatamente 11 dígitos.", 
                        "CPF Inválido", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    Cliente cliente = new Cliente(nome, cpf, prioridade);
                    enviarSolicitacaoRede(nome, cpf, prioridade, escolha);
                    
                    JOptionPane.showMessageDialog(null, 
                        "Cliente " + nome + " cadastrado com sucesso!\n" +
                        "Tipo: " + escolha + "\n" +
                        "Prioritário: " + (prioridade ? "Sim" : "Não"),
                        "Cadastro Realizado", 
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Excecao e1) {
                    JOptionPane.showMessageDialog(null, 
                        "Erro nos dados: " + e1.getMessage(), 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                campoNome.setText("");
                campoCPF.setText("");
                radioNao.setSelected(true);
                prioridade = false;
                comboBox.setSelectedIndex(0);
            }
        });
        
        // === BOTÃO VISUALIZAR FILAS (NOVO) ===
        botaoVisualizarFilas = new JButton("👁️ VISUALIZAR FILAS");
        botaoVisualizarFilas.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVisualizarFilas.setBounds(980, 640, 250, 50);
        botaoVisualizarFilas.setBackground(new Color(41, 128, 185));
        botaoVisualizarFilas.setForeground(Color.WHITE);
        botaoVisualizarFilas.setFocusPainted(false);
        botaoVisualizarFilas.setBorder(BorderFactory.createLineBorder(new Color(31, 97, 141), 2));
        botaoVisualizarFilas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoVisualizarFilas);
        
        botaoVisualizarFilas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoVisualizarFilas.setBackground(new Color(31, 97, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoVisualizarFilas.setBackground(new Color(41, 128, 185));
            }
        });
        
        // === BOTÃO VOLTAR ===
        botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 640, 200, 50);
        botaoVoltar.setBackground(new Color(149, 165, 166));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
        botaoVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoVoltar);
        
        botaoVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(149, 165, 166));
            }
        });
    }

    private void enviarSolicitacaoRede(String nome, String cpf, boolean ehPrioridade, String setor) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            
            String mensagem = String.format("ADD_CLIENTE;%s;%s;%b;%s", nome, cpf, ehPrioridade, setor);
            byte[] buffer = mensagem.getBytes("UTF-8");

            InetAddress address = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 5000);
            
            socket.send(packet);
            System.out.println("[LOG] Pacote UDP enviado: " + mensagem);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao conectar com o Coordenador.\nVerifique se o servidor está ativo.",
                "Erro de Rede",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
    
    public JButton getBotaoVisualizarFilas() {
        return botaoVisualizarFilas;
    }
}