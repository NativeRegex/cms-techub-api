package com.cmstechub.marketplace.controller;

import com.cmstechub.marketplace.entity.Booking;
import com.cmstechub.marketplace.entity.Contact;
import com.cmstechub.marketplace.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PublicController {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int MAX_TEXT_LENGTH = 2000;

    private final ServiceRepository serviceRepo;
    private final ProductRepository productRepo;
    private final PartnerRepository partnerRepo;
    private final BookingRepository bookingRepo;
    private final ContactRepository contactRepo;

    public PublicController(ServiceRepository serviceRepo, ProductRepository productRepo,
                            PartnerRepository partnerRepo, BookingRepository bookingRepo,
                            ContactRepository contactRepo) {
        this.serviceRepo = serviceRepo;
        this.productRepo = productRepo;
        this.partnerRepo = partnerRepo;
        this.bookingRepo = bookingRepo;
        this.contactRepo = contactRepo;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "service", "cms-techub-api");
    }

    @GetMapping("/services")
    public List<com.cmstechub.marketplace.entity.Service> getServices() {
        return serviceRepo.findAll();
    }

    @GetMapping("/services/{id}")
    public com.cmstechub.marketplace.entity.Service getService(@PathVariable Long id) {
        return serviceRepo.findById(id).orElseThrow();
    }

    @GetMapping("/products")
    public List<com.cmstechub.marketplace.entity.Product> getProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/products/{id}")
    public com.cmstechub.marketplace.entity.Product getProduct(@PathVariable Long id) {
        return productRepo.findById(id).orElseThrow();
    }

    @GetMapping("/partners")
    public List<com.cmstechub.marketplace.entity.Partner> getPartners() {
        return partnerRepo.findAll();
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody Booking b) {
        // Honeypot check — bots fill this hidden field, real users don't
        if (b.getDescription() != null && b.getDescription().contains("BOT_TRAP_")) {
            return ResponseEntity.ok(Map.of("status", "ok"));  // silently ignore bots
        }

        String err = validateBooking(b);
        if (err != null) return ResponseEntity.badRequest().body(Map.of("error", err));

        b.setName(sanitize(b.getName()));
        b.setEmail(sanitize(b.getEmail()));
        b.setPhone(sanitize(b.getPhone()));
        b.setDescription(sanitize(b.getDescription()));
        b.setPreferredDate(sanitize(b.getPreferredDate()));

        return ResponseEntity.ok(bookingRepo.save(b));
    }

    @PostMapping("/contact")
    public ResponseEntity<?> createContact(@RequestBody Contact c) {
        // Honeypot check
        if (c.getMessage() != null && c.getMessage().contains("BOT_TRAP_")) {
            return ResponseEntity.ok(Map.of("status", "ok"));
        }

        if (isBlank(c.getName())) return ResponseEntity.badRequest().body(Map.of("error", "Name required"));
        if (isBlank(c.getEmail()) || !EMAIL_PATTERN.matcher(c.getEmail()).matches())
            return ResponseEntity.badRequest().body(Map.of("error", "Valid email required"));
        if (isBlank(c.getMessage())) return ResponseEntity.badRequest().body(Map.of("error", "Message required"));
        if (c.getMessage().length() > MAX_TEXT_LENGTH)
            return ResponseEntity.badRequest().body(Map.of("error", "Message too long"));

        c.setName(sanitize(c.getName()));
        c.setEmail(sanitize(c.getEmail()));
        c.setMessage(sanitize(c.getMessage()));

        return ResponseEntity.ok(contactRepo.save(c));
    }

    private String validateBooking(Booking b) {
        if (isBlank(b.getName())) return "Name is required";
        if (isBlank(b.getEmail()) || !EMAIL_PATTERN.matcher(b.getEmail()).matches())
            return "Valid email is required";
        if (isBlank(b.getPhone())) return "Phone is required";
        if (isBlank(b.getDescription())) return "Description is required";
        if (b.getDescription().length() > MAX_TEXT_LENGTH) return "Description too long";
        return null;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String sanitize(String s) {
        if (s == null) return null;
        return s.replaceAll("<[^>]*>", "").trim();
    }
}
