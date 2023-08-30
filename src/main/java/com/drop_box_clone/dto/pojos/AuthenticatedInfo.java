package com.drop_box_clone.dto.pojos;

import lombok.Getter;

@Getter
public class AuthenticatedInfo {
    public AuthenticatedInfo(String userId){
        this.userId = userId;
    }
    private final String userId;
}
