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
        Saida.println("#include <stdio.h>", true);
        Saida.println("#include <stdlib.h>", true);
        visitDeclaracoes(ctx.declaracoes());
        Saida.println("int main() {", true);
        visitCorpo(ctx.corpo());
        Saida.println("return 0;");
        Saida.println("}", true);
        return null;
    }

    @Override
    public Object visitVariavel(LAParser.VariavelContext ctx) {
        String tipo = (String) visitTipo(ctx.tipo());
        Saida.println(" " + ctx.IDENT().getText(), true);
        if (tipo.equals("literal")) {
            // Como estamos compilando para C, temos que transformar literal 
            // para array de char. Tamanho 80 é utilizado nos códigos de exemplo.
            Saida.println("[80]", true);
        } else {
            visitDimensao(ctx.dimensao());
        }
        visitMais_var(ctx.mais_var());
        Saida.println(";", true);
        return null;
    }

    @Override
    public Object visitMais_var(LAParser.Mais_varContext ctx) {
        if (ctx.IDENT() != null) {
            Saida.println(", " + ctx.IDENT().getText(), true);
        }

        return super.visitMais_var(ctx);
    }

    @Override
    public Object visitTipo(LAParser.TipoContext ctx) {
        if (ctx.registro() != null) {
            return null;
        } else if (ctx.tipo_estendido() != null) {
            visitTipo_estendido(ctx.tipo_estendido());
            String result = "";

            if (ctx.tipo_estendido().ponteiros_opcionais().ponteiros_opcionais() != null) {
                result += "^";
            }

            result += ctx.tipo_estendido().tipo_basico_ident().tipo_basico().getText();
            return result;
        }

        return null;
    }

    @Override
    public Object visitDimensao(LAParser.DimensaoContext ctx) {
        if (ctx.exp_aritmetica() != null) {
            Saida.println("[", true);
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExp_aritmetica(ctx.exp_aritmetica());
            Saida.println(par.a, true);
            Saida.println("]", true);
        }
        return null;
    }

    @Override
    public Object visitTipo_basico(LAParser.Tipo_basicoContext ctx) {
        Tipo tipo = Tipo.valueOf(ctx.getStart().getText());
        if (tipo == TipoEnum.INTEIRO) {
            Saida.println("int", true);
        } else if (tipo == TipoEnum.LITERAL) {
            Saida.println("char", true);
        } else if (tipo == TipoEnum.REAL) {
            Saida.println("float", true);
        } else if (tipo == TipoEnum.LOGICO) {
            Saida.println("boolean", true);
        }
        return null;

    }

    @Override
    public Object visitCmd(LAParser.CmdContext ctx) {
        if ("leia".equals(ctx.getStart().getText())) {
            Saida.print("scanf(\"");
            if (ctx.identificador() != null) {
                EntradaTS tipo = PilhaDeTabelas.getSimbolo(ctx.identificador().getText());
                if (tipo != null) {
                    Saida.print(returnMask(tipo.getTipo()));
                    if (tipo.getTipo() == TipoEnum.LITERAL) {
                        Saida.print("\", " + ctx.identificador().getText());
                    } else {
                        Saida.print("\", &" + ctx.identificador().getText());
                    }
                }

            }
            Saida.println(");", true);

        } else if ("escreva".equals(ctx.getStart().getText())) {
            Saida.print("printf(\"");
            if (ctx.expressao() != null) {
                Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());

                String stringMask = returnMask(par.b);

                Saida.print(stringMask + "\" , " + par.a);
                //Saida.println(returnMask(ALGUMACOISA.getTipo()), true);
            }
            Saida.println(");", true);
            if (ctx.mais_expressao() != null) {
                visitMais_expressao(ctx.mais_expressao());
            }

        } else if ("se".equals(ctx.getStart().getText())) {
            Saida.println("if (", true);
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
            Saida.println(par.a + "){", true);
            visitComandos(ctx.comandos());
            visitSenao_opcional(ctx.senao_opcional());
            Saida.println("}", true);
        } else if ("para".equals(ctx.getStart().getText())) {
            // 'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca' comandos 'fim_para' |
            Saida.print("for (");
            Saida.print(ctx.IDENT().getText());
            Saida.print("=");
            Pair<String, Tipo> para_atr = (Pair<String, Tipo>) visitExp_aritmetica(ctx.para_atr);
            Saida.print(para_atr.a);
            Pair<String, Tipo> para_check = (Pair<String, Tipo>) visitExp_aritmetica(ctx.para_check);
            Saida.print(";");
            Saida.print(ctx.IDENT().getText());
            Saida.print("<=");
            Saida.print(para_check.a);
            Saida.print(";");
            Saida.print(ctx.IDENT().getText());
            Saida.println("++){", true);
            visitComandos(ctx.comandos());
            Saida.println("}", true);
        } else if (ctx.atr_ponteiro != null) {
            // '^' atr_ponteiro=IDENT outros_ident dimensao '<-' expressao |
            Saida.println("^" + ctx.atr_ponteiro.getText() + " = ", true);
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
            Saida.println(par.a, true);
            Saida.println(";", true);
        } else if (ctx.atr_normal != null) {
            Saida.println(ctx.atr_normal.getText() + " = ", true);
            visitChamada_atribuicao(ctx.chamada_atribuicao());
        } else if ("caso".equals(ctx.getStart().getText())) {
            Saida.println("switch(", true);
            //visitexpressão
            Saida.println(")", true);
            //visitselecao
            //visitopcional

        } else if ("faca".equals(ctx.getStart().getText())) {
            Saida.println("do {", true);
            visitComandos(ctx.comandos());
            Saida.println("} while(!(", true);
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
            Saida.println(par.a + "));", true);

        } else if ("enquanto".equals(ctx.getStart().getText())) {
            Saida.println("while(", true);
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
            Saida.println(par.a + "){", true);
            visitComandos(ctx.comandos());
            Saida.println("}", true);
        }

        return null;
    }

    @Override
    public Object visitMais_expressao(LAParser.Mais_expressaoContext ctx) {

        if (ctx.expressao() != null) {
            Saida.print("printf(\"");
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());

            String stringMask = returnMask(par.b);

            Saida.print(stringMask + "\" , " + par.a);
            //Saida.println(returnMask(ALGUMACOISA.getTipo()), true);
            Saida.println(");", true);
        }

        if (ctx.mais_expressao() != null) {
            visitMais_expressao(ctx.mais_expressao());
        }
        return null;
    }

    @Override
    public Object visitSenao_opcional(LAParser.Senao_opcionalContext ctx) {
        if (ctx.getStart().getText().equals("senao")) {
            Saida.println("}else{", true);
        }
        return super.visitSenao_opcional(ctx);
    }

    @Override
    public Object visitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
        if (ctx.expressao() != null) {
            Pair<String, Tipo> par = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
            Saida.println(par.a + ";", true);
        } else /* if(ctx.argumentos_opcional() != null) */ {
            Saida.print("(");
            visitArgumentos_opcional(ctx.argumentos_opcional());
            Saida.print(")");
        }
        
        return null;
    }

    @Override
    public Object visitArgumentos_opcional(LAParser.Argumentos_opcionalContext ctx) {
        return super.visitArgumentos_opcional(ctx);
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
        Pair<String, Tipo> par = (Pair<String, Tipo>) visitTermo_logico(ctx.termo_logico());

        if (par == null) {
            throw new AssertionError();
        }

        return par;
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
        Pair<String, Tipo> par = new Pair<String, Tipo>("", TipoEnum.NONE);
        if (ctx.op_relacional() != null) {
            string1 = ctx.op_relacional().getText();

            if (string1.equals("=")) {
                string1 = "==";
            }

            par = (Pair<String, Tipo>) visitExp_aritmetica(ctx.exp_aritmetica());
        }
        String string2 = string1 + " " + par.a;

        return string2;
    }

    @Override
    public Object visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        //TODO NÃO IMPLEMENTA OUTROS_FATORES_LOGICOS
        Pair<String, Tipo> par = (Pair<String, Tipo>) visitFator_logico(ctx.fator_logico());

        return par;
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
            if (string.equals("verdadeiro")) {
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
        Pair<String, Tipo> par = new Pair<String, Tipo>("", TipoEnum.NONE);
        String string1 = "";
        String string2 = "";
        if (ctx.outros_termos() != null) {
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

        if (par == null) {
            throw new AssertionError();
        }

        if (ctx.outros_fatores() != null) {
            string = " " + (String) visitOutros_fatores(ctx.outros_fatores());
        }
        String stringPar = par.a + string;

        return new Pair<String, Tipo>(stringPar, par.b);
    }

    @Override
    public Object visitOutros_fatores(LAParser.Outros_fatoresContext ctx) {
        Pair<String, Tipo> par = new Pair<String, Tipo>("", TipoEnum.NONE);
        String string1 = "";
        String string2 = "";
        if (ctx.outros_fatores() != null) {
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
        String nome = null;
        Tipo tipo = null;
        Pair<String, Tipo> result = null;

        if (ctx.IDENT() != null) {
            nome = ctx.IDENT().getText();
            EntradaTS tipoEntrada = PilhaDeTabelas.getSimbolo(ctx.IDENT().getText());
            tipo = tipoEntrada.getTipo();
            //TODO NÃO TRATA REGISTRO
            result = new Pair<String, Tipo>(nome, tipo);
        } else if (ctx.NUM_INT() != null) {
            nome = ctx.NUM_INT().getText();
            tipo = TipoEnum.INTEIRO;
            result = new Pair<String, Tipo>(nome, tipo);
        } else if (ctx.NUM_REAL() != null) {
            nome = ctx.NUM_REAL().getText();
            tipo = TipoEnum.REAL;
            result = new Pair<String, Tipo>(nome, tipo);
        } else if (ctx.expressao() != null) {
            result = (Pair<String, Tipo>) visitExpressao(ctx.expressao());
        }
        if (result == null) {
            throw new AssertionError();
        }

        return result;
    }

    @Override
    public Object visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        return (Pair<String, Tipo>) visitExp_relacional(ctx.exp_relacional());
    }

    @Override
    public Object visitParcela(LAParser.ParcelaContext ctx) {
        if (ctx == null) {
            throw new AssertionError();
        }

        Pair<String, Tipo> result = null;
        if (ctx.parcela_unario() != null) {
            result = (Pair<String, Tipo>) visitParcela_unario(ctx.parcela_unario());
        } else if (ctx.parcela_nao_unario() != null) {
            result = (Pair<String, Tipo>) visitParcela_nao_unario(ctx.parcela_nao_unario());
        }

        if (result == null) {
            throw new AssertionError();
        }

        return result;
    }

    @Override
    public Object visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        String nome;
        Tipo tipo;
        if (ctx.CADEIA() != null) {
            nome = ctx.CADEIA().getText();
            tipo = TipoEnum.LITERAL;
        } else {
            //'&' IDENT outros_ident dimensao             
            nome = "&" + ctx.IDENT().getText();
            tipo = PilhaDeTabelas.getSimbolo(ctx.IDENT().getText()).getTipo();
        }

        return new Pair<String, Tipo>(nome, tipo);
    }

}
