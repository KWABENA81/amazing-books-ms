package com.edu.issuermicroservice.common;


import com.edu.issuermicroservice.model.Issuer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceResponse {
    private String customerInfo;
    private Issuer issuer;
    private Book book;
    private String issuanceStatus;
}