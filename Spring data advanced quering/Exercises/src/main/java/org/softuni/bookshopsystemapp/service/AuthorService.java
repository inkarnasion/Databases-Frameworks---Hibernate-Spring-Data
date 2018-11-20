package org.softuni.bookshopsystemapp.service;



import java.io.IOException;

import java.util.List;


public interface AuthorService {

    void seedAuthors() throws IOException;

    //Task06
    List<String> getAuthorsByFirstNamePatter(String s);


}

