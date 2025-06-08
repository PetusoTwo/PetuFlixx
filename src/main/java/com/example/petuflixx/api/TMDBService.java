package com.example.petuflixx.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TMDBService {
    private static final String API_KEY = "df1a47e1246d5dd6ea43dfe69f276b67"; // Reemplazar con tu API key
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final HttpClient client;
    private final Gson gson;

    public TMDBService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    private List<Movie> fetchMovies(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            JsonArray results = jsonResponse.getAsJsonArray("results");

            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JsonObject movieJson = results.get(i).getAsJsonObject();
                Movie movie = new Movie(
                    movieJson.get("id").getAsInt(),
                    movieJson.get("title").getAsString(),
                    movieJson.get("overview").getAsString(),
                    movieJson.get("poster_path").getAsString(),
                    movieJson.get("backdrop_path").getAsString(),
                    movieJson.get("vote_average").getAsDouble(),
                    movieJson.get("release_date").getAsString()
                );
                movies.add(movie);
            }
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Obtener películas populares
    public List<Movie> getPopularMovies(int page) {
        String url = String.format("%s/movie/popular?api_key=%s&page=%d", BASE_URL, API_KEY, page);
        return fetchMovies(url);
    }

    // Buscar películas
    public List<Movie> searchMovies(String query, int page) {
        String url = String.format("%s/search/movie?api_key=%s&query=%s&page=%d", 
            BASE_URL, API_KEY, query.replace(" ", "+"), page);
        return fetchMovies(url);
    }

    // Obtener detalles de una película
    public MovieDetails getMovieDetails(int movieId) {
        try {
            String url = String.format("%s/movie/%d?api_key=%s", BASE_URL, movieId, API_KEY);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject movieJson = gson.fromJson(response.body(), JsonObject.class);

            return new MovieDetails(
                movieJson.get("id").getAsInt(),
                movieJson.get("title").getAsString(),
                movieJson.get("overview").getAsString(),
                movieJson.get("poster_path").getAsString(),
                movieJson.get("release_date").getAsString(),
                movieJson.get("vote_average").getAsDouble(),
                movieJson.get("runtime").getAsInt(),
                movieJson.get("genres").getAsJsonArray().toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Clase para representar una película básica
    public static class Movie {
        private final int id;
        private final String title;
        private final String overview;
        private final String posterPath;
        private final String backdropPath;
        private final double voteAverage;
        private final String releaseDate;

        public Movie(int id, String title, String overview, String posterPath, String backdropPath, 
                    double voteAverage, String releaseDate) {
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.posterPath = posterPath;
            this.backdropPath = backdropPath;
            this.voteAverage = voteAverage;
            this.releaseDate = releaseDate;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getOverview() { return overview; }
        public String getPosterUrl() { return "https://image.tmdb.org/t/p/w500" + posterPath; }
        public String getBackdropUrl() { return "https://image.tmdb.org/t/p/original" + backdropPath; }
        public double getVoteAverage() { return voteAverage; }
        public int getReleaseYear() { 
            return releaseDate != null && !releaseDate.isEmpty() ? 
                   Integer.parseInt(releaseDate.substring(0, 4)) : 0; 
        }
    }

    // Clase para representar detalles completos de una película
    public static class MovieDetails extends Movie {
        private final int runtime;
        private final String genres;

        public MovieDetails(int id, String title, String overview, String posterPath,
                          String releaseDate, double voteAverage, int runtime, String genres) {
            super(id, title, overview, posterPath, "", voteAverage, releaseDate);
            this.runtime = runtime;
            this.genres = genres;
        }

        // Getters adicionales
        public int getRuntime() { return runtime; }
        public String getGenres() { return genres; }
    }

    public List<Movie> getTopRatedMovies(int page) {
        String url = String.format("%s/movie/top_rated?api_key=%s&page=%d", BASE_URL, API_KEY, page);
        return fetchMovies(url);
    }
} 