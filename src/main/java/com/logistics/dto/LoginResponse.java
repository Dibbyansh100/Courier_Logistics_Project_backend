package com.logistics.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Long id;
    private String username;
    private String name;
    private String role; // "ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_DELIVERY"
    private String tokenType;
}
