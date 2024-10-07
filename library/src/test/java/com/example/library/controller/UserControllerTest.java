package com.example.library.controller;

import com.example.library.DTO.BookDTO;
import com.example.library.DTO.UserDTO;
import com.example.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<BookDTO> borrowedBooks = List.of(new BookDTO(1, "Test Title", "Test Author", true));
        List<UserDTO> users = List.of(new UserDTO(1, "John Doe", borrowedBooks));

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("John Doe"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].borrowedBooks[0].title").value("Test Title"));
    }

    @Test
    public void testAddUser() throws Exception {
        List<BookDTO> borrowedBooks = List.of(new BookDTO(1, "Test Title", "Test Author", true));
        UserDTO user = new UserDTO(1, "John Doe", borrowedBooks);

        when(userService.addUser(any(UserDTO.class))).thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.borrowedBooks[0].title").value("Test Title"));
    }

    @Test
    public void testBorrowBook() throws Exception {
        List<BookDTO> borrowedBooks = List.of(new BookDTO(1, "Test Title", "Test Author", true));
        UserDTO user = new UserDTO(1, "John Doe", borrowedBooks);

        when(userService.borrowBook(1, 1)).thenReturn(user);

        mockMvc.perform(post("/borrow/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.borrowedBooks[0].title").value("Test Title"));
    }

    @Test
    public void testReturnBook() throws Exception {
        List<BookDTO> borrowedBooks = List.of(new BookDTO(1, "Test Title", "Test Author", true));
        UserDTO user = new UserDTO(1, "John Doe", borrowedBooks);

        when(userService.returnBook(1, 1)).thenReturn(user);

        mockMvc.perform(post("/return/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.borrowedBooks[0].title").value("Test Title"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}