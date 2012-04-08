package edu.ualberta.cs.papersdb.server.dao;

import java.util.Set;

import edu.ualberta.cs.papersdb.model.Paper;

public interface PaperDAO extends GenericDAO<Paper, Long> {

    Paper getPaperForTitle(String title);

    Set<Paper> getPapersMatching(String match, int start, int max);

    Set<Paper> getPapersForAuthor(long authorId, int start, int max);

    String getCitation();

}
