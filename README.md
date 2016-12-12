# Linguagem LA

Construção de um Compilador para a Linguagem LA.

Trabalho 1 da disciplina de Construção de Compiladores 2 - 2016/2 @ DC/UFSCar - São Carlos.

## Grupo
| Nomes                         | RA     |
|-------------------------------|--------|
| Antonio Carlos Falcão Petri   | 586692 |
| José Antônio dos Santos Júnior| 586765 |
| José Vitor Aquino             | 609170 |
| Muriel Guilherme Alves Mauch  | 553859 |

## Projeto

Esse projeto visou desenvolver um Compilador para a Linguagem LA, incluindo as etapas Léxica, Sintática, Semântica e Geração de Código em Linguagem Intermediária.

Basicamente, a Linguagem LA é um pseudo-código da linguagem C, permitindo coisas como a criação de registros (structs) e a definição de novos tipos (typedef).

O gcc foi utilizado na etapa de backend.

Confira a pasta `corretor/casosDeTesteT1` para um conjunto de códigos exemplos da Linguagem LA.

## Projeto no Windows
O projeto foi praticamente todo desenvolvido em máquinas Linux. Todas as etapas de build e execução dos casos de teste funcionaram em ao menos uma máquina Windows, menos a compilação de códigos C.

Possivelmente, seja suficiente adicionar o GCC ao PATH do Windows. Talvez seja necessário modificar o `pom.xml` para refletir o nome correto do executável (provavelmente o MinGW terá que ser instalado, ao invés do GCC).

## Maven
O projeto [Maven](https://maven.apache.org/) busca facilitar o processo de build, gestão de dependências e outros detalhes de um projeto Java. Ele está de certa forma alinhado com a tendência de práticas Dev-Ops.

A ideia foi automatizar o máximo possível para facilitar o desenvolvimento descentralizado desse projeto. Pode-se dizer que obtivemos um sucesso parcial.

O Maven está responsável por baixar o Antlr 4 e executá-lo de forma apropriada (gerando visitor, listener, -package) durante o processo de build.

Utilizando o plugin `exec-maven`, foi possível também fazer com que o processo de execução do projeto fosse substituído pela invocação do compilador automático disponibilizado.

Infelizmente, ainda precisamos do `Antlr.jar` para executar nosso próprio `.jar`.
O Maven chega a baixá-lo, mas o armazena em uma pasta própria (algo como `~/.m2`).

Acabamos adicionando a biblioteca do Antlr na pasta `libs/` e adicionando-a ao versionamento do projeto.

O Maven fica responsável por linká-la ao executável durante a build, copiando-a para `target/libs/`.

## Building
Este é um projeto Java criado utilizando o NetBeans 8.1, configurado com o Maven, pronto para executar a geração de código
utilizando o Antlr 4.0 e executar os casos de teste disponibilizados (e armazenados em `corretor/`).

> Também estamos utilizando o Java 8, que deve estar instalado no ambiente de execução. Caso apenas o Java 7 esteja instalado, modifique no `pom.xml` as tags `<maven.compiler.source>` e `<maven.compiler.target>` para `1.7`. O projeto deve funcionar normalmente.

Na primeira build, o Maven se encarregará de baixar o Antlr e outras dependências. Esse processo pode demorar alguns minutos.

As Run's estão configuradas para executar o jar `corretor/CorretorTrabalho1.jar`, com os devidos parâmetros.

Para especificar o tipo de teste que deve ser executado, edite a última tag `argument` do arquivo `pom.xml`.

Talvez seja necessário alterar a tag `<argument>gcc</argument>` no Windows, para especificar o path do compilador. Provavelmente é mais prático adicioná-lo ao PATH.

O Antlr já está configurado para gerar o Visitor e o Listener.

## Execução

Toda o texto anterior serve para explicar o por que é tão fácil baixar e executar o projeto. Uma vez importado no NetBeans, podemos executar:

### Corretor automático

Dentro do NetBeans, basta fazer:

- `Run -> Build Project (F11)`
- `Run -> Run Project (F6)`

Pelo terminal, os comandos equivalentes serão:
- `$ mvn install`
- `$ mvn exec:exec`

### Arquivo individual
- Linha de comando: `Main.java` foi criado para servir ao Corretor automático. Assim, ela espera receber dois argumentos, `inputPath` e `outputPath`:

    `java -jar ./target/Trabalho1-1.jar ./corretor/casosDeTesteT1/2.arquivos_com_erros_semanticos/entrada/13.algoritmo_9-4_apostila_LA.txt ./saida`



- Internamente:
    Também é possível editar a `Main.java` para adicionar o `inputPath` e `outputPath` de formar "hardcoded" (ao invés de receber como parâmetro). Instruções estão disponíveis no arquivo.

## Utilizando outra IDE
(Não testamos o projeto com outra IDE).

O Maven é independente da IDE (todas as principais certamente possuem integração nativa com ele). Assim, o processo de Build será executado normalmente.

O único problema é que parte da configuração do Run Project está no arquivo `nbactions.xml`. É nesse arquivo que sobreescrevemos o comportamento default do Run Project no NetBeans.

As outras três formas de executar o projeto/corretor automático descritas na seção anterior devem continuar sendo válidas.
