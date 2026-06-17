package src.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.model.Utilizador;
import src.structures.ListaLigada;

public class UtilizadorDAO {

    public boolean inserir(Utilizador user) {
        String sql = "INSERT INTO Utilizador (nome, morada, cni, email, telefone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNome());
            pstmt.setString(2, user.getMorada());
            pstmt.setString(3, user.getCNI());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getTelefone());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir utilizador: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM Utilizador WHERE id = ?";
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover utilizador: " + e.getMessage());
            return false;
        }
    }

    public ListaLigada<Utilizador> listarTodos() {
        ListaLigada<Utilizador> lista = new ListaLigada<>();
        String sql = "SELECT * FROM Utilizador";
        try (Connection conn = conexaoBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Utilizador u = new Utilizador(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("morada"),
                    rs.getString("cni"),
                    rs.getString("email"),
                    rs.getString("telefone")
                );
                lista.adicionar(u);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar utilizadores: " + e.getMessage());
        }
        return lista;
    }
}