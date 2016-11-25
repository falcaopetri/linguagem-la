package trabalho1;

public class EntradaTS {

    private String nome;
    private Tipo tipo;
    private boolean is_pointer;

    public EntradaTS(String nome, Tipo tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.is_pointer = false;
    }

    public EntradaTS(String nome, Tipo tipo, boolean is_pointer) {
        this.nome = nome;
        this.tipo = tipo;
        this.is_pointer = is_pointer;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public boolean isPointer() {
        return is_pointer;
    }

    @Override
    public String toString() {
        return nome + "(" + (is_pointer ? "^" : "") + tipo + ")";
    }
}
