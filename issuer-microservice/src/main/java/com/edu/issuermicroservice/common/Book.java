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
    private String author;
    private String title;
    private LocalDate publishedDate = LocalDate.now();
    private Integer totalCopies;
    private Integer issuedCopies;

    //  may be ignored / go to other class?
   // private Integer issuanceId;
}