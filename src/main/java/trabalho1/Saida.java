package trabalho1;

public class Saida {

    private static StringBuffer texto = new StringBuffer();

    public static void println(String txt) {
        if (texto.length() == 0) {
            // Força uma única linha de erro
            // O parser continua detectando os próximos erros,
            // mas apenas o primeiro output de erro é processado
            texto.append(txt).append("\n");
        }
    }

    public static void force_println(String txt) {
        texto.append(txt).append("\n");
    }

    public static void clear() {
        texto = new StringBuffer();
    }

    public static String getTexto() {
        return texto.toString();
    }
}
