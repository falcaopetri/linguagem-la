/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import main.antlr4.LALexer;
import main.antlr4.LAParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author petri
 */
public class Main {

    public static void main(String[] args) throws IOException {
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

        parser.programa();
        
        Saida.force_println("Fim da compilacao");

        PrintWriter outputTestCase = new PrintWriter(outputFilePath, "UTF-8");
        outputTestCase.print(Saida.getTexto());
        outputTestCase.close();
    }
}
