package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Caixa.EscolhaFunc;
import Utilitarios.Excecao;

public class TelaCaixa extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField campoNome;
    private JButton botaoVoltar;
    private JButton botaoEntrar;
    private static JTable caixaTable;
    private static JTable gerenteTable;
    
    public TelaCaixa() {
        setSize(1280, 720);
        setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel titulo = new JLabel("Caixa");
        titulo.setBounds(600, 11, 110, 80);
        titulo.setFont(new Font("Tahoma", Font.PLAIN, 44));
        add(titulo);
        
        campoNome = new JTextField();
        campoNome.setFont(new Font("Tahoma", Font.PLAIN, 24));
        campoNome.setBounds(560, 506, 200, 30);
        add(campoNome);
        campoNome.setColumns(10);
        
        JLabel lblNome = new JLabel("Matrícula");
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblNome.setBounds(600, 464, 118, 32);
        add(lblNome);
        
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBounds(678, 588, 124, 57);
        add(botaoVoltar);
        
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    EscolhaFunc.escolherFuncionario(Integer.parseInt(campoNome.getText()));
                    TelaAtendimento.mudarTexto();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Digite uma matrícula válida.");
                }
            }
        });
        botaoEntrar.setBounds(527, 588, 124, 57);
        add(botaoEntrar);
        
        JLabel lblTextoInformativo = new JLabel("Digite a matrícula do funcionário que irá atender");
        lblTextoInformativo.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTextoInformativo.setBounds(517, 546, 300, 32);
        add(lblTextoInformativo);
        
        JPanel tabelaPanel = new JPanel(new GridLayout(1, 2));
        
        caixaTable = new JTable();
        gerenteTable = new JTable();

        JScrollPane caixaScrollPane = new JScrollPane(caixaTable);
        JScrollPane gerenteScrollPane = new JScrollPane(gerenteTable); 
        
        preencherTabelasRemotamente();
        
        tabelaPanel.setBounds(10, 113, 1260, 322);
        tabelaPanel.add(caixaScrollPane);
        tabelaPanel.add(gerenteScrollPane);
        add(tabelaPanel);
    }
    
    // Regras 1 e 3: Obtém a lista de funcionários do Coordenador Central via UDP
    public static void preencherTabelasRemotamente() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000); // Tempo limite de resposta
            socket.setBroadcast(true);

            String pedido = "LISTAR_FUNCIONARIOS";
            byte[] bufferEnvio = pedido.getBytes();
            InetAddress ip = InetAddress.getByName("255.255.255.255");
            DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ip, 5000);
            socket.send(pacoteEnvio);

            byte[] bufferReceber = new byte[4096];
            DatagramPacket pacoteReceber = new DatagramPacket(bufferReceber, bufferReceber.length);
            socket.receive(pacoteReceber);

            String resposta = new String(pacoteReceber.getData(), 0, pacoteReceber.getLength());
            atualizarUI(resposta);

        } catch (Exception e) {
            System.err.println("Erro ao buscar funcionários na rede: " + e.getMessage());
        }
    }

    private static void atualizarUI(String dados) {
        DefaultTableModel caixaModel = new DefaultTableModel(new Object[]{"Nome", "CPF", "Matrícula"}, 0);
        DefaultTableModel gerenteModel = new DefaultTableModel(new Object[]{"Nome", "CPF", "Matrícula"}, 0);

        String[] linhas = dados.split("\n");
        for (String linha : linhas) {
            String[] colunas = linha.split(";");
            if (colunas.length < 4) continue;

            Object[] row = {colunas[1], colunas[2], colunas[3]};
            if (colunas[0].equals("CAIXA")) {
                caixaModel.addRow(row);
            } else if (colunas[0].equals("GERENTE")) {
                gerenteModel.addRow(row);
            }
        }
        caixaTable.setModel(caixaModel);
        gerenteTable.setModel(gerenteModel);
    }

    public JButton getBotaoVoltar() { return botaoVoltar; }
    public JButton getBotaoEntrar() { return botaoEntrar; }
}