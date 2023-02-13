package com.edu.issuermicroservice.common;

import com.edu.issuermicroservice.model.Issuer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceRequest {
    private Book  book;
    private Issuer issuer;
}