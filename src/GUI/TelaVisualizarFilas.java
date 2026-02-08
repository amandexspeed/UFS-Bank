package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Utilitarios.RedeCliente;

import java.awt.Dimension;

/**
 * Tela para visualização em tempo real das filas de clientes.
 * Busca os dados diretamente do Coordenador via UDP.
 * 
 * @author Sistema Bank Itabaiana
 * @version 2.0
 */
public class TelaVisualizarFilas extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JTable tabelaNormal;
    private static JTable tabelaPreferencial;
    private static JTable tabelaVIP;
    private JButton botaoVoltar;
    private JButton botaoAtualizar;
    private static JLabel lblEstatisticas;
    private Timer timer;

    public TelaVisualizarFilas() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);

        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setLayout(null);
        add(headerPanel);

        JLabel titulo = new JLabel("Visualização das Filas", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 1280, 50);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        JLabel subtitulo = new JLabel("Clientes Aguardando Atendimento", SwingConstants.CENTER);
        subtitulo.setBounds(0, 75, 1280, 30);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(new Color(236, 240, 241));
        headerPanel.add(subtitulo);

        // === PAINEL DE ESTATÍSTICAS ===
        JPanel statsPanel = new JPanel();
        statsPanel.setBounds(50, 140, 1180, 60);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
        statsPanel.setLayout(null);
        add(statsPanel);

        lblEstatisticas = new JLabel("Carregando...", SwingConstants.CENTER);
        lblEstatisticas.setFont(new Font("Arial", Font.BOLD, 18));
        lblEstatisticas.setForeground(new Color(52, 73, 94));
        lblEstatisticas.setBounds(0, 0, 1180, 60);
        statsPanel.add(lblEstatisticas);

        // === PAINEL DE TABELAS ===
        JPanel tabelasPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        tabelasPanel.setBounds(50, 220, 1180, 380);
        tabelasPanel.setBackground(new Color(245, 245, 250));

        // Tabela Normal
        tabelaNormal = new JTable();
        estilizarTabela(tabelaNormal, new Color(52, 152, 219));
        JScrollPane scrollNormal = new JScrollPane(tabelaNormal);
        scrollNormal.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            "👥 Fila Normal",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));

        // Tabela Preferencial
        tabelaPreferencial = new JTable();
        estilizarTabela(tabelaPreferencial, new Color(230, 126, 34));
        JScrollPane scrollPreferencial = new JScrollPane(tabelaPreferencial);
        scrollPreferencial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 3),
            "⭐ Fila Preferencial",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(230, 126, 34)
        ));

        // Tabela VIP
        tabelaVIP = new JTable();
        estilizarTabela(tabelaVIP, new Color(155, 89, 182));
        JScrollPane scrollVIP = new JScrollPane(tabelaVIP);
        scrollVIP.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 3),
            "👑 Fila VIP",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(155, 89, 182)
        ));

        tabelasPanel.add(scrollNormal);
        tabelasPanel.add(scrollPreferencial);
        tabelasPanel.add(scrollVIP);
        add(tabelasPanel);

        // Preenche as tabelas inicialmente
        atualizarFilas();

        // === BOTÕES ===
        botaoAtualizar = new JButton("🔄 ATUALIZAR FILAS");
        botaoAtualizar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoAtualizar.setBounds(850, 620, 380, 60);
        botaoAtualizar.setBackground(new Color(46, 204, 113));
        botaoAtualizar.setForeground(Color.WHITE);
        botaoAtualizar.setFocusPainted(false);
        botaoAtualizar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        botaoAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoAtualizar);

        botaoAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoAtualizar.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoAtualizar.setBackground(new Color(46, 204, 113));
            }
        });

        botaoAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarFilas();
                System.out.println("[FILAS] Tabelas atualizadas manualmente");
            }
        });

        botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 620, 250, 60);
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

        // === AUTO-ATUALIZAÇÃO ===
        iniciarAutoAtualizacao();
    }

    private void estilizarTabela(JTable tabela, Color corSelecao) {
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(35);
        tabela.setSelectionBackground(corSelecao);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(189, 195, 199));
        tabela.setShowGrid(true);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(100, 40));
    }

    /**
     * Atualiza todas as filas buscando dados do Coordenador via UDP
     */
    public static void atualizarFilas() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.setBroadcast(true);
            InetAddress ip = InetAddress.getByName("255.255.255.255");

            // Busca estatísticas
            buscarEstatisticas(socket, ip);

            // Busca Fila Normal
            List<ClienteInfo> filaNormal = buscarFila(socket, ip, "LISTAR_FILA_NORMAL");
            atualizarTabela(tabelaNormal, filaNormal, "Normal");

            // Busca Fila Preferencial
            List<ClienteInfo> filaPreferencial = buscarFila(socket, ip, "LISTAR_FILA_PREFERENCIAL");
            atualizarTabela(tabelaPreferencial, filaPreferencial, "Preferencial");

            // Busca Fila VIP
            List<ClienteInfo> filaVIP = buscarFila(socket, ip, "LISTAR_FILA_VIP");
            atualizarTabela(tabelaVIP, filaVIP, "VIP");

            System.out.println("[FILAS] Tabelas atualizadas com sucesso via UDP");

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("[ERRO] Timeout - Coordenador não respondeu");
            lblEstatisticas.setText("⚠️ Coordenador não está respondendo");
            JOptionPane.showMessageDialog(null,
                "Erro: Coordenador não está respondendo.\nVerifique se o servidor está ativo.",
                "Erro de Rede",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao atualizar filas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca as estatísticas das filas do Coordenador
     */
    private static void buscarEstatisticas(DatagramSocket socket, InetAddress ip) throws Exception {
        String comando = "ESTATISTICAS_FILAS";
        byte[] bufferEnvio = comando.getBytes("UTF-8");
        DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ip, RedeCliente.getServidorPorta());
        socket.send(pacoteEnvio);

        byte[] bufferReceber = new byte[1024];
        DatagramPacket pacoteReceber = new DatagramPacket(bufferReceber, bufferReceber.length);
        socket.receive(pacoteReceber);

        String resposta = new String(pacoteReceber.getData(), 0, pacoteReceber.getLength(), "UTF-8");
        System.out.println("[LOG] Estatísticas recebidas: " + resposta);

        // Formato: STATS;TOTAL;NORMAL;PREFERENCIAL;VIP
        String[] partes = resposta.split(";");
        if (partes.length >= 5 && partes[0].equals("STATS")) {
            int total = Integer.parseInt(partes[1]);
            int normal = Integer.parseInt(partes[2]);
            int preferencial = Integer.parseInt(partes[3]);
            int vip = Integer.parseInt(partes[4]);

            lblEstatisticas.setText(String.format(
                "Total: %d clientes  |  Normal: %d  |  Preferencial: %d  |  VIP: %d",
                total, normal, preferencial, vip
            ));
        }
    }

    /**
     * Busca uma fila específica do Coordenador
     */
    private static List<ClienteInfo> buscarFila(DatagramSocket socket, InetAddress ip, String comando) throws Exception {
        List<ClienteInfo> clientes = new ArrayList<>();

        byte[] bufferEnvio = comando.getBytes("UTF-8");
        DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ip, RedeCliente.getServidorPorta());
        socket.send(pacoteEnvio);

        byte[] bufferReceber = new byte[8192];
        DatagramPacket pacoteReceber = new DatagramPacket(bufferReceber, bufferReceber.length);
        socket.receive(pacoteReceber);

        String resposta = new String(pacoteReceber.getData(), 0, pacoteReceber.getLength(), "UTF-8");
        System.out.println("[LOG] Resposta " + comando + ": " + (resposta.length() > 50 ? resposta.substring(0, 50) + "..." : resposta));

        if (resposta.equals("VAZIO")) {
            return clientes; // Retorna lista vazia
        }

        // Formato: TIPO_FILA;NOME;CPF;PRIORIDADE
        String[] linhas = resposta.split("\n");
        for (String linha : linhas) {
            String[] colunas = linha.split(";");
            if (colunas.length >= 4) {
                ClienteInfo info = new ClienteInfo();
                info.nome = colunas[1];
                info.cpf = colunas[2];
                info.prioridade = Boolean.parseBoolean(colunas[3]);
                clientes.add(info);
            }
        }

        return clientes;
    }

    /**
     * Atualiza uma tabela com a lista de clientes
     */
    private static void atualizarTabela(JTable tabela, List<ClienteInfo> clientes, String tipoFila) {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Posição", "Nome", "CPF", "Prioridade"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int posicao = 1;
        for (ClienteInfo cliente : clientes) {
            String prioridade;
            if (tipoFila.equals("VIP")) {
                prioridade = "VIP";
            } else if (cliente.prioridade) {
                prioridade = "Sim";
            } else {
                prioridade = "Não";
            }

            modelo.addRow(new Object[]{
                posicao + "º",
                cliente.nome,
                cliente.cpf,
                prioridade
            });
            posicao++;
        }

        // Se a fila estiver vazia
        if (clientes.isEmpty()) {
            modelo.addRow(new Object[]{"—", "Fila vazia", "—", "—"});
        }

        tabela.setModel(modelo);

        // Ajusta largura das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
    }

    private void iniciarAutoAtualizacao() {
        timer = new Timer(RedeCliente.getServidorPorta(), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarFilas();
            }
        });
        timer.start();
        System.out.println("[FILAS] Auto-atualização iniciada (intervalo: 5 segundos)");
    }

    public void pararAutoAtualizacao() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            System.out.println("[FILAS] Auto-atualização parada");
        }
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }

    public JButton getBotaoAtualizar() {
        return botaoAtualizar;
    }

    /**
     * Classe auxiliar para armazenar informações do cliente
     */
    private static class ClienteInfo {
        String nome;
        String cpf;
        boolean prioridade;
    }
}