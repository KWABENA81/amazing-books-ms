package com.edu.bookmicroservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Issuer {
    private Long issuerId;
    private String custInfo;
    private String isbn;
    private Integer copies;
    private Integer bookId;
    private String status;
    private String transactionId;
}
//( issuerId, customerInfo, isbn, noOfCopies, issuerStatus, issuerTransactionId)
// @Column(name = "book_id",nullable =true)    private Integer bookId;