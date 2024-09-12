package antifraud.user.repository;

import antifraud.user.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findAppUserByUsername(String username);

    <T> List<T> findAllByOrderByIdAsc(Class<T> type);

    Boolean existsByUsername(String username);

}

