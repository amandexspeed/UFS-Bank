package Recepcao;

import Modelos.ModeloLista.Fila;
import Modelos.ModeloLista.No;
import Modelos.ModelosPessoa.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo gerenciamento das filas de atendimento do banco.
 * Mantém três filas separadas: Normal, Preferencial e VIP.
 * 
 * @author Mai-ly Bank
 * @version 2.0
 */
public class GerenciarFila {
    
    /** Fila para clientes normais (sem prioridade) */
    public static Fila<Cliente> filaNormal = new Fila<Cliente>();
    
    /** Fila para clientes preferenciais (idosos, gestantes, deficientes) */
    public static Fila<Cliente> filaPreferencial = new Fila<Cliente>();
    
    /** Fila para clientes VIP (atendimento com gerente) */
    public static Fila<Cliente> filaVIP = new Fila<Cliente>();
    
    /**
     * Verifica se todas as filas estão vazias.
     * Útil para o Coordenador responder rapidamente aos terminais de atendimento.
     * 
     * @return true se todas as filas estiverem vazias, false caso contrário
     */
    public static boolean todasVazias() {
        return filaNormal.vazia() && filaPreferencial.vazia() && filaVIP.vazia();
    }
    
    /**
     * Retorna o número total de clientes em todas as filas.
     * 
     * @return Quantidade total de clientes aguardando
     */
    public static int getTotalClientes() {
        return getTotalFilaNormal() + getTotalFilaPreferencial() + getTotalFilaVIP();
    }
    
    /**
     * Retorna o número de clientes na fila normal.
     * 
     * @return Quantidade de clientes na fila normal
     */
    public static int getTotalFilaNormal() {
        return contarClientes(filaNormal);
    }
    
    /**
     * Retorna o número de clientes na fila preferencial.
     * 
     * @return Quantidade de clientes na fila preferencial
     */
    public static int getTotalFilaPreferencial() {
        return contarClientes(filaPreferencial);
    }
    
    /**
     * Retorna o número de clientes na fila VIP.
     * 
     * @return Quantidade de clientes na fila VIP
     */
    public static int getTotalFilaVIP() {
        return contarClientes(filaVIP);
    }
    
    /**
     * Conta o número de elementos em uma fila.
     * 
     * @param fila A fila a ser contada
     * @return Número de elementos na fila
     */
    private static int contarClientes(Fila<Cliente> fila) {
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
    
    /**
     * Retorna uma lista com todos os clientes da fila normal.
     * 
     * @return Lista de clientes da fila normal
     */
    public static List<Cliente> listarFilaNormal() {
        return listarClientes(filaNormal);
    }
    
    /**
     * Retorna uma lista com todos os clientes da fila preferencial.
     * 
     * @return Lista de clientes da fila preferencial
     */
    public static List<Cliente> listarFilaPreferencial() {
        return listarClientes(filaPreferencial);
    }
    
    /**
     * Retorna uma lista com todos os clientes da fila VIP.
     * 
     * @return Lista de clientes da fila VIP
     */
    public static List<Cliente> listarFilaVIP() {
        return listarClientes(filaVIP);
    }
    
    /**
     * Converte uma fila em uma lista de clientes.
     * 
     * @param fila A fila a ser convertida
     * @return Lista com os clientes da fila
     */
    private static List<Cliente> listarClientes(Fila<Cliente> fila) {
        List<Cliente> lista = new ArrayList<>();
        
        if (fila.vazia()) {
            return lista;
        }
        
        No<Cliente> atual = fila.listar();
        while (atual != null) {
            lista.add(atual.getAtual());
            atual = atual.getProximo();
        }
        
        return lista;
    }
    
    /**
     * Retorna todos os clientes de todas as filas organizados por tipo.
     * 
     * @return Array com 3 listas: [Normal, Preferencial, VIP]
     */
    public static List<Cliente>[] listarTodasFilas() {
        @SuppressWarnings("unchecked")
        List<Cliente>[] todasFilas = new List[3];
        todasFilas[0] = listarFilaNormal();
        todasFilas[1] = listarFilaPreferencial();
        todasFilas[2] = listarFilaVIP();
        return todasFilas;
    }
    
    /**
     * Retorna uma string formatada com as estatísticas das filas.
     * 
     * @return String com estatísticas (ex: "Total: 15 | Normal: 8 | Preferencial: 5 | VIP: 2")
     */
    public static String getEstatisticas() {
        return String.format("Total: %d clientes  |  Normal: %d  |  Preferencial: %d  |  VIP: %d",
                           getTotalClientes(),
                           getTotalFilaNormal(),
                           getTotalFilaPreferencial(),
                           getTotalFilaVIP());
    }
    
    /**
     * Imprime no console as estatísticas detalhadas das filas.
     */
    public static void imprimirEstatisticas() {
        System.out.println("\n========== ESTATÍSTICAS DAS FILAS ==========");
        System.out.println("Fila Normal:        " + getTotalFilaNormal() + " clientes");
        System.out.println("Fila Preferencial:  " + getTotalFilaPreferencial() + " clientes");
        System.out.println("Fila VIP:           " + getTotalFilaVIP() + " clientes");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("TOTAL:              " + getTotalClientes() + " clientes");
        System.out.println("=============================================\n");
    }
    
    /**
     * Limpa todas as filas (remove todos os clientes).
     * ATENÇÃO: Use com cuidado! Esta operação não pode ser desfeita.
     */
    public static void limparTodasFilas() {
        System.out.println("[AVISO] Limpando todas as filas...");
        
        while (!filaNormal.vazia()) {
            filaNormal.removerInicio();
        }
        
        while (!filaPreferencial.vazia()) {
            filaPreferencial.removerInicio();
        }
        
        while (!filaVIP.vazia()) {
            filaVIP.removerInicio();
        }
        
        System.out.println("[INFO] Todas as filas foram limpas.");
    }
    
    /**
     * Retorna a posição de um cliente em uma fila específica.
     * 
     * @param cpf CPF do cliente a ser buscado
     * @param tipoFila Tipo da fila ("Normal", "Preferencial" ou "VIP")
     * @return Posição do cliente (1 = primeiro da fila) ou -1 se não encontrado
     */
    public static int getPosicaoNaFila(String cpf, String tipoFila) {
        Fila<Cliente> fila;
        
        switch (tipoFila.toLowerCase()) {
            case "normal":
                fila = filaNormal;
                break;
            case "preferencial":
                fila = filaPreferencial;
                break;
            case "vip":
                fila = filaVIP;
                break;
            default:
                return -1;
        }
        
        if (fila.vazia()) {
            return -1;
        }
        
        int posicao = 1;
        No<Cliente> atual = fila.listar();
        
        while (atual != null) {
            if (atual.getAtual().getCPF().equals(cpf)) {
                return posicao;
            }
            posicao++;
            atual = atual.getProximo();
        }
        
        return -1; // Cliente não encontrado
    }
}