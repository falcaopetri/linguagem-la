package trabalho1;

import java.util.ArrayList;

/**
 *
 * @author jvcaquino
 */
public class EntradaTSParam extends EntradaTS {

    /*
        Representação de uma função/procedimento (não é necessário fazer distinção entre eles).
        
        Armazena uma lista ordenada de seus parâmetros (formais) e um tipo de retorno.
        Exemplo de polimorfismo: não precisamos modificar nossa Tabela de Símbolos para armazenar
        um novo tipo de EntradaTS.
     */
    private ArrayList<Param> listaParametros;
    private Tipo returnType;

    public EntradaTSParam(String nome, Tipo return_type) {
        this(nome, return_type, new ArrayList<Param>());
    }

    public EntradaTSParam(String nome, Tipo return_type, ArrayList<Param> params) {
        super(nome, TipoEnum.FUNC_PROC);
        this.returnType = return_type;
        listaParametros = params;
    }

    public Tipo getReturnType() {
        return returnType;
    }

    public int contarParametros() {
        return listaParametros.size();
    }

    public void addParametro(String nomeP, Tipo tipoP) {
        Param parametro = new Param(nomeP, tipoP);
        listaParametros.add(parametro);
    }

    @Override
    public boolean equals(Object obj) {
        /*
            Comparamos igualdade com outra EntradaTSParam.
        
            Regra semântica 4: Incompatibilidade entre argumentos e parâmetros formais
            (número, ordem e tipo) na chamada de um procedimento ou uma função
         */

        if (!(obj instanceof EntradaTSParam)) {
            return false;
        }

        EntradaTSParam e1 = (EntradaTSParam) obj;
        if (this.listaParametros.size() == e1.contarParametros()) {
            for (int i = 0; i < contarParametros(); i++) {
                Tipo tipo1 = this.listaParametros.get(i).getTipo();
                Tipo tipo2 = e1.listaParametros.get(i).getTipo();

                if (!Tipo.checkFuncParameters(tipo1, tipo2)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String params = "";
        for (Param p : listaParametros) {
            params += p.toString();
        }

        return super.toString() + " " + params;
    }

}
