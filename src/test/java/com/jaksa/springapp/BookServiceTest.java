package com.jaksa.springapp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jaksa.springapp.exceptions.BookNotFoundException;
import com.jaksa.springapp.datamodels.Book;
import com.jaksa.springapp.repositories.BookRepository;
import com.jaksa.springapp.services.BookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

public class BookServiceTest {
    private BookRepository repository;
    private BookService service;

    public ArrayList<Book> getMockBooks() {
        ArrayList<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("Dune", "Herbert"));
        mockBooks.add(new Book("Serotonin", "Houellebecq"));
        return mockBooks;
    }

    @Before
    public void setup() {
        repository = mock(BookRepository.class);
        service = new BookService(repository);
    }

    @Test
    public void findById_ExistingId_BookWithGivenFields() {
        Optional<Book> optionalBook = Optional.of(new Book("title", "author"));
        when(repository.findByTitle("existing")).thenReturn(optionalBook);
        Assert.assertEquals("title", service.findByTitle("existing").getTitle());
        Assert.assertEquals("author", service.findByTitle("existing").getAuthor());
    }

    @Test(expected = BookNotFoundException.class)
    public void findById_NonExistingId_BookNotFoundExceptionThrown() {
        when(repository.findByTitle("nonexisting")).thenThrow(new BookNotFoundException("nonexisting"));
        service.findByTitle("nonexisting");
    }

    @Test
    public void findAll_NonEmptyRepo_ListOf2GivenBooks() {
        when(repository.findAll()).thenReturn(getMockBooks());
        Assert.assertEquals(getMockBooks(), service.findAll());
    }
}
