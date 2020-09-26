package com.benchmark.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "street")
    private String street;
    @Column(name = "number")
    private Integer number;
    @Column(name = "postal_code")
    private String postalCode;
    @OneToOne(mappedBy = "address")
    private Employee employee;

}
