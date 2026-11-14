package com.cmstechub.marketplace.config;

import com.cmstechub.marketplace.entity.*;
import com.cmstechub.marketplace.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            ServiceRepository serviceRepo,
            ProductRepository productRepo,
            PartnerRepository partnerRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // ---- Admin user ----
            if (userRepo.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("cms2026"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("✅ Admin user seeded (admin / cms2026)");
            }

            // ---- Services ----
            if (serviceRepo.count() == 0) {
                String[][] services = {
                    {"Web Development", "Development", "From KES 15,000", "🌐", "Custom websites and web applications built with modern stacks — Spring Boot, React, and PostgreSQL.",
                        "Responsive design;Custom backend with REST APIs;Database design and integration;Deployment to Netlify / Render / VPS"},
                    {"Networking Setup & Support", "Infrastructure", "From KES 8,000", "🔌", "Complete networking solutions — LAN/WAN setup, router configuration, Wi-Fi deployment, and troubleshooting.",
                        "LAN/WAN installation;Router & switch configuration;Wi-Fi optimization;Network security hardening"},
                    {"Computer Hardware Repair", "Hardware", "From KES 1,500", "🔧", "Professional computer repair and maintenance — diagnostics, hardware installation, component replacement.",
                        "Hardware diagnostics;Component replacement;PC assembly & upgrades;System optimization"},
                    {"Software Installation & Recovery", "Software", "From KES 1,000", "💻", "OS installation (Windows, Linux), driver setup, virus removal, data backup, and system recovery.",
                        "OS installation (Windows / Linux);Driver & application setup;Virus & malware removal;Data backup & recovery"},
                    {"Cybersecurity & Ethical Hacking", "Security", "From KES 10,000", "🛡️", "Vulnerability assessments, penetration testing, and security audits using Kali Linux tools.",
                        "Vulnerability scanning;Penetration testing;Security audits & reports;Firewall & network defense"},
                    {"Database Design", "Development", "From KES 6,000", "🗄️", "Design and optimization of MySQL, PostgreSQL, and Supabase databases — schema design, indexing, query optimization.",
                        "Schema design;Performance optimization;Data migration;Backup strategy"},
                    {"IT Tutoring & Training", "Training", "From KES 2,000/hr", "🎓", "One-on-one tutoring on programming (Java, Python, C), Linux basics, cybersecurity, and web development.",
                        "Java / Python / C;Linux & Kali basics;Cybersecurity fundamentals;Web dev (HTML, CSS, JS)"},
                    {"IT Consultancy", "Consulting", "Custom quote", "💡", "Strategic IT consultancy for small businesses — technology audits, digital transformation advice, IT cost optimization.",
                        "Technology audit;Digital transformation;Cost optimization;Roadmap planning"},
                };
                for (String[] s : services) {
                    Service service = new Service();
                    service.setTitle(s[0]);
                    service.setCategory(s[1]);
                    service.setPrice(s[2]);
                    service.setIcon(s[3]);
                    service.setDescription(s[4]);
                    service.setFeatures(s[5]);
                    serviceRepo.save(service);
                }
                System.out.println("✅ 8 services seeded");
            }

            // ---- Products ----
            if (productRepo.count() == 0) {
                String[][] products = {
                    {"Kali Linux Setup Guide", "eBook", "KES 500", "📘", "Complete beginner-friendly guide to installing and configuring Kali Linux — including virtual machines, dual boot, and essential tool setup."},
                    {"Spring Boot Starter Kit", "Template", "KES 1,500", "⚙️", "Production-ready Spring Boot template with JWT authentication, PostgreSQL integration, and Docker deployment."},
                    {"React Admin Dashboard", "Template", "KES 2,000", "📊", "Full-featured React admin dashboard using Vite, Tailwind, and React Router — with login, CRUD pages, and role-based access."},
                    {"Network Setup Playbook", "eBook", "KES 800", "📡", "Step-by-step guide to setting up small-business networks — cabling, router config, Wi-Fi security, and troubleshooting."},
                    {"5-Hour Network Setup Bundle", "Service Package", "KES 12,000", "📦", "Complete network installation package — site survey, cabling, router/switch setup, and one month of support."},
                    {"Cybersecurity Audit Package", "Service Package", "KES 25,000", "🔒", "Full security audit of your network or web application — includes penetration testing and remediation plan."},
                    {"Windows 11 Pro License", "Software", "KES 3,500", "🪟", "Genuine Microsoft Windows 11 Pro license key. Delivered by email within 24 hours of payment. Activation support included."},
                    {"Microsoft Office 365", "Software", "KES 4,500", "📊", "1-year Microsoft Office 365 subscription — Word, Excel, PowerPoint, Outlook, Teams. Instant license delivery."},
                    {"Windows 10 Pro License", "Software", "KES 2,800", "💾", "Genuine Windows 10 Pro license key for PCs and laptops. Full activation support and updates included."},
                    {"Antivirus License (1 Year)", "Software", "KES 1,500", "🛡️", "1-year antivirus license (Kaspersky / Bitdefender). Protects one device."},
                    {"Linux Support Package", "Software", "KES 2,000", "🐧", "Professional Ubuntu/Debian installation + setup. 1 hour remote session, driver installation, 30 days email support."},
                };
                for (String[] p : products) {
                    Product product = new Product();
                    product.setTitle(p[0]);
                    product.setCategory(p[1]);
                    product.setPrice(p[2]);
                    product.setIcon(p[3]);
                    product.setDescription(p[4]);
                    productRepo.save(product);
                }
                System.out.println("✅ 11 products seeded");
            }

            // ---- Partners ----
            if (partnerRepo.count() == 0) {
                Partner partner = new Partner();
                partner.setName("Coming Soon");
                partner.setCategory("IT Partners");
                partner.setDescription("CMS Techub is building a curated network of trusted IT professionals and companies. Partners will be listed here soon.");
                partner.setIcon("🤝");
                partnerRepo.save(partner);
                System.out.println("✅ Partner placeholder seeded");
            }
        };
    }
}
