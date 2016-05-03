package app.com.example.maverick.project1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by maverick on 1/5/16.
 */
public class MovieTrailer implements Parcelable {
    @SerializedName("results")
    ArrayList<Trailer> trailerArrayList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(trailerArrayList);
    }

    public MovieTrailer() {
    }

    protected MovieTrailer(Parcel in) {
        this.trailerArrayList = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
