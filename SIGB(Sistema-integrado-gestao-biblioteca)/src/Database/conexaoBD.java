package src.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conexaoBD {
    
    // Caminho para o arquivo SQLite que está no diretório do projeto 
    // Isso é relativo ao meu projeto, mas caso queira colocar em outro lugar, basta colocar o caminho completo do arquivo
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    /**
     * Estabelece e retorna uma conexão ativa com a base de dados SQLite
     * @return Connection objeto de conexão ou null em caso de erro
     */
    public static Connection conectar() {
        try {
            // Registra o driver JDBC do SQLite explicitamente
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do SQLite não foi encontrado na pasta lib! " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("Erro: Não foi possível conectar à base de dados biblioteca.db! " + e.getMessage());
            return null;
        }
    }

    /**
     * Lê o arquivo de script SQL e executa as queries para criar as tabelas
     * caso elas ainda não existam no arquivo biblioteca.db
     */
 public static void inicializarBaseDeDados() {
    // 1. Forçar o caminho baseado na raiz do projeto
    String caminhoScript = "sql/srcipt_tabelas.sql"; 
    
    System.out.println("\n=== [DEBUG INICIALIZAÇÃO] DE LER O SCRIPT ===");
    
    java.io.File arquivoScript = new java.io.File(caminhoScript);
    if (!arquivoScript.exists()) {
        System.err.println("[ERRO CRÍTICO] O ficheiro NÃO FOI ENCONTRADO no caminho: " + arquivoScript.getAbsolutePath());
        System.err.println("Por favor, verifica se o nome da pasta é 'sql' e o arquivo é 'srcipt_tabelas.sql'!");
        return;
    } else {
        System.out.println("[OK] Ficheiro encontrado em: " + arquivoScript.getAbsolutePath());
    }

    try (Connection conn = conectar();
         Statement stmt = conn.createStatement();
         BufferedReader br = new BufferedReader(new FileReader(arquivoScript))) {
        
        if (conn == null) {
            System.err.println("[ERRO] Conexão com a base de dados falhou (nula).");
            return;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        String linha;
        
        while ((linha = br.readLine()) != null) {
            String linhaLimpa = linha.trim();
            
            // Ignora comentários comuns
            if (linhaLimpa.startsWith("--") || linhaLimpa.startsWith("/*") || linhaLimpa.isEmpty()) {
                continue;
            }
            sqlBuilder.append(linhaLimpa).append(" ");
        }

        // Executar o script completo de uma vez só para evitar falhas no split do SQLite
        String scriptCompleto = sqlBuilder.toString().trim();
        
        if (scriptCompleto.isEmpty()) {
            System.err.println("[AVISO] O ficheiro SQL parece estar vazio ou só contém comentários!");
            return;
        }

        System.out.println("[DB] A enviar comandos para o SQLite...");
        
        // No SQLite, podemos separar por ";" e rodar individualmente se o execute falhar em bloco
        String[] comandos = scriptCompleto.split(";");
        int contagem = 0;
        
        for (String comando : comandos) {
            String cmdTratado = comando.trim();
            if (!cmdTratado.isEmpty()) {
                System.out.println("[Executando Query]: " + cmdTratado);
                stmt.execute(cmdTratado);
                contagem++;
            }
        }
        
        System.out.println("[Sucesso] Tabelas configuradas! Total de blocos SQL rodados: " + contagem);
        System.out.println("===================================================\n");

    } catch (IOException e) {
        System.err.println("[ERRO DE LEITURA]: " + e.getMessage());
    } catch (SQLException e) {
        System.err.println("[ERRO SQL NO SCRIPT]: Verifique a sintaxe das tabelas! Detalhes: " + e.getMessage());
        }
    }
}