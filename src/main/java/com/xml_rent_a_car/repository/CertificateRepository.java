package com.xml_rent_a_car.repository;

import com.xml_rent_a_car.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Query(value = "Select c from  Certificate c where c.type = 1 and c.valid = true", nativeQuery = true)
    Set<Certificate>  getValidIntermediate();

    @Query(value = "Select c from  Certificate c where c.type = 0 and c.valid = true", nativeQuery = true)
    Set<Certificate>  getValidSelfSigned();

    Certificate getByAlias(String alias);
}
