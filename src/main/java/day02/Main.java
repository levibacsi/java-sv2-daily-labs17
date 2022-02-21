package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("AardvarK");

        } catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach database", throwables);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        day02.ActorsRepository actorsRepository = new ActorsRepository(dataSource);

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 3, 1));
        moviesRepository.saveMovie("Lord of the Rings", LocalDate.of(2000, 2, 4));

        ActorsMoviesRepository  actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        ActorsMoviesService service = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);

        service.insertMovieWithActors("Star Wars", LocalDate.of(1978, 5, 4), List.of("Harrison Ford", "Mark Hamill" ));
        service.insertMovieWithActors("Blade Runner", LocalDate.of(1982, 11, 10), List.of("Daryl Hannah",
                "Harrison Ford"));

        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepository, new RatingsRepository(dataSource));
        moviesRatingService.addRatings("Star Wars", 3, 4, 5);
        moviesRatingService.addRatings("Blade Runner", 5, 5, 1, 3, 5);

        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
    }
}
