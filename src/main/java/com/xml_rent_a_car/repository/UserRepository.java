package com.xml_rent_a_car.repository;

import com.xml_rent_a_car.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
