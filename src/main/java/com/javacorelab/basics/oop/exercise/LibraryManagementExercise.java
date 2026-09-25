package com.javacorelab.basics.oop.exercise;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class LibraryManagementExercise {

    private final Map<String, Book> booksByIsbn = new ConcurrentHashMap<>();
    private final Map<String, Loan> activeLoansByIsbn = new ConcurrentHashMap<>();

    public void addBook(Book book) {
        // TODO: Add validation for null input and store the book by ISBN.
        // Hint: Adding the same ISBN should not create duplicate entries.
        throw new UnsupportedOperationException("TODO: implement addBook");
    }

    public void removeBook(String isbn) {
        // TODO: Remove a book by ISBN from the catalog.
        // Hint: Decide what should happen if the ISBN does not exist.
        throw new UnsupportedOperationException("TODO: implement removeBook");
    }

    public Optional<Book> findBook(String isbn) {
        // TODO: Return Optional.of(book) when found, Optional.empty() otherwise.
        throw new UnsupportedOperationException("TODO: implement findBook");
    }

    public void borrowBook(String isbn, Member member) throws BookNotAvailableException {
        // TODO: Borrow should fail when:
        // - Book does not exist
        // - Book is already borrowed and not returned yet
        // Otherwise create a Loan with borrowDate = LocalDate.now() and returnDate = null.
        throw new UnsupportedOperationException("TODO: implement borrowBook");
    }

    public void returnBook(String isbn) {
        // TODO: Mark the active loan as returned by setting returnDate = LocalDate.now().
        // Hint: Keep Loan immutable or mutable based on your design choice.
        throw new UnsupportedOperationException("TODO: implement returnBook");
    }

    public static final class Book {
        private final String isbn;
        private final String title;
        private final String author;

        public Book(String isbn, String title, String author) {
            if (isbn == null || isbn.isBlank()) {
                throw new IllegalArgumentException("isbn must not be blank");
            }
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("title must not be blank");
            }
            if (author == null || author.isBlank()) {
                throw new IllegalArgumentException("author must not be blank");
            }
            this.isbn = isbn;
            this.title = title;
            this.author = author;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Book other)) {
                return false;
            }
            return Objects.equals(isbn, other.isbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isbn);
        }
    }

    public static final class Member {
        private final String id;
        private final String name;

        public Member(String id, String name) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("id must not be blank");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name must not be blank");
            }
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static final class Loan {
        private final Book book;
        private final Member member;
        private final LocalDate borrowDate;
        private LocalDate returnDate;

        public Loan(Book book, Member member, LocalDate borrowDate, LocalDate returnDate) {
            this.book = Objects.requireNonNull(book, "book must not be null");
            this.member = Objects.requireNonNull(member, "member must not be null");
            this.borrowDate = Objects.requireNonNull(borrowDate, "borrowDate must not be null");
            this.returnDate = returnDate;
        }

        public Book getBook() {
            return book;
        }

        public Member getMember() {
            return member;
        }

        public LocalDate getBorrowDate() {
            return borrowDate;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }
    }

    public static final class BookNotAvailableException extends Exception {
        public BookNotAvailableException(String message) {
            super(message);
        }
    }
}
