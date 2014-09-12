package demo.rhasan.games.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import demo.rhasan.games.R;
import demo.rhasan.games.database.GamesDAO;
import demo.rhasan.games.database.GamesDatabase;
import demo.rhasan.games.database.GamesLoader;
import demo.rhasan.games.models.Game;
import demo.rhasan.games.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GamesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final int LOADER_ID = 0;

    // TODO: Rename and change types of parameters
    /**
     * This fragment supports 2 types of ways to represent
     * its list of games.  One way is to show itself with a way
     * to rate the games.  The other type gives a way to mark
     * a game as finished or not.
     */
    public static final int TYPE_RATE_GAME = 0;
    public static final int TYPE_FINISHED_GAME = 1;

    private OnFragmentInteractionListener mListener;
    private ResourceCursorAdapter mAdapter;
    private GamesDAO mDatabase;
    private int mFragmentType = -1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        mDatabase = new GamesDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_list, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFragmentType == TYPE_RATE_GAME) {
            mAdapter = new GamesRatedCursorAdapter(
                    getActivity(), R.layout.fragment_games_rate_item, mDatabase.getCursor(), 0);
        } else {
            mAdapter = new GamesFinishedCursorAdapter(
                    getActivity(), R.layout.fragment_games_finished_item, mDatabase.getCursor(), 0);
        }

        setListAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Create a new CursorLoader with the following query parameters.
        return new GamesLoader(getActivity(), mDatabase);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void addGame(final Game game) {
        mDatabase.addGame(game);
    }

    public void updateGame(final Game game) {
        mDatabase.updateGame(game);
    }

    public void setType(final int fragmentType) {
        this.mFragmentType = fragmentType;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        if (mListener != null) {
            mListener.onGameItemClick(adapterView, view, i, l);
        }
    }

    /**
     * The below comments are generated automatically, nonetheless they should definitely
     * be followed!!!
     * <p/>
     * <p/>
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onGameItemClick(AdapterView<?> adapterView, View v, int position, long id);
    }


    /**
     * Bonus babyyyy
     */
    public class GamesFinishedCursorAdapter extends ResourceCursorAdapter {

        public GamesFinishedCursorAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, layout, c, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView name = (TextView) view.findViewById(R.id.tv_game_name);
            name.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_NAME)));

            TextView console = (TextView) view.findViewById(R.id.tv_game_console);
            console.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_CONSOLE)));

            ImageView icon = (ImageView) view.findViewById(R.id.iv_game_icon);
            icon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_ICON))));

            CheckBox finished = (CheckBox) view.findViewById(R.id.cb_game_finished);
            finished.setChecked(Utils.itob(cursor.getInt(cursor.getColumnIndex(GamesDatabase.GAME_FINISHED))));
        }

        @Override
        public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_games_finished_item, parent, false);

            TextView name = (TextView) view.findViewById(R.id.tv_game_name);
            name.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_NAME)));

            TextView console = (TextView) view.findViewById(R.id.tv_game_console);
            console.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_CONSOLE)));

            ImageView icon = (ImageView) view.findViewById(R.id.iv_game_icon);
            icon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_ICON))));

            CheckBox finished = (CheckBox) view.findViewById(R.id.cb_game_finished);
            finished.setChecked(Utils.itob(cursor.getInt(cursor.getColumnIndex(GamesDatabase.GAME_FINISHED))));

            return view;
        }
    }

    /**
     * Bonus babyyyy
     */
    public class GamesRatedCursorAdapter extends ResourceCursorAdapter {

        public GamesRatedCursorAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, layout, c, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView name = (TextView) view.findViewById(R.id.tv_game_name);
            name.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_NAME)));

            TextView console = (TextView) view.findViewById(R.id.tv_game_console);
            console.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_CONSOLE)));

            ImageView icon = (ImageView) view.findViewById(R.id.iv_game_icon);
            icon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_ICON))));

            RatingBar rating = (RatingBar) view.findViewById(R.id.rb_game_rating);
            rating.setRating(Utils.stof(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_RATING))));
        }

        @Override
        public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_games_rate_item, parent, false);

            TextView name = (TextView) view.findViewById(R.id.tv_game_name);
            name.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_NAME)));

            TextView console = (TextView) view.findViewById(R.id.tv_game_console);
            console.setText(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_CONSOLE)));

            ImageView icon = (ImageView) view.findViewById(R.id.iv_game_icon);
            icon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_ICON))));

            RatingBar rating = (RatingBar) view.findViewById(R.id.rb_game_rating);
            rating.setRating(Utils.stof(cursor.getString(cursor.getColumnIndex(GamesDatabase.GAME_RATING))));

            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float v, final boolean b) {

                }
            });

            return view;
        }

    }

    @Override
    public void onDestroy() {

        if (mDatabase != null) {
            mDatabase.close();
        }

        super.onDestroy();
    }

    /**
     * What a list view implementation would look like using convert view pattern..
     * A recycler view in Android L would force the ViewHolder implementation
     */
    private static class GamesFinishedAdapter extends ArrayAdapter<Game> {
        private ArrayList<Game> mGames;

        public GamesFinishedAdapter(final Context context, final int resource,
                                    final ArrayList<Game> games) {
            super(context, resource, games);
            mGames = games;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            Game game = getItem(position);

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View rootView = inflater.inflate(R.layout.fragment_games_finished_item, parent, false);
                holder.name = (TextView) rootView.findViewById(R.id.tv_game_name);
                holder.consoleName = (TextView) rootView.findViewById(R.id.tv_game_console);
                holder.gameIcon = (ImageView) rootView.findViewById(R.id.iv_game_icon);
                holder.finished = (CheckBox) rootView.findViewById(R.id.cb_game_finished);

                convertView = rootView;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(game.getName());
            holder.consoleName.setText(game.getConsole());
            holder.finished.setChecked(game.isFinished());
            holder.gameIcon.setImageURI(game.getIcon());

            return convertView;
        }

        static class ViewHolder {
            public TextView name;
            public TextView consoleName;
            public ImageView gameIcon;
            public CheckBox finished;
        }
    }

}
