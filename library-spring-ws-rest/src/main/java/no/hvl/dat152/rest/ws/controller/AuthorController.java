/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

	
	// TODO - getAllAuthor (@Mappings, URI, and method)
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthor(){
        List<Author> authors = authorService.findAll();

        return ResponseEntity.ok(authors); //same as <>(author, HttpStatus.OK
    }
	
	// TODO - getAuthor (@Mappings, URI, and method)
    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable int id) throws AuthorNotFoundException {
        Author author = authorService.findById(id);

        return new ResponseEntity<>(author, HttpStatus.OK);
    }
	
	// TODO - getBooksByAuthorId (@Mappings, URI, and method)
    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable int id) throws AuthorNotFoundException {
        Set<Book> booksByAuthor = authorService.findBooksByAuthorId(id);

        return new ResponseEntity<>(booksByAuthor, HttpStatus.OK);
    }
	
	// TODO - createAuthor (@Mappings, URI, and method)
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author){
        Author newAuthor = authorService.saveAuthor(author);

        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }
	
	// TODO - updateAuthor (@Mappings, URI, and method)
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id,
                                               @RequestBody Author author) throws AuthorNotFoundException {
        Author newAuthor = authorService.findById(id);

        newAuthor.setFirstname(author.getFirstname());
        newAuthor.setLastname(author.getLastname());

        authorService.saveAuthor(newAuthor);

        return new ResponseEntity<>(newAuthor, HttpStatus.OK);
    }


}
