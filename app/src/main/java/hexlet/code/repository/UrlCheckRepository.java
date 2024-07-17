package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = """
                    INSERT INTO url_checks
                        (url_id, status_code, h1, title, description)
                    VALUES
                        (?, ?, ?, ?, ?)
                  """;
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getTitle());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong("id"));
                urlCheck.setCreatedAt(generatedKeys.getTimestamp("created_at"));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity url_checks");
            }
        }
    }

    public static UrlCheck getUrlLastCheck(Long idUrl)  {
        var urlLastCheck = UrlCheck.builder();
        if (!getEntityDetails(idUrl).isEmpty()) {
            var urlCheck = getEntityDetails(idUrl).get(0);
            urlLastCheck.id(urlCheck.getId());
            urlLastCheck.urlId(urlCheck.getUrlId());
            urlLastCheck.statusCode(urlCheck.getStatusCode());
            urlLastCheck.h1(urlCheck.getH1());
            urlLastCheck.title(urlCheck.getTitle());
            urlLastCheck.description(urlCheck.getDescription());
            urlLastCheck.createdAt(urlCheck.getCreatedAt());
        }
        return urlLastCheck.build();
    }

    public static List<UrlCheck> getEntityDetails(Long idUrl) {
        var sql = "SELECT * FROM url_checks where url_id = ? ORDER BY created_at DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUrl);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var urlCheck = UrlCheck.builder()
                        .id(resultSet.getLong("id"))
                        .urlId(idUrl)
                        .statusCode(resultSet.getInt("status_code"))
                        .h1(resultSet.getString("h1"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .build();
                result.add(urlCheck);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
