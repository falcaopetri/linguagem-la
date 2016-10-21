/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author petri
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        System.out.println(inputFilePath);
        System.out.println(outputFilePath);

        FileReader inputTestCase = new FileReader(inputFilePath);
        
        ANTLRInputStream input = new ANTLRInputStream(inputTestCase);
        LALexer lexer = new LALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LAParser parser = new LAParser(tokens);
        
        // parser.programa();

        Saida.println("afjalsdhfkashd");
        PrintWriter outputTestCase = new PrintWriter(outputFilePath, "UTF-8");
        outputTestCase.print(Saida.getTexto());
        outputTestCase.close();
    }
}
