package Data.GerenciarArquivos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader; 

import GUI.ExcecaoPainel;
import Modelos.ModelosPessoa.Funcionario;
import Utilitarios.Excecao;

public class GerenciarArquivos {

    // Caminho base unificado usando File.separator para compatibilidade (Windows/Linux)
    private static final String CAMINHO_BASE = "src" + File.separator + "Data" + File.separator + "Arquivos" + File.separator;

    public static void escreverArquivo(Funcionario objeto, String tipoFunc) {
        // apenas o processo 'server' chama este método
        File arquivo = new File(CAMINHO_BASE + tipoFunc + ".txt");
        
        try (FileWriter fw = new FileWriter(arquivo, true);
             PrintWriter writer = new PrintWriter(fw)) {

            if (objeto != null) {
                writer.println(objeto.getNome() + "," + objeto.getCPF() + "," + objeto.getMatricula());
            } else {
                JOptionPane.showMessageDialog(null, "Objeto inválido para gravação.");
            }
        } catch (IOException e) {
            ExcecaoPainel.exibirExcecao("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public static List<Funcionario> lerArquivo(String tipoFunc) {
        File arquivo = new File(CAMINHO_BASE + tipoFunc + ".txt");
        List<Funcionario> funcionarios = new ArrayList<Funcionario>();

        if (!arquivo.exists()) {
            return funcionarios;
        }

        try (FileReader fr = new FileReader(arquivo);
             BufferedReader reader = new BufferedReader(fr)) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(",");
                if (dados.length < 3) continue;

                String nome = dados[0];
                String cpf = dados[1];
                int matricula = Integer.parseInt(dados[2]);

                try {
                    // Criando a instância de funcionário para carregar na Multilista
                    Funcionario func = new Funcionario(nome, cpf, matricula);
                    funcionarios.add(func);
                } catch (Excecao e) {
                    System.err.println("Erro ao instanciar funcionário do arquivo: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo " + tipoFunc + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato na matrícula: " + e.getMessage());
        }
        return funcionarios;
    }

    public static void removerFuncionario(String tipoFunc, int matricula) {
        // Regra 2: O coordenador central usa este método para manter a consistência
        List<Funcionario> funcionarios = lerArquivo(tipoFunc);
        File arquivo = new File(CAMINHO_BASE + tipoFunc + ".txt");

        try (FileWriter fw = new FileWriter(arquivo, false); 
             PrintWriter writer = new PrintWriter(fw)) {

            for (Funcionario func : funcionarios) {
                if (func.getMatricula() != matricula) {
                    writer.println(func.getNome() + "," + func.getCPF() + "," + func.getMatricula());
                }
            }
        } catch (IOException e) {
            ExcecaoPainel.exibirExcecao("Erro ao remover funcionário do arquivo: " + e.getMessage());
        }
    }
}