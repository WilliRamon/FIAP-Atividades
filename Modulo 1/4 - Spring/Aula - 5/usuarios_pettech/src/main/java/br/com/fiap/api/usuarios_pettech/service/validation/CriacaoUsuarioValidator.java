package br.com.fiap.api.usuarios_pettech.service.validation;

import br.com.fiap.api.usuarios_pettech.dto.UsuarioDTO;
import br.com.fiap.api.usuarios_pettech.entities.Usuario;
import br.com.fiap.api.usuarios_pettech.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CriacaoUsuarioValidator implements ConstraintValidator<CriacaoUsuarioValidJava, UsuarioDTO> {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void initialize(CriacaoUsuarioValidJava anotation){}

    @Override
    public boolean isValid(UsuarioDTO dto, ConstraintValidatorContext context){
        Optional<Usuario> usuario = repository.findByEmail(dto.email());
        return usuario.isEmpty();
    }
}
