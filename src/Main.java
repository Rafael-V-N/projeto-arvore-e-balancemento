import java.util.List;
import java.util.Scanner;

/** Menu interativo do sistema acadêmico de gerenciamento de produtos. */
public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final RedBlackTree RUBRO_NEGRA = new RedBlackTree();
    private static final Tree234 ARVORE_234 = new Tree234();

    public static void main(String[] args) {
        carregarDadosIniciais();
        System.out.println("Foram carregados 21 produtos para a demonstração inicial.");
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> buscarProduto();
                case 3 -> excluirProduto();
                case 4 -> listarProdutos();
                case 5 -> demonstrarArvores();
                case 6 -> mostrarQuantidades();
                case 0 -> System.out.println("Programa encerrado.");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n============================================");
        System.out.println("      SISTEMA DE PRODUTOS - ARVORES");
        System.out.println("============================================");
        System.out.println("1 - Cadastrar ou atualizar produto");
        System.out.println("2 - Buscar produto pelo código");
        System.out.println("3 - Excluir produto pelo código");
        System.out.println("4 - Listar produtos em ordem crescente");
        System.out.println("5 - Mostrar estruturas das árvores");
        System.out.println("6 - Mostrar quantidade de registros");
        System.out.println("0 - Sair");
    }

    private static void carregarDadosIniciais() {
        String[] nomes = {
            "Teclado", "Mouse", "Monitor", "Notebook", "Webcam", "Headset", "Microfone",
            "Impressora", "Scanner", "Roteador", "Switch", "Cabo HDMI", "SSD",
            "Memória RAM", "Processador", "Placa de vídeo", "Fonte", "Gabinete",
            "Cooler", "Pendrive", "HD externo"
        };
        for (int i = 0; i < nomes.length; i++) {
            Produto produto = new Produto(100 + i, nomes[i], "Informática", 35.0 + i * 12.5, 5 + i);
            RUBRO_NEGRA.inserir(produto);
            ARVORE_234.inserir(produto);
        }
    }

    private static void cadastrarProduto() {
        System.out.println("\n--- CADASTRO DE PRODUTO ---");
        int codigo = lerInteiro("Código: ");
        String nome = lerTexto("Nome: ");
        String categoria = lerTexto("Categoria: ");
        double preco = lerDouble("Preço: R$ ");
        int estoque = lerInteiro("Quantidade em estoque: ");
        try {
            Produto produto = new Produto(codigo, nome, categoria, preco, estoque);
            RUBRO_NEGRA.inserir(produto);
            ARVORE_234.inserir(produto);
            System.out.println("Produto cadastrado/atualizado nas duas árvores.");
            System.out.println("Regra: se o código já existir, os dados são atualizados.");
        } catch (IllegalArgumentException erro) {
            System.out.println("Não foi possível cadastrar: " + erro.getMessage());
        }
    }

    private static void buscarProduto() {
        int codigo = lerInteiro("Digite o código que deseja buscar: ");
        Produto resultadoRN = RUBRO_NEGRA.buscar(codigo);
        Produto resultado234 = ARVORE_234.buscar(codigo);
        System.out.println("\nÁrvore Rubro-Negra: " + (resultadoRN == null ? "Produto não encontrado." : resultadoRN));
        System.out.println("Árvore 2-3-4:       " + (resultado234 == null ? "Produto não encontrado." : resultado234));
    }

    private static void excluirProduto() {
        int codigo = lerInteiro("Digite o código que deseja excluir: ");
        boolean removidoRN = RUBRO_NEGRA.excluir(codigo);
        boolean removido234 = ARVORE_234.excluir(codigo);
        if (removidoRN && removido234) {
            System.out.println("Produto removido das duas árvores.");
        } else {
            System.out.println("Código não encontrado.");
        }
    }

    private static void listarProdutos() {
        imprimirLista("RUBRO-NEGRA", RUBRO_NEGRA.listarEmOrdem());
        imprimirLista("2-3-4", ARVORE_234.listarEmOrdem());
    }

    private static void imprimirLista(String titulo, List<Produto> produtos) {
        System.out.println("\n--- PRODUTOS EM ORDEM: ÁRVORE " + titulo + " ---");
        if (produtos.isEmpty()) System.out.println("(nenhum produto cadastrado)");
        else for (Produto produto : produtos) System.out.println(produto);
    }

    private static void demonstrarArvores() {
        System.out.println("\n--- ÁRVORE RUBRO-NEGRA ---");
        System.out.println("V = vermelho | P = preto");
        RUBRO_NEGRA.imprimirEstrutura();
        System.out.println("\n--- ÁRVORE 2-3-4 POR NÍVEIS ---");
        ARVORE_234.imprimirPorNiveis();
        System.out.println("\nValidação Rubro-Negra: " + (RUBRO_NEGRA.validar() ? "OK" : "FALHOU"));
        System.out.println("Validação 2-3-4:       " + (ARVORE_234.validar() ? "OK" : "FALHOU"));
    }

    private static void mostrarQuantidades() {
        System.out.println("Quantidade na Rubro-Negra: " + RUBRO_NEGRA.tamanho());
        System.out.println("Quantidade na 2-3-4:       " + ARVORE_234.tamanho());
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = ENTRADA.nextLine().trim();
            try { return Integer.parseInt(texto); }
            catch (NumberFormatException erro) { System.out.println("Digite um número inteiro válido."); }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = ENTRADA.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (valor < 0 || Double.isNaN(valor) || Double.isInfinite(valor)) throw new NumberFormatException();
                return valor;
            } catch (NumberFormatException erro) {
                System.out.println("Digite um preço válido e não negativo.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = ENTRADA.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("O campo não pode ficar vazio.");
        }
    }
}
