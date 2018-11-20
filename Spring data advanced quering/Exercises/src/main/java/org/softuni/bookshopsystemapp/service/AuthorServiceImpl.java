package org.softuni.bookshopsystemapp.service;


import org.softuni.bookshopsystemapp.domain.entities.Author;
import org.softuni.bookshopsystemapp.repository.AuthorRepository;
import org.softuni.bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final static String AUTHORS_FILE_PATH = "D:\\Java\\Spring Data Advanced Homeworks\\src\\main\\resources\\files\\authors.txt";

    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0) {
            return;
        }

        String[] authorFileContent = this.fileUtil.getFileContent(AUTHORS_FILE_PATH);
        for (String line : authorFileContent) {
            String[] names = line.split("\\s+");

            Author author = new Author();
            author.setFirstName(names[0]);
            author.setLastName(names[1]);

            this.authorRepository.saveAndFlush(author);
        }
    }

    //Task06
    @Override
    public List<String> getAuthorsByFirstNamePatter(String s) {
        List<Author> authors = this.authorRepository.findAllByFirstNameEndingWith(s.toLowerCase());

        return authors.stream().map(a -> String.format("%s %s ", a.getFirstName(), a.getLastName())).collect(Collectors.toList());
    }


}
