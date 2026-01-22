package Caixa;

import javax.swing.JOptionPane;
import Modelos.ModeloLista.*;
import Modelos.ModelosPessoa.*;
import RH.GestaoFuncionarios;

public class EscolhaFunc {
    
    public static Funcionario funcionarioEscolhido; 
    public static String nome = " ";
    
    public static void escolherFuncionario(int matricula) {
        
        No<Lista> listaDeps = GestaoFuncionarios.ListaDepartamento.listar();
        boolean encontrado = false;

        while(listaDeps != null) {
            // Busca a matrícula dentro da sublista do departamento (Multilista)
            No listaFunc = listaDeps.getAtual().buscar(matricula);
            
            if(listaFunc != null) {
                if(listaFunc.getAtual() instanceof GerenteNegocios){
                    funcionarioEscolhido = (GerenteNegocios) listaFunc.getAtual();
                } else if(listaFunc.getAtual() instanceof Caixa){
                    funcionarioEscolhido = (Caixa) listaFunc.getAtual();
                }
                
                if (funcionarioEscolhido != null) {
                    nome = funcionarioEscolhido.getNome();
                    encontrado = true;
                    break;
                }
            }
            listaDeps = listaDeps.getProximo();
        }

        if(!encontrado) {
            JOptionPane.showMessageDialog(null, "Não foi encontrada a matrícula digitada no sistema.");
        } else {
            System.out.println("[LOG] Funcionário logado no terminal: " + nome);
        }
    }
}