package trabalho1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prof. Daniel Lucrédio
 */
public class TabelaDeSimbolos {

    /*
        Tabela de Símbolos reaproveitada de Construção de Compiladores 1.
        Modificada conforme o necessário.
     */
    private String escopo;
    private List<EntradaTS> simbolos;

    public TabelaDeSimbolos(String escopo) {
        simbolos = new ArrayList<EntradaTS>();
        this.escopo = escopo;
    }

    public void adicionarSimbolo(String nome, Tipo tipo) {
        adicionarSimbolo(nome, tipo, false);
    }

    public void adicionarSimbolo(String nome, Tipo tipo, boolean is_pointer) {
        // Flag is_pointer nunca é utilizada. Veja o comentário em EntradaTS.
        simbolos.add(new EntradaTS(nome, tipo/*, is_pointer*/));
    }

    public void adicionarSimbolos(List<String> nomes, Tipo tipo) {
        for (String s : nomes) {
            simbolos.add(new EntradaTS(s, tipo));
        }
    }

    public void adicionarEntrada(EntradaTS entrada) {
        simbolos.add(entrada);
    }

    public boolean existeSimbolo(String nome) {
        return getSimbolo(nome) != null;
    }

    public EntradaTS getSimbolo(String nome) {
        // Formato esperado de nome: IDENT ('.' IDENT)*

        String nomes[] = nome.split("\\.", 2);

        for (EntradaTS etds : simbolos) {
            if (!etds.getNome().equals(nomes[0])) {
                // etds não faz parte do nome
                continue;
            }

            if (nomes.length == 1) {
                // Encontramos nossa entrada
                return etds;
            }

            if (etds.getTipo() instanceof TipoEstendido) {
                // Precisamos recuperar as informações do TipoEstendido,
                // armazenadas em alguma tabela de símbolos da Pilha de Tabelas
                TipoEstendido tipo = (TipoEstendido) etds.getTipo();
                EntradaTSRegistro registro = (EntradaTSRegistro) PilhaDeTabelas.getSimbolo(tipo.tipo_estendido);

                // Verificamos se nomes[1] está declarado na tabela de símbolos do registro
                // invocando essa mesma função existeSimbolo()
                if (nomes.length == 1) {
                    return etds;
                } else if (registro.existeSimbolo(nomes[1])) {
                    return registro.getTabela().getSimbolo(nomes[1]);
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String ret = "Escopo: " + escopo;
        for (EntradaTS etds : simbolos) {
            ret += "\n   " + etds;
        }
        return ret;
    }

    void setNome(String registro_nome) {
        escopo = registro_nome;
    }

    String getNome() {
        return escopo;
    }
}
