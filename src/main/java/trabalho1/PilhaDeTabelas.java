package trabalho1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class PilhaDeTabelas {

    static private LinkedList<TabelaDeSimbolos> pilha = new LinkedList<TabelaDeSimbolos>();

    static public void empilhar(TabelaDeSimbolos ts) {
        pilha.push(ts);
    }

    static public TabelaDeSimbolos topo() {
        return pilha.peek();
    }

    static public boolean existeSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            if (ts.existeSimbolo(nome)) {
                return true;
            }
        }
        return false;
    }

    static public EntradaTS getSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            EntradaTS entry = ts.getSimbolo(nome);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    static public void desempilhar() {
        TabelaDeSimbolos ret = pilha.pop();
        // Saida.println(ret.toString());
    }

    static public List<TabelaDeSimbolos> getTodasTabelas() {
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
