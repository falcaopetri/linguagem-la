package trabalho1;

import main.antlr4.LABaseVisitor;
import main.antlr4.LAParser;

/**
 *
 * @author Júnior
 */
public class Gerador extends LABaseVisitor{

    @Override
    public Object visitPrograma(LAParser.ProgramaContext ctx) {
        SaidaGerador.println("#include <stdio.h>", true);
        SaidaGerador.println("#include <stdlib.h>", true);
        visitDeclaracoes(ctx.declaracoes());
        SaidaGerador.println("int main() {", true);
        visitCorpo(ctx.corpo());
        SaidaGerador.println("return 0;");
        SaidaGerador.println("}", true);
        return null;
    }

    @Override
    public Object visitDeclaracoes(LAParser.DeclaracoesContext ctx) {
        return super.visitDeclaracoes(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVariavel(LAParser.VariavelContext ctx) {
        visitTipo(ctx.tipo());
        SaidaGerador.println(" " + ctx.IDENT().getText(), true);
        visitDimensao(ctx.dimensao());
        visitMais_var(ctx.mais_var());
        SaidaGerador.println(";", true);
        return null; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitTipo_basico(LAParser.Tipo_basicoContext ctx) {
        if("inteiro".equals(ctx.getStart().getText())){
            SaidaGerador.println("int", true);
        }
        else if("literal".equals(ctx.getStart().getText())){
            SaidaGerador.println("char[80]", true);
        }
        else if("real".equals(ctx.getStart().getText())){
            SaidaGerador.println("float", true);
        }
        else if("logico".equals(ctx.getStart().getText())){
            SaidaGerador.println("boolean", true);
        }
        return null;
            
    }

    @Override
    public Object visitCmd(LAParser.CmdContext ctx) {
        if("leia".equals(ctx.getStart().getText())){
            SaidaGerador.println("scanf(\"", true);
            if(ctx.identificador() != null){
                EntradaTS tipo = PilhaDeTabelas.getSimbolo(ctx.identificador().getText());
                if (tipo != null) {
                    SaidaGerador.println(returnMask(tipo.getTipo().toString()), true);
                }
                SaidaGerador.println("\", &" + ctx.identificador().getText(),true);
            }
            SaidaGerador.println(");", true);
        }
        else if("escreva".equals(ctx.getStart().getText())){
            SaidaGerador.println("print(\"", true);
            if(ctx.expressao() != null){
                //SaidaGerador.println(returnMask(ALGUMACOISA.getTipo()), true);
            }
            if(ctx.mais_expressao() != null){
                LAParser.Mais_expressaoContext maisexp = ctx.mais_expressao();
                while(maisexp != null){
                    //SaidaGerador.println(" " + returnMask(ALGUMACOISA.getTipo()), true);
                    maisexp = maisexp.mais_expressao();
                }
            }
            
            SaidaGerador.println(");", true);
        }
        return null;      
    }
    
    public String returnMask(String symbol){
        if (symbol.equals("inteiro")){
            return "%d";
        }
        else if(symbol.equals("real")){
            return "%f";
        }
        else if(symbol.equals("literal")){
            return "%s";
        }
        
        return "Outro tipo";
    }
}