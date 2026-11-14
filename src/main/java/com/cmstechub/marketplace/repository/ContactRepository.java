package com.cmstechub.marketplace.repository;

import com.cmstechub.marketplace.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
