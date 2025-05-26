package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewAuthorService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    public NewAuthorService(AuthorRepository authorRepository, UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
    }

    public List<Author> addAuthor(String name, String penName, String nationality, MyLocalDate born, MyLocalDate died) {
        // Fetch logged-in user (replace this with your actual login/session mechanism)
        String currentUsername = getLoggedInUsername(); // <-- implement this method appropriately

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(UserNotLoggedInException::new);

        if (!(user instanceof Admin admin)) {
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

    private String getLoggedInUsername() {
        // Replace with your session/security context logic
        throw new UnsupportedOperationException("Implement user context retrieval");
    }
}
