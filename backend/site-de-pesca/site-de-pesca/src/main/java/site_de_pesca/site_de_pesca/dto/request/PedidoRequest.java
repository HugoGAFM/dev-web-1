package site_de_pesca.site_de_pesca.dto.request;

public class PedidoRequest {
    private String produto;
    private Double preco;

    public PedidoRequest() {}

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
