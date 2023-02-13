package com.edu.bookmicroservice.common;

import com.edu.bookmicroservice.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Book book;
    private String customerInfo;
    private String issuanceStatus;
    private String issuanceTransactionId;
    private String message;
}
