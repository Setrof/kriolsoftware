package src.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.model.Livro;
import src.structures.ListaLigada;

public class LivroDAO {

    // 1. Método para Inserir um Livro no SQLite
    // 1. Método para Inserir um Livro no SQLite
    public boolean inserir(Livro livro) {
        String sql = "INSERT INTO Livro (isbn, titulo, autor, Genero, editora, anoPublicacao, emprestado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livro.getIsbn());
            pstmt.setString(2, livro.getTitulo());
            pstmt.setString(3, livro.getAutor());
            pstmt.setString(4, livro.getGenero());
            pstmt.setString(5, livro.getEditora());
            pstmt.setInt(6, livro.getAnoPublicacao());
            pstmt.setInt(7, livro.getEmprestado() ? 1 : 0);
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro no SQLite: " + e.getMessage());
            return false;
        }
    }

    // 2. Método para Atualizar um Livro no SQLite
    public boolean atualizar(Livro livro) {
        String sql = "UPDATE Livro SET titulo = ?, autor = ?, Genero = ?, editora = ?, anoPublicacao = ?, emprestado = ? WHERE isbn = ?";
        
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getEditora());
            pstmt.setInt(5, livro.getAnoPublicacao());
            pstmt.setInt(6, livro.getEmprestado() ? 1 : 0);
            pstmt.setString(7, livro.getIsbn());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro no SQLite: " + e.getMessage());
            return false;
        }
    }

    // 3. Método para Remover um Livro do SQLite
    public boolean remover(String isbn) {
        String sql = "DELETE FROM Livro WHERE isbn = ?";
        
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro no SQLite: " + e.getMessage());
            return false;
        }
    }

    // 4. Carrega todos os livros para a Lista Ligada
    public ListaLigada<Livro> listarTodos() {
        ListaLigada<Livro> lista = new ListaLigada<>();
        String sql = "SELECT * FROM Livro";
        
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
                lista.adicionar(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros do SQLite: " + e.getMessage());
        }
        return lista;
    }
}