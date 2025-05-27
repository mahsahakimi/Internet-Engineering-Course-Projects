package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserContext userContext;

    public AuthorService(AuthorRepository authorRepository, UserContext userContex) {
        this.authorRepository = authorRepository;
        this.userContext = userContex;
    }

    public List<Author> addAuthor(String name, String penName, String nationality, MyLocalDate born, MyLocalDate died) {
        User currentUser = userContext.getLoggedInUser();

        if (currentUser == null) {
            throw new UserNotLoggedInException();
        }
        if (!(currentUser instanceof Admin admin)) {
            throw new PermissionDeniedException(Role.CUSTOMER.toString());
        }

        if (!BornDiedValidator.validateBornDied(born.toLocalDate(), died != null ? died.toLocalDate() : null)) {
            throw new InvalidDatesException();
        }

        if (authorRepository.existsById(name)) {
            throw new AuthorAlreadyExistsException(name);
        }

        Author author = new Author(name, penName, nationality, born, died, admin);
        admin.addManagedAuthor(author);

        authorRepository.save(author);
        return authorRepository.findAll();
    }

    public Map<String, Object> getAuthorDetails(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new AuthorNotFoundException(authorName));

        Map<String, Object> authorData = new HashMap<>();
        authorData.put("name", author.getName());
        authorData.put("penName", author.getPenName());
        authorData.put("nationality", author.getNationality());
        authorData.put("born", author.getBorn().toString());

        if (author.getDied() != null) {
            authorData.put("died", author.getDied().toString());
        }

        return authorData;
    }

    public List<Author> getAllAuthors(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize); // Convert 1-based to 0-based
        return authorRepository.findAll(pageable).getContent();
    }
}