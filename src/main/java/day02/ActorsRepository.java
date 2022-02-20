package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into actors(actor_name) values(?)",
                    Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Cannot insert and get id!");
            }
        }
        catch (SQLException sqlException){
            throw new IllegalStateException("Cannot update: " + name, sqlException);
        }
    }

    public Optional<Actor> findActorByName(String name){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from actors where actor_name= ?")){
            statement.setString(1, name);

            return processSelectStatement(statement);
        } catch (SQLException sqlException){
            throw new IllegalStateException("Cannot connect to select by name");
        }
    }

    private Optional<Actor> processSelectStatement(PreparedStatement statement) throws  SQLException{
        try (ResultSet resultSet = statement.executeQuery()){
            if (resultSet.next()){
                return Optional.of(new Actor(resultSet.getLong("id"), resultSet.getString("actor_name")));
            }
        }
        return Optional.empty();
    }

    public List<String> findActorsWithPrefix(String prefix){
        List<String> actors = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select actor_name from actors where actor_name like ?")){
            statement.setString(1, prefix + "%");
            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next()){
                    actors.add(resultSet.getString("actor_name"));
                }
            }
        }
        catch (SQLException sqlException){
            throw new IllegalStateException("Cannot query", sqlException);
        }
        return actors;
    }
}