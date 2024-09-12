package antifraud.validator.repository;

import antifraud.validator.model.IpAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpAddressRepository extends CrudRepository<IpAddress, Long> {

    boolean existsByIp(String ip);
    
    Optional<IpAddress> findByIp(String ip);

    List<IpAddress> findAllByOrderByIdAsc();
}
