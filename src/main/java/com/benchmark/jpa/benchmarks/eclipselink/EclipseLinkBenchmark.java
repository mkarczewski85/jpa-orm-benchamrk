package com.benchmark.jpa.benchmarks.eclipselink;

import com.benchmark.jpa.entity.Employee;
import com.benchmark.jpa.tools.Environment;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class EclipseLinkBenchmark {

    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @Setup
    public void setup(Environment env) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("eclipse-persistence-unit");
        entityManager = emf.createEntityManager();
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void measureSelectAllEmployees(Environment env, Blackhole blackhole) {
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        query.select(root);
        List<Employee> objects = entityManager.createQuery(query)
                .setMaxResults(env.getTotalRows())
                .getResultList();
        blackhole.consume(objects);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void measureSelectEmployeesWithAddress(Environment env, Blackhole blackhole) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        root.fetch("address", JoinType.INNER);
        List<Employee> objects = entityManager.createQuery(criteriaQuery)
                .setMaxResults(env.getTotalRows())
                .getResultList();
        blackhole.consume(objects);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void measureSelectEmployeesByProjectName(Environment env, Blackhole blackhole) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        Join<Object, Object> project = root.join("projects");
        ParameterExpression<String> projectName = criteriaBuilder.parameter(String.class);
        criteriaQuery.where(criteriaBuilder.like(project.get("name"), projectName));
        String name = "%" + (env.getTotalRows() / 10);
        List<Employee> objects = entityManager.createQuery(criteriaQuery)
                .setParameter(projectName, name)
                .getResultList();
        blackhole.consume(objects);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void measureSelectEmployeesByTeamName(Environment env, Blackhole blackhole) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        Join<Object, Object> project = root.join("team");
        ParameterExpression<String> teamName = criteriaBuilder.parameter(String.class);
        criteriaQuery.where(criteriaBuilder.like(project.get("name"), teamName));
        String name = "%" + (env.getTotalRows() / 10);
        List<Employee> objects = entityManager.createQuery(criteriaQuery)
                .setParameter(teamName, name)
                .getResultList();
        blackhole.consume(objects);
    }

}
