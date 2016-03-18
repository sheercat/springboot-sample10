package net.vg4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.vg4.domain.User;
import net.vg4.repository.UserRepository;

@Service
public class LoginUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOne(username);
		if (user == null) {
			throw new UsernameNotFoundException("not found");
		}
		return new LoginUserDetails(user);
	}

}
