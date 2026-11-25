# Projeto de Árvores Balanceadas — Java

Projeto acadêmico de Estruturas de Dados com aplicação em um sistema de gerenciamento de produtos. Implementa manualmente uma Árvore Rubro-Negra e uma Árvore 2-3-4. A árvore k-D é explicada com exemplo, conforme o enunciado, que solicita demonstração e não implementação.

## Requisitos
- Java JDK 17 ou superior.
- Não utiliza `TreeMap`, `TreeSet` ou bibliotecas prontas de árvores.
- As árvores são construídas com nós e referências.

## Regra para elementos repetidos
O código do produto é a chave única. Ao inserir um produto com código já cadastrado, o registro existente é atualizado. Não é criado um segundo nó e a quantidade de elementos permanece igual.

## Estrutura de pastas

```text
Projeto_Arvores_Balanceadas_Java_v2/
├── src/
│   ├── Main.java
│   ├── Produto.java
│   ├── RBNode.java
│   ├── RedBlackTree.java
│   ├── Node234.java
│   ├── Tree234.java
│   └── TreeTests.java
└── README.md
```

## Como compilar e executar
Abra o terminal na pasta do projeto e execute:

```bash
javac -encoding UTF-8 src/*.java
java -cp src Main
```

O programa inicia com 21 produtos para que a árvore binária Rubro-Negra atenda ao requisito mínimo de 21 nós.

## Como executar os testes

```bash
javac -encoding UTF-8 src/*.java
java -cp src TreeTests
```

Os testes verificam inserção inicial de 21 chaves, busca de valores existentes e inexistentes, atualização de chaves repetidas, exclusões, propriedades internas das árvores, ordenação e uma sequência determinística com 3.000 operações aleatórias. A classe `HashMap` aparece somente nos testes como estrutura de referência para conferir resultados; não é utilizada para implementar nenhuma das árvores.

## Funcionalidades do sistema
1. Cadastrar ou atualizar produto.
2. Buscar produto por código.
3. Excluir produto por código.
4. Listar produtos em ordem crescente.
5. Exibir a estrutura Rubro-Negra e a árvore 2-3-4 por níveis.
6. Exibir a quantidade de registros.

Cada produto possui código, nome, categoria, preço e estoque. As operações de cadastro, busca e exclusão são executadas nas duas árvores para permitir comparação.

## Conceitos para a apresentação

### 1. Árvore Rubro-Negra
É uma árvore binária de busca em que cada nó possui uma cor: vermelho ou preto. As regras principais são: a raiz é preta; as folhas sentinela são pretas; um nó vermelho não pode ter filho vermelho; e todos os caminhos de um nó até as folhas sentinela possuem a mesma quantidade de nós pretos. Na inserção, o novo nó entra vermelho e o algoritmo corrige violações com recoloração e rotações. Na exclusão, a estrutura pode precisar corrigir um déficit de preto. Busca, inserção e exclusão são O(log n) no pior caso.

### 2. Árvore 2-3-4
É uma B-tree de grau mínimo 2. Cada nó pode armazenar 1, 2 ou 3 chaves e ter, respectivamente, 2, 3 ou 4 filhos quando não é folha. As chaves separam os intervalos dos filhos e todas as folhas ficam no mesmo nível. Na inserção, um nó cheio é dividido e sua chave mediana sobe ao pai. Na exclusão, o algoritmo pode substituir por predecessor/sucessor, emprestar uma chave de um irmão ou fundir dois nós. Busca, inserção e exclusão são O(log n).

### 3. Árvore k-D (explicação e exemplo)
É uma árvore de busca multidimensional que organiza pontos em k dimensões. Em duas dimensões, cada ponto possui coordenadas (x,y). A dimensão de comparação alterna conforme a profundidade: na raiz compara x, no nível seguinte compara y, depois volta a x.

Exemplo de inserção nesta ordem: (8,6), (3,4), (10,8), (1,2), (6,7):
- (8,6) é a raiz e divide o espaço usando x=8.
- (3,4) vai para a esquerda porque 3 < 8.
- (10,8) vai para a direita porque 10 > 8.
- (1,2) compara y com (3,4); como 2 < 4, fica à esquerda de (3,4).
- (6,7) compara y com (3,4); como 7 > 4, fica à direita de (3,4).

Aplicações: mapas, sistemas de informação geográfica, localização de pontos e consultas espaciais. A árvore k-D básica não garante balanceamento automático; seu desempenho depende da distribuição dos pontos e da estratégia de construção.

## Comparação dos métodos de balanceamento
- **Rubro-Negra:** mantém altura limitada usando cores, recoloração e rotações locais.
- **2-3-4:** mantém todas as folhas na mesma profundidade; usa divisão na inserção e empréstimo/fusão na exclusão.
- **k-D:** divide o espaço por dimensões alternadas; sua forma básica não garante balanceamento.

## Publicação no GitHub
O projeto está publicado em [Rafael-V-N/projeto-arvore-e-balancemento](https://github.com/Rafael-V-N/projeto-arvore-e-balancemento). Os arquivos `.class` gerados pela compilação não devem ser versionados.

## Observação
O enunciado exige implementação de Rubro-Negra e 2-3-4, além de explicação com exemplo da k-D. A implementação de Splay e o conteúdo adicional de B-Tree são exigidos somente para equipes com mais de quatro integrantes; por isso não fazem parte deste escopo individual.
