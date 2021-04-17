package com.jaksa.springapp;

import com.jaksa.springapp.exceptions.UserNotFoundException;
import com.jaksa.springapp.datamodels.User;
import com.jaksa.springapp.repositories.UserRepository;
import com.jaksa.springapp.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserRepository repository;
    private UserService service;

    @Before
    public void setup() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    public void loadUserByUsername_ExistingId_UserWithGivenFields() {
        Optional<User> optionalUser = Optional.of(new User("username", "password", "role"));
        when(repository.findByUsername("existing")).thenReturn(optionalUser);
        Assert.assertEquals("username", service.loadUserByUsername("existing").getUsername());
        Assert.assertEquals("password", service.loadUserByUsername("existing").getPassword());
    }

    @Test(expected = UserNotFoundException.class)
    public void loadUserByUsername_NonExistingId_UserNotFoundExceptionThrown() {
        when(repository.findByUsername("nonexisting")).thenThrow(new UserNotFoundException("nonexisting"));
        service.loadUserByUsername("nonexisting");
    }
}
