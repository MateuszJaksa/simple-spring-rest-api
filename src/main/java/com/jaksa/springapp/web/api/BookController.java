package com.jaksa.springapp.web.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.jaksa.springapp.datatransfer.BookModelAssembler;
import com.jaksa.springapp.datatransfer.UserDTO;
import com.jaksa.springapp.models.Book;
import com.jaksa.springapp.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "registration";
    }
}
