package com.rocksolid.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocksolid.config.JwtService;
import com.rocksolid.repository.UserRepository;
import com.rocksolid.security.enums.Role;
import com.rocksolid.token.Token;
import com.rocksolid.repository.TokenRepository;
import com.rocksolid.token.TokenType;
import com.rocksolid.module.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(final RegisterRequest request) {
        final var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .university(request.getUniversity())
                .build();

        final var savedUser = repository.save(user);
        final var jwtToken = jwtService.generateToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        final var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        final var jwtToken = jwtService.generateToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUsersTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void revokeAllUsersTokens(final User user) {
        final var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(final User user, final String jwtToken) {
        final var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }

    public void refreshToken(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String login;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        login = jwtService.extractLogin(refreshToken);

        if (login != null) {
            final var user = this.repository.findByEmail(login).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                final var accessToken = jwtService.generateToken(user);
                revokeAllUsersTokens(user);
                saveUserToken(user, accessToken);
                final var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
