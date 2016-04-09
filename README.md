# COMP
Para compilar tem de se referir o nome de um fichero de texto que contenha o codigo YAL.
Por exemplo: `java YalToJvm Test.txt`

## TODO's

###Criar AST (Abstract Syntax Tree) &#10003;

###Criar hashtables

### Semantic analysis
  * Ver se a função existe no scope quando invocada
  * Ver se a variável está inicializada no scope quando chamada
  * Array index out of bounds. Ex: a[2]; b = a[2];
  * Inicializar array com tamanho errado. Ex: a = -1; b[a];(b[0]???)
  * Aceder a elemento do array sem estar inicializado. Ex: a[5]; b = a[3];
  * Ver se a variável existe. Ex: a = b
  * Número e tipo errados de argumentos numa função
  * Nas comparações verificar se os operandos são do mesmo tipo.
  * Ver se a função é única para cada module &#10003;
  * Pode haver mais ...
