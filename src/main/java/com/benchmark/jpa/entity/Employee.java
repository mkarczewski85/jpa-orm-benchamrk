package com.benchmark.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "salary", column = @Column(name = "emp_salary")),
            @AttributeOverride(name = "position", column = @Column(name = "emp_position")),
            @AttributeOverride(name = "employmentDate", column = @Column(name = "emp_date"))
    })
    private EmploymentDetails employmentDetails;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address", referencedColumnName = "id")
    private Address address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team")
    private Team team;
    @ManyToMany
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "id_employee"),
            inverseJoinColumns = @JoinColumn(name = "id_project"))
    private Set<Project> projects = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmploymentDetails getEmploymentDetails() {
        return employmentDetails;
    }

    public void setEmploymentDetails(EmploymentDetails employmentDetails) {
        this.employmentDetails = employmentDetails;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
