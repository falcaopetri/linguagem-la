package trabalho1;

import java.util.Arrays;
import main.antlr4.LABaseVisitor;
import main.antlr4.LAParser;
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
        global.adicionarSimbolos(Arrays.asList("literal", "inteiro", "real", "logico"), "tipo");

        ts.empilhar(global);
    }

    @Override
    public Object visitVariavel(LAParser.VariavelContext ctx) {
        String nome = ctx.IDENT().getText();
        String tipo = ctx.tipo().getText();

        // FIXME Check não muito eficiente: percorre as tabelas de símbolos 2 vezes
        if (!ts.existeSimbolo(tipo) || ts.getSimbolo(tipo).getTipo() != "tipo") {
            Saida.println("Linha " + ctx.tipo().getStart().getLine() + ": tipo " + tipo + " nao declarado");
            tryToAddVariable(nome, tipo, ctx.getStart().getLine());
        } else {
            tryToAddVariable(nome, tipo, ctx.getStart().getLine());

            LAParser.Mais_varContext mais_var = ctx.mais_var();
            while (mais_var != null && mais_var.IDENT() != null) {
                tryToAddVariable(mais_var.IDENT().getText(), tipo, mais_var.getStart().getLine());
                mais_var = mais_var.mais_var();
            }
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
        if (ctx.variavel().isEmpty()) { // se variavel() não está vazio, então já tratamos dela em visitVariavel()
            if (!ctx.tipo_basico().isEmpty()) { // constante IDENT : <tipo_basico> = <valor_constante>
                // TODO
                // try to add constant
            } else if (!ctx.tipo().isEmpty()) { // tipo IDENT : <tipo>
                // TODO
                // try to add tipo
            }
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Object visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        if(ctx.IDENT() != null){
            TerminalNode ident = ctx.IDENT();
            if (!ts.existeSimbolo(ident.getText())){
                Saida.println("Linha " + ident.getSymbol().getLine() + ": identificador " + ident.getText() + " nao declarado", true);
            }
        }
        return super.visitParcela_unario(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        return super.visitParcela_nao_unario(ctx); //To change body of generated methods, choose Tools | Templates.
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
    public Object visitChamada_atribuicao(LAParser.Chamada_atribuicaoContext ctx) {
        return super.visitChamada_atribuicao(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    private void tryToAddVariable(String nome, String tipo, int line) {
        if (ts.existeSimbolo(nome)) {
            Saida.println("Linha " + line + ": identificador " + nome + " ja declarado anteriormente", true);
        } else {
            ts.topo().adicionarSimbolo(nome, tipo);
        }
    }

}
