package com.edu.bookmicroservice.common;

import com.edu.bookmicroservice.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Book book;
    private Issuer issuer;
}
