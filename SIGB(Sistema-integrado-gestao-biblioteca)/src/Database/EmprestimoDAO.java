package src.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import src.model.Emprestimo;
import src.model.Livro;
import src.model.Utilizador;
import src.structures.ListaLigada;

public class EmprestimoDAO {

    public boolean inserir(Emprestimo emprestimo) {
        String sql = "INSERT INTO Emprestimo (livro_isbn, utilizador_id, dataEmprestimo, dataDevolucaoPrevista, dataDevolucaoReal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, emprestimo.getLivro().getIsbn());
            pstmt.setInt(2, emprestimo.getUtilizador().getId());
            pstmt.setString(3, emprestimo.getDataEmprestimo().toString());
            pstmt.setString(4, emprestimo.getDataDevolucaoPrevista().toString());
            pstmt.setNull(5, java.sql.Types.VARCHAR);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir emprestimo: " + e.getMessage());
            return false;
        }
    }

    public boolean registarDevolucao(int idEmprestimo, LocalDate dataDevolucaoReal) {
        String sql = "UPDATE Emprestimo SET dataDevolucaoReal = ? WHERE id = ?";
        
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dataDevolucaoReal.toString());
            pstmt.setInt(2, idEmprestimo);
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao registar devolucao: " + e.getMessage());
            return false;
        }
    }

    public ListaLigada<Emprestimo> listarTodos() {
        ListaLigada<Emprestimo> lista = new ListaLigada<>();
        String sql = "SELECT e.id, e.dataEmprestimo, e.dataDevolucaoPrevista, e.dataDevolucaoReal, " +
                     "l.isbn, l.titulo, l.autor, l.Genero, l.editora, l.anoPublicacao, l.emprestado, " +
                     "u.id AS id_user, u.nome, u.morada, u.cni, u.email, u.telefone " +
                     "FROM Emprestimo e " +
                     "INNER JOIN Livro l ON e.livro_isbn = l.isbn " +
                     "INNER JOIN Utilizador u ON e.utilizador_id = u.id";

        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("Genero"),
                    rs.getString("editora"),
                    rs.getInt("anoPublicacao"),
                    rs.getInt("emprestado") == 1
                );
                
                Utilizador utilizador = new Utilizador(
                    rs.getInt("id_user"),
                    rs.getString("nome"),
                    rs.getString("morada"),
                    rs.getString("cni"),
                    rs.getString("email"),
                    rs.getString("telefone")
                );

                LocalDate dataEmp = LocalDate.parse(rs.getString("dataEmprestimo"));
                LocalDate dataPrev = LocalDate.parse(rs.getString("dataDevolucaoPrevista"));

                String dataRealStr = rs.getString("dataDevolucaoReal");
                LocalDate dataReal = (dataRealStr != null && !dataRealStr.isEmpty()) ? LocalDate.parse(dataRealStr) : null;
                
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    livro,
                    utilizador,
                    dataEmp,
                    dataPrev,
                    dataReal
                );
                lista.adicionar(emprestimo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar emprestimos: " + e.getMessage());
        }
        return lista;
    }
}