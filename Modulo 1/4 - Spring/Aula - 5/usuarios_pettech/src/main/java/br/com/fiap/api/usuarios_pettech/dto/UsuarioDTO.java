package br.com.fiap.api.usuarios_pettech.dto;

import br.com.fiap.api.usuarios_pettech.service.validation.CriacaoUsuarioValidJava;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@CriacaoUsuarioValidJava(message = "E-mail já cadastrado")
public record UsuarioDTO(
        Long id,
        @NotBlank(message = "Nome não pode estar em branco.")
        String nome,
        @Email(message = "E-mail inválido.")
        String email,
        @CPF(message = "CPF inválido.")
        String cpf,
        LocalDate dataNascimento
        ) {
}
