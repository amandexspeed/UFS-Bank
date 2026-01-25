package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Caixa.EscolhaFunc;

public class TelaAtendimento extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JLabel lblNomeFuncionario;
    private static JLabel lblNomeCliente;
    private JButton botaoVoltar;

    public TelaAtendimento() {
        setSize(1280, 720);
        setBackground(Color.WHITE);
        setLayout(null);

        JLabel titulo = new JLabel("Atendimento UFS-Bank");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(400, 50, 480, 60);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 36));
        add(titulo);

        lblNomeFuncionario = new JLabel("Funcionário: -");
        lblNomeFuncionario.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNomeFuncionario.setBounds(100, 150, 500, 30);
        add(lblNomeFuncionario);

        lblNomeCliente = new JLabel("Aguardando cliente...");
        lblNomeCliente.setHorizontalAlignment(SwingConstants.CENTER);
        lblNomeCliente.setFont(new Font("Tahoma", Font.ITALIC, 28));
        lblNomeCliente.setBounds(390, 300, 500, 50);
        lblNomeCliente.setForeground(Color.BLUE);
        add(lblNomeCliente);

        JButton botaoChamar = new JButton("Chamar Próximo");
        botaoChamar.setFont(new Font("Tahoma", Font.BOLD, 18));
        botaoChamar.setBounds(540, 400, 200, 60);
        add(botaoChamar);

        botaoVoltar = new JButton("Sair");
        botaoVoltar.setBounds(590, 550, 100, 40);
        add(botaoVoltar);

        // AÇÃO DE CHAMAR CLIENTE VIA REDE (Regras 1 e 2)
        botaoChamar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chamarClienteRemoto();
            }
        });
    }

    private void chamarClienteRemoto() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true); // Regra 3
            socket.setSoTimeout(3000); 
           
            String matricula = String.valueOf(EscolhaFunc.funcionarioEscolhido.getMatricula());
            String mensagem = "CHAMAR_PROXIMO;" + matricula;
            byte[] buffer = mensagem.getBytes();

            InetAddress ip = InetAddress.getByName("255.255.255.255");
            DatagramPacket pacoteEnvio = new DatagramPacket(buffer, buffer.length, ip, 5000);
            socket.send(pacoteEnvio);

            // Recebendo a resposta do Coordenador Central (Regra 2)
            byte[] respostaBuffer = new byte[1024];
            DatagramPacket pacoteResposta = new DatagramPacket(respostaBuffer, respostaBuffer.length);
            
            try {
                socket.receive(pacoteResposta);
                String resposta = new String(pacoteResposta.getData(), 0, pacoteResposta.getLength());
                
                if (resposta.startsWith("CLIENTE;")) {
                    String nomeCliente = resposta.split(";")[1];
                    lblNomeCliente.setText("Atendendo: " + nomeCliente);
                } else if (resposta.equals("FILA_VAZIA")) {
                    lblNomeCliente.setText("Não há clientes na fila.");
                }
            } catch (SocketTimeoutException e) {
                JOptionPane.showMessageDialog(this, "Coordenador central fora de linha.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mudarTexto() {
        if (EscolhaFunc.funcionarioEscolhido != null) {
            lblNomeFuncionario.setText("Funcionário: " + EscolhaFunc.funcionarioEscolhido.getNome());
        }
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
}