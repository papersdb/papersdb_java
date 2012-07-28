package edu.ualberta.cs.papersdb.importer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.ualberta.cs.papersdb.PapersdbSchemaExport;
import edu.ualberta.cs.papersdb.SessionProvider;
import edu.ualberta.cs.papersdb.SessionProvider.Mode;
import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.user.User;
import edu.ualberta.cs.papersdb.model.user.UserAccessType;

/**
 * Imports the data from php version of PapersdDB to this new one.
 * 
 * @author nelson
 * 
 */
@SuppressWarnings("nls")
public class Importer {

    private final Connection dbCon;

    private final SessionProvider sessionProvider;

    private Session session;

    public static void main(String[] args) {
        try {
            new Importer();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    Importer() throws Exception {
        // drop all tables and create a new database schema
        new PapersdbSchemaExport();

        sessionProvider = new SessionProvider(Mode.DEBUG);
        session = sessionProvider.openSession();

        dbCon = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/pubDB", "dummy", "ozzy498");

        fixErrorsInOldDb();

        importUsers();
        importAuthors();
        importPapers();

    }

    private void fixErrorsInOldDb() throws SQLException {
        PreparedStatement ps = dbCon.prepareStatement(
            "SELECT count(*) FROM user WHERE name='PandoraLam'");
        ResultSet rs = ps.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1) {
            ps = dbCon.prepareStatement(
                "UPDATE user SET name='Pandora Lam' WHERE name='PandoraLam'");
            ps.executeUpdate();
        }

        ps = dbCon.prepareStatement(
            "SELECT count(*) FROM user WHERE name='idanis'");
        rs = ps.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1) {
            ps = dbCon.prepareStatement(
                "UPDATE user SET name='Idanis Diaz' WHERE name='idanis'");
            ps.executeUpdate();
        }

        ps = dbCon.prepareStatement(
            "SELECT count(*) FROM user WHERE name='nelson'");
        rs = ps.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1) {
            ps = dbCon.prepareStatement(
                "DELETE FROM user WHERE name='nelson'");
            ps.executeUpdate();
        }

    }

    private void importUsers() throws Exception {
        Number n = (Number) session.createCriteria(User.class)
            .setProjection(Projections.rowCount()).uniqueResult();

        if (!n.equals(0L)) {
            throw new ImportException("user table has already been populated");
        }

        final PreparedStatement ps =
            dbCon.prepareStatement("SELECT * FROM user");

        Transaction tx = session.beginTransaction();

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // skip users that have not been verified
            if (rs.getInt("verified") == 0) continue;

            User user = new User();
            user.setVerified(rs.getBoolean("verified"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));

            UserAccessType accessType = UserAccessType.UNVERIFIED_USER;

            int accessLevel = rs.getInt("access_level");

            switch (accessLevel) {
            case 0:
                break;
            case 1:
                accessType = UserAccessType.EDITOR;
                break;
            case 2:
                accessType = UserAccessType.ADMINISTRATOR;
                break;
            default:
                throw new IllegalStateException(
                    "user has invalid access level: login="
                        + rs.getInt("login") + " access_level=" + accessLevel);

            }

            user.setAccessType(accessType);

            List<String> splitNames = splitNames(rs.getString("name"));
            user.setGivenNames(splitNames.get(0));
            if (splitNames.size() > 1) {
                user.setFamilyNames(splitNames.get(1));
            }

            session.save(user);
        }

        tx.commit();
    }

    private void importAuthors() throws Exception {
        Number n = (Number) session.createCriteria(Author.class)
            .setProjection(Projections.rowCount()).uniqueResult();

        if (!n.equals(0L)) {
            throw new ImportException("author table has already been populated");
        }

        Transaction tx = session.beginTransaction();

        PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM author");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Author author = new Author();
            author.setTitle(rs.getString("title"));

            String names = rs.getString("name");
            String email = rs.getString("email");

            if ((email != null)
                && !email.isEmpty()
                && !(email.equals("steve@cs.ualberta.ca") && names
                    .equals("Lake, Robert"))) {
                author.setEmail(email);
            }

            List<String> splitNames = splitNames(names);
            author.setGivenNames(splitNames.get(0));
            if (splitNames.size() > 1) {
                author.setFamilyNames(splitNames.get(1));
            }

            session.save(author);
        }

        User user;
        Author author;
        ps = dbCon.prepareStatement("SELECT * FROM user_author ua "
            + "JOIN author ON author.author_id=ua.author_id "
            + "JOIN user on user.login=ua.login");
        rs = ps.executeQuery();
        while (rs.next()) {
            String login = rs.getString("login");
            user = getUserByLogin(session, login);
            if (user == null) {
                throw new ImportException("user not found: login=" + login);
            }

            List<String> names = splitNames(rs.getString("name"));
            author = getAuthorByName(session, names.get(0), names.get(1));
            if (author == null) {
                throw new ImportException("author not found: names="
                    + names.get(0) + " " + names.get(1));
            }

            user.getCollaborators().add(author);
            session.save(user);
        }

        tx.commit();

    }

    private void importPapers() throws Exception {
        Number n = (Number) session.createCriteria(User.class)
            .setProjection(Projections.rowCount()).uniqueResult();

        if (n.equals(0L)) {
            throw new ImportException("user table has not been populated");
        }

        final PreparedStatement ps =
            dbCon.prepareStatement("SELECT * FROM publication");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Paper paper = new Paper();
            paper.setTitle(rs.getString("title"));
            paper.setAbstract(rs.getString("abstract"));
            paper.setKeywords(rs.getString("keywords"));
            // paper.setUserSubmittedBy(rs.getString("keywords"));
        }

    }

    /*
     * Accepts two formats:
     * 
     * 1) Delimited by comma: last name is before comma, first name after
     * 
     * 2) Delimited by only spaces: first name is all text up to first space,
     * last name is everything after
     */
    private List<String> splitNames(String names) {
        List<String> result = new ArrayList<String>();
        String[] splitNames;

        if (names.contains(",")) {
            splitNames = names.split(",");
        } else {
            splitNames = names.split("\\s");
        }

        if (splitNames.length == 0) {
            throw new IllegalStateException("name cannot be split: names="
                + names);
        }

        if (names.contains(",")) {
            if (splitNames.length == 2) {
                result.add(splitNames[1].trim());
                result.add(splitNames[0].trim());
            }

        } else {
            result.add(splitNames[0]);

            if (splitNames.length > 1) {
                String[] familyNames =
                    Arrays.copyOfRange(splitNames, 1, splitNames.length);
                result.add(StringUtils.join(familyNames));
            } else {
                throw new IllegalStateException("no last name found in name: "
                    + names);
            }
        }

        return result;
    }

    public static User getUserByLogin(Session session, String login) {
        if (session == null) {
            throw new NullPointerException("session is null");
        }

        Criteria c = session.createCriteria(User.class, "u")
            .add(Restrictions.eq("login", login));

        return (User) c.uniqueResult();
    }

    public static Author getAuthorByName(Session session, String givenNames,
        String familyNames) {
        if (session == null) {
            throw new NullPointerException("session is null");
        }

        Criteria c = session.createCriteria(Author.class, "u")
            .add(Restrictions.eq("givenNames", givenNames))
            .add(Restrictions.eq("familyNames", familyNames));

        return (Author) c.uniqueResult();
    }
}
