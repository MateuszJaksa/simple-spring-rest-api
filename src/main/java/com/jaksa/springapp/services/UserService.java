package com.jaksa.springapp.services;

import com.jaksa.springapp.datatransfer.UserDTO;
import com.jaksa.springapp.exceptions.UserNotFoundException;
import com.jaksa.springapp.exceptions.UsernameExistsException;
import com.jaksa.springapp.datamodels.User;
import com.jaksa.springapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public User registerNewUserAccount(UserDTO accountDto) throws UsernameExistsException {
        if (usernameExists(accountDto.getUsername())) {
            throw new UsernameExistsException(accountDto.getUsername());
        }
        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
