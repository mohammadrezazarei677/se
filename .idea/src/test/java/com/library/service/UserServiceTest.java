package com.library.service;

import com.library.repository.UserRepository;
import com.library.model.Student;
import com.library.model.Employee;
import com.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterStudent_Success() {
        boolean result = userService.registerStudent("student1", "password123");
        assertTrue(result);

        var student = userService.getStudent("student1");
        assertTrue(student.isPresent());
        assertEquals("student1", student.get().getUsername());
    }

    @Test
    void testRegisterStudent_DuplicateUsername() {
        userService.registerStudent("student1", "password123");
        boolean result = userService.registerStudent("student1", "password456");
        assertFalse(result);
    }

    @Test
    void testLogin_Success() {
        userService.registerStudent("student1", "password123");

        var user = userService.login("student1", "password123");
        assertTrue(user.isPresent());
        assertTrue(user.get() instanceof Student);
    }

    @Test
    void testLogin_WrongPassword() {
        userService.registerStudent("student1", "password123");

        var user = userService.login("student1", "wrongpassword");
        assertFalse(user.isPresent());
    }

    @Test
    void testChangePassword_Success() {
        userService.registerStudent("student1", "oldpassword");

        boolean result = userService.changePassword("student1", "oldpassword", "newpassword");
        assertTrue(result);

        var user = userService.login("student1", "newpassword");
        assertTrue(user.isPresent());
    }

    @Test
    void testActivateStudent() {
        userService.registerStudent("student1", "password123");

        boolean result = userService.activateStudent("student1", false);
        assertTrue(result);

        var student = userService.getStudent("student1");
        assertTrue(student.isPresent());
        assertFalse(student.get().isActive());
    }
}