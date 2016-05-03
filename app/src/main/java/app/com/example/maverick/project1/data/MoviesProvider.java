package app.com.example.maverick.project1.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by maverick on 1/5/16.
 */
public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper moviesDbHelper;

    static final int MOVIE = 100;
    static final int REVIEW = 200;
    static final int TRAILER = 300;
    static final int MOVIE_WITH_TITLE = 400;
//    static final int REVIEW_WITH_KEY = 500;
//    static final int TRAILER_WITH_KEY = 600;

    private static final SQLiteQueryBuilder sMovieQueryBuilder;

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case REVIEW :{
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME,null,value);
                        if(_id != -1) {
                            return returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case TRAILER :{
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,value);
                        if(_id != -1) {
                            return returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static {
//        sReviewQueryBuilder = new SQLiteQueryBuilder();
//        sReviewQueryBuilder.setTables(MovieContract.ReviewEntry.TABLE_NAME +
//                        " INNER JOIN "+ MovieContract.MovieEntry.TABLE_NAME +" ON " +
//                        MovieContract.ReviewEntry.TABLE_NAME+"."+ MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = "+
//                        MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry._ID
//        );
//
//        sTrailerQueryBuilder = new SQLiteQueryBuilder();
//        sTrailerQueryBuilder.setTables(MovieContract.TrailerEntry.TABLE_NAME +
//                        " INNER JOIN "+ MovieContract.MovieEntry.TABLE_NAME +" ON " +
//                        MovieContract.TrailerEntry.TABLE_NAME+"."+ MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = "+
//                        MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry._ID
//        );
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder.setTables(
                                MovieContract.ReviewEntry.TABLE_NAME +","+MovieContract.TrailerEntry.TABLE_NAME +
                                " INNER JOIN "+MovieContract.MovieEntry.TABLE_NAME +" ON " +
                                MovieContract.ReviewEntry.TABLE_NAME+"."+ MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = "+
                                MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry._ID + " AND " +
                                MovieContract.TrailerEntry.TABLE_NAME+"."+ MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = "+
                                MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry._ID

        );

    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIE,MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEW,REVIEW);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER,TRAILER);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIE + "/*",MOVIE_WITH_TITLE);
//        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEW +"/#",REVIEW_WITH_KEY);
//        matcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER + "/#",TRAILER_WITH_KEY);
        return matcher;
    }

    private static final String sMovieNameSelection =
            MovieContract.MovieEntry.TABLE_NAME + "."+ MovieContract.MovieEntry.COLUMN_TITLE + " = ? ";
//    private static final String sReviewSelection =
//            MovieContract.ReviewEntry.TABLE_NAME+ "." + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = ? ";
//    private static final String sTrailerSelection =
//            MovieContract.ReviewEntry.TABLE_NAME+ "." + MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = ? ";


    private Cursor getMovieFromTitle(Uri uri){
        String movieTitle = MovieContract.MovieEntry.getMovieTitleFromUri(uri);
        String selectionArgs[] = new String[]{movieTitle};
        return sMovieQueryBuilder.query(moviesDbHelper.getReadableDatabase(),
                null,
                sMovieNameSelection,
                selectionArgs,
                null,
                null,
                null
        );
    }
//    private Cursor getReviewFromKey(Uri uri){
//        Long movieKey = Long.valueOf(MovieContract.ReviewEntry.getMovieKeyFromUri(uri));
//        String[] selectionArgs = new String[] { Long.toString(movieKey)};
//        return sReviewQueryBuilder.query(moviesDbHelper.getWritableDatabase(),
//                null,
//                sReviewSelection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//
//    }
//    private Cursor getTrailerFromKey(Uri uri){
//        Long movieKey = Long.valueOf(MovieContract.TrailerEntry.getMovieKeyFromUri(uri));
//        String[] selectionArgs = new String[] { Long.toString(movieKey)};
//        return sReviewQueryBuilder.query(moviesDbHelper.getWritableDatabase(),
//                null,
//                sReviewSelection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//
//    }

    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE_WITH_TITLE :{
                retCursor = getMovieFromTitle(uri);
                break;
            }
//            case REVIEW_WITH_KEY:{
//                retCursor = getReviewFromKey(uri);
//                break;
//            }
//            case TRAILER_WITH_KEY:{
//                retCursor = getTrailerFromKey(uri);
//                break;
//            }
            case MOVIE:{
                retCursor = moviesDbHelper.getWritableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW:{
                retCursor = moviesDbHelper.getWritableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILER:{
                retCursor = moviesDbHelper.getWritableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE :
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case REVIEW :
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case TRAILER :
                return MovieContract.TrailerEntry.CONTENT_TYPE;
//            case REVIEW_WITH_KEY :
//                return MovieContract.ReviewEntry.CONTENT_TYPE;
//            case TRAILER_WITH_KEY :
//                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case MOVIE_WITH_TITLE :
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE :{
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER :{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,values);
                if( _id > 0 )
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW : {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME,null,values);
                if( _id > 0 )
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final int rowsDeleted;

        if(null == selection) selection = "1";

        switch (match){
            case MOVIE: {
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case TRAILER: {
                rowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
