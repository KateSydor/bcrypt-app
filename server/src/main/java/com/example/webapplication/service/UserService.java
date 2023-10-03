package com.example.webapplication.service;

import com.example.webapplication.entity.UserEntity;
import com.example.webapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username");
        }

        return new User(username, user.getPassword(), Collections.emptyList());
    }
}
