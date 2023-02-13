package com.edu.bookmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOK_TB")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(insertable = true)
    private String isbn = "Ako087DEFAULT";

    @NonNull
    @Column(insertable = true)
    private String title = "Book Title DEFAULT";

    @NonNull
    @Column(insertable = true)
    private LocalDate publishedDate = LocalDate.now();

    @NonNull
    @Column(insertable = true)
    private Integer totalCopies = 111;

    @NonNull
    @Column(insertable = true)
    private Integer issuedCopies = 1;

    @NonNull
    @Column(insertable = true)
    private String author = "Main Author";

}
