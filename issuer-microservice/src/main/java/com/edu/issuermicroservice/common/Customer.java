package com.edu.issuermicroservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String phone;
    private String email;

    public String info() {
        String info = (!this.phone.isEmpty())
                ? this.phone : this.email;
        return info;
    }
}