/*
  Declaracao de ponteiro para inteiro com atribuicao e impressao
  Helena Caseli
  2010
*/

#include <stdio.h>
#include <stdlib.h>

int main() {
	int x;
	int* endx;
	x = 0;
	printf("%d",x);
	printf(" e ");
	endx = &x;
	*endx = 1;
	printf("%d",x);
	return 0;
}
