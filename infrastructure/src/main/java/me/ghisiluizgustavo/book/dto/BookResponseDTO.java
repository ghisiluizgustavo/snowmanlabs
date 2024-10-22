package me.ghisiluizgustavo.book.dto;

import me.ghisiluizgustavo.book.model.Book;

public record BookResponseDTO(
        String id,
        String isbn,
        String name,
        String author
) implements IBookCreate {
    public BookResponseDTO(Book book) {
        this(
                book.id().value(),
                book.isbn().value(),
                book.name(),
                book.author()
        );

    }
}