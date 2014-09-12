package demo.rhasan.games.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

public class GamesLoader extends AsyncTaskLoader<Cursor> implements
        GamesDAO.Observer {

    private Cursor mData;
    private GamesDAO mDatabase;

    public GamesLoader(Context ctx, final GamesDAO database) {
        super(ctx);
        mDatabase = database;
        mDatabase.registerObserver(this);
    }


    @Override
    public Cursor loadInBackground() {
        return mDatabase.getCursor();
    }


    @Override
    public void deliverResult(Cursor data) {
        if (isReset()) {
            release(data);
            return;
        }

        Cursor oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldData != null && oldData != data) {
            release(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();
        if (mData != null) {
            release(mData);
            mData = null;
        }

        if (mDatabase != null) {
            mDatabase.unregisterObserver(this);
        }
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
        release(data);
    }

    private void release(Cursor data) {
        if (data != null) data.close();
    }

    @Override
    public void onContentsChanged() {
        onContentChanged();
    }
}