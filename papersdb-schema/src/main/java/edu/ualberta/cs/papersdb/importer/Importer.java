package edu.ualberta.cs.papersdb.importer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import edu.ualberta.cs.papersdb.PapersdbSchemaExport;
import edu.ualberta.cs.papersdb.SessionProvider;
import edu.ualberta.cs.papersdb.SessionProvider.Mode;
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

        dbCon =
            DriverManager.getConnection("jdbc:mysql://localhost:3306/pubDB",
                "dummy", "ozzy498");

        importUsers();
        importPapers();

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

            String[] names = rs.getString("name").split("\\s");

            if (names.length == 0) {
                throw new IllegalStateException("user name field empty: login="
                    + rs.getInt("login"));
            }

            user.setGivenNames(names[0]);

            if (names.length > 1) {
                String[] familyNames =
                    Arrays.copyOfRange(names, 1, names.length);
                user.setFamilyNames(StringUtils.join(familyNames));
            }

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
}
