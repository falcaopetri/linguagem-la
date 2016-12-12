package trabalho1;

public class Saida {

    /*
        Saida reaproveitada de Construção de Compiladores 1.
        Modificada conforme o necessário.
        Temos duas Saidas, Saida e SaidaGerador. A primeira é responsável pelo
        sintático e semântico, e a segunda pelo 
     */
 /*
        Flag force em println() é realmente uma má ideia (bad design).
        Introduzida para passar nos casos do sintático. Deu certo.
     */
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

    public static void print(String txt) {
        texto.append(txt);
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
