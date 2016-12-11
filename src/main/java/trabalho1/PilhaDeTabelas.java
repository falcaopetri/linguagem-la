package trabalho1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Prof. Daniel Lucrédio
 */
public class PilhaDeTabelas {

    /*
        Tabela de Símbolos reaproveitada de Construção de Compiladores 1.
        Modificada conforme o necessário. De forma especial, foi transformada em
        uma "classe estática". Assim podemos facilmente reutilizar a Tabela de 
        Símbolos no Gerador de código.
     */
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
        pilha.pop();
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
