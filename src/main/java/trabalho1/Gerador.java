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
            SaidaGerador.println("scanf(\"", true);
            if (ctx.identificador() != null) {
                EntradaTS tipo = PilhaDeTabelas.getSimbolo(ctx.identificador().getText());
                if (tipo != null) {
                    SaidaGerador.println(returnMask(tipo.getTipo()), true);
                }
                SaidaGerador.println("\", &" + ctx.identificador().getText(), true);
            }
            SaidaGerador.println(");", true);
        } else if ("escreva".equals(ctx.getStart().getText())) {
            SaidaGerador.println("print(\"", true);
            if (ctx.expressao() != null) {
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
        Tipo tipo1 = (Tipo) visitTermo_logico(ctx.termo_logico());
        Tipo tipo2 = (Tipo) visitOutros_termos_logicos(ctx.outros_termos_logicos());

        return Tipo.mergeTipos(tipo1, tipo2);
    }

    @Override
    public Object visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
        Tipo tipo1 = (Tipo) visitTermo(ctx.termo());
        Tipo tipo2 = (Tipo) visitOutros_termos(ctx.outros_termos());

        return Tipo.mergeTipos(tipo1, tipo2);
    }

    @Override
    public Object visitExp_relacional(LAParser.Exp_relacionalContext ctx) {
        Tipo tipo1 = (Tipo) visitExp_aritmetica(ctx.exp_aritmetica());
        Tipo tipo2 = (Tipo) visitOp_opcional(ctx.op_opcional());

        if (tipo2 == TipoEnum.NONE) {
            return tipo1;
        } else if (Tipo.mergeTipos(tipo1, tipo2) == TipoEnum.UNDEFINED) { // verifica, para A op B, type(A) dá merge com type(B)
            return TipoEnum.UNDEFINED;
        } else {
            return TipoEnum.LOGICO;
        }
    }

    @Override
    public Object visitOp_opcional(LAParser.Op_opcionalContext ctx) {
        if (ctx.op_relacional() != null) {
            return visitExp_aritmetica(ctx.exp_aritmetica());
        } else {
            return TipoEnum.NONE;
        }
    }

    @Override
    public Object visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        Tipo tipo1 = (Tipo) visitFator_logico(ctx.fator_logico());
        Tipo tipo2 = (Tipo) visitOutros_fatores_logicos(ctx.outros_fatores_logicos());

        return Tipo.mergeTipos(tipo1, tipo2);
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
        Tipo tipo1 = (Tipo) visitOp_nao(ctx.op_nao());
        Tipo tipo2 = (Tipo) visitParcela_logica(ctx.parcela_logica());

        return Tipo.mergeTipos(tipo1, tipo2);
    }

    @Override
    public Object visitOutros_termos(LAParser.Outros_termosContext ctx) {
        if (ctx.termo() == null) { // não possui termo
            return TipoEnum.NONE;
        } else {
            Tipo tipo1 = (Tipo) visitTermo(ctx.termo());
            Tipo tipo2 = (Tipo) visitOutros_termos(ctx.outros_termos());

            return Tipo.mergeTipos(tipo1, tipo2);
        }
    }

    @Override
    public Object visitFator(LAParser.FatorContext ctx) {        
        Pair<String, Tipo> par1 = (Pair<String, Tipo>) visitParcela(ctx.parcela()); 
        //TODO NÃO TRATA OUTRAS_PARCELAS

        return Tipo.mergeTipos(tipo1, tipo2);
    }

    @Override
    public Object visitTermo(LAParser.TermoContext ctx) {
        Tipo tipo1 = (Tipo) visitFator(ctx.fator());
        Tipo tipo2 = (Tipo) visitOutros_fatores(ctx.outros_fatores());

        return Tipo.mergeTipos(tipo1, tipo2);
    }

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
        return super.visitParcela_logica(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitParcela(LAParser.ParcelaContext ctx) {
        if (ctx.parcela_unario() != null){
            return (Pair<String, Tipo>) visitParcela_unario(ctx.parcela_unario());
        }
        else if(ctx.parcela_nao_unario() != null){
            return (Pair<String, Tipo>) visitParcela_nao_unario(ctx.parcela_nao_unario());            
        }                       
        return null;
    }

    @Override
    public Object visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        String nome;
        Tipo tipo;
        if (ctx.CADEIA() != null){
            nome = ctx.CADEIA().getText();
            tipo = TipoEnum.LITERAL;
          
            return new Pair<String, Tipo>(nome, tipo);
        }
            
        
        return super.visitParcela_nao_unario(ctx); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
