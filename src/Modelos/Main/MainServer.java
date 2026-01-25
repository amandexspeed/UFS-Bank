package Modelos.Main;

import RH.GestaoFuncionarios;
import Utilitarios.RedeCoordenador;

public class MainServer {
    
    public static void main(String[] args) {

        GestaoFuncionarios.iniciarLista();

        // Se for o servidor, inicia a escuta UDP em uma Thread separada
        // para não travar a interface gráfica (Swing)
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

}


