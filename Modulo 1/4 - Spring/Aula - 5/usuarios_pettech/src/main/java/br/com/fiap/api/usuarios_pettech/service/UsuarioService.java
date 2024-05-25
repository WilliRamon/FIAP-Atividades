package br.com.fiap.api.usuarios_pettech.service;

import br.com.fiap.api.usuarios_pettech.controller.exception.ControllerNotFoundException;
import br.com.fiap.api.usuarios_pettech.dto.UsuarioDTO;
import br.com.fiap.api.usuarios_pettech.entities.Usuario;
import br.com.fiap.api.usuarios_pettech.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Page<UsuarioDTO> findAll(Pageable pageAble){
        Page<Usuario> usuarios = usuarioRepository.findAll(pageAble);
        return usuarios.map(this::toDTO);
    }

    public UsuarioDTO findById(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));
        return toDTO(usuario);
    }
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = toEntity(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO){
        try{
            Usuario buscaUsuario = usuarioRepository.getReferenceById(id);
            buscaUsuario.setNome(usuarioDTO.nome());
            buscaUsuario.setEmail(usuarioDTO.email());
            buscaUsuario.setCpf(usuarioDTO.cpf());
            buscaUsuario.setDataNascimento(usuarioDTO.dataNascimento());
            buscaUsuario = usuarioRepository.save(buscaUsuario);

            return toDTO(buscaUsuario);
        } catch (EntityNotFoundException e){
            throw new ControllerNotFoundException("Usuário não encontrado");
        }
    }

    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getDataNascimento()
        );
    }
    private Usuario toEntity(UsuarioDTO usuarioDTO){
        return new Usuario(usuarioDTO.id(),
                           usuarioDTO.nome(),
                           usuarioDTO.email(),
                           usuarioDTO.cpf(),
                           usuarioDTO.dataNascimento()
        );
    }
}
