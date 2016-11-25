/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.util.ArrayList;

/**
 *
 * @author jvcaquino
 */
public class EntradaTSParam extends EntradaTS {

    private ArrayList<Param> listaParametros;
    private Tipo returnType;

    public EntradaTSParam(String nome, Tipo return_type) {
        super(nome, Tipo.FUNC_PROC);
        this.returnType = return_type;
        listaParametros = new ArrayList<>();
    }

    public EntradaTSParam(String nome, Tipo return_type, ArrayList<Param> params) {
        super(nome, Tipo.FUNC_PROC);
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

        if (!(obj instanceof EntradaTSParam)) {
            return false;
        }

        EntradaTSParam e1 = (EntradaTSParam) obj;
        if (this.listaParametros.size() == e1.contarParametros()) {
            for (int i = 0; i < contarParametros(); i++) {
                Tipo tipo1 = this.listaParametros.get(i).getTipo();
                Tipo tipo2 = e1.listaParametros.get(i).getTipo();

                // TODO possivelmente esse check não é o que a gente espera
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
