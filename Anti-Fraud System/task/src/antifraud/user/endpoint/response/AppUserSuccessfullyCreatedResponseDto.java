package antifraud.user.endpoint.response;

import antifraud.user.model.AppUser;

public record AppUserSuccessfullyCreatedResponseDto(
        Long id,
        String name,
        String username,
        String role) {

    public AppUserSuccessfullyCreatedResponseDto(AppUser registeredUser) {
        this(registeredUser.getId(),
             registeredUser.getName(),
             registeredUser.getUsername(),
             registeredUser.getRole().name());
    }

}
