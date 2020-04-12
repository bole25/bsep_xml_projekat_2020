package com.xml_rent_a_car.repository;

import com.xml_rent_a_car.model.SelfSignedCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfSignedCertificateRepository extends JpaRepository<SelfSignedCertificate, Long> {
}
