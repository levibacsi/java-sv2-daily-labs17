package day02;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoviesRatingService {
    private MoviesRepository moviesRepository;
    private RatingsRepository ratingsRepository;

    public MoviesRatingService(MoviesRepository moviesRepository, RatingsRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> actualMovie = moviesRepository.findMovieByTitle(title);

        if (actualMovie.isPresent()) {
            ratingsRepository.insertRating(actualMovie.get().getId(), Arrays.asList(ratings));

        } else {
            throw new IllegalArgumentException("Cannot find movie: " + title);
        }
    }
}
