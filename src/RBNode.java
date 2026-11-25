/** Nó utilizado exclusivamente pela árvore Rubro-Negra. */
class RBNode {
    Produto produto;
    RBNode esquerda;
    RBNode direita;
    RBNode pai;
    boolean vermelho;

    RBNode(Produto produto, boolean vermelho) {
        this.produto = produto;
        this.vermelho = vermelho;
    }
}
