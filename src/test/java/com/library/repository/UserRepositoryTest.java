package com.library.repository;

import com.library.model.Student;
import com.library.model.Employee;
import com.library.model.Manager;
import com.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class UserRepositoryTest {
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void testAddStudent_Success() {
        // Given
        Student student = new Student("student1", "password123");

        // When
        boolean result = userRepository.addStudent(student);

        // Then
        assertTrue(result);
        Optional<Student> foundStudent = userRepository.findStudentByUsername("student1");
        assertTrue(foundStudent.isPresent());
        assertEquals("student1", foundStudent.get().getUsername());
    }

    @Test
    void testAddStudent_DuplicateUsername() {
        // Given
        Student student1 = new Student("student1", "password123");
        Student student2 = new Student("student1", "password456");

        // When
        boolean firstResult = userRepository.addStudent(student1);
        boolean secondResult = userRepository.addStudent(student2);

        // Then
        assertTrue(firstResult);
        assertFalse(secondResult);
    }

    @Test
    void testAddEmployee_Success() {
        // Given
        Employee employee = new Employee("emp1", "password123");

        // When
        boolean result = userRepository.addEmployee(employee);

        // Then
        assertTrue(result);
        Optional<Employee> foundEmployee = userRepository.findEmployeeByUsername("emp1");
        assertTrue(foundEmployee.isPresent());
        assertEquals("emp1", foundEmployee.get().getUsername());
    }

    @Test
    void testFindByUsername_Student() {
        // Given
        Student student = new Student("student1", "password123");
        userRepository.addStudent(student);

        // When
        Optional<User> foundUser = userRepository.findByUsername("student1");

        // Then
        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get() instanceof Student);
        assertEquals("student1", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_Employee() {
        // Given
        Employee employee = new Employee("emp1", "password123");
        userRepository.addEmployee(employee);

        // When
        Optional<User> foundUser = userRepository.findByUsername("emp1");

        // Then
        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get() instanceof Employee);
        assertEquals("emp1", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindStudentByUsername_NotFound() {
        // When
        Optional<Student> foundStudent = userRepository.findStudentByUsername("nonexistent");

        // Then
        assertFalse(foundStudent.isPresent());
    }

    @Test
    void testFindEmployeeByUsername_NotFound() {
        // When
        Optional<Employee> foundEmployee = userRepository.findEmployeeByUsername("nonexistent");

        // Then
        assertFalse(foundEmployee.isPresent());
    }

    @Test
    void testGetAllStudents() {
        // Given
        Student student1 = new Student("student1", "pass1");
        Student student2 = new Student("student2", "pass2");
        userRepository.addStudent(student1);
        userRepository.addStudent(student2);

        // When
        var students = userRepository.getAllStudents();

        // Then
        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getUsername().equals("student1")));
        assertTrue(students.stream().anyMatch(s -> s.getUsername().equals("student2")));
    }

    @Test
    void testGetAllEmployees() {
        // Given
        Employee emp1 = new Employee("emp1", "pass1");
        Employee emp2 = new Employee("emp2", "pass2");
        userRepository.addEmployee(emp1);
        userRepository.addEmployee(emp2);

        // When
        var employees = userRepository.getAllEmployees();

        // Then
        assertEquals(2, employees.size());
        assertTrue(employees.stream().anyMatch(e -> e.getUsername().equals("emp1")));
        assertTrue(employees.stream().anyMatch(e -> e.getUsername().equals("emp2")));
    }

    @Test
    void testGetStudentCount() {
        // Given
        Student student1 = new Student("student1", "pass1");
        Student student2 = new Student("student2", "pass2");
        userRepository.addStudent(student1);
        userRepository.addStudent(student2);

        // When
        int count = userRepository.getStudentCount();

        // Then
        assertEquals(2, count);
    }

    @Test
    void testUpdateStudent() {
        // Given
        Student student = new Student("student1", "password");
        userRepository.addStudent(student);
        student.setActive(false);
        student.setTotalBorrows(5);
        student.setDelayedReturns(2);
        student.setNotReturnedBooks(1);

        // When
        boolean result = userRepository.updateStudent(student);

        // Then
        assertTrue(result);
        Optional<Student> updatedStudent = userRepository.findStudentByUsername("student1");
        assertTrue(updatedStudent.isPresent());
        assertFalse(updatedStudent.get().isActive());
        assertEquals(5, updatedStudent.get().getTotalBorrows());
        assertEquals(2, updatedStudent.get().getDelayedReturns());
        assertEquals(1, updatedStudent.get().getNotReturnedBooks());
    }

    @Test
    void testUpdateEmployee() {
        // Given
        Employee employee = new Employee("emp1", "password");
        userRepository.addEmployee(employee);
        employee.setBooksRegistered(10);
        employee.setBooksLent(20);
        employee.setBooksReceived(15);

        // When
        boolean result = userRepository.updateEmployee(employee);

        // Then
        assertTrue(result);
        Optional<Employee> updatedEmployee = userRepository.findEmployeeByUsername("emp1");
        assertTrue(updatedEmployee.isPresent());
        assertEquals(10, updatedEmployee.get().getBooksRegistered());
        assertEquals(20, updatedEmployee.get().getBooksLent());
        assertEquals(15, updatedEmployee.get().getBooksReceived());
    }

    @Test
    void testDefaultManagerExists() {
        // When
        Optional<User> admin = userRepository.findByUsername("admin");

        // Then
        assertTrue(admin.isPresent());
        assertTrue(admin.get() instanceof Manager);
        assertEquals("admin", admin.get().getUsername());
    }
}