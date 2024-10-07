package com.example.library.controller;

import com.example.library.DTO.BookDTO;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "book_methods")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг")
    @GetMapping("/books")
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по указанному ID")
    @GetMapping("/books/{id}")
    public BookDTO getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Добавить книгу", description = "Создает новую книгу")
    @PostMapping("/books")
    public BookDTO addBook(@RequestBody BookDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по ID")
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }

    @Operation(summary = "Найти книги по названию", description = "Возвращает список книг по указанному названию")
    @GetMapping("/search/title")
    public List<BookDTO> findBookByTitle(@RequestParam String title) {
        return bookService.findBookByTitle(title);
    }
    @Operation(summary = "Найти книги по автору", description = "Возвращает список книг по указанному автору")
    @GetMapping("/search/author")
    public List<BookDTO> findBookByAuthor(@RequestParam String author) {
        return bookService.findBookByAuthor(author);
    }
}
