package demo.rhasan.games.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import demo.rhasan.games.models.Game;
import demo.rhasan.games.utils.Utils;

/**
 * Created by rhasan on 9/10/2014.
 */
public class GamesDAO {
    private String[] GAMES_TABLE_COLUMNS = {GamesDatabase.GAME_ID,
            GamesDatabase.GAME_NAME,
            GamesDatabase.GAME_CONSOLE,
            GamesDatabase.GAME_RATING,
            GamesDatabase.GAME_FINISHED,
            GamesDatabase.GAME_ICON
    };
    private SQLiteDatabase database;
    private GamesDatabase dbHelper;
    private Observer mObserver;

    public GamesDAO(Context context) {
        dbHelper = new GamesDatabase(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Game addGame(Game game) {

        ContentValues values = new ContentValues();

        String iconUri = null;
        if (game.getIcon() != null)
            iconUri = game.getIcon().toString();

        String rating = String.valueOf(game.getRating());

        values.put(GamesDatabase.GAME_NAME, game.getName());
        values.put(GamesDatabase.GAME_CONSOLE, game.getConsole());
        values.put(GamesDatabase.GAME_RATING, rating);
        values.put(GamesDatabase.GAME_FINISHED, game.isFinished());

        if (game.getIcon() != null)
            values.put(GamesDatabase.GAME_ICON, game.getIcon().toString());
        else
            values.put(GamesDatabase.GAME_ICON, "");

        long gameId = database.insert(GamesDatabase.GAMES, null, values);
        Cursor cursor = database.query(GamesDatabase.GAMES,
                GAMES_TABLE_COLUMNS, GamesDatabase.GAME_ID + " = "
                        + gameId, null, null, null, null);

        cursor.moveToFirst();

        Game newGame = getGame(cursor);
        cursor.close();

        /*
        *
         */
        if (mObserver != null) {
            mObserver.onContentsChanged();
        }

        return newGame;
    }

    public void deleteGame(Game game) {
        long id = game.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(GamesDatabase.GAMES, GamesDatabase.GAME_ID
                + " = " + id, null);

        if (mObserver != null) {
            mObserver.onContentsChanged();
        }
    }

    public void updateGame(final Game game) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        String iconUri = null;
        if (game.getIcon() != null)
            iconUri = game.getIcon().toString();

        String rating = String.valueOf(game.getRating());

        values.put(GamesDatabase.GAME_NAME, game.getName());
        values.put(GamesDatabase.GAME_CONSOLE, game.getConsole());
        values.put(GamesDatabase.GAME_RATING, rating);
        values.put(GamesDatabase.GAME_FINISHED, game.isFinished());

        if (game.getIcon() != null)
            values.put(GamesDatabase.GAME_ICON, game.getIcon().toString());
        else
            values.put(GamesDatabase.GAME_ICON, "");

        if (mObserver != null) {
            mObserver.onContentsChanged();
        }

        db.update(GamesDatabase.GAMES,
                values, "_id=" + game.getId(), null);
    }

    public List getAllGames() {
        ArrayList<Game> games = new ArrayList<Game>();

        Cursor cursor = database.query(GamesDatabase.GAMES,
                GAMES_TABLE_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game game = getGame(cursor);
            games.add(game);
            cursor.moveToNext();
        }

        cursor.close();
        return games;
    }

    public Cursor getCursor() {
        String query = "SELECT * FROM " + GamesDatabase.GAMES;
        return database.rawQuery(query, null);
    }

    private Game getGame(Cursor cursor) {
        Game game = new Game();
        game.setId((cursor.getInt(0)));
        game.setName(cursor.getString(1));
        game.setConsole(cursor.getString(2));

        String rating = cursor.getString(3);
        game.setRating(Utils.stof(rating));

        boolean finished = Utils.itob(cursor.getInt(4));
        game.setIsFinished(finished);

        if (!cursor.getString(5).isEmpty()) {
            String uriPath = cursor.getString(5);
            Uri uri = Uri.parse(uriPath);
            game.setIcon(uri);
        }

        return game;
    }

    public void registerObserver(final Observer observer) {
        mObserver = observer;
    }

    public void unregisterObserver(final Observer observer) {
        if (mObserver != null) {
            mObserver = null;
        }
    }

    public Cursor getCursorById(final Integer id) {
        Cursor cursor = database.rawQuery("select * from " + GamesDatabase.GAMES + " where "
                + GamesDatabase.GAME_ID + "='" + id + "'", null);
        cursor.moveToFirst();

        return cursor;
    }

    public interface Observer {
        public void onContentsChanged();
    }

    public static Game toGame(final Cursor cursor) {
        Game game = new Game();

        int id = (cursor.getInt(cursor.getColumnIndex(GamesDatabase.GAME_ID)));
        game.setId(id);

        String name = (cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_NAME)));
        game.setName(name);

        String console = (cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_CONSOLE)));
        game.setConsole(console);

        String uriStr = cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_ICON));
        if (uriStr == null || uriStr.isEmpty()) {
            game.setIcon(null);
        } else {
            game.setIcon((Uri.parse(uriStr)));
        }

        boolean finished = (Utils.itob(cursor.getInt(cursor.getColumnIndex(GamesDatabase.GAME_FINISHED))));
        game.setIsFinished(finished);

        float rating = Utils.stof(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_RATING)));
        game.setRating(rating);

        return game;
    }
}
