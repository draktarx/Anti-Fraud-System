package antifraud.validator.repository;

import antifraud.validator.endpoint.res.TransactionDto;
import antifraud.validator.model.enums.Region;
import antifraud.validator.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT COUNT(DISTINCT t.region) > 2 FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :startTime AND :endTime AND t.region <> :currentRegion")
    boolean existsTransactionsFromMoreThanTwoRegions(@Param("number") String number,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("currentRegion") Region currentRegion);

    @Query("SELECT COUNT(DISTINCT t.ip) > 2 FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :startTime AND :endTime AND t.ip <> :currentIp")
    boolean existsTransactionsFromMoreThanTwoUniqueIps(@Param("number") String number,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime,
                                                       @Param("currentIp") String currentIp);

    @Query("SELECT COUNT(DISTINCT t.region) = 2 FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :startTime AND :endTime AND t.region <> :currentRegion")
    boolean existsTransactionsFromExactlyTwoDistinctRegions(@Param("number") String number,
                                                            @Param("startTime")
                                                            LocalDateTime startTime,
                                                            @Param("endTime") LocalDateTime endTime,
                                                            @Param("currentRegion")
                                                            Region currentRegion);

    @Query("SELECT COUNT(DISTINCT t.ip) = 2 FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :startTime AND :endTime AND t.ip <> :currentIp")
    boolean existsTransactionsFromExactlyTwoUniqueIps(@Param("number") String number,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("currentIp") String currentIp);

    @Override
    Optional<Transaction> findById(Long id);

    @Query("SELECT t FROM Transaction t ORDER BY t.transactionId ASC")
    List<Transaction> findAllTransactionsSortedByIdAsc();

    @Query("SELECT t FROM Transaction t WHERE t.number = :number ORDER BY t.transactionId ASC")
    List<Transaction> findAllTransactionsByNumberSortedByIdAsc(@Param("number") String number);

    boolean existsByNumber(String number);
}
