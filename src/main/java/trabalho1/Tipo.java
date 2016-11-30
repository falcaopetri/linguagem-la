package trabalho1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author petri
 */
interface Tipo {

    public static Tipo valueOf(String value) {
        try {
            return TipoEnum.valueOf(value);
        } catch (Exception e) {
            return TipoEstendido.valueOf(value);
        }
    }

    public static Tipo mergeTipos(Tipo first, Tipo second) {
        if ((first instanceof TipoEnum) && (second instanceof TipoEnum || second == null)) {
            return TipoEnum.mergeTipos((TipoEnum) first, (TipoEnum) second);
        } else if ((first instanceof TipoEstendido) && (second instanceof TipoEstendido || second == null)) {
            return TipoEstendido.mergeTipos((TipoEstendido) first, (TipoEstendido) second);
        } else if ((first instanceof TipoEstendido) && (second instanceof TipoEnum)) {
            if ((TipoEnum) second == TipoEnum.NONE) {
                return first;
            } else {
                return TipoEnum.UNDEFINED;
            }
        }
        else if ((first instanceof TipoEnum) && (second instanceof TipoEstendido)) {
            if (first == TipoEnum.NONE) {
                return second;
            } else {
                return TipoEnum.UNDEFINED;
            }
        }
        return TipoEnum.UNDEFINED;
    }

    static boolean checkAtribuicao(Tipo first, Tipo second) {
        if ((first instanceof TipoEnum) && (second instanceof TipoEnum)) {
            return TipoEnum.checkAtribuicao((TipoEnum) first, (TipoEnum) second);
        } else if ((first instanceof TipoEstendido) && (second instanceof TipoEstendido)) {
            return TipoEstendido.checkAtribuicao((TipoEstendido) first, (TipoEstendido) second);
        } else if ((first instanceof TipoEstendido) && (second instanceof TipoEnum)) {
            return false;
        }
        
        return false;
    }

    public static boolean checkFuncParameters(Tipo first, Tipo second) {
        try {
            return TipoEnum.checkFuncParameters((TipoEnum) first, (TipoEnum) second);
        } catch (Exception e) {
            // TODO
            throw new UnsupportedOperationException("Not supported yet."); //TipoEstendido.mergeTipos(first, second);
        }
    }
}

class TipoEstendido implements Tipo {
    // TODO mergeTipos()

    private static List<TipoEstendido> tipos;

    static boolean checkAtribuicao(TipoEstendido first, TipoEstendido second) {
        return first.tipo_estendido.equals(second.tipo_estendido);
    }

    String tipo_estendido;

    static {
        tipos = new ArrayList<>();
    }

    public static Tipo mergeTipos(TipoEstendido first, TipoEstendido second) {
        if (second == null) {
            return first;
        }

        if (first == second) {
            return first;
        } else {
            return TipoEnum.UNDEFINED;
        }
    }

    public static void addTipo(String tipo) {
        // TODO flaw: tipo estendido é case insensitive!
        tipos.add(new TipoEstendido(tipo.toUpperCase()));
    }

    public TipoEstendido(String tipo_estendido) {
        this.tipo_estendido = tipo_estendido;
    }

    public static Tipo valueOf(String value) {
        for (TipoEstendido t : tipos) {
            if (t.equals(value)) {
                return t;
            }
        }

        throw new RuntimeException("tipo " + value + " not found");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String s = (String) o;
        return this.tipo_estendido.equals(s.toUpperCase());
    }

}

enum TipoEnum implements Tipo {
    NONE, LOGICO, INTEIRO, REAL, PONTEIRO, ENDERECO, LITERAL, REGISTRO, FUNC_PROC, UNDEFINED, TIPO;

    static Tipo mergeTipos(TipoEnum first, TipoEnum second) {
        if (first == null || first == TipoEnum.NONE) {
            return second;
        }
        if (second == null || second == TipoEnum.NONE) {
            return first;
        }

        if ((first == TipoEnum.REAL || first == TipoEnum.INTEIRO) && (second == TipoEnum.REAL || second == TipoEnum.INTEIRO)) {
            // TODO se first e second são INTEIROs, não faz sentido transformar o tipo para REAL
            return TipoEnum.REAL;
        }

        if (first != second) {
            return TipoEnum.UNDEFINED;
        }

        return first;
    }

    static boolean checkAtribuicao(TipoEnum first, TipoEnum second) {
        if (first == TipoEnum.PONTEIRO && second == TipoEnum.ENDERECO) {
            return true;
        }
        if ((first == TipoEnum.REAL || first == TipoEnum.INTEIRO) && (second == TipoEnum.REAL || second == TipoEnum.INTEIRO)) {
            return true;
        }
        if (first == TipoEnum.LITERAL && second == TipoEnum.LITERAL) {
            return true;
        }
        if (first == TipoEnum.LOGICO && second == TipoEnum.LOGICO) {
            return true;
        }
        if (first == TipoEnum.REGISTRO && second == TipoEnum.REGISTRO) {
            return true;
        }

        return false;
    }

    static boolean checkFuncParameters(TipoEnum first, TipoEnum second) {
        if (first == TipoEnum.PONTEIRO && second == TipoEnum.ENDERECO) {
            return true;
        }

        return first == second;
    }
}
