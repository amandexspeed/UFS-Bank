package GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import Data.GerenciarArquivos.GerenciarArquivos;
import Modelos.ModelosPessoa.*;
import RH.GestaoFuncionarios;
import Utilitarios.Excecao;
import Utilitarios.RedeCliente;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.awt.event.ActionEvent;

public class TelaCadastro extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField campoNome;
    private JTextField campoCPF;
    private JTextField campoMatricula;
    private JButton botaoVoltar;
    private JButton botaoCadastrar;

    public TelaCadastro() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);
        
        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Cadastro de Funcionários", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);
        
        // === FORMULÁRIO ===
        JPanel formularioPanel = new JPanel();
        formularioPanel.setBounds(290, 160, 700, 460);
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        formularioPanel.setLayout(null);
        add(formularioPanel);
        
        // Tipo de Funcionário
        JLabel lblTipo = new JLabel("Tipo de Funcionário:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTipo.setForeground(new Color(52, 73, 94));
        lblTipo.setBounds(30, 30, 250, 30);
        formularioPanel.add(lblTipo);
        
        String[] opcoes = {"Caixa", "Gerente de Negócios"};
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setBounds(30, 65, 620, 40);
        comboBox.setBackground(Color.WHITE);
        formularioPanel.add(comboBox);
        
        // Campo Nome
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        lblNome.setForeground(new Color(52, 73, 94));
        lblNome.setBounds(30, 125, 200, 30);
        formularioPanel.add(lblNome);
        
        campoNome = new JTextField();
        campoNome.setFont(new Font("Arial", Font.PLAIN, 18));
        campoNome.setBounds(30, 160, 620, 40);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formularioPanel.add(campoNome);
        
        // Campo CPF
        JLabel lblCPF = new JLabel("CPF (apenas números):");
        lblCPF.setFont(new Font("Arial", Font.BOLD, 18));
        lblCPF.setForeground(new Color(52, 73, 94));
        lblCPF.setBounds(30, 220, 300, 30);
        formularioPanel.add(lblCPF);
        
        campoCPF = new JTextField();
        campoCPF.setFont(new Font("Arial", Font.PLAIN, 18));
        campoCPF.setBounds(30, 255, 280, 40);
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
        
        // Campo Matrícula
        JLabel lblMatricula = new JLabel("Matrícula:");
        lblMatricula.setFont(new Font("Arial", Font.BOLD, 18));
        lblMatricula.setForeground(new Color(52, 73, 94));
        lblMatricula.setBounds(370, 220, 200, 30);
        formularioPanel.add(lblMatricula);
        
        campoMatricula = new JTextField();
        campoMatricula.setFont(new Font("Arial", Font.PLAIN, 18));
        campoMatricula.setBounds(370, 255, 280, 40);
        campoMatricula.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formularioPanel.add(campoMatricula);
        
        campoMatricula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        
        // Botão Cadastrar
        botaoCadastrar = new JButton("CADASTRAR FUNCIONÁRIO");
        botaoCadastrar.setFont(new Font("Arial", Font.BOLD, 18));
        botaoCadastrar.setBounds(180, 350, 320, 60);
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
                String escolha = (String) comboBox.getSelectedItem();
                String nome = campoNome.getText().trim();
                String cpf = campoCPF.getText().trim();
                String matriculaStr = campoMatricula.getText().trim();
                
                // Validações
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, preencha o nome do funcionário.", 
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
                
                if (matriculaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, preencha a matrícula.", 
                        "Campo Obrigatório", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int matricula;
                try {
                    matricula = Integer.parseInt(matriculaStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Matrícula deve conter apenas números.", 
                        "Matrícula Inválida", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean sucesso = enviarSolicitacaoRede(nome, cpf, escolha, matricula);
                if (!sucesso) {
                    return;
                }
                // Limpa os campos
                campoNome.setText("");
                campoCPF.setText("");
                campoMatricula.setText("");
                comboBox.setSelectedIndex(0);
                
                // Atualiza as tabelas
                GestaoFuncionarios.atualizarTabelas();
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

    private boolean enviarSolicitacaoRede(String nome, String cpf, String setor, int matricula) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(15000);
            
            String mensagem = String.format("ADD_FUNCIONARIO;%s;%s;%s;%d", nome, cpf, setor, matricula);
            byte[] buffer = mensagem.getBytes("UTF-8");

            InetAddress address = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, RedeCliente.getServidorPorta());
            
            socket.send(packet);
            System.out.println("[LOG] Pacote UDP enviado: " + mensagem);
            
            byte[] bufferReceberRespostaServidor = new byte[1024];
            DatagramPacket pacoteReceberResposta = new DatagramPacket(bufferReceberRespostaServidor, bufferReceberRespostaServidor.length);
            socket.receive(pacoteReceberResposta);

            String respostaServidor = new String(pacoteReceberResposta.getData(), 0, pacoteReceberResposta.getLength(), "UTF-8");
            System.out.println("[LOG] Resposta SERVIDOR: " + respostaServidor);

                if (respostaServidor.startsWith("OK")) {
                    JOptionPane.showMessageDialog(this, 
                        "Funcionário cadastrado com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else if (respostaServidor.startsWith("ERRO")) {
                    String[] partes = respostaServidor.split(";", 2);
                    String mensagemErro = partes.length > 1 ? partes[1] : "Erro desconhecido.";
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao cadastrar funcionário: " + mensagemErro, 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Resposta inesperada do servidor: " + respostaServidor, 
                        "Erro de Comunicação", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao conectar com o Coordenador.\nVerifique se o servidor está ativo.",
                "Erro de Rede",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
}