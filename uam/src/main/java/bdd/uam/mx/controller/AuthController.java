package bdd.uam.mx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import bdd.uam.mx.DTO.LoginRequestDTO;
import bdd.uam.mx.security.CustomUserDetailsService;
import bdd.uam.mx.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Credenciales incorrectas o usuario inactivo", HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(jwt);
    }
}