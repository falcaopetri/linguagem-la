/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

/**
 *
 * @author petri
 */
enum Tipo {
    NONE, LOGICO, INTEIRO, REAL, PONTEIRO, ENDERECO, LITERAL, REGISTRO, FUNC_PROC, UNDEFINED, TIPO;

    static Tipo mergeTipos(Tipo first, Tipo second) {
        if (first == null || first == Tipo.NONE) {
            return second;
        }
        if (second == null || second == Tipo.NONE) {
            return first;
        }

        if ((first == Tipo.REAL || first == Tipo.INTEIRO) && (second == Tipo.REAL || second == Tipo.INTEIRO)) {
            // TODO se first e second são INTEIROs, não faz sentido transformar o tipo para REAL
            return Tipo.REAL;
        }

        if (first != second) {
            return Tipo.UNDEFINED;
        }

        return first;
    }

    static boolean checkAtribuicao(Tipo first, Tipo second) {
        if (first == Tipo.PONTEIRO && second == Tipo.ENDERECO) {
            return true;
        }
        if ((first == Tipo.REAL || first == Tipo.INTEIRO) && (second == Tipo.REAL || second == Tipo.INTEIRO)) {
            return true;
        }
        if (first == Tipo.LITERAL && second == Tipo.LITERAL) {
            return true;
        }
        if (first == Tipo.LOGICO && second == Tipo.LOGICO) {
            return true;
        }
        if (first == Tipo.REGISTRO && second == Tipo.REGISTRO) {
            return true;
        }

        return false;
    }

    static boolean checkFuncParameters(Tipo first, Tipo second) {
        if (first == Tipo.PONTEIRO && second == Tipo.ENDERECO) {
            return true;
        }

        return first == second;
    }
}
