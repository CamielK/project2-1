package Library.AI.MLR.Data.Reader;

import org.relique.io.TableReader;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class NothanksReader implements TableReader {

    public Reader getReader(Statement statement, String tableName) throws SQLException {
        try {
            URL path = new URL(this.getClass().getResource("/mit3prototype/data/nothanksTraining.csv").toExternalForm());
            JarURLConnection connection = (JarURLConnection) path.openConnection();
            //InputStream data = JarReader.class.getClassLoader().getResourceAsStream("/mit3prototype/data/ratingCalcDatabase.csv");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            return reader;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }


    public List getTableNames(Connection connection) {
        // Return list of available table names
        Vector v = new Vector();

        v.add("bPickedCard");
        v.add("CardNumber");
        v.add("NumChipsOnCard");
        v.add("ScoreDiff");
        v.add("bFitsInSeries");
        v.add("bFitsInSeriesAlmost");
        v.add("NumHighCardsLeft");
        v.add("NumMediumCardsLeft");
        v.add("NumLowCardsLeft");
        v.add("NumOwnedChips");

        return v;
    }
}