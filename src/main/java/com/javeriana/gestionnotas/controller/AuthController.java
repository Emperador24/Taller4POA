package com.javeriana.gestionnotas.controller;

import com.javeriana.gestionnotas.config.SessionManager;
import com.javeriana.gestionnotas.model.Usuario;
import com.javeriana.gestionnotas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private SessionManager sessionManager;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String contrasena = credentials.get("contrasena");
        
        Optional<Usuario> usuario = usuarioService.autenticar(email, contrasena);
        
        if (usuario.isPresent()) {
            sessionManager.setUsuarioActual(usuario.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Autenticación exitosa");
            response.put("usuario", Map.of(
                "id", usuario.get().getId(),
                "nombre", usuario.get().getNombre(),
                "email", usuario.get().getEmail(),
                "rol", usuario.get().getRol()
            ));
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        sessionManager.clear();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Sesión cerrada exitosamente");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/session")
    public ResponseEntity<?> getSession() {
        if (sessionManager.isAuthenticated()) {
            Usuario usuario = sessionManager.getUsuarioActual();
            
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
            ));
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            
            return ResponseEntity.ok(response);
        }
    }
}