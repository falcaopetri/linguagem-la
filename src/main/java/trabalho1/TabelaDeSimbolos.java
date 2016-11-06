public class TabelaDeSimbolos {
    private String escopo;
    private List<EntradaTS> simbolos;
    
    public TabelaDeSimbolos(String escopo) {
        simbolos = new ArrayList<EntradaTS>();
        this.escopo = escopo;
    }
    
    public void adicionarSimbolo(String nome, String tipo) {
        simbolos.add(new EntradaTS(nome,tipo));
    }
    
    public void adicionarSimbolos(List<String> nomes, String tipo) {
        for(String s:nomes) {
            simbolos.add(new EntradaTS(s, tipo));
        }
    }
    
    public boolean existeSimbolo(String nome) {
        for(EntradaTS etds:simbolos) {
            if(etds.getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        String ret = "Escopo: "+escopo;
        for(EntradaTS etds:simbolos) {
            ret += "\n   "+etds;
        }
        return ret;
    }
}