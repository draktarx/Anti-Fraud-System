package antifraud.user.service;

import antifraud.user.endpoint.request.NewAppUserRequest;
import antifraud.user.model.AppUser;
import antifraud.user.model.AppUserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserCreationService {

    private final PasswordEncoder passwordEncoder;

    public AppUserCreationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser initNewUser(NewAppUserRequest request, boolean firstUser) {
        var user = new AppUser();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(firstUser ? AppUserRole.ADMINISTRATOR : AppUserRole.MERCHANT);
        user.setLocked(!firstUser);
        return user;
    }
}
