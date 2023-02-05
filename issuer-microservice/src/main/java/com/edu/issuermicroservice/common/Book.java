package com.edu.issuermicroservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private LocalDate publishedDate = LocalDate.now();
    private Integer issuedId;
    private Integer totalCopies;
}
