package trabalho1;

public class EntradaTS {

    private String nome;
    private Tipo tipo;

    /*
        Em algum momento foi adicionado essa flag para ajudar na implementação
        de manipulações com ponteiros.
        Aparentemente ela não foi utilizada. Provavelmente acabamos utilizando
        o acesso à gramática (e.g. verificar se '^' aparece no input) para
        tratar os casos de teste.
        Estou removendo a função isPointer(), mas deixarei essa flag aqui.
     */
    // private boolean is_pointer;

    public EntradaTS(String nome, Tipo tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return nome + "(" + tipo + ")";
    }
}
