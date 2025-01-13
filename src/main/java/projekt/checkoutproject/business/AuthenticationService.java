package projekt.checkoutproject.business;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt.checkoutproject.api.dto.UserDTO;
import projekt.checkoutproject.api.dto.auth.LoginRequest;
import projekt.checkoutproject.api.dto.auth.LoginResponse;
import projekt.checkoutproject.api.dto.auth.RegisterRequest;
import projekt.checkoutproject.infrastructure.configuration.JwtService;
import projekt.checkoutproject.infrastructure.database.entity.UserEntity;
import projekt.checkoutproject.infrastructure.database.repository.UserRepository;
import projekt.checkoutproject.infrastructure.exceptions.UnknownUserException;
import projekt.checkoutproject.infrastructure.exceptions.UserAlreadyExistsException;
import projekt.checkoutproject.infrastructure.exceptions.UserNotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserDTO register(RegisterRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        UserEntity user = new UserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));
        String jwtToken = jwtService.generateToken(user);
        UserDTO userDTO = new UserDTO(user);
        LoginResponse loginResponse = new LoginResponse(jwtToken, userDTO);

        return loginResponse;
    }
}
