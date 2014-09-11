package demo.rhasan.games.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import demo.rhasan.games.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnListGames = (Button) findViewById(R.id.btn_list_games);
        Button btnRateGames = (Button) findViewById(R.id.btn_rate_games);

        btnListGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(MainActivity.this, ListGamesActivity.class));
            }
        });

        btnRateGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(MainActivity.this, RateGamesActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_game) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
