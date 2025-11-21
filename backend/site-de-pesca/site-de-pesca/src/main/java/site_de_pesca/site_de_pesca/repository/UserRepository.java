package site_de_pesca.site_de_pesca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import site_de_pesca.site_de_pesca.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<UserDetails> findUserByEmail(String username);
}
