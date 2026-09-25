package com.javacorelab.basics.oop.exercise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class LibraryManagementExerciseTest {

    @Test
    void testAddBook() {
        // Arrange
        LibraryManagementExercise library = new LibraryManagementExercise();
        LibraryManagementExercise.Book book = new LibraryManagementExercise.Book(
                "978-0134685991", "Effective Java", "Joshua Bloch"
        );

        // Act
        library.addBook(book);
        var result = library.findBook("978-0134685991");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    void testFindBookByIsbn() {
        // Arrange
        LibraryManagementExercise library = new LibraryManagementExercise();
        LibraryManagementExercise.Book cleanCode = new LibraryManagementExercise.Book(
                "978-0132350884", "Clean Code", "Robert C. Martin"
        );
        library.addBook(cleanCode);

        // Act
        var found = library.findBook("978-0132350884");
        var notFound = library.findBook("missing-isbn");

        // Assert
        assertThat(found).contains(cleanCode);
        assertThat(notFound).isEmpty();
    }

    @Test
    void testBorrowBookSuccess() {
        // Arrange
        LibraryManagementExercise library = new LibraryManagementExercise();
        LibraryManagementExercise.Book book = new LibraryManagementExercise.Book(
                "978-1617294945", "Spring in Action", "Craig Walls"
        );
        LibraryManagementExercise.Member member = new LibraryManagementExercise.Member("M-001", "Alice");
        library.addBook(book);

        // Act
        // Assert
        assertThatCode(() -> library.borrowBook("978-1617294945", member))
                .doesNotThrowAnyException();
    }

    @Test
    void testBorrowBookFailsWhenNotAvailable() throws Exception {
        // Arrange
        LibraryManagementExercise library = new LibraryManagementExercise();
        LibraryManagementExercise.Book book = new LibraryManagementExercise.Book(
                "978-0596009205", "Head First Java", "Kathy Sierra"
        );
        LibraryManagementExercise.Member firstBorrower = new LibraryManagementExercise.Member("M-001", "Alice");
        LibraryManagementExercise.Member secondBorrower = new LibraryManagementExercise.Member("M-002", "Bob");
        library.addBook(book);
        library.borrowBook("978-0596009205", firstBorrower);

        // Act
        // Assert
        assertThatThrownBy(() -> library.borrowBook("978-0596009205", secondBorrower))
                .isInstanceOf(LibraryManagementExercise.BookNotAvailableException.class);
    }

    @Test
    void testReturnBook() throws Exception {
        // Arrange
        LibraryManagementExercise library = new LibraryManagementExercise();
        LibraryManagementExercise.Book book = new LibraryManagementExercise.Book(
                "978-1492056270", "Java Concurrency in Practice", "Brian Goetz"
        );
        LibraryManagementExercise.Member firstBorrower = new LibraryManagementExercise.Member("M-001", "Alice");
        LibraryManagementExercise.Member nextBorrower = new LibraryManagementExercise.Member("M-002", "Bob");
        library.addBook(book);
        library.borrowBook("978-1492056270", firstBorrower);

        // Act
        library.returnBook("978-1492056270");

        // Assert
        assertThatCode(() -> library.borrowBook("978-1492056270", nextBorrower))
                .doesNotThrowAnyException();
    }

    @Test
    void testEqualsHashCodeContract() {
        // Arrange
        LibraryManagementExercise.Book first = new LibraryManagementExercise.Book(
                "978-0321356680", "Title A", "Author A"
        );
        LibraryManagementExercise.Book second = new LibraryManagementExercise.Book(
                "978-0321356680", "Title B", "Author B"
        );
        LibraryManagementExercise.Book third = new LibraryManagementExercise.Book(
                "978-0134494166", "Core Java", "Cay S. Horstmann"
        );

        // Act
        // Assert
        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(third);
    }
}
