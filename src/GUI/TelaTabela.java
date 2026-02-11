package GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Modelos.ModeloLista.No;
import Modelos.ModelosPessoa.Caixa;
import Modelos.ModelosPessoa.GerenteNegocios;
import RH.GestaoFuncionarios;
import Utilitarios.Excecao;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TelaTabela extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JTable caixaTable;
    private static JTable gerenteTable;
    private static JLabel lblStats;
    private static JPanel statsPanel;
    private JButton botaoVoltar;

    public TelaTabela() throws Excecao {
        setSize(1280, 720);
        setBackground(new Color(245, 245, 250));
        setLayout(null);

        // === CABEÇALHO ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1280, 120);
        headerPanel.setBackground(new Color(155, 89, 182)); // Roxo para listar
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel titulo = new JLabel("Lista de Funcionários", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 1280, 60);
        titulo.setFont(new Font("Arial", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        // === LABEL INFORMATIVO ===
        JLabel lblInfo = new JLabel("Funcionários Cadastrados no Sistema", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 20));
        lblInfo.setForeground(new Color(52, 73, 94));
        lblInfo.setBounds(0, 140, 1280, 30);
        add(lblInfo);

        // === PAINEL DE TABELAS ===
        JPanel tabelaPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tabelaPanel.setBounds(50, 180, 1180, 420);
        tabelaPanel.setBackground(new Color(245, 245, 250));

        // Tabela de Caixas
        caixaTable = new JTable();
        estilizarTabela(caixaTable);
        JScrollPane caixaScrollPane = new JScrollPane(caixaTable);
        caixaScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            "📊 Caixas",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 18),
            new Color(52, 152, 219)
        ));
        caixaScrollPane.setBackground(Color.WHITE);

        // Tabela de Gerentes
        gerenteTable = new JTable();
        estilizarTabela(gerenteTable);
        JScrollPane gerenteScrollPane = new JScrollPane(gerenteTable);
        gerenteScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 3),
            "📊 Gerentes de Negócios",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 18),
            new Color(155, 89, 182)
        ));
        gerenteScrollPane.setBackground(Color.WHITE);

        tabelaPanel.add(caixaScrollPane);
        tabelaPanel.add(gerenteScrollPane);
        add(tabelaPanel);

        statsPanel = new JPanel();
        statsPanel.setBounds(400, 620, 480, 50);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        statsPanel.setLayout(null);
        add(statsPanel);

        // Preenche as tabelas
        preencherTabelas();

        

        lblStats = new JLabel(obterEstatisticas(), SwingConstants.CENTER);
        lblStats.setFont(new Font("Arial", Font.BOLD, 16));
        lblStats.setForeground(new Color(52, 73, 94));
        lblStats.setBounds(0, 0, 480, 50);
        statsPanel.add(lblStats);

        // === BOTÃO VOLTAR ===
        botaoVoltar = new JButton("← Voltar ao RH");
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoVoltar.setBounds(50, 620, 250, 50);
        botaoVoltar.setBackground(new Color(149, 165, 166));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
        botaoVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(botaoVoltar);

        // Efeito hover
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
        tabela.setRowHeight(35);
        tabela.setSelectionBackground(new Color(52, 152, 219));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(189, 195, 199));
        tabela.setShowGrid(true);
        tabela.setIntercellSpacing(new Dimension(1, 1));
        
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(100, 40));
        header.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
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

        atualizarEstatisticas();
    }

    /**
     * Retorna string com estatísticas dos funcionários
     */
    private static String obterEstatisticas() {
        int totalCaixas = caixaTable.getRowCount();
        int totalGerentes = gerenteTable.getRowCount();
        int total = totalCaixas + totalGerentes;
        
        return String.format("Total: %d funcionários  |  Caixas: %d  |  Gerentes: %d", 
                           total, totalCaixas, totalGerentes);
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }

    private static void atualizarEstatisticas() {
        // === PAINEL DE ESTATÍSTICAS ===

        JLabel lblStatsNovo = new JLabel(obterEstatisticas(), SwingConstants.CENTER);
        lblStatsNovo.setFont(new Font("Arial", Font.BOLD, 16));
        lblStatsNovo.setForeground(new Color(52, 73, 94));
        lblStatsNovo.setBounds(0, 0, 480, 50);
        statsPanel.removeAll();
        statsPanel.add(lblStatsNovo);
        lblStats = lblStatsNovo;
    }
}