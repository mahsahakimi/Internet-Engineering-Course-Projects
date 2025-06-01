package ir.ac.ut.ece.ie.miobook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ir.ac.ut.ece.ie.miobook.model.Genre;
import ir.ac.ut.ece.ie.miobook.repository.GenreRepository;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<String> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        List<String> genreNames = new ArrayList<>();
        for (Genre genre : genres) {
            genreNames.add(genre.getName());
        }

        return genreNames;
    }
}
