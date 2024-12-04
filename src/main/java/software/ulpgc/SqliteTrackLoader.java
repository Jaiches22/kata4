package software.ulpgc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqliteTrackLoader implements TrackLoader {

    private final Connection con;
    private final static String URL = "SELECT Track.Name AS track, Track.Composer AS composer, Album.Title AS album, Artist.Name AS artist, Track.Milliseconds AS milliseconds FROM Track JOIN Album ON Track.AlbumId = Album.AlbumId JOIN Artist ON Album.ArtistId = Artist.ArtistId";

    public SqliteTrackLoader(Connection con) {
        this.con = con;
    }

    @Override
    public List<Track> loadAll() {
        try {
            return load(queryAll());
        } catch (SQLException e) {
            e.printStackTrace(); // Agrega un log en caso de error
            return Collections.emptyList();
        }
    }

    private List<Track> load(ResultSet resultSet) throws SQLException {
        List<Track> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(trackOf(resultSet));
        }
        return result;
    }

    private Track trackOf(ResultSet resultSet) throws SQLException {
        return new Track(
                resultSet.getString("track"),        // Nombre de la pista
                resultSet.getString("composer"),    // Compositor
                resultSet.getString("album"),       // Álbum
                resultSet.getString("artist"),      // Artista
                resultSet.getInt("milliseconds") / 1000 // Duración en segundos
        );
    }

    private ResultSet queryAll() throws SQLException {
        return con
                .createStatement()
                .executeQuery(URL);
    }
}
