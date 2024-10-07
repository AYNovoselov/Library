package com.example.library.service;

import com.example.library.DTO.BookDTO;
import com.example.library.DTO.UserDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public UserDTO addUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        userRepository.save(user);
        return mapToDTO(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO borrowBook(int userId, int bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        book.setAvailable(false);
        user.getBorrowedBooks().add(book);
        userRepository.save(user);
        bookRepository.save(book);

        return mapToDTO(user);
    }

    public UserDTO returnBook(int userId, int bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if (user.getBorrowedBooks().remove(book)) {
            book.setAvailable(true);
            userRepository.save(user);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book was not borrowed by this user");
        }

        return mapToDTO(user);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setBorrowedBooks(user.getBorrowedBooks().stream()
                .map(book -> {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setId(book.getId());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setAuthor(book.getAuthor());
                    bookDTO.setAvailable(book.isAvailable());
                    return bookDTO;
                }).collect(Collectors.toList())
        );
        return userDTO;
    }
}