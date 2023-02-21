package com.edu.issuermicroservice.common;

import com.edu.issuermicroservice.model.Issuer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceRequest {
    private Book  book;
    private Issuer issuer;
}