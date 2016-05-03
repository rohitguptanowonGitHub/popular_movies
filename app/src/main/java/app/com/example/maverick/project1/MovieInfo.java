package app.com.example.maverick.project1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by maverick on 29/4/16.
 */
public class MovieInfo implements Parcelable {

//    @SerializedName("page")
//    public int page;

    @SerializedName("results")
    public ArrayList<Movie> movieArrayList;

//    @SerializedName("total_pages")
//    public int totalPages;
//
//    @SerializedName("total_results")
//    public int totalResults;

    protected MovieInfo(Parcel in) {
        //page = in.readInt();
        movieArrayList = in.createTypedArrayList(Movie.CREATOR);
        //totalPages = in.readInt();
        //totalResults = in.readInt();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(page);
        dest.writeTypedList(movieArrayList);
        //dest.writeInt(totalPages);
        //dest.writeInt(totalResults);
    }
}
