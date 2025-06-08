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
import java.io.IOException;
import java.util.logging.Logger;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.util.Collections;

public class TMDBService {
    private static final String API_KEY = "df1a47e1246d5dd6ea43dfe69f276b67"; // Reemplazar con tu API key
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final HttpClient client;
    private final Gson gson;
    private static final Logger logger = Logger.getLogger(TMDBService.class.getName());

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
                    movieJson.get("vote_average").getAsDouble(),
                    movieJson.get("vote_count").getAsInt(),
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
        private final double voteAverage;
        private final int voteCount;
        private final String releaseDate;
        private final List<Integer> genreIds;

        public Movie(int id, String title, String overview, String posterPath, double voteAverage, int voteCount, String releaseDate) {
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.posterPath = posterPath;
            this.voteAverage = voteAverage;
            this.voteCount = voteCount;
            this.releaseDate = releaseDate;
            this.genreIds = new ArrayList<Integer>();
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getOverview() { return overview; }
        public String getPosterPath() { return posterPath; }
        public double getVoteAverage() { return voteAverage; }
        public int getVoteCount() { return voteCount; }
        public String getReleaseDate() { return releaseDate; }
        public List<Integer> getGenreIds() { return genreIds; }
        
        public String getPosterUrl() {
            return "https://image.tmdb.org/t/p/w500" + posterPath;
        }
        
        public int getReleaseYear() {
            return Integer.parseInt(releaseDate.substring(0, 4));
        }
    }

    // Clase para representar detalles completos de una película
    public static class MovieDetails {
        private final Movie movie;
        private final int runtime;
        private final String genres;

        public MovieDetails(int id, String title, String overview, String posterPath,
                          String releaseDate, double voteAverage, int runtime, String genres) {
            this.movie = new Movie(id, title, overview, posterPath, voteAverage, 0, releaseDate);
            this.runtime = runtime;
            this.genres = genres;
        }

        public int getId() { return movie.getId(); }
        public String getTitle() { return movie.getTitle(); }
        public String getOverview() { return movie.getOverview(); }
        public String getPosterPath() { return movie.getPosterPath(); }
        public double getVoteAverage() { return movie.getVoteAverage(); }
        public int getVoteCount() { return movie.getVoteCount(); }
        public String getReleaseDate() { return movie.getReleaseDate(); }
        public List<Integer> getGenreIds() { return movie.getGenreIds(); }
        public String getPosterUrl() { return movie.getPosterUrl(); }
        public int getReleaseYear() { return movie.getReleaseYear(); }
        public int getRuntime() { return runtime; }
        public String getGenres() { return genres; }
    }

    public List<Movie> getTopRatedMovies(int page) {
        String url = String.format("%s/movie/top_rated?api_key=%s&page=%d", BASE_URL, API_KEY, page);
        return fetchMovies(url);
    }

    public List<Movie> getMoviesByIds(List<Integer> movieIds) throws IOException {
        List<Movie> movies = new ArrayList<>();
        for (Integer movieId : movieIds) {
            try {
                String url = String.format("%s/movie/%d?api_key=%s&language=es-ES", BASE_URL, movieId, API_KEY);
                String response = makeRequest(url);
                JsonObject movieJson = JsonParser.parseString(response).getAsJsonObject();
                movies.add(parseMovie(movieJson));
            } catch (Exception e) {
                logger.warning("Error al obtener película con ID " + movieId + ": " + e.getMessage());
            }
        }
        return movies;
    }

    private String makeRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Error en la petición HTTP: " + response.statusCode());
        }
        return response.body();
    }

    private Movie parseMovie(JsonObject movieJson) {
        int id = movieJson.get("id").getAsInt();
        String title = movieJson.get("title").getAsString();
        String overview = movieJson.get("overview").getAsString();
        String posterPath = movieJson.get("poster_path").getAsString();
        double voteAverage = movieJson.get("vote_average").getAsDouble();
        int voteCount = movieJson.get("vote_count").getAsInt();
        String releaseDate = movieJson.get("release_date").getAsString();
        
        Movie movie = new Movie(id, title, overview, posterPath, voteAverage, voteCount, releaseDate);
        
        if (movieJson.has("genre_ids")) {
            JsonArray genresJson = movieJson.get("genre_ids").getAsJsonArray();
            for (JsonElement element : genresJson) {
                movie.getGenreIds().add(element.getAsInt());
            }
        }
        
        return movie;
    }

    private MovieDetails parseMovieDetails(JsonObject movieJson) {
        int id = movieJson.get("id").getAsInt();
        String title = movieJson.get("title").getAsString();
        String overview = movieJson.get("overview").getAsString();
        String posterPath = movieJson.get("poster_path").getAsString();
        String releaseDate = movieJson.get("release_date").getAsString();
        double voteAverage = movieJson.get("vote_average").getAsDouble();
        int runtime = movieJson.get("runtime").getAsInt();
        
        JsonArray genresArray = movieJson.get("genres").getAsJsonArray();
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i < genresArray.size(); i++) {
            if (i > 0) genres.append(", ");
            genres.append(genresArray.get(i).getAsJsonObject().get("name").getAsString());
        }

        return new MovieDetails(
            id,
            title,
            overview,
            posterPath,
            releaseDate,
            voteAverage,
            runtime,
            genres.toString()
        );
    }
} 