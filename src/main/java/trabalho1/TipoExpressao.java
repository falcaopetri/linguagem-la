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

// TODO rename TipoExpressao to Tipo
enum TipoExpressao {
    NONE, LOGICO, INTEIRO, REAL, PONTEIRO, ENDERECO, LITERAL, REGISTRO, FUNC_PROC, UNDEFINED;

    static TipoExpressao mergeTipos(TipoExpressao first, TipoExpressao second) {
        if (first == null || first == TipoExpressao.NONE) return second;
        if (second == null || second == TipoExpressao.NONE) return first;
        
        if ((first == TipoExpressao.REAL || first == TipoExpressao.INTEIRO) && (second == TipoExpressao.REAL || second == TipoExpressao.INTEIRO)) {
            // TODO se first e second são INTEIROs, não faz sentido transformar o tipo para REAL
            return TipoExpressao.REAL;
        }

        if (first != second) {
            return TipoExpressao.UNDEFINED;
        }

        return first;
    }

    static boolean checkAtribuicao(TipoExpressao first, TipoExpressao second) {
        if (first == TipoExpressao.PONTEIRO && second == TipoExpressao.ENDERECO) {
            return true;
        }
        if ((first == TipoExpressao.REAL || first == TipoExpressao.INTEIRO) && (second == TipoExpressao.REAL || second == TipoExpressao.INTEIRO)) {
            return true;
        }
        if (first == TipoExpressao.LITERAL && second == TipoExpressao.LITERAL) {
            return true;
        }
        if (first == TipoExpressao.LOGICO && second == TipoExpressao.LOGICO) {
            return true;
        }
        if (first == TipoExpressao.REGISTRO && second == TipoExpressao.REGISTRO) {
            return true;
        }

        return false;
    }
}
