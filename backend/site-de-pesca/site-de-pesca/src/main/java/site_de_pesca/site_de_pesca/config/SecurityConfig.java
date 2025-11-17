package site_de_pesca.site_de_pesca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails amanda = User.withUsername("amandalps")
            .password(encoder.encode("1234"))
            .roles("USER")
            .build();

        UserDetails victor = User.withUsername("victorcorrea")
            .password(encoder.encode("45678"))
            .roles("USER")
            .build();

        UserDetails andressa = User.withUsername("andressa")
            .password(encoder.encode("abcd"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(amanda, victor, andressa);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .formLogin()
            .and()
            .httpBasic();
        return http.build();
    }
