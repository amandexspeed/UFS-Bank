package Modelos.Main;

import GUI.ExcecaoPainel;
import GUI.JanelaPrincipal;
import RH.GestaoFuncionarios;
import Utilitarios.Excecao;
import Utilitarios.RedeCoordenador;

public class MainCliente {

    public static void main(String[] args) {

        GestaoFuncionarios.iniciarLista();
        if (!Utilitarios.RedeCliente.buscarServidor()) {
            return; // Se não encontrar o servidor, encerra o cliente
        }

        try {
           
            JanelaPrincipal telaPrincipal = new JanelaPrincipal(false);
            telaPrincipal.setVisible(true);

            // Se for o servidor, inicia a escuta UDP em uma Thread separada
            // para não travar a interface gráfica (Swing)
            if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
                System.out.println("[COORDENADOR] Iniciando monitoramento de rede...");
                
                new Thread(() -> {
                    try {
                        RedeCoordenador servidor = new RedeCoordenador();
                        servidor.iniciar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (Excecao e) {
            ExcecaoPainel.exibirExcecao(e.getMessage());
        }
    }
}