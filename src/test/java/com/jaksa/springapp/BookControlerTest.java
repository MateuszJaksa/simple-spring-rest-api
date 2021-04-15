package com.jaksa.springapp;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaksa.springapp.datatransfer.BookModelAssembler;
import com.jaksa.springapp.exceptions.BookNotFoundException;
import com.jaksa.springapp.models.Book;
import com.jaksa.springapp.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "test_user")
public class BookControlerTest {

    private final MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .defaultRequest(get("/"))
            .apply(springSecurity())
            .build();
    }

    public ArrayList<Book> getMockBooks() {
        ArrayList<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("Dune", "Herbert"));
        mockBooks.add(new Book("Serotonin", "Houellebecq"));
        return mockBooks;
    }

    @Test
    public void getBookById_ExistingId_BookWithGivenFields() throws Exception {
        Book mockBook = new Book("Dune", "Herbert");
        when(service.findById(1L)).thenReturn(mockBook);
        this.mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Dune"))
            .andExpect(jsonPath("$.author").value("Herbert"));
    }

    @Test
    public void getBookById_NonExistingId_BookNotFoundExceptionThrown() throws Exception {
        when(service.findById(-1L)).thenThrow(new BookNotFoundException(-1L));
        this.mockMvc.perform(get("/books/-1")).andExpect(status().is4xxClientError());
    }

    @Test
    public void getAll_NonEmptyRepo_ListOf2GivenBooks() throws Exception {
        when(service.findAll()).thenReturn(getMockBooks());
        this.mockMvc.perform(get("/books")).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.bookList[0].title").value("Dune"))
            .andExpect(jsonPath("$._embedded.bookList[1].title").value("Serotonin"));
    }

    @Test
    public void addBook_newBookObject_SuccessfulBookSending() throws Exception {
        String custJson = new ObjectMapper().writeValueAsString(new Book("test_title", "test_author"));
        this.mockMvc.perform(post("/books").with(csrf())
            .content(custJson)
            .contentType(MEDIA_TYPE_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    public void addBook_objectOfWrongClass_BadRequest() throws Exception {
        this.mockMvc.perform(post("/books").with(csrf())
            .content("test_bad_data")
            .contentType(MEDIA_TYPE_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBook_newBookObject_SuccessfulBookSending() throws Exception {
        Book mockBook = new Book("Dune", "Herbert");
        when(service.findById(1L)).thenReturn(mockBook);
        String custJson = new ObjectMapper().writeValueAsString(new Book("test_title", "test_author"));
        this.mockMvc.perform(put("/books/1").with(csrf())
            .content(custJson)
            .contentType(MEDIA_TYPE_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteBook_existingBookId_NoContentResponse() throws Exception {
        this.mockMvc.perform(delete("/books/1").with(csrf()))
            .andExpect(status().isNoContent());
    }
}
