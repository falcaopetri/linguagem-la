algoritmo
  declare x: inteiro
  declare endx: ^inteiro
  x <- 0
  escreva(x," e ")
  endx <- &x
  ^endx <- 1
  escreva(x)
fim_algoritmo
