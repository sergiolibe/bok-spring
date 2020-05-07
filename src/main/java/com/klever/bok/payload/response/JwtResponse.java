package com.klever.bok.payload.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtResponse {
    @NonNull private String token;
    private String type = "Bearer";
//    @NonNull private UUID id;
    @NonNull private String name;
    @NonNull private String lastname;
    @NonNull private String username;
    @NonNull private String email;
//    @NonNull private List<String> roles;
}
