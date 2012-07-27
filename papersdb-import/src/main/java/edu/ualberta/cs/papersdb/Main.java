package edu.ualberta.cs.papersdb;

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

import edu.ualberta.cs.papersdb.SessionProvider.Mode;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.User;

@SuppressWarnings("nls")
public class Main {

    private final Connection bbpdbCon;

    private final SessionProvider sessionProvider;

    private Session session;

    public static void main(String[] args) {
        try {
            new Main();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    Main() throws Exception {
        sessionProvider = new SessionProvider(Mode.DEBUG);
        session = sessionProvider.openSession();

        bbpdbCon =
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
            bbpdbCon.prepareStatement("SELECT * FROM user");

        Transaction tx = session.beginTransaction();

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // skip users that have not been verified
            if (rs.getInt("verified") == 0) continue;

            User user = new User();
            user.setVerified(rs.getBoolean("verified"));
            user.setAccessLevel(rs.getInt("access_level"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));

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
            bbpdbCon.prepareStatement("SELECT * FROM publication");
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
