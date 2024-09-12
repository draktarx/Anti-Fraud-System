package antifraud.user.service;

import antifraud.user.endpoint.request.ChangeUserAccessRequest;
import antifraud.user.endpoint.request.ChangeUserRoleRequest;
import antifraud.user.endpoint.request.NewAppUserRequest;
import antifraud.user.model.AppUser;

import java.util.List;

public interface AppUserService {

    AppUser register(NewAppUserRequest request);

    <T> List<T> list(Class<T> type);

    void delete(String username);

    AppUser changeRole(ChangeUserRoleRequest request);

    AppUser changeAccess(ChangeUserAccessRequest request);

    Boolean userIsLocked(String username);
}
