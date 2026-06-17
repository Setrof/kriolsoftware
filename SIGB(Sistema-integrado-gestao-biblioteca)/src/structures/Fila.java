package src.structures;

public class Fila<T> {
    private No<T> primeiro; // Inicio da fila (onde tiramos elementos)
    private No<T> ultimo; // Fim da fila (onde inserimos elementos)
    private int tamanho;

    public Fila() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }
    // esse metodo vai inserir no fim da
    public void enfileirar(T dado) {
        No<T> novoNo = new No<>(dado);
        if (estaVazia()) {
            primeiro = novoNo;
        } else {
            ultimo.setProximo(novoNo);
        }
        ultimo = novoNo;
        tamanho++;
    }

    // esse metodo vai remover do inicio da fila(deseinfileirar/dequeue)
    public T desenfileirar() {
        if (estaVazia()) return null;

        T dado = primeiro.getDado();
        primeiro = primeiro.getProximo();
        if (primeiro == null) {
            ultimo = null;
        } // se a fila ficar vazia, atualizamos o ultimo para null
        tamanho--;
        return dado;
    }
    //verifica o elemento no inicio da fila sem removê-lo (peek)
    public T espreitar() {
        if (estaVazia()) return null;
        return primeiro.getDado();
    }

    public boolean estaVazia() {
        return primeiro == null;
    }

    public int getTamanho() {
        return tamanho;
    }
}
