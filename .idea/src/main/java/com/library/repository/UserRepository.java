package com.library.repository;

import com.library.model.*;
import java.util.*;

public class UserRepository {
    private Map<String, User> users;
    private Map<String, Student> students;
    private Map<String, Employee> employees;
    private Map<String, Manager> managers;

    public UserRepository() {
        this.users = new HashMap<>();
        this.students = new HashMap<>();
        this.employees = new HashMap<>();
        this.managers = new HashMap<>();

        // ایجاد مدیر پیش‌فرض
        Manager defaultManager = new Manager("admin", "admin123");
        managers.put("admin", defaultManager);
        users.put("admin", defaultManager);
    }

    public boolean addStudent(Student student) {
        if (users.containsKey(student.getUsername())) {
            return false;
        }
        students.put(student.getUsername(), student);
        users.put(student.getUsername(), student);
        return true;
    }

    public boolean addEmployee(Employee employee) {
        if (users.containsKey(employee.getUsername())) {
            return false;
        }
        employees.put(employee.getUsername(), employee);
        users.put(employee.getUsername(), employee);
        return true;
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Optional<Student> findStudentByUsername(String username) {
        return Optional.ofNullable(students.get(username));
    }

    public Optional<Employee> findEmployeeByUsername(String username) {
        return Optional.ofNullable(employees.get(username));
    }

    public Optional<Manager> findManagerByUsername(String username) {
        return Optional.ofNullable(managers.get(username));
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public int getStudentCount() {
        return students.size();
    }

    public boolean updateStudent(Student student) {
        if (students.containsKey(student.getUsername())) {
            students.put(student.getUsername(), student);
            users.put(student.getUsername(), student);
            return true;
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        if (employees.containsKey(employee.getUsername())) {
            employees.put(employee.getUsername(), employee);
            users.put(employee.getUsername(), employee);
            return true;
        }
        return false;
    }
}