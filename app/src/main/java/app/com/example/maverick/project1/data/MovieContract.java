package app.com.example.maverick.project1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by maverick on 1/5/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "app.com.example.maverick.project1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";

    public static final class MovieEntry implements BaseColumns{

        // table name
        public static final String TABLE_NAME = "movie";

        // columns
        //public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";

        // create content uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // for building uris on insertion
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static Uri buildMovieUriFromTitle(String movieName){
            return CONTENT_URI.buildUpon().appendPath(movieName).build();
        }
        public static String getMovieTitleFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        // table name
        public static final String TABLE_NAME = "review";

        // columns
        public static final String COLUMN_MOVIE_KEY = "movie_key";
        public static final String COLUMN_AUTOR = "author";
        public static final String COLUMN_CONTENT = "content";

        // create content uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // for building uris on insertion
        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TrailerEntry implements BaseColumns {

        // table name
        public static final String TABLE_NAME = "trailer";

        // columns
        public static final String COLUMN_MOVIE_KEY = "movie_key";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_TRAILER_NAME = "name";
        public static final String COLUMN_TRAILER_TYPE = "type";

        // create content uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // for building uris on insertion
        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

}
