package src.model;
public class Livro {
    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private String editora;
    private int anoPublicacao;
    private boolean esmprestado;

    //construtor para usar ao carregar os dados do livro da base de dados
    public Livro(String isbn, String titulo, String autor, String genero, String editora, int anoPublicacao, boolean esmprestado) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.esmprestado = esmprestado;
    }
// Getters and setters (eu adicionei essa parte para me facilitar em chamadas de servico e para a view)
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEditora() {
        return editora;
    }
    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public boolean getEmprestado() {
        return esmprestado;
    }
    public void setEmprestado(boolean esmprestado) {
        this.esmprestado = esmprestado;
    }
    //para facilitar listagem via consola rapido
    @Override
    public String toString() {
        return "Livro[isbn=" + isbn + ", titulo=" + titulo + ", autor=" + autor + ", genero=" + genero + ", editora=" + editora + ", anoPublicacao=" + anoPublicacao + ", esmprestado=" + esmprestado + "]";
    }
}
