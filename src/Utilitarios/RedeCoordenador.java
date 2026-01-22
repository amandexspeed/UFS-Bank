package Utilitarios;

import java.net.*;
import Modelos.ModeloLista.Fila;
import Modelos.ModelosPessoa.Cliente;
import Modelos.ModelosPessoa.Caixa;
import Modelos.ModelosPessoa.GerenteNegocios;
import Modelos.ModeloLista.No;
import RH.GestaoFuncionarios;
import Recepcao.GerenciarFila;

public class RedeCoordenador {
    private DatagramSocket socket;
    private final int PORTA = 5000;

    public void iniciar() throws Exception {
        socket = new DatagramSocket(PORTA);
        byte[] buffer = new byte[4096];
        System.out.println(">>> COORDENADOR UFS-BANK INICIADO NA PORTA " + PORTA + " <<<");

        while (true) {
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacote);
            
            String mensagem = new String(pacote.getData(), 0, pacote.getLength());
            String[] partes = mensagem.split(";");
            String comando = partes[0];

            System.out.println("[LOG] Comando recebido: " + comando);

            switch (comando) {
                case "ADD_CLIENTE":
                    // Regra 1 e 2: Controlar filas centralmente
                    processarCadastro(partes);
                    responder("OK", pacote.getAddress(), pacote.getPort());
                    break;

                case "LISTAR_FUNCIONARIOS":
                    // Regra 1: Mensagem entre processos com dados do RH
                    String lista = gerarListaFuncionarios();
                    responder(lista, pacote.getAddress(), pacote.getPort());
                    break;

                case "CHAMAR_PROXIMO":
                    // Regra 2: Coordenador decide quem é o próximo
                    String proximo = chamarProximo();
                    responder(proximo, pacote.getAddress(), pacote.getPort());
                    break;
            }
        }
    }

    private void processarCadastro(String[] p) {
        try {
            // p[1]=nome, p[2]=cpf, p[3]=prioridade, p[4]=setor
            boolean prioridade = Boolean.parseBoolean(p[3]);
            Cliente c = new Cliente(p[1], p[2], prioridade);
            
            if (p[4].equals("Caixa")) {
                if (prioridade) GerenciarFila.filaPreferencial.inserirFim(c);
                else GerenciarFila.filaNormal.inserirFim(c);
            } else {
                GerenciarFila.filaVIP.inserirFim(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String gerarListaFuncionarios() {
        StringBuilder sb = new StringBuilder();
        No<Caixa> c = GestaoFuncionarios.ListaCaixa.listar();
        while (c != null) {
            sb.append("CAIXA;").append(c.getAtual().getNome()).append(";")
              .append(c.getAtual().getCPF()).append(";")
              .append(c.getAtual().getMatricula()).append("\n");
            c = c.getProximo();
        }
        return sb.toString();
    }

    private String chamarProximo() {
        if (!GerenciarFila.filaVIP.vazia()) return "CLIENTE;" + GerenciarFila.filaVIP.removerInicio().getNome();
        if (!GerenciarFila.filaPreferencial.vazia()) return "CLIENTE;" + GerenciarFila.filaPreferencial.removerInicio().getNome();
        if (!GerenciarFila.filaNormal.vazia()) return "CLIENTE;" + GerenciarFila.filaNormal.removerInicio().getNome();
        return "FILA_VAZIA";
    }

    private void responder(String msg, InetAddress ip, int porta) throws Exception {
        byte[] b = msg.getBytes();
        socket.send(new DatagramPacket(b, b.length, ip, porta));
    }
}