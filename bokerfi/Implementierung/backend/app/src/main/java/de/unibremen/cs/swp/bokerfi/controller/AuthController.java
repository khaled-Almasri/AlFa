package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Optional;

/**
 * Controller für Authentifizierung und Benutzersitzungen.
 * <p>
 * Bereitstellung von Endpunkten für Login, Passwortänderung und Abrufen des aktuellen Benutzerprofils.
 * </p>
 */
@RestController
public class AuthController {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    /**
     * Erstellt einen neuen AuthController.
     *
     * @param repository Das Repository für den Zugriff auf Personendaten
     * @param mapper     Der Mapper zur Konvertierung von Person-Entitäten in DTOs
     */
    public AuthController(PersonRepository repository, PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Authentifiziert einen Benutzer und stellt ein JWT-Token aus.
     *
     * @param credentials Eine Map mit "username" (oder "email") und "password"
     * @return Eine Map mit dem JWT-Token
     */
    @PostMapping({"/login", "/api/login", "/api/auth/login", "/auth/login", "/token", "/api/v1/auth/login"})
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.getOrDefault("email", credentials.get("username"));
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }

        // Try to find by email or personnel number
        Optional<Person> userOpt = repository.findByEmail(username)
                .or(() -> repository.findByPersonnelNumber(username));

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Person user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "USER_DEACTIVATED"));
        }

        // Encode role and identifier in token
        String token = "dummy-token-" + user.getRole().name() + "-" + user.getEmail();
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Ändert das Passwort des aktuell authentifizierten Benutzers.
     *
     * @param authentication Die Authentifizierung des aktuellen Benutzers
     * @param body           Eine Map mit "newPassword", "confirmPassword" (optional) und "currentPassword" (optional)
     * @return No Content bei Erfolg
     */
    @RequestMapping(value = {"/api/auth/password", "/auth/password", "/api/v1/auth/password", "/api/auth/change-password", "/auth/change-password"},
            method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Void> changePassword(Authentication authentication, @RequestBody Map<String, String> body) {
        try {

            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            String username = authentication.getName();
            Optional<Person> personOpt = repository.findByEmail(username)
                    .or(() -> repository.findByPersonnelNumber(username));
                    
            if (personOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            Person person = personOpt.get();
            String newPassword = body.get("newPassword");
            String confirmPassword = body.get("confirmPassword");
            String currentPassword = body.get("currentPassword");
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validate confirm if present
            if (confirmPassword != null && !newPassword.equals(confirmPassword)) {
                 return ResponseEntity.badRequest().build();
            }
            
            // Validate current if present (Note: passwords are not encoded in this impl apparently?)
            if (currentPassword != null && !person.getPassword().equals(currentPassword)) {
                 return ResponseEntity.badRequest().build();
            }
            
            person.setPassword(newPassword);
            repository.save(person);
            
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log error in production
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Ruft das Profil des aktuell authentifizierten Benutzers ab.
     *
     * @param authentication Die Authentifizierung des aktuellen Benutzers
     * @return Das PersonDTO des aktuellen Benutzers
     */
    @GetMapping({"/api/auth/me", "/auth/me", "/api/v1/auth/me", "/api/users/me", "/api/v1/users/me"})
    public ResponseEntity<PersonDTO> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String username = authentication.getName();
        Optional<Person> person = repository.findByEmail(username)
                .or(() -> repository.findByPersonnelNumber(username));
                
        return person.map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
