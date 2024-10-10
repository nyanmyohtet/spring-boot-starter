package com.nyanmyohtet.springbootstarter.api.rest.auth;

import com.nyanmyohtet.springbootstarter.api.rest.auth.request.LogOutRequest;
import com.nyanmyohtet.springbootstarter.api.rest.auth.request.LoginRequest;
import com.nyanmyohtet.springbootstarter.api.rest.auth.request.RegisterRequest;
import com.nyanmyohtet.springbootstarter.api.rest.auth.request.TokenRefreshRequest;
import com.nyanmyohtet.springbootstarter.api.rest.auth.response.JwtResponse;
import com.nyanmyohtet.springbootstarter.api.rest.response.MessageResponse;
import com.nyanmyohtet.springbootstarter.exception.BadRequestException;
import com.nyanmyohtet.springbootstarter.exception.TokenRefreshException;
import com.nyanmyohtet.springbootstarter.exception.UnauthorizedException;
import com.nyanmyohtet.springbootstarter.model.RefreshToken;
import com.nyanmyohtet.springbootstarter.service.RefreshTokenService;
import com.nyanmyohtet.springbootstarter.service.UserService;
import com.nyanmyohtet.springbootstarter.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController @RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    /**
     * Endpoint to register a new user.
     *
     * @param request the registration request payload
     * @return a success or error message
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (isUsernameOrEmailTaken(request.getUsername(), request.getEmail())) {
            throw new BadRequestException("Username or Email is already taken!");
        }

        userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!", HttpStatus.OK.value()));
    }

    /**
     * Endpoint to authenticate a user and issue a JWT.
     *
     * @param loginRequest the login request payload
     * @return the JWT token or error message
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticateUser(loginRequest);
            String jwt = jwtUtil.generate(authentication.getName());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userService.findByUsername(loginRequest.getUsername()).get());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password.");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

        refreshTokenService.verifyExpiration(refreshToken);

        String token = jwtUtil.generate(refreshToken.getUser().getUsername());
        return ResponseEntity.ok(new JwtResponse(token, requestRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.findByToken(logOutRequest.getRefreshToken())
                .ifPresent(refreshToken -> refreshTokenService.deleteByUser(refreshToken.getUser()));

        return ResponseEntity.ok(new MessageResponse("Log out successful!", HttpStatus.OK.value()));
    }

    private boolean isUsernameOrEmailTaken(String username, String email) {
        return userService.existsByUsername(username) || userService.existsByEmail(email);
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}