package com.jaksa.springapp.web.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.jaksa.springapp.datatransfer.BookModelAssembler;
import com.jaksa.springapp.datamodels.Book;
import com.jaksa.springapp.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookModelAssembler assembler;

    @GetMapping("/books")
    public CollectionModel<EntityModel<Book>> getAll() {
        List<EntityModel<Book>> books = bookService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(books, linkTo(methodOn(BookController.class).getAll()).withSelfRel());
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> getBookById(@PathVariable Long id) {
        return assembler.toModel(bookService.findById(id));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return new ResponseEntity<>(bookService.update(id, book), HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
