

// ============================================================================
// CÓDIGO JanelaPrincipal ATUALIZADA
// ============================================================================

package GUI;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Utilitarios.Excecao;

public class JanelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel conteudoPainel;
    private CardLayout cardLayout;

    public JanelaPrincipal(boolean isServer) throws Excecao {
        
        if (isServer) {
            setTitle("UFS-Bank [MODO COORDENADOR CENTRAL]");
        } else {
            setTitle("UFS-Bank [TERMINAL CLIENTE]");
        }
        
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        conteudoPainel = new JPanel();
        cardLayout = new CardLayout();
        conteudoPainel.setLayout(cardLayout);
        setContentPane(conteudoPainel);
        
        // === INSTANCIAÇÃO DAS TELAS ===
        TelaPrincipal telaPrincipal = new TelaPrincipal();
        TelaCadastro telaCadastro = new TelaCadastro();
        TelaTabela telaTabela = new TelaTabela();
        TelaRH telaRH = new TelaRH();
        TelaCaixa telaCaixa = new TelaCaixa();
        TelaExcluir telaExcluir = new TelaExcluir();
        TelaAtendimento telaAtendimento = new TelaAtendimento();
        TelaCadastrarCliente telaCadastrarCliente = new TelaCadastrarCliente();
        TelaVisualizarFilas telaVisualizarFilas = new TelaVisualizarFilas(); // NOVA TELA
        
        // === ADICIONANDO AO CARDLAYOUT ===
        conteudoPainel.add(telaPrincipal, "telaPrincipal");
        conteudoPainel.add(telaCadastro, "telaCadastro");
        conteudoPainel.add(telaTabela, "telaTabela");
        conteudoPainel.add(telaRH, "telaRH");
        conteudoPainel.add(telaCaixa, "telaCaixa");
        conteudoPainel.add(telaExcluir,"telaExcluir");
        conteudoPainel.add(telaAtendimento,"telaAtendimento");
        conteudoPainel.add(telaCadastrarCliente, "telaCadastrarCliente");
        conteudoPainel.add(telaVisualizarFilas, "telaVisualizarFilas"); // NOVA TELA
        
        // === LISTENERS DE NAVEGAÇÃO EXISTENTES ===
        
        telaPrincipal.getBotaoRH().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaRH");
            }
        });
        
        telaPrincipal.getBotaoCaixa().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaCaixa.preencherTabelasRemotamente();
                cardLayout.show(conteudoPainel, "telaCaixa");
            }
        });

        telaPrincipal.getBotaoRecepcao().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaCadastrarCliente");
            }
        });
        
        telaCadastro.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaRH");
            }
        });

        telaTabela.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaRH");
            }
        });

        telaRH.getBotaoCadastrar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaCadastro");
            }
        });

        telaRH.getBotaoListar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaTabela");
            }
        });

        telaRH.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaPrincipal");
            }
        });
        
        telaCaixa.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaPrincipal");
            }
        });
        
        telaRH.getBotaoExcluir().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaExcluir");
            }
        });
        
        telaExcluir.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaRH");
            }
        });
        
        telaAtendimento.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaPrincipal");
            }
        });

        telaCaixa.getBotaoEntrar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaAtendimento.mudarTexto();
                cardLayout.show(conteudoPainel, "telaAtendimento");
            }
        });

        telaCadastrarCliente.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaPrincipal");
            }
        });
        
        // === NOVOS LISTENERS PARA A TELA DE VISUALIZAR FILAS ===
        
        // Botão "Visualizar Filas" na tela de cadastro de clientes
        telaCadastrarCliente.getBotaoVisualizarFilas().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaVisualizarFilas.atualizarFilas(); // Atualiza antes de mostrar
                cardLayout.show(conteudoPainel, "telaVisualizarFilas");
            }
        });
        
        // Botão "Voltar" na tela de visualizar filas
        telaVisualizarFilas.getBotaoVoltar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteudoPainel, "telaCadastrarCliente");
            }
        });
    }

    public JPanel getConteudoPainel() {
        return conteudoPainel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }
}