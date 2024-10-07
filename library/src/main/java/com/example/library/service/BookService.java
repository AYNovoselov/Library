package com.example.library.service;

import com.example.library.DTO.BookDTO;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setAvailable(true);
        bookRepository.save(book);
        return mapToDTO(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public BookDTO getBookById(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToDTO(book);
    }

    public List<BookDTO> findBookByTitle(String title) {
        return bookRepository.findByTitle(title).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findBookByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO mapToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setAvailable(book.isAvailable());
        return bookDTO;
    }
}
