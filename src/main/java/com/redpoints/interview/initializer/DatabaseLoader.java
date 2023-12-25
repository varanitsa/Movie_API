package com.redpoints.interview.initializer;

import com.redpoints.interview.service.data.MovieEntity;
import com.redpoints.interview.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.db-loader.enabled", havingValue = "true", matchIfMissing = true)

public class DatabaseLoader implements CommandLineRunner {

    private final MovieRepository repository;

    public DatabaseLoader(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) {
        this.repository.save(new MovieEntity("Tenet", "Christopher Nolan", 2020));
        this.repository.save(new MovieEntity("Men", "Alex Garland", 2022));
        this.repository.save(new MovieEntity("The Princess Bride", "Rob Reiner", 1987));
    }
}
