package src.structures;

public class Pilha<T> {
    private No<T> topo; // o elemento que esta no topo da pilha 
    private int tamanho;

    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }

    // metodo para adicionar elemento no topo da pilha (push)
    public void empilhar(T dado) {
        No<T> novoNo = new No<>(dado);
        novoNo.setProximo(topo);
        topo = novoNo;
        tamanho++;
    }

    // metodo para remover elemento do topo da pilha (desimpilhar/pop)
    public T desimpilhar() {
        if (estaVazia()) return null;

        T dado = topo.getDado();
        topo = topo.getProximo();
        tamanho--;
        return dado;
    }

    //verifica quem esta no topo sem o remover (peek)
    public T espreitar() {
        if (estaVazia()) return null;
        return topo.getDado();
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public int getTamanho() {
        return tamanho;
    }
    //getter para o no do topo (util para listar a pilha inteira na view)
    public No<T> getTopo() {
        return topo;
    }
}