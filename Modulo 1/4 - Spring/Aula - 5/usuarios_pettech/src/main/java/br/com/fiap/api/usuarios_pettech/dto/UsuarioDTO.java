package br.com.fiap.api.usuarios_pettech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UsuarioDTO(
        Long id,
        @NotBlank(message = "Nome não pode estar em branco.")
        String nome,
        @Email(message = "E-mail inválido.")
        String email,
        String cpf,
        LocalDate dataNascimento
        ) {
}
