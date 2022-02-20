package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name){
        try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into actors(actor_name) values(?)")){
            statement.setString(1, name);
            statement.executeUpdate();
        }
        catch (SQLException sqlException){
            throw new IllegalStateException("Cannot update: " + name, sqlException);
        }
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
