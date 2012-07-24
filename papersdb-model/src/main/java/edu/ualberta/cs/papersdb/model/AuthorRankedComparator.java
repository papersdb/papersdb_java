package edu.ualberta.cs.papersdb.model;

import java.util.Comparator;

public class AuthorRankedComparator implements Comparator<AuthorRanked> {

    @Override
    public int compare(AuthorRanked ar1, AuthorRanked ar2) {
        return ar1.getRank() - ar2.getRank();
    }

}
