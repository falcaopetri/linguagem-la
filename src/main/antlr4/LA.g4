grammar LA;

@header {
  import trabalho1.Saida;
}

/*
1. <programa> ::= <declaracoes> algoritmo <corpo> fim_algoritmo
*/
programa : declaracoes 'algoritmo' corpo 'fim_algoritmo';

/*
2. <declaracoes> ::= <decl_local_global> <declaracoes> | ε
*/
declaracoes : decl_local_global declaracoes | ;

/*
3. <decl_local_global> ::= <declaracao_local> | <declaracao_global>
*/
decl_local_global : declaracao_local | declaracao_global;

/*
4. <declaracao_local> ::= declare <variavel>
 | constante IDENT : <tipo_basico> = <valor_constante>
 | tipo IDENT : <tipo>
*/
declaracao_local :  'declare' variavel | 
                    'constante' IDENT ':' tipo_basico '=' valor_constante |
                    'tipo' IDENT ':' tipo;

/*
5. <variavel> ::= IDENT <dimensao> <mais_var> : <tipo>
*/
variavel : IDENT dimensao mais_var ':' tipo;

/*
6. <mais_var> ::= , IDENT <dimensao> <mais_var> | ε
*/
mais_var : ',' IDENT dimensao mais_var | ;

/*
7. <identificador> ::= <ponteiros_opcionais> IDENT <dimensao> <outros_ident>
*/
identificador : ponteiros_opcionais IDENT dimensao outros_ident;

/*
8. <ponteiros_opcionais> ::= ^ <ponteiros_opcionais> | ε
*/
ponteiros_opcionais : '^' ponteiros_opcionais | ;

/*
9. <outros_ident> ::= . <identificador> | ε
*/
outros_ident : '.' identificador | ;

/*
10. <dimensao> ::= [ <exp_aritmetica> ] <dimensao>| ε
*/
dimensao : '[' exp_aritmetica ']' dimensao | ;

/*
11. <tipo> ::= <registro> | <tipo_estendido>
*/
tipo : registro | tipo_estendido;

/*
12. <mais_ident> ::= , <identificador> <mais_ident> | ε
*/
mais_ident : ',' identificador mais_ident | ;

/*
13. <mais_variaveis> ::= <variavel> <mais_variaveis> | ε
*/
mais_variaveis : variavel mais_variaveis | ;

/*
14. <tipo_basico> ::= literal | inteiro | real | logico
*/
tipo_basico : 'literal' | 'inteiro' | 'real' | 'logico';

/*
15. <tipo_basico_ident> ::= <tipo_basico> | IDENT
*/
tipo_basico_ident : tipo_basico | IDENT;

/*
16. <tipo_estendido> ::= <ponteiros_opcionais> <tipo_basico_ident>
*/
tipo_estendido : ponteiros_opcionais tipo_basico_ident;

/*
17. <valor_constante> ::= CADEIA | NUM_INT | NUM_REAL | verdadeiro | falso
*/
valor_constante : CADEIA | NUM_INT | NUM_REAL | 'verdadeiro' | 'falso';

/*
18. <registro> ::= registro <variavel> <mais_variaveis> fim_registro
*/
registro : 'registro' variavel mais_variaveis 'fim_registro';

/*
19. <declaracao_global> ::= procedimento IDENT ( <parametros_opcional> ) <declaracoes_locais> <comandos> fim_procedimento
 | funcao IDENT ( <parametros_opcional> ) : <tipo_estendido> <declaracoes_locais> <comandos> fim_funcao
*/
declaracao_global : 'procedimento' IDENT '(' parametros_opcional ')' declaracoes_locais comandos 'fim_procedimento' |
                    'funcao' IDENT '(' parametros_opcional ')' ':' tipo_estendido declaracoes_locais comandos 'fim_funcao';

/*
20. <parametros_opcional> ::= <parametro> | ε
*/
parametros_opcional : parametro | ;

/*
21. <parametro> ::= <var_opcional> <identificador> <mais_ident> : <tipo_estendido> <mais_parametros>
*/
parametro : var_opcional identificador mais_ident ':' tipo_estendido mais_parametros;

/*
22. <var_opcional> ::= var | ε
*/
var_opcional : 'var' | ;

/*
23. <mais_parametros> ::= , <parametro> | ε
*/
mais_parametros : ',' parametro | ;

/*
24. <declaracoes_locais> ::= <declaracao_local> <declaracoes_locais> | ε
*/
declaracoes_locais : declaracao_local declaracoes_locais | ;

/*
25. <corpo> ::= <declaracoes_locais> <comandos>
*/
corpo : declaracoes_locais comandos;

/*
26. <comandos> ::= <cmd> <comandos> | ε
*/
comandos : cmd comandos | ;

/*
27. <cmd> ::= leia ( <identificador> <mais_ident> )
 | escreva ( <expressao> <mais_expressao> )
 | se <expressao> entao <comandos> <senao_opcional> fim_se
 | caso <exp_aritmetica> seja <selecao> <senao_opcional> fim_caso
 | para IDENT <- <exp_aritmetica> ate <exp_aritmetica> faca <comandos> fim_para
 | enquanto <expressao> faca <comandos> fim_enquanto
 | faca <comandos> ate <expressao>
 | ^ IDENT <outros_ident> <dimensao> <- <expressao>
 | IDENT <chamada_atribuicao>
 | retorne <expressao>
*/
cmd :   'leia' '(' identificador mais_ident ')' |
        'escreva' '(' expressao mais_expressao ')' |
        'se' expressao 'entao' comandos senao_opcional 'fim_se' |
        'caso' exp_aritmetica 'seja' selecao senao_opcional 'fim_caso' |
        'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca' comandos 'fim_para' |
        'enquanto' expressao 'faca' comandos 'fim_enquanto' |
        'faca' comandos 'ate' expressao |
        '^' IDENT outros_ident dimensao '<-' expressao |
        IDENT chamada_atribuicao |
        'retorne' expressao;

/*
28. <mais_expressao> ::= , <expressao> <mais_expressao> | ε
*/
mais_expressao : ',' expressao mais_expressao | ;

/*
29. <senao_opcional> ::= senao <comandos> | ε
*/
senao_opcional : 'senao' comandos | ;

/*
30. <chamada_atribuicao> ::= ( <argumentos_opcional> ) | <outros_ident> <dimensao> <- <expressao>
*/
chamada_atribuicao :    '(' argumentos_opcional ')' |
                        outros_ident dimensao '<-' expressao;

/*
31. <argumentos_opcional> ::= <expressao> <mais_expressao> | ε
*/
argumentos_opcional : expressao mais_expressao | ;

/*
32. <selecao> ::= <constantes> : <comandos> <mais_selecao>
*/
selecao : constantes ':' comandos mais_selecao;

/*
33. <mais_selecao> ::= <selecao> | ε
*/
mais_selecao : selecao | ;

/*
34. <constantes> ::= <numero_intervalo> <mais_constantes>
*/
constantes : numero_intervalo mais_constantes;

/*
35. <mais_constantes> ::= , <constantes> | ε
*/
mais_constantes : ',' constantes | ;

/*
36. <numero_intervalo> ::= <op_unario> NUM_INT <intervalo_opcional>
*/
numero_intervalo : op_unario NUM_INT intervalo_opcional;

/*
37. <intervalo_opcional> ::= .. <op_unario> NUM_INT | ε
*/
intervalo_opcional : '..' op_unario NUM_INT | ;

/*
38. <op_unario> ::= - | ε
*/
op_unario : '-' | ;

/*
39. <exp_aritmetica> ::= <termo> <outros_termos>
*/
exp_aritmetica : termo outros_termos;

/*
40. <op_multiplicacao> ::= * | /
*/
op_multiplicacao : '*' | '/';


/*
41. <op_adicao> ::= + | -
*/
op_adicao : '+' |
            '-';

/*
42. <termo> ::= <fator> <outros_fatores>
*/
termo : fator outros_fatores;

/*
43. <outros_termos> ::= <op_adicao> <termo> <outros_termos> | ε
*/
outros_termos : op_adicao termo outros_termos | ;

/*
44. <fator> ::= <parcela> <outras_parcelas>
*/
fator : parcela outras_parcelas;

/*
45. <outros_fatores> ::= <op_multiplicacao> <fator> <outros_fatores> | ε
*/
outros_fatores : op_multiplicacao fator outros_fatores | ;

/*
46. <parcela> ::= <op_unario> <parcela_unario> | <parcela_nao_unario>
*/
parcela :   op_unario parcela_unario |
            parcela_nao_unario;

/*
47. <parcela_unario> ::= ^ IDENT <outros_ident> <dimensao> | IDENT <chamada_partes> | NUM_INT | NUM_REAL | ( <expressao> )
*/
parcela_unario :     '^' IDENT outros_ident dimensao |
                    IDENT chamada_partes |
                    NUM_INT |
                    NUM_REAL |
                    '(' expressao ')';

/*
48. <parcela_nao_unario> ::= & IDENT <outros_ident> <dimensao> | CADEIA
*/
parcela_nao_unario : '&' IDENT outros_ident dimensao | CADEIA;

/*
49. <outras_parcelas> ::= % <parcela> <outras_parcelas> | ε
*/
outras_parcelas : '%' parcela outras_parcelas | ;

/*
50. <chamada_partes> ::= ( <expressao> <mais_expressao> ) | <outros_ident> <dimensao> | ε
*/
chamada_partes :    '(' expressao mais_expressao ')' |
                    outros_ident dimensao | ;

/*
51. <exp_relacional> ::= <exp_aritmetica> <op_opcional>
*/
exp_relacional : exp_aritmetica op_opcional;

/*
52. <op_opcional> ::= <op_relacional> <exp_aritmetica> | ε
*/
op_opcional : op_relacional exp_aritmetica | ;

/*
53. <op_relacional> ::= = | <> | >= | <= | > | <
*/
op_relacional : '=' |
                '<>' |
                '>=' |
                '<=' |
                '>' |
                '<';

/*
54. <expressao> ::= <termo_logico> <outros_termos_logicos>
*/
expressao : termo_logico outros_termos_logicos;

/*
55. <op_nao> ::= nao | ε
*/
op_nao : 'nao' | ;

/*
56. <termo_logico> ::= <fator_logico> <outros_fatores_logicos>
*/
termo_logico : fator_logico outros_fatores_logicos;

/*
57. <outros_termos_logicos> ::= ou <termo_logico> <outros_termos_logicos> | ε
*/
outros_termos_logicos : 'ou' termo_logico outros_termos_logicos | ;

/*
58. <outros_fatores_logicos> ::= e <fator_logico> <outros_fatores_logicos> | ε
*/
outros_fatores_logicos : 'e' fator_logico outros_fatores_logicos | ;

/*
59. <fator_logico> ::= <op_nao> <parcela_logica>
*/
fator_logico : op_nao parcela_logica;


/*
60. <parcela_logica> ::= verdadeiro | falso | <exp_relacional> 
*/
parcela_logica :    'verdadeiro' |
                    'falso' |
                    exp_relacional;

IDENT : (('a'..'z')|('A'..'Z')|'_')(('a'..'z')|('A'..'Z')|'_'|('0'..'9'))*;
NUM_INT : ('0'..'9')+ ;
NUM_REAL : ('0'..'9')+'.'('0'..'9')+;

/*
    *? altera o comportamento guloso
*/
CADEIA : '"' (~('\n'))*? '"';

COMMENTS  : '{' ~( '\n' | '\r' )* '}' { skip(); };

/*
    Foi criada uma regra do lexer para identificar espaços em branco de acordo
    com a especificação da linguagem e com o enunciado do trabalho.
*/
WS : (' ' |'\t' | '\r' | '\n') {skip(); };


/*
    Força o matching de um abre parênteses para passar no caso de testes sintático número 14
*/
ABRE : '{' { Saida.println("Linha " + (getLine()+1) + ": comentario nao fechado"); };

/*
    Detecta erros léxicos
*/
ANY : . { Saida.println("Linha " + getLine() + ": " + getText() + " - simbolo nao identificado"); };
