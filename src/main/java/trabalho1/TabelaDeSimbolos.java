package trabalho1;

import java.util.ArrayList;
import java.util.List;

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
        simbolos.add(new EntradaTS(nome, tipo, is_pointer));
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
        for (EntradaTS etds : simbolos) {
            String nomes[] = nome.split("\\.", 2);
            if (etds.getNome().equals(nomes[0])) {
                if (etds.getTipo() instanceof TipoEnum) {
                    return nomes.length == 1;
                } else if (etds.getTipo() instanceof TipoEstendido) {
                    TipoEstendido tipo = (TipoEstendido) etds.getTipo();
                    EntradaTSRegistro registro = (EntradaTSRegistro) PilhaDeTabelas.getSimbolo(tipo.tipo_estendido);

                    if (nomes.length == 1) {
                        return true;
                    } else if (registro.existeSimbolo(nomes[1])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public EntradaTS getSimbolo(String nome) {
        for (EntradaTS etds : simbolos) {
            String nomes[] = nome.split("\\.", 2);
            if (etds.getNome().equalsIgnoreCase(nomes[0])) {
                if (etds.getTipo() instanceof TipoEnum) {
                    if (nomes.length == 1) {
                        return etds;
                    }
                    else {
                        return null;
                    }
                } else if (etds.getTipo() instanceof TipoEstendido) {
                    TipoEstendido tipo = (TipoEstendido) etds.getTipo();
                    EntradaTSRegistro registro = (EntradaTSRegistro) PilhaDeTabelas.getSimbolo(tipo.tipo_estendido);

                    if (nomes.length == 1) {
                        return etds;
                    } else if (registro.existeSimbolo(nomes[1])) {
                        return registro.getTabela().getSimbolo(nomes[1]);
                    }
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
