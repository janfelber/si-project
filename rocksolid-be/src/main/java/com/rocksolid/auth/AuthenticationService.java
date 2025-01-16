package com.rocksolid.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocksolid.config.JwtService;
import com.rocksolid.dto.ResetPasswordDto;
import com.rocksolid.dto.ResetPasswordRequestDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public AuthenticationResponse register(final RegisterRequest request) {
        //check if user with email already exists
        //method isPresent() returns true if user with email exists in database
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Tento e-mail už existuje.");
        }
        else {
            final var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .last_name(request.getLastName())
                .first_name(request.getFirstName())
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

        final String redirectUrl = determineRedirectUrl(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .redirectUrl(redirectUrl)
                .build();
    }

    private String determineRedirectUrl(final User user) {
        if (user.getRole() == Role.ADMIN) {
            return "/admin/users";
        } else if (user.getRole() == Role.STUDENT || user.getRole() == Role.REVIEWER) {
            return "/web/home";
        } else {
            return "/login";
        }
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

    public void requestPasswordReset(final ResetPasswordRequestDto request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        final User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> {
                return new UsernameNotFoundException("User not found with email: " + request.getEmail());
            });

        final String resetToken = UUID.randomUUID().toString();

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        repository.save(user);

        emailService.sendEmail(user.getEmail(), "Reset Your Password",
            "Hello,\n\n" +
                "We received a request to reset your password. Click the link below to create a new password for your account\n\n" +
                "Reset your password: http://localhost:3000/reset-password?token=" + resetToken + "\n" +
                "If you didn’t request a password reset, please ignore this email. For any concerns or assistance, please contact us at: rocksolid.supp@gmail.com.\n\n" +
                "Thanks,\n" +
                "The Rock Solid Team");
    }

    public void resetPassword(final ResetPasswordDto request) {
        final User user = repository.findByResetToken(request.getToken())
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        System.out.println(user.getResetTokenExpiry());

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        repository.save(user);
    }
}
