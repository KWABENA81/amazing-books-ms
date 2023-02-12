package com.edu.issuermicroservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String isbn;
    private String author;
    private String title;
    private LocalDate publishedDate = LocalDate.now();
    private Integer totalCopies;
    private Integer issuedCopies;

}