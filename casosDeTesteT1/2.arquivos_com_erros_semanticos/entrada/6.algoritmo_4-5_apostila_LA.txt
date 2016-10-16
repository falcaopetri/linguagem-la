{ verificação se um projétil, lançado de um ângulo de a graus em relação ao solo colide ou não com um objeto situado em seu caminho, sabendo-se a distância do lançamento e a espessura e altura do objeto; o resultado produzido é verdadeiro se há colisão e falso caso contrário }

{ A Figura 4-1 deve ser consultada para melhor referência (apostila de Construção de Algoritmos, página 24) }

algoritmo
	{ declarações }
	declare
		angulo, velocidadeInicial, distancia, espessura, altura,
		momento1, momento2, alturaProjetil1, alturaProjetil2: real
	declare
		colidiu: logico

	colidiu <- "falso"

	{ obtenção do ângulo, v0, distância do objeto, sua espessura e altura }
	leia(angulo, velocidadeInicial, distancia, espessura, altura)

	{ cálculo dos momentos em que o projétil passa pelos limites do objeto }
	momento1 <- distancia
	momento2 <- (distancia + espessura)/velocidadeInicial

	{ cálculo da altura do projétil nestes dois momentos }
	alturaProjetil1 <- velocidadeInicial

	alturaProjetil2 <- velocidadeInicial

	{ verificação se em algum desses dois momentos o projétil não está acima da altura do objeto }
	colidiu <- alturaProjetil1 <= altura e alturaProjetil2 <= altura

	{ escrita do resultado }
 	escreva("Houve colisao?", colisao) { escreve "V" ou "F" }

fim_algoritmo
