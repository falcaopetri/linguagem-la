# Linguagem LA

Construção de um Compilador para a Linguagem LA.
Trabalho 1 da disciplina de Construção de Compiladores 2 @ DC/UFSCar - São Carlos.

## Netbeans Project
Este é um projeto Java criado utilizando o Netbeans. Configurado com o Maven, pronto para executar a geração de código
utilizando o Antlr 4.0 e executar os casos de teste.

Na primeira build, o Maven se encarregará de baixar o Antlr e executá-lo.
As Run's estão configuradas para executar o jar corretor/CorretorTrabalho1.jar, com os devidos parâmetros.

Para especificar o tipo de teste que deve ser executado, edite a última tag `argument` do arquivo `pom.xml`.

Talvez seja necessário alterar a tag `<argument>gcc</argument>` no Windows, para especificar o path do compilador. Provavelmente é mais prático adicioná-lo ao PATH.

O Antlr já está configurado para gerar o Visitor e o Listener.
