/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

/**
 *
 * @author Júnior
 */
public class SaidaGerador{
   private static StringBuffer texto = new StringBuffer();

    public static void println(String txt) {
        println(txt, false);
    }
    
    public static void println(String txt, boolean force) {
        if (force || !is_modified()) {
            // Força uma única linha de erro
            // O parser continua detectando os próximos erros,
            // mas apenas o primeiro output de erro é processado
            texto.append(txt).append("\n");
        }
    }

    public static boolean is_modified() {
        return texto.length() != 0;
    }

    public static void clear() {
        texto = new StringBuffer();
    }

    public static String getTexto() {
        return texto.toString();
    }  
}
