package Recepcao;

import Modelos.ModeloLista.Fila;
import Modelos.ModelosPessoa.Cliente;

public class GerenciarFila {

    public static Fila<Cliente> filaNormal = new Fila<Cliente>();
    public static Fila<Cliente> filaPreferencial = new Fila<Cliente>();
    public static Fila<Cliente> filaVIP = new Fila<Cliente>();

    /**
     * Método auxiliar para verificar se todas as filas estão vazias
     * Útil para o Coordenador responder rapidamente aos terminais de atendimento
     */
    public static boolean todasVazias() {
        return filaNormal.vazia() && filaPreferencial.vazia() && filaVIP.vazia();
    }
}