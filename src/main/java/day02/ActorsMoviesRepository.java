package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActorsMoviesRepository {
    private DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovie(long actorId, long movieId){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into actors_movies(actor_id, movie_id) values(?,?)")){
            statement.setLong(1, actorId);
            statement.setLong(2, movieId);
            statement.executeUpdate();

        } catch (SQLException sqlException){
            throw new IllegalStateException("Cannot insert row to actors-movies");
        }
    }
}
