package com.xml_rent_a_car.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "test_table")
public class TestClass {

    @Id
    private Long id;

    @Column(name = "test_string")
    private String randomString;

}
