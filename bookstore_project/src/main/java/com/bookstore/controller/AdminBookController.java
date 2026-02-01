package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.entity.Book;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.SupabaseStorageService;
import com.bookstore.service.impl.BulkBookServiceimpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/admin/books")
//@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final SupabaseStorageService storageService;
    private final BulkBookServiceimpl bulkBookServiceimpl;



    public AdminBookController(BookService bookService,
            BookRepository bookRepository,
            SupabaseStorageService storageService,BulkBookServiceimpl bulkBookServiceimpl) {

			this.bookService = bookService;
			this.bookRepository = bookRepository;
			this.storageService = storageService;
			this.bulkBookServiceimpl=bulkBookServiceimpl;
			}


    // ---------------- CREATE BOOK ----------------
    @PostMapping
    public BookDto createBook(@RequestBody BookDto dto) {
        return bookService.createBook(dto);
    }

    // ---------------- UPDATE BOOK ----------------
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto dto) {
        return bookService.updateBook(id, dto);
    }

    // ---------------- ENABLE / DISABLE ----------------
    @PutMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        bookService.updateBookStatus(id, active);
        return "Book status updated";
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    // ---------------- IMAGE UPLOAD ----------------
//    @PostMapping("/{id}/image")
//    public void uploadImage(
//            @PathVariable Long id,
//            @RequestParam("file") MultipartFile file
//    ) throws IOException {
//
//        Path uploadDir = Paths.get("uploads/books");
//        Files.createDirectories(uploadDir);
//
//        Path imagePath = uploadDir.resolve(id + ".png");
//        Files.write(imagePath, file.getBytes());
//
//        Book book = bookRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
//
//        // ✅ ONLY CHANGE IS HERE
//        book.setImageUrl("/images/books/" + id + ".png");
//
//        bookRepository.save(book);
//    }
    
    
    @PostMapping("/{id}/image")
    public void uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Upload to Supabase
        String publicUrl = storageService.uploadBookImage(id, file);

        // Save URL in DB
        book.setImageUrl(publicUrl);
        bookRepository.save(book);
    }


    // ---------------- PUBLIC READ APIs ----------------
    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam String q) {
        return bookService.searchBooks(q);
    }

    @GetMapping("/by-author/{authorId}")
    public List<BookDto> byAuthor(@PathVariable Long authorId) {
        return bookService.getBooksByAuthor(authorId);
    }

    @GetMapping("/top-rated")
    public List<BookDto> topRated() {
        return bookService.getTopRatedBooks();
    }

    @GetMapping("/latest")
    public List<BookDto> latest() {
        return bookService.getLatestBooks();
    }
    
    
    @PostMapping("/bulk-upload")
    public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        int imported = bulkBookServiceimpl.importCsv(file);
        return ResponseEntity.ok("Imported " + imported + " books successfully");
    }


}
