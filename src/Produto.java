/** Representa um produto armazenado pelas árvores. O código é a chave única. */
public class Produto {
    private final int codigo;
    private String nome;
    private String categoria;
    private double preco;
    private int estoque;

    public Produto(int codigo, String nome, String categoria, double preco, int estoque) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("O nome não pode ficar vazio.");
        if (categoria == null || categoria.isBlank()) throw new IllegalArgumentException("A categoria não pode ficar vazia.");
        if (preco < 0 || Double.isNaN(preco) || Double.isInfinite(preco)) throw new IllegalArgumentException("Preço inválido.");
        if (estoque < 0) throw new IllegalArgumentException("Estoque não pode ser negativo.");
        this.codigo = codigo;
        this.nome = nome.trim();
        this.categoria = categoria.trim();
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }

    @Override
    public String toString() {
        return String.format("Código: %d | Nome: %s | Categoria: %s | Preço: R$ %.2f | Estoque: %d",
                codigo, nome, categoria, preco, estoque);
    }
}
