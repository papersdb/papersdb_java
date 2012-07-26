package edu.ualberta.cs.papersdb.persist.dao;

import java.util.Set;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Publisher;
import edu.ualberta.cs.papersdb.model.Ranking;

public interface PublisherDAO extends GenericDAO<Author, Long> {

    Publisher getByName(String name);

    Set<Publisher> getMatchingName(String match, int start, int max);

    Set<Publisher> getByRanking(Ranking ranking, int start, int max);

    Set<Publisher> countByRanking(Ranking ranking);

}
