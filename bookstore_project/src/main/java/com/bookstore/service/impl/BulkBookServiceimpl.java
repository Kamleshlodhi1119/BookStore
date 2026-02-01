package com.bookstore.service.impl;

import java.io.*;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.opencsv.CSVReader;

import jakarta.transaction.Transactional;


@Service
//@RequiredArgsConstructor
@Transactional
public class BulkBookServiceimpl {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    
    
    

    public BulkBookServiceimpl(BookRepository bookRepository, AuthorRepository authorRepository) {
		super();
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}




	public int importCsv(MultipartFile file) {

        int count = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {

                // Skip header
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] data = line.split(",");

                // Safety check
                if (data.length < 6) continue;

                String title = data[0].trim();
                String authorName = data[1].trim();
                String description = data[2].trim();
                double price = Double.parseDouble(data[3].trim());
                int stock = Integer.parseInt(data[4].trim());
                String isbn = data[5].trim();

                Author author = authorRepository
                        .findByName(authorName)
                        .orElseGet(() -> {
                            Author a = new Author();
                            a.setName(authorName);
                            return authorRepository.save(a);
                        });

                Book book = new Book();
                book.setTitle(title);
                book.setDescription(description);
                book.setPrice(price);
                book.setStockQuantity(stock);
                book.setIsbn(isbn);
                book.setActive(true);
                book.setAuthor(author);

                bookRepository.save(book);
                count++;
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV Import Failed: " + e.getMessage());
        }

        return count;
    }
}
