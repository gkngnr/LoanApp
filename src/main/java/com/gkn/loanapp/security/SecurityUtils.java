package com.gkn.loanapp.security;

import com.gkn.loanapp.security.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SecurityUtils {

    public static boolean hasAdminRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_" + Role.ADMIN.name()));
    }

    public static boolean hasCustomerRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_" + Role.CUSTOMER.name()));
    }
}
