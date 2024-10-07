package com.example.library.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDTO {
    int id;
    String title;
    String author;
    boolean available;
}
