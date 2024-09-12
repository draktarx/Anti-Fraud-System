package antifraud.user.service;

import antifraud.user.endpoint.request.ChangeUserAccessRequest;
import antifraud.user.endpoint.request.ChangeUserRoleRequest;
import antifraud.user.endpoint.request.NewAppUserRequest;
import antifraud.user.exception.AppUserAlreadyExists;
import antifraud.user.exception.AppUserChangeAccessBadRequestException;
import antifraud.user.exception.AppUserChangeRoleBadRequestException;
import antifraud.user.model.AppUser;
import antifraud.user.model.AppUserRole;
import antifraud.user.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AppUserServiceImpl implements AppUserService {

    private static final Set<AppUserRole> VALID_ROLES = Set.of(AppUserRole.SUPPORT,
                                                               AppUserRole.MERCHANT);

    private final AppUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final AppUserCreationService creationService;

    public AppUserServiceImpl(AppUserRepository repository,
                              PasswordEncoder passwordEncoder,
                              AppUserCreationService creationService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.creationService = creationService;
    }

    @Override
    public AppUser register(NewAppUserRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new AppUserAlreadyExists(request.username());
        }
        var firstUser = repository.count() == 0;
        var user = creationService.initNewUser(request, firstUser);
        return repository.save(user);
    }

    @Override
    public <T> List<T> list(Class<T> type) {
        return repository.findAllByOrderByIdAsc(type);
    }

    @Override
    public void delete(String username) {
        var user = repository
                .findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        repository.delete(user);
    }

    @Override
    public AppUser changeRole(ChangeUserRoleRequest request) {
        var user = repository
                .findAppUserByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));
        validateRoleChange(request.role(), user.getRole());
        user.setRole(request.role());
        return repository.save(user);
    }

    @Override
    public AppUser changeAccess(ChangeUserAccessRequest request) {
        var user = repository
                .findAppUserByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));
        if (user.getRole() == AppUserRole.ADMINISTRATOR) {
            throw new AppUserChangeAccessBadRequestException(HttpStatus.BAD_REQUEST,
                                                             "Cannot change access for administrator");
        }
        user.setLocked(request.operation().getValue());
        return repository.save(user);
    }

    @Override
    public Boolean userIsLocked(String username) {
        var user = repository
                .findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getLocked();
    }

    private void validateRoleChange(AppUserRole newRole, AppUserRole currentRole) {
        if (!VALID_ROLES.contains(newRole)) {
            throw new AppUserChangeRoleBadRequestException(HttpStatus.BAD_REQUEST, "Invalid role");
        }
        if (currentRole == newRole) {
            throw new AppUserChangeRoleBadRequestException(HttpStatus.CONFLICT, "Same role");
        }
    }

}
