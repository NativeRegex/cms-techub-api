package com.cmstechub.marketplace.controller;

import com.cmstechub.marketplace.entity.User;
import com.cmstechub.marketplace.repository.UserRepository;
import com.cmstechub.marketplace.security.JwtUtil;
import com.cmstechub.marketplace.security.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RateLimiter rateLimiter;

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder, RateLimiter rateLimiter) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String ip = getClientIp(request);
        String rateKey = "login:" + ip;

        if (!rateLimiter.isAllowed(rateKey)) {
            long secs = rateLimiter.secondsUntilReset(rateKey);
            return ResponseEntity.status(429).body(Map.of(
                "error", "Too many login attempts. Try again in " + (secs / 60) + " minutes."
            ));
        }

        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        // Successful login — reset the rate limit
        rateLimiter.reset(rateKey);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of("username", user.getUsername(), "role", user.getRole())
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "No token"));
        }
        String token = auth.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        return ResponseEntity.ok(Map.of(
                "username", jwtUtil.extractUsername(token),
                "role", jwtUtil.extractRole(token)
        ));
    }

    private String getClientIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }
}
