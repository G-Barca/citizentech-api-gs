package br.com.fiap.citizentech_api.controller;


import br.com.fiap.citizentech_api.dto.AuthDto;
import br.com.fiap.citizentech_api.exception.RegraNegocioException;
import br.com.fiap.citizentech_api.model.Papel;
import br.com.fiap.citizentech_api.model.Usuario;
import br.com.fiap.citizentech_api.repository.UsuarioRepository;
import br.com.fiap.citizentech_api.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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


@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto.UsuarioResponse> cadastrar(@Valid @RequestBody AuthDto.CadastroRequest req) {
        if (usuarioRepository.existsByLogin(req.login())) {
            throw new RegraNegocioException("Login ja esta em uso.");
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(req.login());
        usuario.setSenha(passwordEncoder.encode(req.senha()));
        usuario.setPapel(Papel.USER);
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthDto.UsuarioResponse(salvo.getId(), salvo.getLogin(), salvo.getPapel().name()));
    }

    @PostMapping("/login")
    public AuthDto.TokenResponse login(@Valid @RequestBody AuthDto.LoginRequest req) {
        Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.login(), req.senha()));
        String papel = autenticacao.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        return new AuthDto.TokenResponse(jwtService.gerarToken(req.login(), papel), "Bearer", papel);
    }
}