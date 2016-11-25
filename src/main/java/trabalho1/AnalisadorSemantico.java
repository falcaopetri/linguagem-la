package trabalho1;

import java.util.Arrays;
import main.antlr4.LABaseVisitor;
import main.antlr4.LAParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AnalisadorSemantico extends LABaseVisitor {

    PilhaDeTabelas ts;

    public AnalisadorSemantico() {
        ts = new PilhaDeTabelas();

        TabelaDeSimbolos global = new TabelaDeSimbolos("global");

        /*
            Hardcoded tipos. Retirados da regra 14 <tipo_basico>.
            Necessário, já que:
                - <tipo_basico_ident> permite IDENT
                - <declaracao_local> permite declaração de tipos
        
            Isto é, é necessário uma forma de verificar se um tipo utilizado ao
            longo do programa foi declarado anteriormente.
         */
        // TODO "tipo" deveria ser um ENUM
        global.adicionarSimbolos(Arrays.asList("literal", "inteiro", "real", "logico"), Tipo.TIPO);

        ts.empilhar(global);
    }

    @Override
    public Object visitCorpo(LAParser.CorpoContext ctx) {
        // assert que não existe return
        assert_does_not_return(ctx.comandos());

        return super.visitCorpo(ctx);
    }

    @Override
    public Object visitCmd(LAParser.CmdContext ctx) {
        if (ctx.IDENT() == null) {
            return super.visitCmd(ctx);
        }

        TerminalNode ident = ctx.IDENT();
        if (!ts.existeSimbolo(ident.getText())) {
            Saida.println("Linha " + ident.getSymbol().getLine() + ": identificador " + ident.getText() + " nao declarado", true);
            return super.visitCmd(ctx);
        }

        if (ctx.atr_normal == null && ctx.atr_ponteiro == null) {
            return super.visitCmd(ctx);
        }

        Tipo tipo_ident = ts.getSimbolo(ident.getText()).getTipo();
        if (ctx.atr_ponteiro != null) {
            System.out.println(ts.getSimbolo(ident.getText()).isPointer());
            Tipo tipo = (Tipo) visitExpressao(ctx.expressao());

            if (!Tipo.checkAtribuicao(tipo, tipo_ident)) {
                Saida.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": atribuicao nao compativel para ^" + ctx.IDENT(), true);
            }
        } else if (ctx.atr_normal != null) {
            Tipo tipo = (Tipo) visitChamada_atribuicao(ctx.chamada_atribuicao());

            if (!Tipo.checkAtribuicao(tipo, tipo_ident)) {
                Saida.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": atribuicao nao compativel para " + ctx.IDENT(), true);
            }
        }

        return super.visitCmd(ctx);
    }

    @Override
    public Object visitVariavel(LAParser.VariavelContext ctx) {
        String nome = ctx.IDENT().getText();
        String tipo;
        boolean is_pointer;

        if (ctx.tipo().registro() != null) {
            tipo = ctx.tipo().registro().getText();
            is_pointer = false;
        } else {
            tipo = ctx.tipo().tipo_estendido().tipo_basico_ident().getText();
            is_pointer = true;
        }

        // FIXME Check não muito eficiente: percorre as tabelas de símbolos 2 vezes
        if (!ts.existeSimbolo(tipo) || ts.getSimbolo(tipo).getTipo() != Tipo.TIPO) {
            Saida.println("Linha " + ctx.tipo().getStart().getLine() + ": tipo " + tipo + " nao declarado", true);
        }

        // Tenta recuperar do erro semântico (tipo não declarado). Adiciona o tipo na TS para que outros passar no Test Case 1.
        tryToAddVariable(nome, tipo, is_pointer, ctx.getStart().getLine());

        LAParser.Mais_varContext mais_var = ctx.mais_var();
        while (mais_var != null && mais_var.IDENT() != null) {
            tryToAddVariable(mais_var.IDENT().getText(), tipo, is_pointer, mais_var.getStart().getLine());
            mais_var = mais_var.mais_var();
        }

        return super.visitVariavel(ctx);
    }

    @Override
    public Object visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        // Atenção: declaração GLOBAL -> CUIDADO COM O ESCOPO DE INSERÇÃO: topo()

        /*
        19. <declaracao_global> ::= procedimento IDENT ( <parametros_opcional> ) <declaracoes_locais> <comandos> fim_procedimento
                                    | funcao IDENT ( <parametros_opcional> ) : <tipo_estendido> <declaracoes_locais> <comandos> fim_funcao
         */
        Token declaracao_tipo = ctx.getStart();
        if (declaracao_tipo.toString().equals("procedimento")) {
            // assert que não existe return
            assert_does_not_return(ctx.comandos());
        }
        // TODO vai ser necessário armazenar os parâmetros formais do procedimento/funcao para satisfazer a Regra Semântica 4:
        // 4) Incompatibilidade entre argumentos e parâmetros formais (número, ordem e tipo) na chamada de um procedimento ou uma função
        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Object visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        // Atenção: declaração LOCAL -> CUIDADO COM O ESCOPO DE INSERÇÃO: topo()

        /*
            4. <declaracao_local> ::= declare <variavel>
                                        | constante IDENT : <tipo_basico> = <valor_constante>
                                        | tipo IDENT : <tipo>
         */
        if (ctx.variavel() == null) { // se variavel() não está vazio, então já tratamos dela em visitVariavel()
            if (ctx.tipo_basico() != null) { // constante IDENT : <tipo_basico> = <valor_constante>
                tryToAddVariable(ctx.IDENT().getText(), ctx.tipo_basico().getText(), false, ctx.getStart().getLine());
            } else if (ctx.tipo() != null) { // tipo IDENT : <tipo>
                // TODO
                // try to add tipo
            }
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Object visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        if (ctx.IDENT() != null) {
            TerminalNode ident = ctx.IDENT();
            if (!ts.existeSimbolo(ident.getText())) {
                Saida.println("Linha " + ident.getSymbol().getLine() + ": identificador " + ident.getText() + " nao declarado", true);
            } else if (ctx.chamada_partes() != null) {
                return ts.getSimbolo(ctx.IDENT().getText()).getTipo();
            }
        }

        if (ctx.NUM_INT() != null) {
            return Tipo.INTEIRO;
        }

        if (ctx.NUM_REAL() != null) {
            return Tipo.REAL;
        }

        if (ctx.expressao() != null) {
            return visitExpressao(ctx.expressao());
        }

        return super.visitParcela_unario(ctx);
    }

    @Override
    public Object visitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
        if (ctx.expressao() != null) {
            visitOutros_ident(ctx.outros_ident());
            visitDimensao(ctx.dimensao());
            Tipo tipo = (Tipo) visitExpressao(ctx.expressao());

            return tipo;
        } else {
            return super.visitChamada_atribuicao(ctx);
        }
    }

    @Override
    public Object visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        if (ctx.CADEIA() != null) {
            return Tipo.LITERAL;
        }
        return super.visitParcela_nao_unario(ctx);
    }

    @Override
    public Object visitParcela(LAParser.ParcelaContext ctx) {
        // 46. < parcela > ::= <op_unario > <parcela_unario> | <parcela_nao_unario>
        if (ctx.parcela_nao_unario() != null) {
            return visitParcela_nao_unario(ctx.parcela_nao_unario());
        } else {
            return super.visitParcela(ctx);
        }
    }

    @Override
    public Object visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() == null) {
            return Tipo.LOGICO;
        } else {
            return visitExp_relacional(ctx.exp_relacional());
        }
    }

    @Override
    public Object visitIdentificador(LAParser.IdentificadorContext ctx) {
        TerminalNode ident = ctx.IDENT();

        // TODO add checking ts.getSimbolo(ident.getText()).getTipo() not in TIPOS
        if (!ts.existeSimbolo(ident.getText())) {
            Saida.println("Linha " + ident.getSymbol().getLine() + ": identificador " + ident.getText() + " nao declarado", true);
        }

        return super.visitIdentificador(ctx);
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

        if (tipo2 == Tipo.NONE) {
            return tipo1;
        } else if (Tipo.mergeTipos(tipo1, tipo2) == Tipo.UNDEFINED) { // verifica, para A op B, type(A) dá merge com type(B)
            return Tipo.UNDEFINED;
        } else {
            return Tipo.LOGICO;
        }
    }

    @Override
    public Object visitOp_opcional(LAParser.Op_opcionalContext ctx) {
        if (ctx.op_relacional() != null) {
            return visitExp_aritmetica(ctx.exp_aritmetica());
        } else {
            return Tipo.NONE;
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
            return Tipo.NONE;
        } else {
            return Tipo.LOGICO;
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
            return Tipo.NONE;
        } else {
            Tipo tipo1 = (Tipo) visitTermo(ctx.termo());
            Tipo tipo2 = (Tipo) visitOutros_termos(ctx.outros_termos());

            return Tipo.mergeTipos(tipo1, tipo2);
        }
    }

    @Override
    public Object visitFator(LAParser.FatorContext ctx) {
        Tipo tipo1 = (Tipo) visitParcela(ctx.parcela());
        Tipo tipo2 = (Tipo) visitOutras_parcelas(ctx.outras_parcelas());

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
            return Tipo.NONE;
        } else {
            // TODO provavelmente tipo1 é inútil

            Tipo tipo1 = Tipo.LOGICO;
            Tipo tipo2 = (Tipo) visitTermo_logico(ctx.termo_logico());
            Tipo tipo3 = (Tipo) visitOutros_termos_logicos(ctx.outros_termos_logicos());

            return Tipo.mergeTipos(tipo1, Tipo.mergeTipos(tipo2, tipo3));
        }
    }

    private void tryToAddVariable(String nome, String tipo, boolean is_pointer, int line) {
        if (ts.existeSimbolo(nome)) {
            Saida.println("Linha " + line + ": identificador " + nome + " ja declarado anteriormente", true);
        } else {
            try {
                ts.topo().adicionarSimbolo(nome, Tipo.valueOf(tipo.toUpperCase()), is_pointer);
            } catch (IllegalArgumentException e) {
                ts.topo().adicionarSimbolo(nome, Tipo.NONE);
            }
        }
    }

    private void assert_does_not_return(LAParser.ComandosContext comandos) {
        // TODO comments

        while (comandos != null) {
            LAParser.CmdContext cmd = comandos.cmd();
            if (cmd != null && cmd.retorno != null) {
                // existe o comando "retorne" dentro do conjunto de comandos de um procedimento
                Saida.println("Linha " + cmd.getStart().getLine() + ": comando retorne nao permitido nesse escopo");
            }

            comandos = comandos.comandos();
        }
    }

}
