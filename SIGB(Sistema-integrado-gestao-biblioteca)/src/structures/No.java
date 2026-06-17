package src.structures;

public class No <T>{
    private T dado;
    private No <T> proximo;

    //contrutor para receber o objeto (pode ser utilizador,livro ou emprestimo...)
    public No(T dado) {
        this.dado = dado;
        this.proximo = null;
    }

    public T getDado() {
        return dado;
    }
    public void setDado(T dado) {
        this.dado = dado;
    }
    
    public No<T> getProximo() {
        return proximo;
    }
    public void setProximo(No<T> proximo) {
        this.proximo = proximo;
    }
}

