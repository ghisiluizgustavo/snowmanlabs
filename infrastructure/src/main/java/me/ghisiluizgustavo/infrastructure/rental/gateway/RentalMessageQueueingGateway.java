package me.ghisiluizgustavo.infrastructure.rental.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ghisiluizgustavo.domain.book.model.Book;
import me.ghisiluizgustavo.domain.customer.model.Customer;
import me.ghisiluizgustavo.domain.rental.gateway.RentalNotificationGateway;
import me.ghisiluizgustavo.infrastructure.rental.event.model.RentalEvent;
import me.ghisiluizgustavo.infrastructure.rental.event.model.StatusEventRental;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RentalMessageQueueingGateway implements RentalNotificationGateway {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void notifyRental(List<Book> books, Customer customer) {
        RentalEvent event = new RentalEvent(this, books, customer, StatusEventRental.RENTED);

        log.info("Sending event to message queue" +
                " books=" + books +
                " customer=" + customer.name() +
                " bookStatus=" + StatusEventRental.RENTED
        );

        eventPublisher.publishEvent(event);
    }

}
