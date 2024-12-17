package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription savedSubscription = subscriptionService.addSubscription(subscription);

        assertNotNull(savedSubscription);
        assertEquals(subscription.getStartDate().plusYears(1), savedSubscription.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionService.updateSubscription(subscription);

        assertNotNull(updatedSubscription);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        Long numSubscription = 1L;
        Subscription subscription = new Subscription();
        subscription.setNumSub(numSubscription);

        when(subscriptionRepository.findById(numSubscription)).thenReturn(Optional.of(subscription));

        Subscription retrievedSubscription = subscriptionService.retrieveSubscriptionById(numSubscription);

        assertNotNull(retrievedSubscription);
        assertEquals(numSubscription, retrievedSubscription.getNumSub());
        verify(subscriptionRepository, times(1)).findById(numSubscription);
    }

    @Test
    public void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.MONTHLY;
        Set<Subscription> subscriptions = Set.of(new Subscription());

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionService.getSubscriptionByType(type);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    public void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = List.of(new Subscription());

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionService.retrieveSubscriptionsByDates(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    public void testRetrieveSubscriptions() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setEndDate(LocalDate.now());

        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");

        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(List.of(subscription));
        when(skierRepository.findBySubscription(subscription)).thenReturn(skier);

        subscriptionService.retrieveSubscriptions();

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(subscription);
    }

    @Test
    public void testShowMonthlyRecurringRevenue() {
        // Set up mock return values for different subscription types
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200f);

        // Call the method under test
        subscriptionService.showMonthlyRecurringRevenue();

        // Calculate expected revenue for validation
        Float expectedRevenue = 100f + 600f / 6 + 1200f / 12;

        // Verify each type of subscription is called exactly once
        verify(subscriptionRepository, times(2)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(2)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(2)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);

        // Additional validation if needed (assert statements can be added here)
    }
}
