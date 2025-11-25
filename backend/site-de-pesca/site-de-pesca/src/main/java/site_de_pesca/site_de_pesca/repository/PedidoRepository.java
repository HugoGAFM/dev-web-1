package site_de_pesca.site_de_pesca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site_de_pesca.site_de_pesca.entities.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	List<Pedido> findByUserId(Long userId);

}
