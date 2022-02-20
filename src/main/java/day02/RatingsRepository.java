package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingsRepository {
    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> ratings) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("insert into ratings(movie_id, rating) values(?,?)")) {
                for (Integer actual : ratings) {
                    if (actual < 1 || actual > 5) {
                        throw new IllegalArgumentException("Invalid rating!");
                    }
                    stmt.setLong(1, movieId);
                    stmt.setLong(2, actual);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (IllegalArgumentException iae) {
                conn.rollback();
            }

        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot connect to ratings!", sqlException);
        }
    }

    public List<Integer> getRatingsByMovieId(long movieId) {
        List<Integer> ratingByMovie = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select rating from ratings where movie_id = ?")) {
            statement.setLong(1, movieId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ratingByMovie.add(resultSet.getInt("rating"));
                }
            } catch (SQLException sqlException) {
                throw new IllegalStateException("Cannot query", sqlException);
            }

        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot connect");
        }
        return ratingByMovie;
    }

    public int insertAvgRating(long movieId, double avgRating) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("update `movies-actors`.`movies` SET `avg_rating`= (?) where `id`= (?)")) {
                    stmt.setLong(1, movieId);
                    stmt.setDouble(2, avgRating);
                    stmt.executeUpdate();
                    return 1;
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot connect to ratings!", sqlException);
        }
    }
}