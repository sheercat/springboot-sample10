package net.vg4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.val;
// import net.vg4.domain.User;
import net.vg4.repository.UserRepository;

@Service
@val
public class LoginUserDetailsService implements UserDetailsService {
    final UserRepository userRepository;

    @Autowired
    public LoginUserDetailsService(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = userRepository.findOne(username);
        if (user == null) {
            throw new UsernameNotFoundException("not found");
        }
        return new LoginUserDetails(user);
    }

}
