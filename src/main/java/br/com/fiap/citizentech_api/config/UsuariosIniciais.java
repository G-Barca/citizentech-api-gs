package br.com.fiap.citizentech_api.config;

import br.com.fiap.citizentech_api.model.Papel;
import br.com.fiap.citizentech_api.model.Usuario;
import br.com.fiap.citizentech_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuariosIniciais implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuariosIniciais(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        criarSeNaoExiste("admin", "admin123", Papel.ADMIN);
        criarSeNaoExiste("usuario", "usuario123", Papel.USER);
    }

    private void criarSeNaoExiste(String login, String senha, Papel papel) {
        if (repository.existsByLogin(login)) {
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(encoder.encode(senha));
        usuario.setPapel(papel);
        repository.save(usuario);
    }
}