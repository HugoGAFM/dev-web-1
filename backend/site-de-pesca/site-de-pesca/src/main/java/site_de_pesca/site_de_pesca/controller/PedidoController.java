package site_de_pesca.site_de_pesca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site_de_pesca.site_de_pesca.config.JWTUserData;
import site_de_pesca.site_de_pesca.dto.request.PedidoRequest;
import site_de_pesca.site_de_pesca.entities.Pedido;
import site_de_pesca.site_de_pesca.entities.User;
import site_de_pesca.site_de_pesca.repository.PedidoRepository;
import site_de_pesca.site_de_pesca.repository.UserRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoRepository pedidoRepository;
    private final UserRepository userRepository;

    public PedidoController(PedidoRepository pedidoRepository, UserRepository userRepository) {
        this.pedidoRepository = pedidoRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequest request, Authentication authentication) {
        logger.info("POST /pedidos payload: produto={} preco={}", request.getProduto(), request.getPreco());
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof JWTUserData) {
            Long userId = ((JWTUserData) principal).userId();
            Optional<User> opt = userRepository.findById(userId);
            if (opt.isPresent()) user = opt.get();
        }

        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setProduto(request.getProduto());
        pedido.setPreco(request.getPreco());
        pedido.setUser(user);

        Pedido salvo = pedidoRepository.save(pedido);
        logger.info("Pedido salvo id={} userId={}", salvo.getId(), user.getId());

        URI location = URI.create("/pedidos/" + salvo.getId());
        return ResponseEntity.created(location).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarMeusPedidos(Authentication authentication) {
        logger.info("GET /pedidos by authentication principal={}", authentication != null ? authentication.getPrincipal() : null);
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        Long userId = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            userId = ((User) principal).getId();
        } else if (principal instanceof JWTUserData) {
            userId = ((JWTUserData) principal).userId();
        }

        if (userId == null) return ResponseEntity.status(401).build();

        List<Pedido> pedidos = pedidoRepository.findByUserId(userId);
        return ResponseEntity.ok(pedidos);
    }
}
