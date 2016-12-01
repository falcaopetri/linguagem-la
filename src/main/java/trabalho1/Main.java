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
        //String inputFilePath = "corretor/casosDeTesteT1/2.arquivos_com_erros_semanticos/entrada/10.algoritmo_7-2_apostila_LA.txt";
        //String outputFilePath = "saida";
        String inputFilePath = args[0];
        String outputFilePath = args[1];

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
            // TODO nem sempre que existe erro parser.programa() lançará exceção
            ProgramaContext aas = parser.programa();

            if (Saida.is_modified()) {
                // Força lançamento de exceção. Útil para exceções definidas apenas na gramática
                throw new ParseCancellationException("Exceção gerada na gramática.");
            }

            AnalisadorSemantico as = new AnalisadorSemantico();
            as.visitPrograma(aas);

            Gerador g = new Gerador();
            g.visitPrograma(aas);
        } catch (ParseCancellationException ex) {

        }
        PrintWriter outputTestCase = new PrintWriter(outputFilePath, "UTF-8");

        if (Saida.is_modified()) {
            Saida.println("Fim da compilacao", true);
            outputTestCase.print(Saida.getTexto());
        } else {
            outputTestCase.print(SaidaGerador.getTexto());
        }
        outputTestCase.close();
    }
}
