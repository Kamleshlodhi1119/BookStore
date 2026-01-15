package com.bookstore.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bookstore.dto.BookDto;
import com.bookstore.entity.Book;

@Configuration
public class ModelMapperConfig {
//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}
	
	
	@Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(Book.class, BookDto.class)
              .addMappings(m -> {
                  m.map(
                      src -> src.getAuthor() != null ? src.getAuthor().getName() : null,
                      BookDto::setAuthorName
                  );
                  m.map(Book::getAverageRating, BookDto::setAverageRating);
              });

        return mapper;
	}
}
