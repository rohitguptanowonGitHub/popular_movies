package app.com.example.maverick.project1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maverick on 18/3/16.
 */
public class Movie implements Parcelable{

    private String
            title,
            releaseDate,
            moviePoster,
            voteAverage,
            plotSynopsis;

    //private Bitmap moviePosterBitmap;

    public Movie(){

    }

    protected Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
        //moviePosterBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    //public Bitmap getMoviePosterBitmap() {
    //    return moviePosterBitmap;
    //}
    //public void setMoviePosterBitmap(Bitmap moviePosterBitmap) {
    //    this.moviePosterBitmap = moviePosterBitmap;
    //}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(moviePoster);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
        //parcel.writeParcelable(moviePosterBitmap, i);
    }
}
