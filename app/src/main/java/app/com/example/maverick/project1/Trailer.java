package app.com.example.maverick.project1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maverick on 1/5/16.
 */
public class Trailer implements Parcelable {
    @SerializedName("key")
    private String mkey;

    @SerializedName("name")
    private String mName;

    @SerializedName("type")
    private String mType;

    public Trailer(){}

    public Trailer(String mkey, String mName, String mType) {
        this.mkey = mkey;
        this.mName = mName;
        this.mType = mType;
    }
    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmType() {
        return mType;
    }
    public String getMkey() {
        return mkey;
    }
    public String getmName() {
        return mName;
    }

    protected Trailer(Parcel in) {

        mkey = in.readString();
        mName = in.readString();
        mType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mkey);
        dest.writeString(mName);
    }
}
