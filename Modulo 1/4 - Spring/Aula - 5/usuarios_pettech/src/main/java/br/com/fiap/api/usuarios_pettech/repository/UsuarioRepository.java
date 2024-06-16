package br.com.fiap.api.usuarios_pettech.repository;

import br.com.fiap.api.usuarios_pettech.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
