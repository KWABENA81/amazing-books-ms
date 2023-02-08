package com.edu.bookmicroservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
