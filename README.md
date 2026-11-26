# Sistema de Gerenciamento de Produtos — Árvores Balanceadas

Aplicação web acadêmica para explorar estruturas de dados por meio do cadastro de produtos e de visualizações interativas. As árvores são implementadas manualmente em JavaScript, sem bibliotecas de estruturas de dados.

## Abrir a aplicação

Abra [`sistema_de_gerenciamento_de_produtos_rvores_balanceadas.html`](./sistema_de_gerenciamento_de_produtos_rvores_balanceadas.html) em um navegador moderno.

Opcionalmente, sirva a pasta localmente:

```bash
python -m http.server 8000
```

Em seguida, acesse <http://localhost:8000/sistema_de_gerenciamento_de_produtos_rvores_balanceadas.html>.

O Tailwind CSS e as fontes Inter/JetBrains Mono são carregados de CDNs; é necessária conexão com a internet para que esses recursos visuais sejam carregados. A aplicação não exige instalação de dependências ou etapa de build.

## Funcionalidades

- Cadastrar, atualizar, buscar e excluir produtos. O código é único: cadastrar um código existente atualiza o produto sem duplicar o registro.
- Exibir e comparar os mesmos registros em uma árvore Rubro-Negra e em uma árvore 2-3-4.
- Visualizar as árvores em SVG, consultar informações dos nós e acompanhar logs das operações.
- Filtrar e ordenar a lista de produtos.
- Inserir pontos em uma árvore k-D bidimensional e buscar o vizinho mais próximo.
- Executar uma suíte automatizada de seis grupos de verificações das árvores Rubro-Negra e 2-3-4.
- Alternar entre tema claro e escuro e restaurar os dados de demonstração.

## Estruturas implementadas

### Árvore Rubro-Negra

Árvore binária de busca balanceada por recoloração e rotações. A implementação inclui inserção, busca, exclusão, percurso em ordem e validação de propriedades.

### Árvore 2-3-4

B-tree de grau mínimo 2. Cada nó comporta de uma a três chaves; a inserção divide nós cheios e a exclusão pode emprestar chaves ou fundir nós. A implementação inclui inserção, busca, exclusão, percurso em ordem e validação estrutural.

### Árvore k-D (2D)

Árvore de busca espacial que alterna os eixos X e Y por nível. Permite inserir pontos e buscar o vizinho mais próximo. A versão básica não garante balanceamento automático.

## Dados e testes

A aplicação começa com 21 produtos de demonstração. As alterações existem apenas durante a sessão no navegador: atualizar ou fechar a página restaura os dados iniciais. Use **Restaurar 21 Produtos** para reiniciar os dados manualmente.

Abra a aba **Testes & Comparação** e execute a suíte para verificar inserções, buscas, atualização de códigos repetidos, exclusões, invariantes estruturais e equivalência dos percursos das árvores. A suíte também é executada ao carregar a página.

## Arquivos

```text
.
├── sistema_de_gerenciamento_de_produtos_rvores_balanceadas.html
├── README.md
└── .gitignore
```

## Publicação no GitHub Pages

Configure o GitHub Pages para publicar a branch `main` a partir da raiz do repositório. Como o arquivo inicial mantém o nome solicitado, abra a URL publicada com `/sistema_de_gerenciamento_de_produtos_rvores_balanceadas.html`.
