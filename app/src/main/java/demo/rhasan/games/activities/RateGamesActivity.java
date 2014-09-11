package demo.rhasan.games.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import demo.rhasan.games.R;
import demo.rhasan.games.fragments.GamesFragment;

public class RateGamesActivity extends Activity implements GamesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_games);

        ((GamesFragment) getFragmentManager().findFragmentById(R.id.fragment_games))
                .setType(GamesFragment.TYPE_RATE_GAME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_game) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameItemClick(final ListView l, final View v, final int position, final long id) {
        // perform any controller/business related action here
    }
}
