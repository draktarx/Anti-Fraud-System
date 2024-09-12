package antifraud.user.repository.projections;

import antifraud.user.model.AppUser;
import antifraud.user.model.AppUserRole;

public record AppUserBasicInfo(Long id, String name, String username, AppUserRole role) {

    public AppUserBasicInfo(Long id, String name, String username, AppUserRole role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
    }

}
