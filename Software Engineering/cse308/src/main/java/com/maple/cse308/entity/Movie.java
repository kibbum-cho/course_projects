package com.maple.cse308.entity;

import javax.persistence.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movieId", nullable = false)
    private Integer movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "overview", length = 2048)
    private String overview;

    @Column(name = "director")
    private String director;

    @Column(name = "ratingAvg")
    private Float ratingAvg;

    @Column(name = "ratingCount")
    private Integer ratingCount;

    @Column(name = "mpaaRating")
    private String mpaaRating;

    @Column(name = "revenue")
    private Integer revenue;

    @Column(name = "releaseDate")
    private Date releaseDate;

    @Column(name = "poster")
    private String poster;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "trailerThumb")
    private String trailerThumb;

    @Column(name = "runtime")
    private String runtime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "movieId")
    private Set<Writer> writers = new HashSet();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "movieGenre", joinColumns = @JoinColumn(name = "movieId"), inverseJoinColumns = @JoinColumn(name = "genreId"))
    private Set<Genre> genre = new HashSet();

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Float getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Float ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getRevenue() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String numberAsString = decimalFormat.format(revenue);
        return numberAsString;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public int getRevenueUnformatted() {
        return revenue;
    }

    //needed for backend
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    //use this for frontend
    public String getDateString() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("MMM dd, yyyy");
        String date = dataFormat.format(releaseDate);
        return date;
    }

    public String getDateForList() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("MMM dd");
        String date = dataFormat.format(releaseDate);
        return date;
    }

    public String getReleaseYear() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy");
        String year = dataFormat.format(releaseDate);
        return year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTrailerThumb() {
        return trailerThumb;
    }

    public void setTrailerThumb(String trailerThumb) {
        this.trailerThumb = trailerThumb;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public Set<Writer> getWriters() {
        return writers;
    }

    public void setWriters(Set<Writer> writers) {
        this.writers = writers;
    }

    public Set<Genre> getGenre() {
        return genre;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre;
    }

}