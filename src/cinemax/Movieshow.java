package cinemax;

import java.sql.Date;
import java.sql.Time;

public class Movieshow {
    private int showId;
    private Time startTime;
    private Time endTime;
    private Date date;
    private int price;
    private int screenId;
    private String movieName;
    private int movieId;
    private String showType;
    private boolean ac;
    public Movieshow(int showId, Time startTime, Time endTime, Date date, int price, int screenId, String movieName, int movieId, String showType, boolean ac) {
        this.showId = showId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.price = price;
        this.screenId = screenId;
        this.movieName = movieName;
        this.movieId = movieId;
        this.showType = showType;
        this.ac = ac;
    }}