package com.benchmark.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "proj_name")
    private String name;
    @ManyToMany(mappedBy = "projects")
    private Set<Employee> assignedEmployees = new HashSet<>();

}
