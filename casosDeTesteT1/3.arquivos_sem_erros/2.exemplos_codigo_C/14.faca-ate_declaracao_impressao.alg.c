/*
  Declaracao de variavel do tipo inteiro, comando 'faca-ate' com impressao
  Helena Caseli
  2010
*/

#include <stdio.h>
#include <stdlib.h>

int main() {
	int i;
	i = 1;
	do {
		printf("%d",i);
		printf("\n");
		i = i + 1;
	} while (!(i == 6));
	return 0;
}
