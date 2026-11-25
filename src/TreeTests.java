import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Testes automatizados sem utilizar bibliotecas prontas de árvores. */
public class TreeTests {
    private static int testesExecutados;

    public static void main(String[] args) {
        RedBlackTree rubroNegra = new RedBlackTree();
        Tree234 arvore234 = new Tree234();
        Map<Integer, Produto> referencia = new HashMap<>();

        // O conjunto inicial contém pelo menos 21 chaves distintas.
        for (int codigo = 1; codigo <= 21; codigo++) {
            Produto produto = produto(codigo, "Produto " + codigo);
            rubroNegra.inserir(produto);
            arvore234.inserir(produto);
            referencia.put(codigo, produto);
        }
        verificar(rubroNegra.tamanho() == 21 && arvore234.tamanho() == 21, "21 inserções iniciais");
        validarTudo(rubroNegra, arvore234, referencia, "após inserções iniciais");

        verificar(rubroNegra.buscar(10) != null && arvore234.buscar(10) != null, "busca de chave existente");
        verificar(rubroNegra.buscar(999) == null && arvore234.buscar(999) == null, "busca de chave inexistente");

        Produto atualizado = produto(10, "Produto 10 atualizado");
        rubroNegra.inserir(atualizado);
        arvore234.inserir(atualizado);
        referencia.put(10, atualizado);
        verificar(rubroNegra.tamanho() == 21 && arvore234.tamanho() == 21, "repetição atualiza sem duplicar");
        verificar("Produto 10 atualizado".equals(rubroNegra.buscar(10).getNome()), "atualização na Rubro-Negra");
        verificar("Produto 10 atualizado".equals(arvore234.buscar(10).getNome()), "atualização na 2-3-4");

        int[] remocoes = {1, 2, 3, 4, 5, 10, 15, 21, 8, 12, 17};
        for (int codigo : remocoes) {
            boolean a = rubroNegra.excluir(codigo);
            boolean b = arvore234.excluir(codigo);
            referencia.remove(codigo);
            verificar(a && b, "exclusão da chave " + codigo);
            validarTudo(rubroNegra, arvore234, referencia, "após excluir " + codigo);
        }
        verificar(!rubroNegra.excluir(999) && !arvore234.excluir(999), "exclusão de chave inexistente");

        // Teste de estresse determinístico: mistura inserções, atualizações e exclusões.
        Random random = new Random(20261124L);
        for (int passo = 1; passo <= 3000; passo++) {
            int codigo = random.nextInt(150);
            if (random.nextInt(100) < 58) {
                Produto p = produto(codigo, "Produto teste " + passo);
                rubroNegra.inserir(p);
                arvore234.inserir(p);
                referencia.put(codigo, p);
            } else {
                boolean esperado = referencia.remove(codigo) != null;
                boolean a = rubroNegra.excluir(codigo);
                boolean b = arvore234.excluir(codigo);
                verificar(a == esperado && b == esperado, "resultado de exclusão aleatória, passo " + passo);
            }
            if (passo % 25 == 0) validarTudo(rubroNegra, arvore234, referencia, "teste aleatório passo " + passo);
        }
        validarTudo(rubroNegra, arvore234, referencia, "estado final dos testes aleatórios");
        System.out.println("\nTODOS OS TESTES PASSARAM. Total de verificações: " + testesExecutados);
    }

    private static Produto produto(int codigo, String nome) {
        return new Produto(codigo, nome, "Categoria teste", codigo * 2.5, codigo % 20);
    }

    private static void validarTudo(RedBlackTree rn, Tree234 t, Map<Integer, Produto> referencia, String contexto) {
        verificar(rn.validar(), "propriedades Rubro-Negra " + contexto);
        verificar(t.validar(), "propriedades 2-3-4 " + contexto);
        verificar(rn.tamanho() == referencia.size(), "tamanho Rubro-Negra " + contexto);
        verificar(t.tamanho() == referencia.size(), "tamanho 2-3-4 " + contexto);

        for (Map.Entry<Integer, Produto> entrada : referencia.entrySet()) {
            int codigo = entrada.getKey();
            verificar(rn.buscar(codigo) != null, "busca Rubro-Negra chave " + codigo + " " + contexto);
            verificar(t.buscar(codigo) != null, "busca 2-3-4 chave " + codigo + " " + contexto);
        }
        verificarOrdenacao(rn.listarEmOrdem(), "ordenação Rubro-Negra " + contexto);
        verificarOrdenacao(t.listarEmOrdem(), "ordenação 2-3-4 " + contexto);
    }

    private static void verificarOrdenacao(List<Produto> produtos, String descricao) {
        int anterior = Integer.MIN_VALUE;
        for (Produto produto : produtos) {
            verificar(produto.getCodigo() > anterior, descricao);
            anterior = produto.getCodigo();
        }
    }

    private static void verificar(boolean condicao, String descricao) {
        testesExecutados++;
        if (!condicao) throw new AssertionError("TESTE FALHOU: " + descricao);
    }
}
