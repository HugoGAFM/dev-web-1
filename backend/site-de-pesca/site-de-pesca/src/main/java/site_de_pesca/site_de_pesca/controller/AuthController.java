package site_de_pesca.site_de_pesca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.validation.Valid;
import site_de_pesca.site_de_pesca.config.TokenConfig;
import site_de_pesca.site_de_pesca.dto.request.LoginRequest;
import site_de_pesca.site_de_pesca.dto.request.RegisterUserRequest;
import site_de_pesca.site_de_pesca.dto.response.LoginResponse;
import site_de_pesca.site_de_pesca.dto.response.RegisterUserResponse;
import site_de_pesca.site_de_pesca.entities.User;
import site_de_pesca.site_de_pesca.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Received login attempt for email={}", request.email());
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        try {
            Authentication authentication = authenticationManager.authenticate(userAndPass);
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                String token = tokenConfig.generateToken(user);
                return ResponseEntity.ok(new LoginResponse(token));
            } else {
                logger.warn("Authentication succeeded but principal is not User: {}", principal);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication principal unexpected");
            }
        } catch (AuthenticationException ex) {
            logger.warn("Authentication failed for email={}: {}", request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception ex) {
            logger.error("Unexpected error during authentication", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setUsername(request.username());
        newUser.setRole(request.role());
        newUser.setNome(request.nome());
        newUser.setSobrenome(request.sobrenome());

        userRepository.save(newUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getNome(), newUser.getEmail()));
    }
}
