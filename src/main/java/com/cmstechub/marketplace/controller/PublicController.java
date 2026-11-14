package com.cmstechub.marketplace.controller;

import com.cmstechub.marketplace.entity.Booking;
import com.cmstechub.marketplace.entity.Contact;
import com.cmstechub.marketplace.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PublicController {

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
    public Booking createBooking(@RequestBody Booking b) {
        return bookingRepo.save(b);
    }

    @PostMapping("/contact")
    public Contact createContact(@RequestBody Contact c) {
        return contactRepo.save(c);
    }
}
