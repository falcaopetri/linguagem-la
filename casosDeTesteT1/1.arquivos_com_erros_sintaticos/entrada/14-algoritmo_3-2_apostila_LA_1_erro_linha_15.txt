{ dados, separadamente, prenome e sobrenome, escrevê-los em dois formatos: "Fulano Tal" e "Tal, F." }

algoritmo
	declare prenome, sobrenome,
		formato1, formato2: literal

	{ obtenção dos nomes }
	leia(prenome, sobrenome)

	{ composição dos nomes }
	formato1 <- prenome + " " + sobrenome
	formato2 <- sobrenome + ", " + subLiteral(prenome, 1, 1) + "."

	{ resultados 
	escreva(formato1)
	escreva(formato2)

fim_algoritmo
