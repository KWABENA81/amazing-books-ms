package com.edu.issuermicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ISSUER_TB")
public class Issuer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isbn", nullable = false, length = 30)
    private String isbn;

    @Column(name = "cust_id", length = 30)
    private String custId;

    @Column(name = "no_of_copies")
    private Integer noOfCopies;

}