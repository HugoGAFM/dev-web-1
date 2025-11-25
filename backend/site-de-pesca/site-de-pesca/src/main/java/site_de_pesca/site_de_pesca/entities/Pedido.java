package site_de_pesca.site_de_pesca.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
        private LocalDateTime data;

    @Column(nullable = false)
    private String produto;

    private Double preco;

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private site_de_pesca.site_de_pesca.entities.User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getProduto() { return produto; }
    public void setProduto(String produto) { this.produto = produto; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public site_de_pesca.site_de_pesca.entities.User getUser() { return user; }
    public void setUser(site_de_pesca.site_de_pesca.entities.User user) { this.user = user; }
}
