package ru.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.DTO.RegisterDTO;
import ru.DTO.UserDTO;
import ru.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO request) {
        UserDTO userDTO = authService.register(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/assign-role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> assignRole(@PathVariable Long userId, @PathVariable String roleName) {
        UserDTO userDTO = authService.assignRole(userId, roleName);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/{userId}/remove-role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> removeRole(@PathVariable Long userId, @PathVariable String roleName) {
        UserDTO userDTO = authService.removeRole(userId, roleName);
        return ResponseEntity.ok(userDTO);
    }
}
