import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Árvore 2-3-4 implementada como B-tree de grau mínimo t=2.
 * Cada nó guarda de 1 a 3 chaves (exceto a raiz vazia da árvore vazia).
 * Chaves repetidas atualizam o registro já existente.
 */
public class Tree234 {
    private static final int T = 2;
    private Node234 raiz = new Node234();
    private int tamanho;

    public int tamanho() { return tamanho; }
    public boolean estaVazia() { return tamanho == 0; }

    public Produto buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Produto buscar(Node234 no, int codigo) {
        int i = 0;
        while (i < no.quantidade && codigo > no.chaves[i].getCodigo()) i++;
        if (i < no.quantidade && codigo == no.chaves[i].getCodigo()) return no.chaves[i];
        if (no.folha) return null;
        return buscar(no.filhos[i], codigo);
    }

    public void inserir(Produto produto) {
        if (produto == null) throw new IllegalArgumentException("Produto não pode ser nulo.");
        if (buscar(produto.getCodigo()) != null) {
            atualizar(raiz, produto);
            return;
        }

        if (raiz.quantidade == 2 * T - 1) {
            Node234 novaRaiz = new Node234();
            novaRaiz.folha = false;
            novaRaiz.filhos[0] = raiz;
            dividirFilho(novaRaiz, 0);
            raiz = novaRaiz;
        }
        inserirEmNoNaoCheio(raiz, produto);
        tamanho++;
    }

    private void atualizar(Node234 no, Produto produto) {
        int i = 0;
        while (i < no.quantidade && produto.getCodigo() > no.chaves[i].getCodigo()) i++;
        if (i < no.quantidade && produto.getCodigo() == no.chaves[i].getCodigo()) {
            no.chaves[i] = produto;
        } else if (!no.folha) {
            atualizar(no.filhos[i], produto);
        }
    }

    private void inserirEmNoNaoCheio(Node234 no, Produto produto) {
        int i = no.quantidade - 1;
        if (no.folha) {
            while (i >= 0 && produto.getCodigo() < no.chaves[i].getCodigo()) {
                no.chaves[i + 1] = no.chaves[i];
                i--;
            }
            no.chaves[i + 1] = produto;
            no.quantidade++;
        } else {
            while (i >= 0 && produto.getCodigo() < no.chaves[i].getCodigo()) i--;
            i++;
            if (no.filhos[i].quantidade == 2 * T - 1) {
                dividirFilho(no, i);
                if (produto.getCodigo() > no.chaves[i].getCodigo()) i++;
            }
            inserirEmNoNaoCheio(no.filhos[i], produto);
        }
    }

    /** Divide um filho cheio e promove sua chave mediana ao nó pai. */
    private void dividirFilho(Node234 pai, int indice) {
        Node234 cheio = pai.filhos[indice];
        Node234 novo = new Node234();
        novo.folha = cheio.folha;
        novo.quantidade = T - 1;
        novo.chaves[0] = cheio.chaves[T];

        if (!cheio.folha) {
            novo.filhos[0] = cheio.filhos[T];
            novo.filhos[1] = cheio.filhos[T + 1];
            cheio.filhos[T] = null;
            cheio.filhos[T + 1] = null;
        }

        Produto mediana = cheio.chaves[T - 1];
        cheio.chaves[T - 1] = null;
        cheio.chaves[T] = null;
        cheio.quantidade = T - 1;

        for (int j = pai.quantidade; j >= indice + 1; j--) {
            pai.filhos[j + 1] = pai.filhos[j];
        }
        pai.filhos[indice + 1] = novo;

        for (int j = pai.quantidade - 1; j >= indice; j--) {
            pai.chaves[j + 1] = pai.chaves[j];
        }
        pai.chaves[indice] = mediana;
        pai.quantidade++;
    }

    public boolean excluir(int codigo) {
        if (buscar(codigo) == null) return false;
        remover(raiz, codigo);
        if (raiz.quantidade == 0) {
            if (raiz.folha) raiz = new Node234();
            else raiz = raiz.filhos[0];
        }
        tamanho--;
        return true;
    }

    private void remover(Node234 no, int codigo) {
        int indice = encontrarIndice(no, codigo);
        if (indice < no.quantidade && no.chaves[indice].getCodigo() == codigo) {
            if (no.folha) removerDaFolha(no, indice);
            else removerDeNoInterno(no, indice);
            return;
        }

        if (no.folha) return; // A chave não existe; normalmente já filtrada por excluir().

        boolean eraUltimoFilho = indice == no.quantidade;
        if (no.filhos[indice].quantidade < T) preencherFilho(no, indice);

        if (eraUltimoFilho && indice > no.quantidade) remover(no.filhos[indice - 1], codigo);
        else remover(no.filhos[indice], codigo);
    }

    private int encontrarIndice(Node234 no, int codigo) {
        int indice = 0;
        while (indice < no.quantidade && no.chaves[indice].getCodigo() < codigo) indice++;
        return indice;
    }

    private void removerDaFolha(Node234 no, int indice) {
        for (int i = indice + 1; i < no.quantidade; i++) no.chaves[i - 1] = no.chaves[i];
        no.chaves[no.quantidade - 1] = null;
        no.quantidade--;
    }

    private void removerDeNoInterno(Node234 no, int indice) {
        Produto alvo = no.chaves[indice];
        if (no.filhos[indice].quantidade >= T) {
            Produto predecessor = obterPredecessor(no.filhos[indice]);
            no.chaves[indice] = predecessor;
            remover(no.filhos[indice], predecessor.getCodigo());
        } else if (no.filhos[indice + 1].quantidade >= T) {
            Produto sucessor = obterSucessor(no.filhos[indice + 1]);
            no.chaves[indice] = sucessor;
            remover(no.filhos[indice + 1], sucessor.getCodigo());
        } else {
            fundirFilhos(no, indice);
            remover(no.filhos[indice], alvo.getCodigo());
        }
    }

    private Produto obterPredecessor(Node234 no) {
        while (!no.folha) no = no.filhos[no.quantidade];
        return no.chaves[no.quantidade - 1];
    }

    private Produto obterSucessor(Node234 no) {
        while (!no.folha) no = no.filhos[0];
        return no.chaves[0];
    }

    /** Garante que o filho escolhido tenha pelo menos T chaves antes da descida. */
    private void preencherFilho(Node234 pai, int indice) {
        if (indice > 0 && pai.filhos[indice - 1].quantidade >= T) {
            emprestarDoAnterior(pai, indice);
        } else if (indice < pai.quantidade && pai.filhos[indice + 1].quantidade >= T) {
            emprestarDoProximo(pai, indice);
        } else if (indice < pai.quantidade) {
            fundirFilhos(pai, indice);
        } else {
            fundirFilhos(pai, indice - 1);
        }
    }

    private void emprestarDoAnterior(Node234 pai, int indice) {
        Node234 filho = pai.filhos[indice];
        Node234 irmao = pai.filhos[indice - 1];

        for (int i = filho.quantidade - 1; i >= 0; i--) filho.chaves[i + 1] = filho.chaves[i];
        if (!filho.folha) {
            for (int i = filho.quantidade; i >= 0; i--) filho.filhos[i + 1] = filho.filhos[i];
        }

        filho.chaves[0] = pai.chaves[indice - 1];
        if (!filho.folha) {
            filho.filhos[0] = irmao.filhos[irmao.quantidade];
            irmao.filhos[irmao.quantidade] = null;
        }
        pai.chaves[indice - 1] = irmao.chaves[irmao.quantidade - 1];
        irmao.chaves[irmao.quantidade - 1] = null;
        filho.quantidade++;
        irmao.quantidade--;
    }

    private void emprestarDoProximo(Node234 pai, int indice) {
        Node234 filho = pai.filhos[indice];
        Node234 irmao = pai.filhos[indice + 1];

        filho.chaves[filho.quantidade] = pai.chaves[indice];
        if (!filho.folha) filho.filhos[filho.quantidade + 1] = irmao.filhos[0];
        pai.chaves[indice] = irmao.chaves[0];

        for (int i = 1; i < irmao.quantidade; i++) irmao.chaves[i - 1] = irmao.chaves[i];
        irmao.chaves[irmao.quantidade - 1] = null;
        if (!irmao.folha) {
            for (int i = 1; i <= irmao.quantidade; i++) irmao.filhos[i - 1] = irmao.filhos[i];
            irmao.filhos[irmao.quantidade] = null;
        }
        filho.quantidade++;
        irmao.quantidade--;
    }

    /** Combina filho esquerdo + chave do pai + filho direito em um único nó. */
    private void fundirFilhos(Node234 pai, int indice) {
        Node234 filho = pai.filhos[indice];
        Node234 irmao = pai.filhos[indice + 1];
        int posicao = filho.quantidade;

        filho.chaves[posicao] = pai.chaves[indice];
        for (int i = 0; i < irmao.quantidade; i++) filho.chaves[posicao + 1 + i] = irmao.chaves[i];
        if (!filho.folha) {
            for (int i = 0; i <= irmao.quantidade; i++) {
                filho.filhos[posicao + 1 + i] = irmao.filhos[i];
            }
        }
        filho.quantidade += irmao.quantidade + 1;

        for (int i = indice + 1; i < pai.quantidade; i++) pai.chaves[i - 1] = pai.chaves[i];
        pai.chaves[pai.quantidade - 1] = null;
        for (int i = indice + 2; i <= pai.quantidade; i++) pai.filhos[i - 1] = pai.filhos[i];
        pai.filhos[pai.quantidade] = null;
        pai.quantidade--;
    }

    public List<Produto> listarEmOrdem() {
        List<Produto> resultado = new ArrayList<>();
        listarEmOrdem(raiz, resultado);
        return resultado;
    }

    private void listarEmOrdem(Node234 no, List<Produto> resultado) {
        for (int i = 0; i < no.quantidade; i++) {
            if (!no.folha) listarEmOrdem(no.filhos[i], resultado);
            resultado.add(no.chaves[i]);
        }
        if (!no.folha) listarEmOrdem(no.filhos[no.quantidade], resultado);
    }

    public void imprimirPorNiveis() {
        Queue<Node234> fila = new ArrayDeque<>();
        fila.add(raiz);
        int nivel = 0;
        while (!fila.isEmpty()) {
            int quantidadeNoNivel = fila.size();
            System.out.print("Nível " + nivel++ + ": ");
            for (int i = 0; i < quantidadeNoNivel; i++) {
                Node234 no = fila.remove();
                System.out.print("[");
                for (int j = 0; j < no.quantidade; j++) {
                    if (j > 0) System.out.print(" | ");
                    System.out.print(no.chaves[j].getCodigo());
                }
                System.out.print("] ");
                if (!no.folha) {
                    for (int j = 0; j <= no.quantidade; j++) fila.add(no.filhos[j]);
                }
            }
            System.out.println();
        }
    }

    /** Verifica ordenação, ocupação dos nós, quantidade de filhos e altura uniforme das folhas. */
    public boolean validar() {
        if (tamanho == 0) return raiz.folha && raiz.quantidade == 0;
        if (raiz.quantidade < 1 || raiz.quantidade > 3) return false;
        int[] contagem = {0};
        int[] profundidadeFolha = {-1};
        boolean valido = validarNo(raiz, true, Long.MIN_VALUE, Long.MAX_VALUE, 0,
                profundidadeFolha, contagem);
        return valido && contagem[0] == tamanho;
    }

    private boolean validarNo(Node234 no, boolean ehRaiz, long minimo, long maximo,
                              int profundidade, int[] profundidadeFolha, int[] contagem) {
        if (no == null || no.quantidade > 3) return false;
        if (!ehRaiz && no.quantidade < 1) return false;
        if (ehRaiz && no.quantidade < 1) return false;

        for (int i = 0; i < no.quantidade; i++) {
            if (no.chaves[i] == null) return false;
            int chave = no.chaves[i].getCodigo();
            if (chave <= minimo || chave >= maximo) return false;
            if (i > 0 && no.chaves[i - 1].getCodigo() >= chave) return false;
            contagem[0]++;
        }

        if (no.folha) {
            for (Node234 filho : no.filhos) if (filho != null) return false;
            if (profundidadeFolha[0] == -1) profundidadeFolha[0] = profundidade;
            return profundidadeFolha[0] == profundidade;
        }

        for (int i = 0; i <= no.quantidade; i++) {
            if (no.filhos[i] == null) return false;
            long limiteInferior = (i == 0) ? minimo : no.chaves[i - 1].getCodigo();
            long limiteSuperior = (i == no.quantidade) ? maximo : no.chaves[i].getCodigo();
            if (!validarNo(no.filhos[i], false, limiteInferior, limiteSuperior,
                    profundidade + 1, profundidadeFolha, contagem)) return false;
        }
        for (int i = no.quantidade + 1; i < no.filhos.length; i++) {
            if (no.filhos[i] != null) return false;
        }
        return true;
    }
}
