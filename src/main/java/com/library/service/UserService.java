package com.library.service;

import com.library.repository.UserRepository;
import com.library.model.*;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1-1 ثبت نام دانشجو
    public boolean registerStudent(String username, String password) {
        Student student = new Student(username, password);
        return userRepository.addStudent(student);
    }

    // 4-1 ثبت کارمند توسط مدیر
    public boolean registerEmployee(String username, String password) {
        Employee employee = new Employee(username, password);
        return userRepository.addEmployee(employee);
    }

    // 1-2 و 3-1 ورود به سیستم
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().authenticate(password)) {
            return user;
        }
        return Optional.empty();
    }

    // 3-2 تغییر رمز عبور
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().authenticate(oldPassword)) {
            user.get().setPassword(newPassword);
            return true;
        }
        return false;
    }

    // 3-7 فعال/غیرفعال کردن دانشجو
    public boolean activateStudent(String username, boolean active) {
        Optional<Student> student = userRepository.findStudentByUsername(username);
        if (student.isPresent()) {
            student.get().setActive(active);
            return userRepository.updateStudent(student.get());
        }
        return false;
    }

    // 2-1 مشاهده تعداد دانشجویان
    public int getStudentCount() {
        return userRepository.getStudentCount();
    }

    public Optional<Student> getStudent(String username) {
        return userRepository.findStudentByUsername(username);
    }

    public boolean updateStudent(Student student) {
        return userRepository.updateStudent(student);
    }

    public boolean updateEmployee(Employee employee) {
        return userRepository.updateEmployee(employee);
    }
}