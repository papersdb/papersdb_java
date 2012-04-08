package edu.ualberta.cs.papersdb.server.dao;

import java.util.Set;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Publisher;
import edu.ualberta.cs.papersdb.model.publication.Publication;

public interface PaperDAO extends GenericDAO<Paper, Long> {

    Paper getPaperForTitle(String title);

    Set<Paper> getPapersMatching(String match, int start, int max);

    Set<Paper> getPapersForAuthor(long authorId, int start, int max);

    Set<Author> getAuthors(long paperId);

    Publisher getPublisher(long paperId);

    Publication getPublication(long paperId);

    // getAttachment(long paperId);

}
