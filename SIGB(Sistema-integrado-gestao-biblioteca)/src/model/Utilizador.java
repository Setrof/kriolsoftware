package src.model;

public class Utilizador {
    private int id;
    private String nome;
    private String morada;
    private String CNI;
    private String email;
    private String telefone;

    //construtor para usar ao carregar os dados do utilizador da base de dados
    public Utilizador(int id, String nome, String morada, String CNI, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.CNI = CNI;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }
    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCNI() {
        return CNI;
    }
    public void setCNI(String CNI) {
        this.CNI = CNI;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

//para facilitar listagem via consola rapido
@Override
public String toString() {
    return "Utilizador[id=" + id + ", nome=" + nome + ", morada=" + morada + ", CNI=" + CNI + ", email=" + email + ", telefone=" + telefone + "]";
    }
}