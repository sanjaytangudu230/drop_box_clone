package com.drop_box_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.drop_box_clone.dto.requests.UserRequest;
import com.drop_box_clone.entites.User;
import com.drop_box_clone.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Test
    void testHashString() {
        assertEquals("36ecb4f8669133ce744c21982ba4abe2ecd7086e1dc2226ccd6f266f3a5005f8",
                UserServiceImpl.hashString("Input"));
    }

    @Test
    void testCreateUser2() {
        when(userRepository.save(Mockito.<User>any())).thenThrow(new RuntimeException("SHA-256"));

        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("vfdert3456");
        userRequest.setUsername("asdfg");
        assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userRequest));
        verify(userRepository).save(Mockito.<User>any());
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setId(UUID.randomUUID());
        user.setPassword("vfdert3456");
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setUsername("asdfg");
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user);
        assertEquals("Invalid password", userServiceImpl.login("asdfg", "vfdert3456"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    @Test
    void testLogin2() {
        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setId(UUID.randomUUID());
        user.setPassword("16811c3da3607cd843c9b420ca9e8f6cd122dfc74bd548eee1af5022b6f8daee");
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setUsername("asdfg");
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user);
        assertNull(userServiceImpl.login("asdfg", "vfdert3456"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }


    @Test
    void testLogin3() {
        when(userRepository.findByUsername(Mockito.<String>any())).thenThrow(new RuntimeException("SHA-256"));
        assertThrows(RuntimeException.class, () -> userServiceImpl.login("asdfg", "vfdert3456"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }


    @Test
    void testFetchUserById() {
        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setId(UUID.randomUUID());
        user.setPassword("vfdert3456");
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setUsername("asdfg");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        assertSame(user, userServiceImpl.FetchUserById(UUID.randomUUID()));
        verify(userRepository).findById(Mockito.<UUID>any());
    }


    @Test
    void testFetchUserById2() {
        when(userRepository.findById(Mockito.<UUID>any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> userServiceImpl.FetchUserById(UUID.randomUUID()));
        verify(userRepository).findById(Mockito.<UUID>any());
    }


    @Test
    void testFetchUserByUserName() {
        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setId(UUID.randomUUID());
        user.setPassword("vfdert3456");
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setUsername("asdfg");
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user);
        assertSame(user, userServiceImpl.FetchUserByUserName("asdfg"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }


    @Test
    void testFetchUserByUserName2() {
        when(userRepository.findByUsername(Mockito.<String>any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> userServiceImpl.FetchUserByUserName("asdfg"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }
}

