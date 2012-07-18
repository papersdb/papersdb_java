package edu.ualberta.cs.papersdb.server.dao;

import java.util.Set;

import edu.ualberta.cs.papersdb.model.Author;

public interface AuthorDAO extends GenericDAO<Author, Long> {

    Author getByFamilyName(String familyName);

    Set<Author> getMatchingFamilyName(String match, int start,
        int max);

    Author getByEmail(String email);

    Set<Author> getPaperAuthors(long paperId);

}
