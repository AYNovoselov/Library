package com.example.library.controller;

import com.example.library.DTO.UserDTO;
import com.example.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user_methods")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Добавить пользователя", description = "Создает нового пользователя")
    @PostMapping("/users")
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }
    @Operation(summary = "Взять книгу", description = "Позволяет пользователю взять книгу")
    @PostMapping("/borrow/{userId}/{bookId}")
    public UserDTO borrowBook(@PathVariable int userId, @PathVariable int bookId) {
        UserDTO updatedUser = userService.borrowBook(userId, bookId);
        log.info("Пользователь с ID {} успешно взял книгу с ID {}", userId, bookId);
        return updatedUser;
    }

    @Operation(summary = "Вернуть книгу", description = "Позволяет пользователю вернуть книгу")
    @PostMapping("/return/{userId}/{bookId}")
    public UserDTO returnBook(@PathVariable int userId, @PathVariable int bookId) {
        return userService.returnBook(userId, bookId);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
