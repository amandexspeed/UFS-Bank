package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Caixa.EscolhaFunc;

public class TelaCaixa extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField campoMatricula;
    private JButton botaoVoltar;
    private JButton botaoEntrar;
    private static JTable caixaTable;
    private static JTable gerenteTable;
    
    public TelaCaixa() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);
        
        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Seleção de Funcionário", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);
        
        // === PAINEL DE TABELAS ===
        JLabel lblListaFunc = new JLabel("Funcionários Disponíveis:", SwingConstants.CENTER);
        lblListaFunc.setFont(new Font("Arial", Font.BOLD, 20));
        lblListaFunc.setForeground(new Color(52, 73, 94));
        lblListaFunc.setBounds(0, 140, 1280, 30);
        add(lblListaFunc);
        
        JPanel tabelaPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tabelaPanel.setBounds(50, 180, 1180, 280);
        tabelaPanel.setBackground(new Color(245, 245, 250));
        
        // Tabela de Caixas
        caixaTable = new JTable();
        estilizarTabela(caixaTable);
        JScrollPane caixaScrollPane = new JScrollPane(caixaTable);
        caixaScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Caixas",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));
        
        // Tabela de Gerentes
        gerenteTable = new JTable();
        estilizarTabela(gerenteTable);
        JScrollPane gerenteScrollPane = new JScrollPane(gerenteTable);
        gerenteScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Gerentes",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(155, 89, 182)
        ));
        
        tabelaPanel.add(caixaScrollPane);
        tabelaPanel.add(gerenteScrollPane);
        add(tabelaPanel);
        
        // Preenche as tabelas ao carregar
        preencherTabelasRemotamente();
        
        // === PAINEL DE LOGIN ===
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(350, 490, 580, 150);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Acesso ao Sistema",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));
        loginPanel.setLayout(null);
        add(loginPanel);
        
        JLabel lblMatricula = new JLabel("Digite a Matrícula:");
        lblMatricula.setFont(new Font("Arial", Font.BOLD, 18));
        lblMatricula.setForeground(new Color(52, 73, 94));
        lblMatricula.setBounds(50, 35, 200, 30);
        loginPanel.add(lblMatricula);
        
        campoMatricula = new JTextField();
        campoMatricula.setFont(new Font("Arial", Font.PLAIN, 20));
        campoMatricula.setBounds(250, 35, 280, 35);
        campoMatricula.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(campoMatricula);
        
        botaoEntrar = new JButton("ENTRAR");
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoEntrar.setBounds(200, 85, 180, 45);
        botaoEntrar.setBackground(new Color(46, 204, 113));
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        botaoEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginPanel.add(botaoEntrar);
        
        // Efeito hover botão entrar
        botaoEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoEntrar.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoEntrar.setBackground(new Color(46, 204, 113));
            }
        });
        
        botaoEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int matricula = Integer.parseInt(campoMatricula.getText().trim());
                    EscolhaFunc.escolherFuncionario(matricula);
                    TelaAtendimento.mudarTexto();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, digite uma matrícula válida (apenas números).",
                        "Erro de Entrada",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // === BOTÃO VOLTAR ===
        botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 660, 200, 40);
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
    }
    
    /**
     * Estiliza a aparência das tabelas
     */
    private void estilizarTabela(JTable tabela) {
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setSelectionBackground(new Color(52, 152, 219));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(189, 195, 199));
        
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(new Color(52, 73, 94));
        header.setPreferredSize(new java.awt.Dimension(100, 35));
    }
    
    /**
     * Busca TANTO funcionários (caixas) QUANTO gerentes do Coordenador Central
     */
    public static void preencherTabelasRemotamente() {
        DefaultTableModel caixaModel = new DefaultTableModel(new Object[]{"Nome", "CPF", "Matrícula"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        
        DefaultTableModel gerenteModel = new DefaultTableModel(new Object[]{"Nome", "CPF", "Matrícula"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.setBroadcast(true);
            InetAddress ip = InetAddress.getByName("255.255.255.255");

            // ===== BUSCAR CAIXAS =====
            String pedidoCaixas = "LISTAR_FUNCIONARIOS";
            byte[] bufferEnvioCaixas = pedidoCaixas.getBytes("UTF-8");
            DatagramPacket pacoteEnvioCaixas = new DatagramPacket(bufferEnvioCaixas, bufferEnvioCaixas.length, ip, 5000);
            socket.send(pacoteEnvioCaixas);

            byte[] bufferReceberCaixas = new byte[8192];
            DatagramPacket pacoteReceberCaixas = new DatagramPacket(bufferReceberCaixas, bufferReceberCaixas.length);
            socket.receive(pacoteReceberCaixas);

            String respostaCaixas = new String(pacoteReceberCaixas.getData(), 0, pacoteReceberCaixas.getLength(), "UTF-8");
            System.out.println("[LOG] Resposta CAIXAS: " + respostaCaixas);
            
            if (!respostaCaixas.equals("VAZIO")) {
                String[] linhasCaixas = respostaCaixas.split("\n");
                for (String linha : linhasCaixas) {
                    String[] colunas = linha.split(";");
                    if (colunas.length >= 4 && colunas[0].equals("CAIXA")) {
                        caixaModel.addRow(new Object[]{colunas[1], colunas[2], colunas[3]});
                    }
                }
            }

            // ===== BUSCAR GERENTES =====
            String pedidoGerentes = "LISTAR_GERENTES";
            byte[] bufferEnvioGerentes = pedidoGerentes.getBytes("UTF-8");
            DatagramPacket pacoteEnvioGerentes = new DatagramPacket(bufferEnvioGerentes, bufferEnvioGerentes.length, ip, 5000);
            socket.send(pacoteEnvioGerentes);

            byte[] bufferReceberGerentes = new byte[8192];
            DatagramPacket pacoteReceberGerentes = new DatagramPacket(bufferReceberGerentes, bufferReceberGerentes.length);
            socket.receive(pacoteReceberGerentes);

            String respostaGerentes = new String(pacoteReceberGerentes.getData(), 0, pacoteReceberGerentes.getLength(), "UTF-8");
            System.out.println("[LOG] Resposta GERENTES: " + respostaGerentes);
            
            if (!respostaGerentes.equals("VAZIO")) {
                String[] linhasGerentes = respostaGerentes.split("\n");
                for (String linha : linhasGerentes) {
                    String[] colunas = linha.split(";");
                    if (colunas.length >= 4 && colunas[0].equals("GERENTE")) {
                        gerenteModel.addRow(new Object[]{colunas[1], colunas[2], colunas[3]});
                    }
                }
            }

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("[ERRO] Timeout ao buscar funcionários - Coordenador não respondeu");
            JOptionPane.showMessageDialog(null, 
                "Coordenador não está respondendo.\nVerifique se o servidor está ativo.", 
                "Erro de Rede", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("[ERRO] Erro ao buscar funcionários: " + e.getMessage());
            e.printStackTrace();
        }
        
        caixaTable.setModel(caixaModel);
        gerenteTable.setModel(gerenteModel);
    }

    public JButton getBotaoVoltar() { 
        return botaoVoltar; 
    }
    
    public JButton getBotaoEntrar() { 
        return botaoEntrar; 
    }
}