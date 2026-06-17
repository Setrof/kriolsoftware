package src.structures;

public class ListaLigada<T> {
    private No<T> head;
    private int size;

    public ListaLigada() {
        this.head = null;
        this.size = 0;
    }

    //para adicionar novo elemento no final da lista 
    public void adicionar(T dado) {
        No<T> novoNo = new No<>(dado);
        if (head == null) {
            head = novoNo;
        } else {
            No<T> atual = head;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(novoNo);
        }
        size++;
    }

    // getter par ao primeiro no (essencial para buscar e filtrar fora da classe)
    public No<T> getHead() {
        return head;
    }

    //vai retornar o tamanho da lista 
    public int getSize() {
        return size;
    }

    //vai verificar se a lista estiver vazia
    public boolean estaVazia() {
        return size == 0;
    }
    //para limpar a lista (util quando prexcisarmos de recarregar os dados da BD)
    public void limpar() {
        this.head = null;
        this.size = 0;
    }
}
