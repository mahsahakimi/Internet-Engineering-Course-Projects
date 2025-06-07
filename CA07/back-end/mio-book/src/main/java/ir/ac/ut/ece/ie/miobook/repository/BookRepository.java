package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findByTitle(String title);
    Page<Book> findByAuthor(Author author, Pageable pageable);

    List<Book> findByTitleContainingIgnoreCase(String title);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Book> findByAuthorNameContainingIgnoreCase(Author author);
    Page<Book> findByAuthorNameContainingIgnoreCase(Author author, Pageable pageable);

    List<Book> findByGenresNameIgnoreCase(String genreName);
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE LOWER(g.name) = LOWER(:genreName)")
    Page<Book> findByGenreName(@Param("genreName") String genreName, Pageable pageable);


    List<Book> findByYearBetween(int fromYear, int toYear);
    Page<Book> findByYearBetween(int fromYear, int toYear, Pageable pageable);

    @Query("""
        SELECT b FROM Book b
        JOIN b.author a
        LEFT JOIN b.genres g
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:authorName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%')))
        AND (:genre IS NULL OR LOWER(g.name) = LOWER(:genre))
        AND (:fromYear IS NULL OR b.year >= :fromYear)
        AND (:toYear IS NULL OR b.year <= :toYear)
        GROUP BY b
        """)
    List<Book> searchBooksWithFilters(
        @Param("title") String title,
        @Param("authorName") String authorName,
        @Param("genre") String genre,
        @Param("fromYear") Integer fromYear,
        @Param("toYear") Integer toYear
    );

    List<Book> findTop5ByOrderByYearDesc(); 

    boolean existsByTitle(String title);
    boolean existsByTitleAndAuthor(String title, Author author);
}