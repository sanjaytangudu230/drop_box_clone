package com.drop_box_clone.service;

import com.drop_box_clone.dto.requests.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
     String createUser(UserRequest user);
     String login(String userName, String password);

}
