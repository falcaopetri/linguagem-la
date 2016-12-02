package trabalho1;

import main.antlr4.LABaseVisitor;
import main.antlr4.LAParser;
import org.antlr.v4.runtime.misc.Pair;

/**
 *
 * @author Júnior
 */
public class Gerador extends LABaseVisitor {

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
        Tipo tipo = Tipo.valueOf(ctx.getStart().getText().toUpperCase());
        if (tipo == TipoEnum.INTEIRO) {
            SaidaGerador.println("int", true);
        } else if (tipo == TipoEnum.LITERAL) {
            SaidaGerador.println("char[80]", true);
        } else if (tipo == TipoEnum.REAL) {
            SaidaGerador.println("float", true);
        } else if (tipo == TipoEnum.LOGICO) {
            SaidaGerador.println("boolean", true);
        }
        return null;

    }

    @Override
    public Object visitCmd(LAParser.CmdContext ctx) {
        if ("leia".equals(ctx.getStart().getText())) {
            SaidaGerador.print("scanf(\"", true);
            if (ctx.identificador() != null) {
                EntradaTS tipo = PilhaDeTabelas.getSimbolo(ctx.identificador().getText());
                if (tipo != null) {
                    SaidaGerador.print(returnMask(tipo.getTipo()), true);
                }
                if(tipo.getTipo() == TipoEnum.LITERAL){
                    SaidaGerador.print("\", " + ctx.identificador().getText(), true);
                }
                else{
                    SaidaGerador.print("\", &" + ctx.identificador().getText(), true);
                }
            }
            SaidaGerador.println(");", true);
            
            
        } else if ("escreva".equals(ctx.getStart().getText())) {
            SaidaGerador.print("printf(\"", true);
            if (ctx.expressao() != null) {
                Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());

                String stringMask = returnMask(par.b);

                SaidaGerador.print(stringMask + "\" , " + par.a, true);
                //SaidaGerador.println(returnMask(ALGUMACOISA.getTipo()), true);
            }

            if (ctx.mais_expressao() != null) {
                LAParser.Mais_expressaoContext maisexp = ctx.mais_expressao();
                while (maisexp != null) {
                    //SaidaGerador.println(" " + returnMask(ALGUMACOISA.getTipo()), true);
                    maisexp = maisexp.mais_expressao();
                }
            }
            SaidaGerador.println(");", true);
        }
        else if("se".equals(ctx.getStart().getText())){
            SaidaGerador.println("if (", true);
            //visitarExpressão
            SaidaGerador.println("){", true);
            //visitar comandos
            if (ctx.senao_opcional() != null){
                SaidaGerador.println("} else( ");
                //visitarSenaoOp
            }
            SaidaGerador.println("}");
            
        }
        else if("caso".equals(ctx.getStart().getText())){
            SaidaGerador.println("switch(", true);
            //visitexpressão
            SaidaGerador.println(")", true);
            
            
        }
        else if(ctx.IDENT()!= null){
            SaidaGerador.println(ctx.IDENT().getText() + " = ", true);
            //visitChamadaAtribuicao
        }
        
        else if("caso".equals(ctx.getStart().getText())){
            SaidaGerador.println("switch(", true);
            //visitexpressão
            SaidaGerador.println(")", true);
            //visitselecao
            //visitopcional
            
            
        }
        else if("faca".equals(ctx.getStart().getText())){
            SaidaGerador.println("for(", true);
            //visitexpressão
            SaidaGerador.println(") {", true);
            //visitComandos
            SaidaGerador.println("}", true);
            
            
        }
        
        else if("enquanto".equals(ctx.getStart().getText())){
            SaidaGerador.println("while(", true);
            //visitexpressão
            SaidaGerador.println("){ ", true);
            //visitComandos
            SaidaGerador.println("}", true);
        }
        
        
        return null;
    }

    public String returnMask(Tipo symbol) {
        if (symbol == TipoEnum.INTEIRO) {
            return "%d";
        } else if (symbol == TipoEnum.REAL) {
            return "%f";
        } else if (symbol == TipoEnum.LITERAL) {
            return "%s";
        }

        return "Outro tipo";
    }

    @Override
    public Object visitExpressao(LAParser.ExpressaoContext ctx) {
        //TODO NÃO IMPLEMENTE OUTROS_TERMOS_LOGICOS
        return (Pair<String, Tipo>) visitTermo_logico(ctx.termo_logico());
    }

    @Override
    public Object visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
        String string1 = "";
        Pair<String, Tipo> par = (Pair<String, Tipo>) visitTermo(ctx.termo());
        
        string1 = (String) visitOutros_termos(ctx.outros_termos());
        
        String string2 = par.a + " " + string1;

        return new Pair<String, Tipo>(string2, par.b);
    }

    @Override
    public Object visitExp_relacional(LAParser.Exp_relacionalContext ctx) {
        String string1 = " ";
        Pair<String, Tipo> par = (Pair<String, Tipo>) visitExp_aritmetica(ctx.exp_aritmetica());

        if (ctx.op_opcional() != null) {
            string1 = (String) visitOp_opcional(ctx.op_opcional());
        }

        String string2 = par.a + " " + string1;

        return new Pair<String, Tipo>(string2, par.b);
    }

    @Override
    public Object visitOp_opcional(LAParser.Op_opcionalContext ctx) {
        String string1 = "";
        Pair<String, Tipo> par = new Pair<String, Tipo> ("", TipoEnum.NONE);
        if(ctx.op_relacional() != null){
            string1 = ctx.op_relacional().getText();
            par = (Pair<String, Tipo>) visitExp_aritmetica(ctx.exp_aritmetica());
        }
        String string2 = string1 + " " + par.a;

        return string2;
    }

    @Override
    public Object visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        //TODO NÃO IMPLEMENTA OUTROS_FATORES_LOGICOS
        return (Pair<String, Tipo>) visitFator_logico(ctx.fator_logico());
    }

    @Override
    public Object visitOp_nao(LAParser.Op_naoContext ctx) {
        if (ctx.nao == null) {
            return TipoEnum.NONE;
        } else {
            return TipoEnum.LOGICO;
        }
    }

    @Override
    public Object visitFator_logico(LAParser.Fator_logicoContext ctx) { 
        Pair<String, Tipo> par;
        //TODO NÃO IMPLEMENTA OP_NÃO
        if (ctx.parcela_logica().exp_relacional() != null) {
            par = (Pair<String, Tipo>) visitParcela_logica(ctx.parcela_logica());
        } else {
            String string = ctx.parcela_logica().getText();
            if (string.equals("verdadeiro")){
                string = "true";
            } else {
                string = "false";
            }
            Tipo tipo = TipoEnum.LOGICO;

            par = new Pair<String, Tipo>(string, tipo);
        }
        return par;
    }

    @Override
    public Object visitOutros_termos(LAParser.Outros_termosContext ctx) {
        Pair<String, Tipo> par = new Pair<String, Tipo> ("", TipoEnum.NONE);
        String string1 = "";
        String string2 = "";
        if(ctx.outros_termos() != null){
            string1 = ctx.op_adicao().getText();
            par = (Pair<String, Tipo>) visitTermo(ctx.termo());
            string2 = " " + (String) visitOutros_termos(ctx.outros_termos());
        }
        return string1 + " " + par.a + string2;
    }

    @Override
    public Object visitFator(LAParser.FatorContext ctx) {
        //TODO NÃO IMPLEMENTA OUTRAS_PARCELAS    
        return (Pair<String, Tipo>) visitParcela(ctx.parcela());
    }

    @Override
    public Object visitTermo(LAParser.TermoContext ctx) {
        String string = "";
        Pair<String, Tipo> par = (Pair<String, Tipo>) visitFator(ctx.fator());
        if(ctx.outros_fatores() != null){
            string = " " + (String) visitOutros_fatores(ctx.outros_fatores());
        }
        String stringPar = par.a + string;

        return new Pair<String, Tipo>(stringPar, par.b);
    }

    @Override
    public Object visitOutros_fatores(LAParser.Outros_fatoresContext ctx) {
        Pair<String, Tipo> par = new Pair<String, Tipo> ("", TipoEnum.NONE);
        String string1 = ""; 
        String string2 = "";
        if(ctx.outros_fatores() != null){    
            string1 = ctx.op_multiplicacao().getText();
            par = (Pair<String, Tipo>) visitFator(ctx.fator());
        
            string2 = " " + (String) visitOutros_fatores(ctx.outros_fatores());
        }
        return string1 + " " + par.a + string2;
    }
/*
    @Override
    public Object visitOp_multiplicacao(LAParser.Op_multiplicacaoContext ctx) {
        if ("*".equals(ctx.getStart().getText())) {
            return "*";
        } else {
            return "/";
        }
    }
*/

    @Override
    public Object visitOutros_termos_logicos(LAParser.Outros_termos_logicosContext ctx) {
        if (ctx.termo_logico() == null) { // não possui termo lógico
            return TipoEnum.NONE;
        } else {
            // TODO provavelmente tipo1 é inútil

            Tipo tipo1 = TipoEnum.LOGICO;
            Tipo tipo2 = (Tipo) visitTermo_logico(ctx.termo_logico());
            Tipo tipo3 = (Tipo) visitOutros_termos_logicos(ctx.outros_termos_logicos());

            return Tipo.mergeTipos(tipo1, Tipo.mergeTipos(tipo2, tipo3));
        }
    }

    //TODO
    @Override
    public Object visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        String nome;
        Tipo tipo;
        if (ctx.IDENT() != null) {
            nome = ctx.IDENT().getText();
            EntradaTS tipoEntrada = PilhaDeTabelas.getSimbolo(ctx.IDENT().getText());
            tipo = tipoEntrada.getTipo();
            //tipo = TipoEnum.INTEIRO;
            //TODO NÃO TRATA REGISTRO            
            return new Pair<String, Tipo>(nome, tipo);
        } else if (ctx.NUM_INT() != null) {
            nome = ctx.NUM_INT().getText();
            tipo = TipoEnum.INTEIRO;

            return new Pair<String, Tipo>(nome, tipo);
        } else if (ctx.NUM_REAL() != null) {
            nome = ctx.NUM_REAL().getText();
            tipo = TipoEnum.REAL;

            return new Pair<String, Tipo>(nome, tipo);

        }
        return super.visitParcela_unario(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        return (Pair<String, Tipo>) visitExp_relacional(ctx.exp_relacional());
    }

    @Override
    public Object visitParcela(LAParser.ParcelaContext ctx) {
        if (ctx.parcela_unario() != null) {
            return (Pair<String, Tipo>) visitParcela_unario(ctx.parcela_unario());
        } else if (ctx.parcela_nao_unario() != null) {
            return (Pair<String, Tipo>) visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        return null;
    }

    @Override
    public Object visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        String nome;
        Tipo tipo;
        if (ctx.CADEIA() != null) {
            nome = ctx.CADEIA().getText();
            tipo = TipoEnum.LITERAL;

            return new Pair<String, Tipo>(nome, tipo);
        }

        return super.visitParcela_nao_unario(ctx);
    }

}