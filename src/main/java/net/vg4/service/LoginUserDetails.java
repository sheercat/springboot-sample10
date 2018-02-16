package net.vg4.service;

import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Data;

import net.vg4.domain.User;

@Data
public class LoginUserDetails extends org.springframework.security.core.userdetails.User {
    private final User user;

    public LoginUserDetails(User user) {
        super(user.getUsername(), user.getEncodedPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = user;
    }
}
