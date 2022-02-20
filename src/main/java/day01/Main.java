package day01;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("jelszo");

        } catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach database", throwables);
        }


        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        //actorsRepository.saveActor("June Doe");
        System.out.println(actorsRepository.findActorsWithPrefix("J"));
    }

}
