package Utilitarios;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import Modelos.ModeloLista.No;
import Modelos.ModeloLista.Fila; // ✅ AJUSTE AQUI
import Modelos.ModelosPessoa.Caixa;
import Modelos.ModelosPessoa.Cliente;
import Modelos.ModelosPessoa.GerenteNegocios;
import RH.GestaoFuncionarios;
import Recepcao.GerenciarFila;

public class RedeCoordenador {
    private DatagramSocket socket;
    private int porta;
    private int contPreferencial = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private volatile boolean rodando = true;
     private final static int[] portasTentativas = new int[]{10000, 55555, 49876, 61234, 58008}; // Portas para tentar 

    public void iniciar() throws Exception {
        try {
            socket = abrirServidor();
            porta = socket.getLocalPort();
            socket.setSoTimeout(10000);
            byte[] buffer = new byte[8192];
            System.out.println(">>> COORDENADOR UFS-BANK INICIADO NA PORTA " + porta + " <<<");

            while (rodando) {
                try {
                    DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                    socket.receive(pacote);

                    String mensagem = new String(
                            pacote.getData(),
                            0,
                            pacote.getLength(),
                            "UTF-8"
                    );
                    processarMensagem(mensagem, pacote);

                } catch (SocketTimeoutException e) {
                } catch (Exception e) {
                    System.err.println("[ERRO] Falha ao processar pacote: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println(">>> COORDENADOR ENCERRADO <<<");
            }
        }
    }

    private DatagramSocket abrirServidor() throws Exception {
        for (int p : portasTentativas) {
            try {
                DatagramSocket s = new DatagramSocket(p);
                System.out.println("[COORDENADOR] Servidor iniciado na porta " + p);
                return s;
            } catch (Exception e) {
                System.err.println("[COORDENADOR] Porta " + p + " indisponível, tentando próxima...");
            }
        }
        throw new Exception("Não foi possível iniciar o servidor em nenhuma das portas tentadas.");
    }

    private void processarMensagem(String mensagem, DatagramPacket pacote) {
        String[] partes = mensagem.split(";");
        if (partes.length == 0) {
            System.err.println("[ERRO] Mensagem vazia recebida");
            return;
        }

        String comando = partes[0];
        System.out.println("[LOG] Comando recebido: " + comando + " de "
                + pacote.getAddress() + ":" + pacote.getPort());

        try {
            switch (comando) {

                case "ADD_CLIENTE":
                    if (partes.length >= 5) {
                        processarCadastro(partes);
                        responder("OK", pacote.getAddress(), pacote.getPort());
                    } else {
                        responder("ERRO;Parametros insuficientes", pacote.getAddress(), pacote.getPort());
                    }
                    break;

                case "LISTAR_FUNCIONARIOS":
                    responder(gerarListaFuncionarios(), pacote.getAddress(), pacote.getPort());
                    break;

                case "LISTAR_GERENTES":
                    responder(gerarListaGerentes(), pacote.getAddress(), pacote.getPort());
                    break;

                case "CHAMAR_PROXIMO":
                    String tipoFuncionario = partes.length > 1 ? partes[1] : "Caixa";
                    responder(realizarChamada(tipoFuncionario), pacote.getAddress(), pacote.getPort());
                    break;

                case "LISTAR_FILA_NORMAL":
                    responder(
                            gerarListaFila(GerenciarFila.filaNormal, "NORMAL"),
                            pacote.getAddress(),
                            pacote.getPort()
                    );
                    break;

                case "LISTAR_FILA_PREFERENCIAL":
                    responder(
                            gerarListaFila(GerenciarFila.filaPreferencial, "PREFERENCIAL"),
                            pacote.getAddress(),
                            pacote.getPort()
                    );
                    break;

                case "LISTAR_FILA_VIP":
                    responder(
                            gerarListaFila(GerenciarFila.filaVIP, "VIP"),
                            pacote.getAddress(),
                            pacote.getPort()
                    );
                    break;

                case "ESTATISTICAS_FILAS":
                    responder(gerarEstatisticasFilas(), pacote.getAddress(), pacote.getPort());
                    break;

                case "ACHAR_SERVIDOR":
                    responder("SERVIDOR_AQUI:" + InetAddress.getLocalHost().getHostAddress() + ":" + porta,
                            pacote.getAddress(), pacote.getPort());
                    break;

                default:
                    responder("ERRO;Comando desconhecido", pacote.getAddress(), pacote.getPort());
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao processar comando " + comando + ": " + e.getMessage());
        }
    }

    private void processarCadastro(String[] p) {
        lock.lock();
        try {
            boolean prioridade = Boolean.parseBoolean(p[3]);
            Cliente c = new Cliente(p[1], p[2], prioridade);
            
            System.out.println("[LOG] Cadastrando cliente: " + c.getNome() + 
                             " (Prioridade: " + prioridade + ", Tipo: " + p[4] + ")");
            
            if (p[4].equalsIgnoreCase("Caixa")) {
                if (prioridade) {
                    GerenciarFila.filaPreferencial.inserirFim(c);
                } else {
                    GerenciarFila.filaNormal.inserirFim(c);
                }
            } else {
                GerenciarFila.filaVIP.inserirFim(c);
            }
        } catch (Excecao e) {
            System.err.println("[ERRO] Falha ao inserir cliente na fila: " + e.getMessage());
            throw new RuntimeException("Erro ao processar cadastro", e);
        } catch (Exception e) {
            System.err.println("[ERRO] Erro inesperado no cadastro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private String gerarListaFuncionarios() {
        StringBuilder sb = new StringBuilder();
        No<Caixa> c = GestaoFuncionarios.ListaCaixa.listar();

        while (c != null) {
            sb.append("CAIXA;")
              .append(c.getAtual().getNome()).append(";")
              .append(c.getAtual().getCPF()).append(";")
              .append(c.getAtual().getMatricula()).append("\n");
            c = c.getProximo();
        }

        return sb.length() == 0 ? "VAZIO" : sb.toString();
    }

    private String gerarListaGerentes() {
        StringBuilder sb = new StringBuilder();
        No<GerenteNegocios> g = GestaoFuncionarios.ListaGerente.listar();

        while (g != null) {
            sb.append("GERENTE;")
              .append(g.getAtual().getNome()).append(";")
              .append(g.getAtual().getCPF()).append(";")
              .append(g.getAtual().getMatricula()).append("\n");
            g = g.getProximo();
        }

        return sb.length() == 0 ? "VAZIO" : sb.toString();
    }

    private String gerarListaFila(Fila<Cliente> fila, String tipoFila) {
        lock.lock();
        try {
            if (fila.vazia()) {
                return "VAZIO";
            }

            StringBuilder sb = new StringBuilder();
            No<Cliente> atual = fila.listar();

            while (atual != null) {
                Cliente cliente = atual.getAtual();
                sb.append(tipoFila).append(";")
                  .append(cliente.getNome()).append(";")
                  .append(cliente.getCPF()).append(";")
                  .append(cliente.isPrioridade()).append("\n");
                atual = atual.getProximo();
            }

            return sb.toString();
        } finally {
            lock.unlock();
        }
    }

    private String gerarEstatisticasFilas() {
        lock.lock();
        try {
            int totalNormal = contarFila(GerenciarFila.filaNormal);
            int totalPreferencial = contarFila(GerenciarFila.filaPreferencial);
            int totalVIP = contarFila(GerenciarFila.filaVIP);
            int total = totalNormal + totalPreferencial + totalVIP;

            return String.format(
                    "STATS;%d;%d;%d;%d",
                    total, totalNormal, totalPreferencial, totalVIP
            );
        } finally {
            lock.unlock();
        }
    }

    private int contarFila(Fila<Cliente> fila) {
        if (fila.vazia()) {
            return 0;
        }

        int count = 0;
        No<Cliente> atual = fila.listar();
        while (atual != null) {
            count++;
            atual = atual.getProximo();
        }
        return count;
    }

    private String realizarChamada(String tipo) {
        lock.lock();
        try {
            if (tipo.equalsIgnoreCase("Gerente")) {
                if (!GerenciarFila.filaVIP.vazia()) {
                    Cliente cliente = GerenciarFila.filaVIP.removerInicio();
                    return "CLIENTE;" + cliente.getNome();
                }
                return "FILA_VAZIA";
            }

            if (!GerenciarFila.filaPreferencial.vazia()
                    && (contPreferencial < 2 || GerenciarFila.filaNormal.vazia())) {

                Cliente cliente = GerenciarFila.filaPreferencial.removerInicio();
                contPreferencial++;
                return "CLIENTE;" + cliente.getNome();
            }

            if (!GerenciarFila.filaNormal.vazia()) {
                Cliente cliente = GerenciarFila.filaNormal.removerInicio();
                contPreferencial = 0;
                return "CLIENTE;" + cliente.getNome();
            }

            return "FILA_VAZIA";
        } finally {
            lock.unlock();
        }
    }

    private void responder(String msg, InetAddress ip, int porta) throws Exception {
        byte[] b = msg.getBytes("UTF-8");
        DatagramPacket resposta = new DatagramPacket(b, b.length, ip, porta);
        socket.send(resposta);
        System.out.println("[LOG] Resposta enviada para " + ip + ":" + porta + " -> " + 
                         (msg.length() > 50 ? msg.substring(0, 50) + "..." : msg));
    }

    public void parar() {
        rodando = false;
    }
}
