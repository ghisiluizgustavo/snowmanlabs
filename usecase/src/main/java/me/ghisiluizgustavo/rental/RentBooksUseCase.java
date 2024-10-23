package me.ghisiluizgustavo.rental;

import lombok.RequiredArgsConstructor;
import me.ghisiluizgustavo.book.gateway.BookPersistenceGateway;
import me.ghisiluizgustavo.book.model.Book;
import me.ghisiluizgustavo.customer.gateway.CustomerPersistenceGateway;
import me.ghisiluizgustavo.customer.model.Customer;
import me.ghisiluizgustavo.rental.dto.IRentBooks;
import me.ghisiluizgustavo.rental.gateway.RentalNotificationGateway;
import me.ghisiluizgustavo.rental.gateway.RentalPersistenceGateway;
import me.ghisiluizgustavo.rental.model.Rental;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentBooksUseCase {

    private final RentalPersistenceGateway rentalPersistenceGateway;
    private final CustomerPersistenceGateway customerPersistenceGateway;
    private final BookPersistenceGateway bookPersistenceGateway;
    private final RentalNotificationGateway rentalNotificationGateway;

    public List<Rental> execute(IRentBooks rentBooks){
        Customer customer = customerPersistenceGateway.findById(rentBooks.customerId());
        List<Book> books = rentBooks.booksIds().stream().map(bookPersistenceGateway::findById).toList();

        List<Rental> rentals = new ArrayList<>();
        for (Book book : books){
            rentals.add(new Rental(null, customer, book, LocalDateTime.now(), LocalDateTime.now().plusDays(20)));
        }

        List<Rental> rentalsSaved = rentalPersistenceGateway.rentBooks(rentals);

        books.forEach(book -> bookPersistenceGateway.makeUnavailable(book.id().value()));
        rentalNotificationGateway.notifyRental(books, customer);

        return rentalsSaved;
    }

}