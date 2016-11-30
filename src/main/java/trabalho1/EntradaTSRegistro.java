package trabalho1;

/**
 *
 * @author petri
 */
public class EntradaTSRegistro extends EntradaTS {

    TabelaDeSimbolos ts;

    public EntradaTSRegistro(String nome, TabelaDeSimbolos ts) {
        super(nome, TipoEnum.REGISTRO);
        this.ts = ts;
    }

    public TabelaDeSimbolos getTabela() {
        return ts;
    }

    public boolean existeSimbolo(String nome) {
        return ts.existeSimbolo(nome);
    }
}
