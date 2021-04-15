package com.jaksa.springapp.services;

import com.jaksa.springapp.models.Book;
import com.jaksa.springapp.exceptions.BookNotFoundException;
import com.jaksa.springapp.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository repository;

    public Book findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }
    public Book findByTitle(String title) { return repository.findByTitle(title).orElseThrow(() -> new BookNotFoundException(title));}

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book update(Long id, Book newBook) {
        return repository.findById(id)
            .map(book -> {
                book.setTitle(newBook.getTitle());
                book.setAuthor(newBook.getAuthor());
                return repository.save(book);
            })
            .orElseGet(() -> {
                newBook.setId(id);
                return repository.save(newBook);
            });
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
