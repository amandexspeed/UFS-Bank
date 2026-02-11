package GUI;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JComboBox;

import Modelos.ModelosPessoa.*;
import Modelos.ModeloLista.*;
import RH.GestaoFuncionarios;
import Utilitarios.Excecao;
import Utilitarios.RedeCliente;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class TelaExcluir extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField campoMatricula;
    private JButton botaoVoltar;
    private JButton botaoExcluir;
    private static JTable caixaTable;
    private static JTable gerenteTable;
    private JComboBox<String> comboTipoFunc;
    
    public TelaExcluir() {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);

        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(231, 76, 60)); // Vermelho para exclusão
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Excluir Funcionários", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        // === LABEL INFORMATIVO ===
        JLabel lblInfo = new JLabel("⚠️ Selecione o funcionário para remover do sistema", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 18));
        lblInfo.setForeground(new Color(192, 57, 43));
        lblInfo.setBounds(0, 140, 1280, 30);
        add(lblInfo);

        // === PAINEL DE TABELAS ===
        JPanel tabelaPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tabelaPanel.setBounds(50, 180, 1180, 280);
        tabelaPanel.setBackground(new Color(245, 245, 250));

        // Tabela de Caixas
        caixaTable = new JTable();
        estilizarTabela(caixaTable);
        JScrollPane caixaScrollPane = new JScrollPane(caixaTable);
        caixaScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            "Caixas Cadastrados",
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
            BorderFactory.createLineBorder(new Color(155, 89, 182), 3),
            "Gerentes Cadastrados",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(155, 89, 182)
        ));

        tabelaPanel.add(caixaScrollPane);
        tabelaPanel.add(gerenteScrollPane);
        add(tabelaPanel);

        // Preenche as tabelas
        try {
            preencherTabelas();
        } catch (Excecao e) {
            e.printStackTrace();
        }

        // === PAINEL DE EXCLUSÃO ===
        JPanel exclusaoPanel = new JPanel();
        exclusaoPanel.setBounds(290, 490, 700, 150);
        exclusaoPanel.setBackground(Color.WHITE);
        exclusaoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 3),
            "Dados para Exclusão",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(231, 76, 60)
        ));
        exclusaoPanel.setLayout(null);
        add(exclusaoPanel);

        // Tipo de Funcionário
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTipo.setForeground(new Color(52, 73, 94));
        lblTipo.setBounds(30, 40, 100, 30);
        exclusaoPanel.add(lblTipo);

        String[] opcoes = {"Caixa", "Gerente"};
        comboTipoFunc = new JComboBox<>(opcoes);
        comboTipoFunc.setFont(new Font("Arial", Font.PLAIN, 16));
        comboTipoFunc.setBounds(130, 40, 200, 35);
        comboTipoFunc.setBackground(Color.WHITE);
        exclusaoPanel.add(comboTipoFunc);

        // Matrícula
        JLabel lblMatricula = new JLabel("Matrícula:");
        lblMatricula.setFont(new Font("Arial", Font.BOLD, 16));
        lblMatricula.setForeground(new Color(52, 73, 94));
        lblMatricula.setBounds(370, 40, 100, 30);
        exclusaoPanel.add(lblMatricula);

        campoMatricula = new JTextField();
        campoMatricula.setFont(new Font("Arial", Font.PLAIN, 16));
        campoMatricula.setBounds(470, 40, 200, 35);
        campoMatricula.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        exclusaoPanel.add(campoMatricula);

        // Aceita apenas números na matrícula
        campoMatricula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        // Botão Excluir
        botaoExcluir = new JButton("🗑 EXCLUIR FUNCIONÁRIO");
        botaoExcluir.setFont(new Font("Arial", Font.BOLD, 16));
        botaoExcluir.setBounds(200, 95, 300, 45);
        botaoExcluir.setBackground(new Color(231, 76, 60));
        botaoExcluir.setForeground(Color.WHITE);
        botaoExcluir.setFocusPainted(false);
        botaoExcluir.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43), 2));
        botaoExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exclusaoPanel.add(botaoExcluir);

        // Efeito hover botão excluir
        botaoExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoExcluir.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoExcluir.setBackground(new Color(231, 76, 60));
            }
        });

        botaoExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String matriculaStr = campoMatricula.getText().trim();
                String tipo = (String) comboTipoFunc.getSelectedItem();

                // Validações
                if (matriculaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Por favor, digite a matrícula do funcionário.",
                        "Campo Obrigatório",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int matricula;
                try {
                    matricula = Integer.parseInt(matriculaStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "Matrícula inválida. Digite apenas números.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirmação
                int confirmacao = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja excluir o " + tipo + " com matrícula " + matricula + "?\n" +
                    "Esta ação não pode ser desfeita!",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        
                        if(matricula <= 0) {
                            throw new Excecao("Matrícula inválida!");
                        }

                        No funcionario = tipo.equals("Caixa") ? GestaoFuncionarios.ListaCaixa.buscar(matricula) : GestaoFuncionarios.ListaGerente.buscar(matricula);
                        if(funcionario == null) {
                            throw new Excecao("Funcionário não encontrado!");
                        }
                        
                        boolean sucesso = enviarSolicitacaoRede(tipo, matricula);
                        if(sucesso) {
                            campoMatricula.setText("");
                            GestaoFuncionarios.atualizarTabelas();
                        }
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                            "Erro ao excluir funcionário: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // === BOTÃO VOLTAR ===
        botaoVoltar = new JButton("← Voltar ao RH");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 660, 250, 40);
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

    /**
     * Estiliza a aparência das tabelas
     */
    private void estilizarTabela(JTable tabela) {
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setSelectionBackground(new Color(231, 76, 60));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(189, 195, 199));
        
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(100, 35));
    }

    /**
     * Preenche as tabelas com os dados dos funcionários
     */
    public static void preencherTabelas() throws Excecao {
        caixaTable.removeAll();
        gerenteTable.removeAll();

        // Modelo para Caixas (não editável)
        DefaultTableModel caixaModel = new DefaultTableModel(
            new Object[]{"Nome", "CPF", "Matrícula"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Modelo para Gerentes (não editável)
        DefaultTableModel gerenteModel = new DefaultTableModel(
            new Object[]{"Nome", "CPF", "Matrícula"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Popula tabela de Caixas
        No<Caixa> caixa = GestaoFuncionarios.ListaCaixa.listar();
        while (caixa != null) {
            caixaModel.addRow(new Object[]{
                caixa.getAtual().getNome(),
                caixa.getAtual().getCPF(),
                caixa.getAtual().getMatricula()
            });
            caixa = caixa.getProximo();
        }

        // Popula tabela de Gerentes
        No<GerenteNegocios> gerente = GestaoFuncionarios.ListaGerente.listar();
        while (gerente != null) {
            gerenteModel.addRow(new Object[]{
                gerente.getAtual().getNome(),
                gerente.getAtual().getCPF(),
                gerente.getAtual().getMatricula()
            });
            gerente = gerente.getProximo();
        }

        caixaTable.setModel(caixaModel);
        gerenteTable.setModel(gerenteModel);
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }

     private boolean enviarSolicitacaoRede(String setor, int matricula) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(30000);
            
            String mensagem = String.format("REMOVE_FUNCIONARIO;%s;%d", setor, matricula);
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
                        "Funcionário removido com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else if (respostaServidor.startsWith("ERRO")) {
                    String[] partes = respostaServidor.split(";", 2);
                    String mensagemErro = partes.length > 1 ? partes[1] : "Erro desconhecido.";
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao remover funcionário: " + mensagemErro, 
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

}