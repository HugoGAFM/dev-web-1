package site_de_pesca.site_de_pesca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import site_de_pesca.site_de_pesca.config.TokenConfig;
import site_de_pesca.site_de_pesca.dto.request.LoginRequest;
import site_de_pesca.site_de_pesca.dto.request.RegisterUserRequest;
import site_de_pesca.site_de_pesca.dto.response.LoginResponse;
import site_de_pesca.site_de_pesca.dto.response.RegisterUserResponse;
import site_de_pesca.site_de_pesca.entities.User;
import site_de_pesca.site_de_pesca.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
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
