package com.benchmark.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "team_name")
    private String name;
    @OneToMany(mappedBy = "team")
    private Set<Employee> members = new HashSet<>();

}
