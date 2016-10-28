package trabalho1;

import main.antlr4.LABaseVisitor;
import main.antlr4.LAParser;

public class AnalisadorSemantico extends LABaseVisitor {
    // TODO implementar AnalisadorSemantico
    
    
    @Override
    public Object visitPrograma(LAParser.ProgramaContext ctx) {
        return visitCorpo(ctx.corpo()); 
    }
    
}
