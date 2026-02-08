package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Caixa.EscolhaFunc;
import Modelos.ModelosPessoa.Caixa;
import Modelos.ModelosPessoa.GerenteNegocios;
import Utilitarios.RedeCliente;

public class TelaAtendimento extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JLabel nomeAtendente;
    private static JLabel clienteAtual;
    private JButton botaoVoltar;
    private JButton botaoChamarProximo;

    public TelaAtendimento() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);

        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setLayout(null);
        add(headerPanel);

        JLabel titulo = new JLabel("Sistema de Atendimento", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        // === PAINEL DO ATENDENTE ===
        JPanel painelAtendente = new JPanel();
        painelAtendente.setBounds(100, 160, 1080, 120);
        painelAtendente.setBackground(Color.WHITE);
        painelAtendente.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Informações do Atendente",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));
        painelAtendente.setLayout(null);
        add(painelAtendente);

        JLabel lblAtendente = new JLabel("Atendente:");
        lblAtendente.setFont(new Font("Arial", Font.BOLD, 22));
        lblAtendente.setForeground(new Color(52, 73, 94));
        lblAtendente.setBounds(30, 40, 150, 40);
        painelAtendente.add(lblAtendente);

        nomeAtendente = new JLabel("Não selecionado");
        nomeAtendente.setFont(new Font("Arial", Font.PLAIN, 22));
        nomeAtendente.setForeground(new Color(231, 76, 60));
        nomeAtendente.setBounds(180, 40, 850, 40);
        painelAtendente.add(nomeAtendente);

        // === PAINEL DO CLIENTE ===
        JPanel painelCliente = new JPanel();
        painelCliente.setBounds(100, 310, 1080, 200);
        painelCliente.setBackground(Color.WHITE);
        painelCliente.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Cliente em Atendimento",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(46, 204, 113)
        ));
        painelCliente.setLayout(null);
        add(painelCliente);

        JLabel lblCliente = new JLabel("Cliente Atual:");
        lblCliente.setFont(new Font("Arial", Font.BOLD, 26));
        lblCliente.setForeground(new Color(52, 73, 94));
        lblCliente.setBounds(30, 50, 200, 40);
        painelCliente.add(lblCliente);

        clienteAtual = new JLabel("Aguardando chamada...");
        clienteAtual.setFont(new Font("Arial", Font.BOLD, 32));
        clienteAtual.setForeground(new Color(149, 165, 166));
        clienteAtual.setBounds(30, 100, 1020, 50);
        painelCliente.add(clienteAtual);

        // === BOTÕES ===
        botaoChamarProximo = new JButton("CHAMAR PRÓXIMO CLIENTE");
        botaoChamarProximo.setFont(new Font("Arial", Font.BOLD, 20));
        botaoChamarProximo.setBounds(350, 550, 580, 70);
        botaoChamarProximo.setBackground(new Color(46, 204, 113));
        botaoChamarProximo.setForeground(Color.WHITE);
        botaoChamarProximo.setFocusPainted(false);
        botaoChamarProximo.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        botaoChamarProximo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoChamarProximo);

        // Efeito hover no botão chamar
        botaoChamarProximo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoChamarProximo.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoChamarProximo.setBackground(new Color(46, 204, 113));
            }
        });

        botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(100, 550, 200, 70);
        botaoVoltar.setBackground(new Color(149, 165, 166));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
        botaoVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoVoltar);

        // Efeito hover no botão voltar
        botaoVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoVoltar.setBackground(new Color(149, 165, 166));
            }
        });

        // Ação do botão Chamar Próximo
        botaoChamarProximo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chamarProximoClienteRemoto();
            }
        });
    }

    private static String obterTipoFuncionario() {
        if (EscolhaFunc.funcionarioEscolhido instanceof GerenteNegocios) {
            return "Gerente";
        } else if (EscolhaFunc.funcionarioEscolhido instanceof Caixa) {
            return "Caixa";
        }
        return "Desconhecido";
    }

    public static void mudarTexto() {
        if (EscolhaFunc.funcionarioEscolhido != null) {
            String tipo = obterTipoFuncionario();
            nomeAtendente.setText(EscolhaFunc.funcionarioEscolhido.getNome() + " - " + tipo);
            nomeAtendente.setForeground(new Color(39, 174, 96));
        }
    }

    private void chamarProximoClienteRemoto() {
        if (EscolhaFunc.funcionarioEscolhido == null) {
            JOptionPane.showMessageDialog(this, "Nenhum funcionário selecionado!");
            return;
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.setBroadcast(true);

            String tipoFuncionario = obterTipoFuncionario();
            String mensagem = "CHAMAR_PROXIMO;" + tipoFuncionario;
            byte[] bufferEnvio = mensagem.getBytes("UTF-8");

            InetAddress ip = InetAddress.getByName("255.255.255.255");
            DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ip, RedeCliente.getServidorPorta());
            socket.send(pacoteEnvio);

            System.out.println("[LOG] Solicitação enviada: " + mensagem);

            byte[] bufferReceber = new byte[1024];
            DatagramPacket pacoteReceber = new DatagramPacket(bufferReceber, bufferReceber.length);
            socket.receive(pacoteReceber);

            String resposta = new String(pacoteReceber.getData(), 0, pacoteReceber.getLength(), "UTF-8");
            System.out.println("[LOG] Resposta recebida: " + resposta);

            if (resposta.startsWith("CLIENTE;")) {
                String nomeCliente = resposta.split(";")[1];
                clienteAtual.setText("🟢 " + nomeCliente);
                clienteAtual.setForeground(new Color(39, 174, 96));
                
                JOptionPane.showMessageDialog(this, 
                    "Cliente " + nomeCliente + " chamado com sucesso!", 
                    "Atendimento", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } else if (resposta.equals("FILA_VAZIA")) {
                clienteAtual.setText("❌ Fila vazia - Nenhum cliente aguardando");
                clienteAtual.setForeground(new Color(231, 76, 60));
                
                JOptionPane.showMessageDialog(this, 
                    "Não há clientes na fila!", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                clienteAtual.setText("⚠ Erro desconhecido");
                clienteAtual.setForeground(new Color(230, 126, 34));
            }

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("[ERRO] Timeout: Coordenador não respondeu");
            clienteAtual.setText("⚠ Coordenador não está respondendo");
            clienteAtual.setForeground(new Color(230, 126, 34));
            
            JOptionPane.showMessageDialog(this, 
                "Erro: Coordenador não está respondendo.\nVerifique se o servidor está ativo.", 
                "Erro de Rede", 
                JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na comunicação: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao conectar com o Coordenador: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
    
    public JButton getBotaoChamarProximo() {
        return botaoChamarProximo;
    }
}