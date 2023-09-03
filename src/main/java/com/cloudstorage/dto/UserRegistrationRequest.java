package com.cloudstorage.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String email;
}
