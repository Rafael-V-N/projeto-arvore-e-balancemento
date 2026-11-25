/** Nó da árvore 2-3-4: comporta até três chaves e quatro filhos. */
class Node234 {
    int quantidade;
    boolean folha = true;
    Produto[] chaves = new Produto[3];
    Node234[] filhos = new Node234[4];
}
