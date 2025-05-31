package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;

    public AuthorService(AuthorRepository authorRepository, UserRepository userRepository, UserContext userContex) {
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.userContext = userContex;
    }

    public List<Map<String, Object>> addAuthor(String name, String penName, String nationality, MyLocalDate born, MyLocalDate died) {
        User currentUser = userContext.getLoggedInUser();

        if (currentUser == null) {
            throw new UserNotLoggedInException();
        }
        if (!(currentUser instanceof Admin)) {
            throw new PermissionDeniedException(Role.CUSTOMER.toString());
        }

        if (!BornDiedValidator.validateBornDied(born.toLocalDate(), died != null ? died.toLocalDate() : null)) {
            throw new InvalidDatesException();
        }

        if (authorRepository.existsById(name)) {
            throw new AuthorAlreadyExistsException(name);
        }

        Optional<User> optionalUser = userRepository.findById(currentUser.getUsername());

        if (optionalUser.isEmpty() || !(optionalUser.get() instanceof Admin)) {
            throw new PermissionDeniedException(Role.CUSTOMER.toString());
        }

        Admin managedAdmin = (Admin) optionalUser.get();

        Author author = new Author(name, penName, nationality, born, died, managedAdmin);
        authorRepository.save(author);
        
        List<Map<String, Object>> authorList = new ArrayList<>();
        for (Author authorr : authorRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", authorr.getName());
            map.put("penName", authorr.getPenName());
            map.put("nationality", authorr.getNationality());
            map.put("born", authorr.getBorn());
            map.put("died", authorr.getDied());
            authorList.add(map);
        }

        return authorList;
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

    public List<Map<String, Object>> getAllAuthors(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize); 
        List<Author> authors = authorRepository.findAll(pageable).getContent();
        List<Map<String, Object>> formattedAuthors = new ArrayList<>();
        for (Author author : authors){
            Map<String, Object> formattedAuthor = new HashMap<>();
            formattedAuthor.put("name", author.getName());
            formattedAuthor.put("penName", author.getPenName());
            formattedAuthor.put("nationality", author.getNationality());
            formattedAuthor.put("born", author.getBorn());
            formattedAuthor.put("died", author.getDied());

            formattedAuthors.add(formattedAuthor);
        }

        return formattedAuthors;
    }
}