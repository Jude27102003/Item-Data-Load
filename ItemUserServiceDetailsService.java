package com.item.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.item.entity.User;
import com.item.repository.UserRepository;

//Custom implementation of Spring Security's UserDetailsService.
//Used to load user-specific data during authentication.
@Service
public class ItemUserServiceDetailsService implements UserDetailsService {

	//Repository to fetch user data from the database
	private UserRepository userRepository;

	//Constructor injection for UserRepository
	public ItemUserServiceDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	//Loads user details by username or email for authentication
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsernameOrEmail(username, username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}

}
