package de.unibremen.cs.swp.bokerfi.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;

/**
 * Filter zur Verarbeitung von Mock-Tokens für Authentifizierung.
 * <p>
 * Erlaubt das Testen der API mit einfachen Tokens (z.B. "Bearer dummy-token-ADMIN").
 * </p>
 */
public class MockTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (token.isEmpty()) token = "dummy-token"; // prevent empty string logic
            
            String roleName = "ADMIN"; // Default to ADMIN for tests
            String username = "mockUser";
            
            // Expected format: dummy-token-ROLE-EMAIL
            String[] parts = token.split("-");
            if (parts.length >= 2) {
                 if (token.contains("-ADMIN-")) roleName = "ADMIN";
                 else if (token.contains("-MANAGER-")) roleName = "MANAGER";
                 else if (token.contains("-EMPLOYEE-")) roleName = "EMPLOYEE";
                 
                 // Try to get email (last part)
                 if (parts.length >= 3) {
                     username = parts[parts.length-1];
                 }
            }

            // Grant extracted role
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + roleName)));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response);
    }
}
