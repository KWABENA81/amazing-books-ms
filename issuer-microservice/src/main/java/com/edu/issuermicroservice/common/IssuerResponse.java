package com.edu.issuermicroservice.common;


import com.edu.issuermicroservice.model.Issuer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuerResponse {
    private String customerInfo;
    private Issuer issuer;
    private List<Book> books;
}