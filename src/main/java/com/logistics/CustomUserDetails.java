package com.logistics;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.logistics.entity.Customer;
import com.logistics.entity.DeliveryAgent;
import com.logistics.entity.User;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private String role;

    public CustomUserDetails(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public CustomUserDetails(Customer customer) {
        this.username = customer.getEmail();
        this.password = customer.getPassword();
        this.role = customer.getRole() != null ? customer.getRole() : "ROLE_CUSTOMER";
    }

    public CustomUserDetails(DeliveryAgent agent) {
        this.username = agent.getContact();
        this.password = agent.getPassword();
        this.role = agent.getRole() != null ? agent.getRole() : "ROLE_DELIVERY";
    }

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole() != null ? user.getRole() : "ROLE_ADMIN";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authorityRole = role;
        if (authorityRole != null && !authorityRole.startsWith("ROLE_")) {
            authorityRole = "ROLE_" + authorityRole;
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityRole != null ? authorityRole : "ROLE_USER");
        return Arrays.asList(authority);
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
