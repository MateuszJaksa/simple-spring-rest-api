package com.jaksa.springapp.datatransfer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.jaksa.springapp.models.Book;
import com.jaksa.springapp.web.api.BookController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {

    @Override
    public EntityModel<Book> toModel(Book book) {
        return EntityModel.of(book,
            linkTo(methodOn(BookController.class).getBookById(book.getId())).withSelfRel(),
            linkTo(methodOn(BookController.class).getAll()).withRel("books"));
    }
}