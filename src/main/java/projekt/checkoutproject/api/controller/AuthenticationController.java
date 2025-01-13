package projekt.checkoutproject.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt.checkoutproject.api.dto.UserDTO;
import projekt.checkoutproject.api.dto.auth.LoginRequest;
import projekt.checkoutproject.api.dto.auth.LoginResponse;
import projekt.checkoutproject.api.dto.auth.RegisterRequest;
import projekt.checkoutproject.business.AuthenticationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDTO user = authenticationService.register(registerRequest);
        return ResponseEntity.ok(user);
    }
}
