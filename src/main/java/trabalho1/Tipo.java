package trabalho1;

/**
 *
 * @author petri
 */
interface Tipo {

    /*
        Essa interface foi uma proposta do grupo para facilitar o Semântico e o 
        Gerador, abstraindo as manipulações de tipos.
    
        Aqui podemos dar merge em dois tipos (de acordo com as regras semânticas 
        e possíveis regras de casting) utilizando mergeTipos().
        
        Também podemos verificar se é possível "atribuir um tipo a outro" (com checkAtribuicao())
        e se um parâmetro casa com um argumento (formal vs real) com checkFuncParameters(). 
    
        Essa interface permite acessar dois "tipos distintos": TipoEnum ("tipos constantes/estáticos")
        e TipoEstendido ("tipos dinâmicos", registros).
     */

    public static Tipo valueOf(String value) {
        try {
            // Enums utilizam a versão UPPERCASE da string
            return TipoEnum.valueOf(value.toUpperCase());
        } catch (Exception e) {
            // Registros são case-sensitive
            return TipoEstendido.valueOf(value);
        }
    }

    public static Tipo mergeTipos(Tipo first, Tipo second) {
        /*
            Essa função permite dar merge em dois tipos, de acordo com a lógica
            permitida pelo semântico. Em especial, permite saber se dois tipos
            não dão merge, gerando nesse caso um TipoEnum.UNDEFINED.
        
            Ex: real merge inteiro -> real
                inteiro merge literal -> UNDEFINED
         */
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
        } else if ((first instanceof TipoEnum) && (second instanceof TipoEstendido)) {
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
            return TipoEstendido.checkFuncParameters((TipoEstendido) first, (TipoEstendido) second);
        }
    }
}

class TipoEstendido implements Tipo {

    String tipo_estendido;

    static boolean checkAtribuicao(TipoEstendido first, TipoEstendido second) {
        return first.tipo_estendido.equals(second.tipo_estendido);
    }

    static boolean checkFuncParameters(TipoEstendido first, TipoEstendido second) {
        return mergeTipos(first, second) != TipoEnum.UNDEFINED;
    }

    public static Tipo mergeTipos(TipoEstendido first, TipoEstendido second) {
        if (second == null) {
            return first;
        }

        if (first.equals(second)) {
            return first;
        } else {
            return TipoEnum.UNDEFINED;
        }
    }

    public TipoEstendido(String tipo_estendido) {
        this.tipo_estendido = tipo_estendido;
    }

    public static Tipo valueOf(String value) {
        EntradaTS entrada = PilhaDeTabelas.getSimbolo(value);
        if (entrada != null && entrada.getTipo() == TipoEnum.REGISTRO) {
            return new TipoEstendido(entrada.getNome());
        }

        throw new RuntimeException("tipo " + value + " not found");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            return this.tipo_estendido.equals(s);
        } else if (o instanceof TipoEstendido) {
            TipoEstendido s = (TipoEstendido) o;
            return this.tipo_estendido.equals(s.tipo_estendido);
        }
        return false;
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
