package com.cmstechub.marketplace.repository;

import com.cmstechub.marketplace.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
