import java.util.ArrayList;
import java.util.List;

/**
 * Árvore binária de busca Rubro-Negra implementada manualmente com nós.
 * Chaves repetidas: o produto existente é atualizado; não é criado outro nó.
 */
public class RedBlackTree {
    private final RBNode nil = new RBNode(null, false);
    private RBNode raiz;
    private int tamanho;

    public RedBlackTree() {
        nil.esquerda = nil;
        nil.direita = nil;
        nil.pai = nil;
        raiz = nil;
    }

    public int tamanho() { return tamanho; }
    public boolean estaVazia() { return raiz == nil; }

    public void inserir(Produto produto) {
        if (produto == null) throw new IllegalArgumentException("Produto não pode ser nulo.");

        RBNode pai = nil;
        RBNode atual = raiz;
        while (atual != nil) {
            pai = atual;
            int comparacao = Integer.compare(produto.getCodigo(), atual.produto.getCodigo());
            if (comparacao == 0) {
                atual.produto = produto;
                return;
            }
            atual = comparacao < 0 ? atual.esquerda : atual.direita;
        }

        RBNode novo = new RBNode(produto, true);
        novo.esquerda = nil;
        novo.direita = nil;
        novo.pai = pai;
        if (pai == nil) raiz = novo;
        else if (produto.getCodigo() < pai.produto.getCodigo()) pai.esquerda = novo;
        else pai.direita = novo;

        tamanho++;
        corrigirInsercao(novo);
    }

    private void corrigirInsercao(RBNode no) {
        while (no.pai.vermelho) {
            if (no.pai == no.pai.pai.esquerda) {
                RBNode tio = no.pai.pai.direita;
                if (tio.vermelho) {
                    no.pai.vermelho = false;
                    tio.vermelho = false;
                    no.pai.pai.vermelho = true;
                    no = no.pai.pai;
                } else {
                    if (no == no.pai.direita) {
                        no = no.pai;
                        rotacaoEsquerda(no);
                    }
                    no.pai.vermelho = false;
                    no.pai.pai.vermelho = true;
                    rotacaoDireita(no.pai.pai);
                }
            } else {
                RBNode tio = no.pai.pai.esquerda;
                if (tio.vermelho) {
                    no.pai.vermelho = false;
                    tio.vermelho = false;
                    no.pai.pai.vermelho = true;
                    no = no.pai.pai;
                } else {
                    if (no == no.pai.esquerda) {
                        no = no.pai;
                        rotacaoDireita(no);
                    }
                    no.pai.vermelho = false;
                    no.pai.pai.vermelho = true;
                    rotacaoEsquerda(no.pai.pai);
                }
            }
        }
        raiz.vermelho = false;
    }

    public Produto buscar(int codigo) {
        RBNode no = localizar(codigo);
        return no == nil ? null : no.produto;
    }

    private RBNode localizar(int codigo) {
        RBNode atual = raiz;
        while (atual != nil && codigo != atual.produto.getCodigo()) {
            atual = codigo < atual.produto.getCodigo() ? atual.esquerda : atual.direita;
        }
        return atual;
    }

    public boolean excluir(int codigo) {
        RBNode removido = localizar(codigo);
        if (removido == nil) return false;

        RBNode substituto = removido;
        boolean corOriginal = substituto.vermelho;
        RBNode filho;

        if (removido.esquerda == nil) {
            filho = removido.direita;
            transplantar(removido, removido.direita);
        } else if (removido.direita == nil) {
            filho = removido.esquerda;
            transplantar(removido, removido.esquerda);
        } else {
            substituto = minimo(removido.direita);
            corOriginal = substituto.vermelho;
            filho = substituto.direita;
            if (substituto.pai == removido) {
                filho.pai = substituto;
            } else {
                transplantar(substituto, substituto.direita);
                substituto.direita = removido.direita;
                substituto.direita.pai = substituto;
            }
            transplantar(removido, substituto);
            substituto.esquerda = removido.esquerda;
            substituto.esquerda.pai = substituto;
            substituto.vermelho = removido.vermelho;
        }

        tamanho--;
        if (!corOriginal) corrigirExclusao(filho);
        if (raiz != nil) raiz.pai = nil;
        nil.vermelho = false;
        return true;
    }

    private void corrigirExclusao(RBNode no) {
        while (no != raiz && !no.vermelho) {
            if (no == no.pai.esquerda) {
                RBNode irmao = no.pai.direita;
                if (irmao.vermelho) {
                    irmao.vermelho = false;
                    no.pai.vermelho = true;
                    rotacaoEsquerda(no.pai);
                    irmao = no.pai.direita;
                }
                if (!irmao.esquerda.vermelho && !irmao.direita.vermelho) {
                    if (irmao != nil) irmao.vermelho = true;
                    no = no.pai;
                } else {
                    if (!irmao.direita.vermelho) {
                        irmao.esquerda.vermelho = false;
                        irmao.vermelho = true;
                        rotacaoDireita(irmao);
                        irmao = no.pai.direita;
                    }
                    irmao.vermelho = no.pai.vermelho;
                    no.pai.vermelho = false;
                    irmao.direita.vermelho = false;
                    rotacaoEsquerda(no.pai);
                    no = raiz;
                }
            } else {
                RBNode irmao = no.pai.esquerda;
                if (irmao.vermelho) {
                    irmao.vermelho = false;
                    no.pai.vermelho = true;
                    rotacaoDireita(no.pai);
                    irmao = no.pai.esquerda;
                }
                if (!irmao.direita.vermelho && !irmao.esquerda.vermelho) {
                    if (irmao != nil) irmao.vermelho = true;
                    no = no.pai;
                } else {
                    if (!irmao.esquerda.vermelho) {
                        irmao.direita.vermelho = false;
                        irmao.vermelho = true;
                        rotacaoEsquerda(irmao);
                        irmao = no.pai.esquerda;
                    }
                    irmao.vermelho = no.pai.vermelho;
                    no.pai.vermelho = false;
                    irmao.esquerda.vermelho = false;
                    rotacaoDireita(no.pai);
                    no = raiz;
                }
            }
        }
        no.vermelho = false;
    }

    private RBNode minimo(RBNode no) {
        while (no.esquerda != nil) no = no.esquerda;
        return no;
    }

    private void transplantar(RBNode origem, RBNode destino) {
        if (origem.pai == nil) raiz = destino;
        else if (origem == origem.pai.esquerda) origem.pai.esquerda = destino;
        else origem.pai.direita = destino;
        destino.pai = origem.pai;
    }

    private void rotacaoEsquerda(RBNode x) {
        RBNode y = x.direita;
        x.direita = y.esquerda;
        if (y.esquerda != nil) y.esquerda.pai = x;
        y.pai = x.pai;
        if (x.pai == nil) raiz = y;
        else if (x == x.pai.esquerda) x.pai.esquerda = y;
        else x.pai.direita = y;
        y.esquerda = x;
        x.pai = y;
    }

    private void rotacaoDireita(RBNode y) {
        RBNode x = y.esquerda;
        y.esquerda = x.direita;
        if (x.direita != nil) x.direita.pai = y;
        x.pai = y.pai;
        if (y.pai == nil) raiz = x;
        else if (y == y.pai.direita) y.pai.direita = x;
        else y.pai.esquerda = x;
        x.direita = y;
        y.pai = x;
    }

    public List<Produto> listarEmOrdem() {
        List<Produto> lista = new ArrayList<>();
        listarEmOrdem(raiz, lista);
        return lista;
    }

    private void listarEmOrdem(RBNode no, List<Produto> lista) {
        if (no == nil) return;
        listarEmOrdem(no.esquerda, lista);
        lista.add(no.produto);
        listarEmOrdem(no.direita, lista);
    }

    public void imprimirEstrutura() {
        if (raiz == nil) {
            System.out.println("(árvore vazia)");
            return;
        }
        imprimirEstrutura(raiz, "", true);
    }

    private void imprimirEstrutura(RBNode no, String prefixo, boolean ultimo) {
        if (no == nil) return;
        System.out.println(prefixo + (ultimo ? "└── " : "├── ") + no.produto.getCodigo()
                + (no.vermelho ? " (Vermelho)" : " (Preto)"));
        String proximo = prefixo + (ultimo ? "    " : "│   ");
        if (no.esquerda != nil || no.direita != nil) {
            imprimirEstrutura(no.esquerda, proximo, false);
            imprimirEstrutura(no.direita, proximo, true);
        }
    }

    /** Verifica ordenação, cores, ponteiros de pais, altura negra e quantidade de nós. */
    public boolean validar() {
        if (raiz == nil) return tamanho == 0;
        if (raiz.vermelho || raiz.pai != nil) return false;
        int[] contador = {0};
        int alturaNegra = validarNo(raiz, Long.MIN_VALUE, Long.MAX_VALUE, contador);
        return alturaNegra >= 0 && contador[0] == tamanho && !nil.vermelho;
    }

    private int validarNo(RBNode no, long minimo, long maximo, int[] contador) {
        if (no == nil) return 1;
        int chave = no.produto.getCodigo();
        if (chave <= minimo || chave >= maximo) return -1;
        if (no.esquerda == null || no.direita == null || no.pai == null) return -1;
        if (no.esquerda != nil && no.esquerda.pai != no) return -1;
        if (no.direita != nil && no.direita.pai != no) return -1;
        if (no.vermelho && (no.esquerda.vermelho || no.direita.vermelho)) return -1;
        contador[0]++;
        int esq = validarNo(no.esquerda, minimo, chave, contador);
        int dir = validarNo(no.direita, chave, maximo, contador);
        if (esq < 0 || dir < 0 || esq != dir) return -1;
        return esq + (no.vermelho ? 0 : 1);
    }
}
