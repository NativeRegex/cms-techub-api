package com.cmstechub.marketplace.controller;

import com.cmstechub.marketplace.entity.*;
import com.cmstechub.marketplace.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final ServiceRepository serviceRepo;
    private final ProductRepository productRepo;
    private final PartnerRepository partnerRepo;
    private final BookingRepository bookingRepo;
    private final ContactRepository contactRepo;

    public AdminController(ServiceRepository serviceRepo, ProductRepository productRepo,
                           PartnerRepository partnerRepo, BookingRepository bookingRepo,
                           ContactRepository contactRepo) {
        this.serviceRepo = serviceRepo;
        this.productRepo = productRepo;
        this.partnerRepo = partnerRepo;
        this.bookingRepo = bookingRepo;
        this.contactRepo = contactRepo;
    }

    // Bookings
    @GetMapping("/bookings")
    public List<Booking> getBookings() {
        return bookingRepo.findAll();
    }

    @PatchMapping("/bookings/{id}/status")
    public Booking updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Booking b = bookingRepo.findById(id).orElseThrow();
        b.setStatus(body.get("status"));
        return bookingRepo.save(b);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    // Contacts
    @GetMapping("/contacts")
    public List<Contact> getContacts() {
        return contactRepo.findAll();
    }

    @PatchMapping("/contacts/{id}/status")
    public Contact updateContactStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Contact c = contactRepo.findById(id).orElseThrow();
        c.setStatus(body.get("status"));
        return contactRepo.save(c);
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    // Services
    @PostMapping("/services")
    public Service createService(@RequestBody Service s) { return serviceRepo.save(s); }

    @PutMapping("/services/{id}")
    public Service updateService(@PathVariable Long id, @RequestBody Service s) {
        s.setId(id); return serviceRepo.save(s);
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        serviceRepo.deleteById(id); return ResponseEntity.ok(Map.of("deleted", id));
    }

    // Products
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product p) { return productRepo.save(p); }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product p) {
        p.setId(id); return productRepo.save(p);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id); return ResponseEntity.ok(Map.of("deleted", id));
    }

    // Partners
    @PostMapping("/partners")
    public Partner createPartner(@RequestBody Partner p) { return partnerRepo.save(p); }

    @PutMapping("/partners/{id}")
    public Partner updatePartner(@PathVariable Long id, @RequestBody Partner p) {
        p.setId(id); return partnerRepo.save(p);
    }

    @DeleteMapping("/partners/{id}")
    public ResponseEntity<?> deletePartner(@PathVariable Long id) {
        partnerRepo.deleteById(id); return ResponseEntity.ok(Map.of("deleted", id));
    }
}
