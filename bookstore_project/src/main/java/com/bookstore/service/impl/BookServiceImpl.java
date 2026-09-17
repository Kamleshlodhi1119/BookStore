package com.bookstore.service.impl;

import com.bookstore.dto.BookDto;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Rating;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.RatingRepository;
import com.bookstore.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final RatingRepository ratingRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    // SINGLE CONSTRUCTOR — no Lombok, no conflicts
    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           RatingRepository ratingRepository,
                           CartItemRepository cartItemRepository,
                           ModelMapper modelMapper) {

        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.ratingRepository = ratingRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    // ---------------- CREATE ----------------

    @Override
    @Transactional
    public BookDto createBook(BookDto dto) {

        // ---- VALIDATION ----
        if (dto.getAuthorName() == null || dto.getAuthorName().isBlank()) {
            throw new IllegalArgumentException("authorName is required");
        }

        // ---- MAP BASIC FIELDS ----
        Book book = modelMapper.map(dto, Book.class);

        // Hibernate safety
        if (book.getRatings() == null) {
            book.setRatings(new ArrayList<>());
        }

        // ---- FIND OR CREATE AUTHOR ----
        Author author = authorRepository.findByName(dto.getAuthorName())
                .orElseGet(() -> {
                    Author a = new Author();
                    a.setName(dto.getAuthorName());
                    return authorRepository.save(a);
                });

        book.setAuthor(author);

        // ---- FIRST SAVE (ID GENERATED) ----
        Book saved = bookRepository.save(book);

        // ---- AUTO IMAGE URL ----
        if (saved.getImageUrl() == null || saved.getImageUrl().isBlank()) {
            saved.setImageUrl("images/books/" + saved.getId() + ".png");
            saved = bookRepository.save(saved);
        }

        return map(saved);
    }

    // ---------------- UPDATE ----------------

    @Override
    @Transactional
    public BookDto updateBook(Long id, BookDto dto) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStockQuantity(dto.getStockQuantity());
        book.setPublishDate(dto.getPublishDate());

        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            book.setImageUrl(dto.getImageUrl());
        }

        // Optional author update
        if (dto.getAuthorName() != null && !dto.getAuthorName().isBlank()) {

            Author author = authorRepository.findByName(dto.getAuthorName())
                    .orElseGet(() -> {
                        Author a = new Author();
                        a.setName(dto.getAuthorName());
                        return authorRepository.save(a);
                    });

            book.setAuthor(author);
        }

        return map(bookRepository.save(book));
    }

    // ---------------- READ ----------------

    @Override
    public BookDto getBookById(Long id) {
        return map(
                bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found"))
        );
    }

    @Override
    public List<BookDto> getAllBooksByAdmin() {
        return bookRepository.findAll().stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .filter(Book::getActive)
                .map(this::map)
                .toList();
    }

    // ---------------- DELETE ----------------

    @Override
    @Transactional
    public void deleteBook(Long id) {
        // Enable if FK constraint exists
        // cartItemRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    // ---------------- RATINGS ----------------

    @Override
    @Transactional
    public void addRating(Long bookId, Integer rating, String comment, String username) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Rating r = new Rating();
        r.setRating(rating);
        r.setComment(comment);
        r.setUsername(username);
        r.setBook(book);

        ratingRepository.save(r);
    }

    // ---------------- SEARCH / FILTER ----------------

    @Override
    public List<BookDto> searchBooks(String keyword) {
        return bookRepository
                .findByTitleContainingIgnoreCaseAndActiveTrue(keyword)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByAuthor(Long authorId) {
        return bookRepository
                .findByAuthor_IdAndActiveTrue(authorId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<BookDto> getLatestBooks() {
        return bookRepository
                .findTop5ByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<BookDto> getTopRatedBooks() {
        return bookRepository.findByActiveTrue().stream()
                .sorted((a, b) -> {
                    double ar = a.getRatings() == null ? 0 :
                            a.getRatings().stream()
                                    .mapToInt(Rating::getRating)
                                    .average()
                                    .orElse(0);

                    double br = b.getRatings() == null ? 0 :
                            b.getRatings().stream()
                                    .mapToInt(Rating::getRating)
                                    .average()
                                    .orElse(0);

                    return Double.compare(br, ar);
                })
                .limit(5)
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional
    public void updateBookStatus(Long bookId, boolean active) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setActive(active);
        bookRepository.save(book);
    }

    @Override
    public List<BookDto> filterBooks(String name, String author, Double maxPrice) {

        return bookRepository.findAll().stream()
                .filter(Book::getActive)
                .filter(b ->
                        name == null || name.isBlank()
                                || b.getTitle().toLowerCase().contains(name.toLowerCase())
                )
                .filter(b ->
                        author == null || author.isBlank()
                                || (b.getAuthor() != null
                                && b.getAuthor().getName().equalsIgnoreCase(author))
                )
                .filter(b ->
                        maxPrice == null || b.getPrice() <= maxPrice
                )
                .map(this::map)
                .toList();
    }

    // ---------------- MAPPER ----------------

    private BookDto map(Book book) {

        BookDto dto = new BookDto();

        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setStockQuantity(book.getStockQuantity());
        dto.setPublishDate(book.getPublishDate());
        dto.setActive(book.getActive());
        dto.setImageUrl(book.getImageUrl());

        if (book.getAuthor() != null) {
            dto.setAuthorName(book.getAuthor().getName());
            dto.setAuthorId(book.getAuthor().getId());
        }

        List<Rating> ratings = ratingRepository.findByBookId(book.getId());

        double avg = ratings.isEmpty()
                ? 0
                : ratings.stream()
                        .mapToInt(Rating::getRating)
                        .average()
                        .orElse(0);

        dto.setAverageRating(avg);

        return dto;
    }
}