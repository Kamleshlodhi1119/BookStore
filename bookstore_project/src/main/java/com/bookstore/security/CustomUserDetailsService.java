package com.bookstore.security;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepo;

	public CustomUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + email));

		return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPasswordHash())
				.roles(user.getRoles().stream().map(r -> r.replace("ROLE_", "")).toArray(String[]::new)).build();
	}

}
