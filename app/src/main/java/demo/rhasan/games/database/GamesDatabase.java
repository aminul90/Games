package demo.rhasan.games.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import demo.rhasan.games.utils.Constants;

public class GamesDatabase extends SQLiteOpenHelper {

    public static final String GAMES = "Games";
    public static final String GAME_ID = "_id";
    public static final String GAME_NAME = "name";
    public static final String GAME_CONSOLE = "console";
    public static final String GAME_RATING = "rating";
    public static final String GAME_FINISHED = "finished";
    public static final String GAME_ICON = "icon";

    private static final String DATABASE_NAME = "Games.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + GAMES
            + "(" + GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GAME_NAME + " TEXT NOT NULL, "
            + GAME_CONSOLE + " TEXT NOT NULL, "
            + GAME_RATING + " TEXT NOT NULL, "
            + GAME_FINISHED + " INTEGER, "
            + GAME_ICON + " TEXT NOT NULL" +
            ");";

    public GamesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(Constants.TAG,
                "Database is upgrading.");

        db.execSQL("DROP TABLE IF EXISTS " + GAMES);
        onCreate(db);
    }
}