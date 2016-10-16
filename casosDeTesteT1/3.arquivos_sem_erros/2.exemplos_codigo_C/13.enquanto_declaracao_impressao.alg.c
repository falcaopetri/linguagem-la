/*
  Declaracao de variavel do tipo inteiro, comando 'enquanto' com impressao
  Helena Caseli
  2010
*/

#include <stdio.h>
#include <stdlib.h>

int main() {
	int i;
	i = 1;
	while (i <= 5) {
		printf("%d",i);
		printf("\n");
		i = i + 1;
	}
	return 0;
}
