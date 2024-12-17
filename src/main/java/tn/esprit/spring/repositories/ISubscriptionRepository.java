package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISubscriptionRepository extends CrudRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.typeSub = :typeS ORDER BY s.startDate")
    Set<Subscription> findByTypeSubOrderByStartDateAsc(@Param("typeS") TypeSubscription typeSub);

    List<Subscription> getSubscriptionsByStartDateBetween(LocalDate date1, LocalDate date2);

    @Query("SELECT DISTINCT s FROM Subscription s WHERE s.endDate <= CURRENT_DATE ORDER BY s.endDate")
    List<Subscription> findDistinctOrderByEndDateAsc();

    @Query("SELECT (SUM(s.price) / COUNT(s)) FROM Subscription s WHERE s.typeSub = :typeSub")
    Float recurringRevenueByTypeSubEquals(@Param("typeSub") TypeSubscription typeSub);
}
