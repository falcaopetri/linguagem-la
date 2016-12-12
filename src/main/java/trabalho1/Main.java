package trabalho1;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import main.antlr4.LALexer;
import main.antlr4.LAParser;
import main.antlr4.LAParser.ProgramaContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Main {

    public static void main(String[] args) throws IOException {
        /*
            Main permite dois tipos de execução:
                - corretor automático: recebe input/output files de args[], pois é assim
                                        que o corretor disponibilizado espera que a main funcione.
                                        Para executá-lo, basta fazer a build e executar o projeto (F6).
                - debugging: podemos também especificar o path de input/output diretamente. Nesse caso,
                                executamos o arquivo Main.java (SHIFT+F6), e não o projeto.
         */

        // Corretor automático:
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        // Debugging:
        // String inputFilePath = "corretor/casosDeTesteT1/3.arquivos_sem_erros/1.entrada/17.registro_tipo_impressao.alg";
        // String outputFilePath = "saida";
        FileReader inputTestCase = new FileReader(inputFilePath);

        ANTLRInputStream input = new ANTLRInputStream(inputTestCase);
        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LAParser parser = new LAParser(tokens);

        MeuErrorListener mel = new MeuErrorListener();

        lexer.removeErrorListeners();
        parser.removeErrorListeners();

        parser.addErrorListener(mel);

        try {
            ProgramaContext aas = parser.programa();

            if (Saida.is_modified()) {
                // Força lançamento de exceção quando ocorrido algum erro léxico/sintático
                throw new ParseCancellationException("Exceção gerada no léxico/sintático.");
            }

            AnalisadorSemantico as = new AnalisadorSemantico();
            as.visitPrograma(aas);

            if (Saida.is_modified()) {
                // Força lançamento de exceção quando ocorrido algum erro semântico
                throw new ParseCancellationException("Exceção gerada no semântico.");
            }

            Gerador g = new Gerador();
            g.visitPrograma(aas);
        } catch (ParseCancellationException ex) {
            // Adiciona esse output, esperado no Sintático e Semântico
            Saida.println("Fim da compilacao", true);
        }

        PrintWriter outputTestCase = new PrintWriter(outputFilePath, "UTF-8");

        outputTestCase.print(Saida.getTexto());

        outputTestCase.close();
    }
}
