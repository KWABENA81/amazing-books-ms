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

    public String custInfo() {
        StringBuilder info = new StringBuilder();
        if (util(this.phone) && util(this.email)) {
            info.append( this.phone).append("#").append(this.email);
        } else if (util(this.phone)) {
            info.append( this.phone);
        } else if (util(this.email)) {
            info.append( this.email);
        }
        return info.toString();
    }

    private boolean util(String string) {
        return string != null && !string.isEmpty();
    }
}