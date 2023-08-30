package com.drop_box_clone.controller;

import com.drop_box_clone.constants.APIMappings;
import com.drop_box_clone.dto.requests.UserRequest;
import com.drop_box_clone.dto.responses.BaseApiResponse;
import com.drop_box_clone.exception.DropBoxException;
import com.drop_box_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (path= APIMappings.BASE_URL + APIMappings.UserEndpoints.USER)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path=APIMappings.UserEndpoints.SIGNUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewUser (@RequestBody UserRequest userRequest) {
        try {
            String token = userService.createUser(userRequest);
            return new ResponseEntity<>(BaseApiResponse.okResponse(token), HttpStatus.CREATED);
        } catch (DropBoxException exception) {
            return new ResponseEntity<>(BaseApiResponse.errorResponse(exception.getMessage()), exception.getStatus());
        }

    }

    @PostMapping(path=APIMappings.UserEndpoints.LOGIN,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        try {
            String token = userService.login(userRequest.getUsername(), userRequest.getPassword());
            return new ResponseEntity<>(BaseApiResponse.okResponse(token), HttpStatus.OK);
        } catch (DropBoxException exception) {
            return new ResponseEntity<>(BaseApiResponse.errorResponse(exception.getMessage()), exception.getStatus());
        }
    }

}
