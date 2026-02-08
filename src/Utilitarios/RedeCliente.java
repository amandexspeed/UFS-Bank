package Utilitarios;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import GUI.ExcecaoPainel;

public class RedeCliente {
    private final static int[] portasTentativas = new int[]{10000, 55555, 49876, 61234, 58008}; // Portas para tentar encontrar o servidor
    private static String servidorIP;
    private static int servidorPorta;

    public static boolean buscarServidor() {

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.setBroadcast(true);
            
            String resposta = tentarAcharServidor(socket);
            if (resposta == null) {
                ExcecaoPainel.exibirExcecao("Não foi possível encontrar o servidor.");
                return false;
            }

            String[] partes = resposta.split(":");
            if (partes.length == 3) {
                servidorIP = partes[1];
                servidorPorta = Integer.parseInt(partes[2]);
                System.out.println("[CLIENTE] Servidor encontrado: " + servidorIP + ":" + servidorPorta);
                return true;
            }else {
            ExcecaoPainel.exibirExcecao("Resposta do servidor mal formatada.");
            return false;
            }
            
        } catch (Exception e) {
            ExcecaoPainel.exibirExcecao("Não foi possível encontrar o servidor. Verifique sua conexão de rede e tente novamente.");
            return false;
        }
    }

    private static String tentarAcharServidor(DatagramSocket socket) {

        boolean encontrado = false;
        int indice = 0;
        byte[] sendData = "ACHAR_SERVIDOR".getBytes();
        InetAddress ip;
        try {
            ip = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            ExcecaoPainel.exibirExcecao("Erro ao obter endereço de broadcast: " + e.getMessage());
            return null;
        }

        while(!encontrado && indice < portasTentativas.length) {
            try {
                
                socket.send(new java.net.DatagramPacket(sendData, sendData.length, ip,portasTentativas[indice]));
                byte[] receiveData = new byte[1024];
                java.net.DatagramPacket receivePacket = new java.net.DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String resposta = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (resposta.startsWith("SERVIDOR_AQUI")) {
                    encontrado = true;
                    return resposta;
                }
            }catch(Exception e) {
                // Ignorar exceções, pois o servidor pode não responder em algumas portas
            }
            indice++;
        }
        return null;
    }

    public static String getServidorIP() {
        return servidorIP;
    }

    public static int getServidorPorta() {
        return servidorPorta;
    }
    
}
