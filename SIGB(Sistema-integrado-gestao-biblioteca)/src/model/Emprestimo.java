package src.model;

import java.time.LocalDate;
public class Emprestimo {
    private int id; //vai pegar PRIMARY KEY da minha BD
    private Livro livro; // objeto livro completo envez de apenas o ISBN, para facilitar a visualizacao na view
    private Utilizador utilizador; //objeto utilizador completo envez de apenas o ID, para facilitar a visualizacao na view
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal; //deve ser null ate o slivro ser devolvido

//construtor para caregarmos os dados da BD
public Emprestimo(int id, Livro livro, Utilizador utilizador, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal) {
    this.id = id;
    this.livro = livro;
    this.utilizador = utilizador;
    this.dataEmprestimo = dataEmprestimo;
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    this.dataDevolucaoReal = dataDevolucaoReal;
    }
    
// Getters and setters
    public int getId() {
        return id;
    }    
    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }
    public void setLivro(Livro livro) {
        this.livro = livro;
}
    public Utilizador getUtilizador() {
        return utilizador;
    }
    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }
    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }
    //para facilitar listagem via consola rapido
    @Override
    public String toString() {
        String status = (dataDevolucaoReal == null) ? "" : "Devolvido em: " + dataDevolucaoReal;
        return "Emprestimo{" +
                "id=" + id +
                ", livro=" + livro.getTitulo() +
                ", utilizador=" + utilizador.getNome() +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
                ", status='" + status + '\'' +
                '}';
    }
}