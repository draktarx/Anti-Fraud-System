package antifraud.validator.repository;

import antifraud.validator.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StolenCardRepository extends CrudRepository<Card, Long> {

    boolean existsByNumber(String number);

    List<Card> findAllByOrderByIdAsc();

    Optional<Card> findByNumber(String number);

}
