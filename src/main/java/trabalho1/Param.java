package trabalho1;

/**
 *
 * @author jvcaquino
 */
public class Param extends EntradaTS {

    /*
        Classe auxiliar para EntradaTSParam, onde representa um argumento da
        função/procedimento declarado.
     */
    // TODO Provavelmente tipo seria suficiente para o seu uso. Armazenar um nome é desnecessário.
    // Mesmo assim podemos utilizar EntradaTS, basta um nome default.
    public Param(String nome, Tipo tipo) {
        super(nome, tipo);
    }

    public String getNome() {
        return super.getNome();
    }

    public Tipo getTipo() {
        return super.getTipo();
    }

}
