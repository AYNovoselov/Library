package com.example.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;


import com.example.library.DTO.BookDTO;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBookById() throws Exception {
        BookDTO book = new BookDTO(1, "Test Title", "Test Author", true);

        when(bookService.getBookById(1)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"));
    }


    @Test
    public void testAddBook() throws Exception {
        BookDTO book = new BookDTO(1, "Test Title", "Test Author", true);
        when(bookService.addBook(any(BookDTO.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Title\", \"author\": \"Test Author\", \"available\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO book1 = new BookDTO(1, "Book 1", "Author 1", true);
        BookDTO book2 = new BookDTO(2, "Book 2", "Author 2", false);
        List<BookDTO> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }

    @Test
    public void testFindBookByTitle() throws Exception {

        BookDTO book = new BookDTO(1, "Test Book", "Test Author", true);
        List<BookDTO> books = Arrays.asList(book);

        when(bookService.findBookByTitle("Test Book")).thenReturn(books);

        mockMvc.perform(get("/search/title?title=Test Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }
    @Test
    public void testFindBookByAuthor() throws Exception {
        List<BookDTO> books = List.of(new BookDTO(1, "Test Title", "Test Author", true));

        when(bookService.findBookByAuthor("Test Author")).thenReturn(books);

        mockMvc.perform(get("/search/author")
                        .param("author", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].author").value("Test Author"))
                .andExpect(jsonPath("$[0].available").value(true));
    }
}