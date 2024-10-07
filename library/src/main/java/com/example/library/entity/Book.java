package com.example.library.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "avaiable")
    boolean available = true;

}
