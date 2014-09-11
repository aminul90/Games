package demo.rhasan.games.models;

import android.net.Uri;

/**
 * Created by rhasan on 9/10/2014.
 */
public class Game {

    public static final int XBOX = 0;
    public static final int PS = 1;
    public static final int Wii = 2;
    public static final int OTHER = 99;

    private String mName;
    private String mConsole;
    private Uri iconPath;
    private boolean mIsFinished;
    private float mRating = 0.0f;
    private long mId;

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        this.mName = name;
    }

    public String getConsole() {
        return mConsole;
    }

    public void setConsole(final String console) {
        this.mConsole = console;
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public void setIsFinished(final boolean finished) {
        this.mIsFinished = finished;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(final float rating) {
        this.mRating = rating;
    }

    public Uri getIcon() {
        return iconPath;
    }

    public void setIcon(final Uri iconPath) {
        this.iconPath = iconPath;
    }

    public void setId(final long id) {
        this.mId = id;
    }

    public long getId() {
        return mId;
    }

}
