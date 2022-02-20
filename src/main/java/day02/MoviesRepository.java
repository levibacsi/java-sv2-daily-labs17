package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate releaseDate){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into movies(title, release_date) values(?,?)",
                     Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, title);
            statement.setDate(2, Date.valueOf(releaseDate));
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()){
                if (resultSet.next()){
                    return resultSet.getLong(1);
                }
                throw new IllegalStateException("Insert to movies failed");
            }
        }
        catch (SQLException sqlException){
            throw new IllegalStateException("Cannot connect");
        }
    }

    public List<Movie> findAllMovies(){
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from movies");
            ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()){
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    LocalDate releaseDate = resultSet.getDate("release_Date").toLocalDate();
                    movies.add(new Movie(id, title, releaseDate));
                }
            }
        catch (SQLException sqlException){
            throw new IllegalStateException("Cannot query", sqlException);
        }

        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from movies where title=?")){
            stmt.setString(1,title);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return Optional.of(new Movie(rs.getLong("id"),rs.getString("title"),rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }

        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot connect to movies!",sqlException);
        }
    }
}
