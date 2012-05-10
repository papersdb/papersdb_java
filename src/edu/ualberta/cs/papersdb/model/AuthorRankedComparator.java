package edu.ualberta.cs.papersdb.model;

import java.util.Comparator;

public class AuthorRankedComparator implements Comparator<AuthorRanked> {

    @Override
    public int compare(AuthorRanked author1, AuthorRanked author2) {
        return author1.getRank() - author2.getRank();
    }
}
