package com.xml_rent_a_car.repository;

import com.xml_rent_a_car.model.EndEntityCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndEntityCertificateRepository extends JpaRepository<EndEntityCertificate, Long> {
}
