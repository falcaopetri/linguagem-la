package trabalho1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class PilhaDeTabelas {

    private LinkedList<TabelaDeSimbolos> pilha;

    public PilhaDeTabelas() {
        pilha = new LinkedList<TabelaDeSimbolos>();
    }

    public void empilhar(TabelaDeSimbolos ts) {
        pilha.push(ts);
    }

    public TabelaDeSimbolos topo() {
        return pilha.peek();
    }

    public boolean existeSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            if (ts.existeSimbolo(nome)) {
                return true;
            }
        }
        return false;
    }

    public EntradaTS getSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            EntradaTS entry = ts.getSimbolo(nome);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    public void desempilhar() {
        TabelaDeSimbolos ret = pilha.pop();
        // Saida.println(ret.toString());
    }

    public List<TabelaDeSimbolos> getTodasTabelas() {
        return pilha;
    }

    @Override
    public String toString() {
        String r = "";
        for (TabelaDeSimbolos t : getTodasTabelas()) {
            r += t.toString();
        }
        return r;
    }

}
