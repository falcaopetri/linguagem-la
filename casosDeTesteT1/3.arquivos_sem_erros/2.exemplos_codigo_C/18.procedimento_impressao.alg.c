/*
  Declaracao de procedimento com impressao
  Helena Caseli
  2010
*/

#include <stdio.h>
#include <stdlib.h>

void proc_imprime (char* mensagem){
	printf("%s",mensagem);
	printf("\n");
}

int main() {
	proc_imprime("teste");
	return 0; 
}
