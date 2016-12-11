package trabalho1;

/**
 *
 * @author petri
 */
public class EntradaTSRegistro extends EntradaTS {

    /*
        Representação de um registro (struct).
        
        Armazena uma tabela de simbolos interna.
        Exemplo de polimorfismo: não precisamos modificar nossa Tabela de Símbolos para armazenar
        um novo tipo de EntradaTS (apesar de termos que modificar getSimbolo() para saber trabalhar
        com uma entrada que tem uma tabela aninhada...).
     */
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
