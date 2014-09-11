package demo.rhasan.games.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import demo.rhasan.games.R;
import demo.rhasan.games.fragments.GamesFragment;
import demo.rhasan.games.models.Game;
import demo.rhasan.games.utils.Utils;


public class ListGamesActivity extends Activity implements GamesFragment.OnFragmentInteractionListener {

    private static final int FILE_CHOSEN = 1;
    private Dialog mFormDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);

        // in case a silly dev along the way modifies the manifest
        // of this activity to be full screen, nullifying action bar
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.title_activity_list_games));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_game) {
            onActionAddGame();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show the add game dialog form
     */
    private void onActionAddGame() {
        hideGames();
        showAddGameForm();
    }

    private void hideGames() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().hide(fm.findFragmentById(R.id.fragment_games)).commit();
        fm.executePendingTransactions();
    }

    private void showGames() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().show(fm.findFragmentById(R.id.fragment_games)).commit();
        fm.executePendingTransactions();
    }

    private void showAddGameForm() {
        View layout = LayoutInflater.from(this).inflate(R.layout.form_add_game, (ViewGroup) findViewById(R.id.fl_list_games_root), false);
        initializeFormlayout(layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText icon = (EditText) mFormDialog.findViewById(R.id.et_form_game_icon);
                EditText name = (EditText) mFormDialog.findViewById(R.id.et_form_game_name);
                Spinner console = (Spinner) mFormDialog.findViewById(R.id.spin_form_game_console);
                CheckBox finished = (CheckBox) mFormDialog.findViewById(R.id.cb_form_game_finished);
                RatingBar rating = (RatingBar) mFormDialog.findViewById(R.id.rb_form_game_rating);

                Game game = new Game();
                game.setName(name.getText().toString());
                game.setConsole((String) console.getSelectedItem());
                game.setIsFinished(finished.isChecked());
                game.setRating(rating.getRating());

                if (icon.getText() != null &&
                        !icon.getText().toString().isEmpty()) {
                    if (icon.getTag() != null) {
                        game.setIcon((Uri) icon.getTag());
                    }
                }

                ((GamesFragment)
                        getFragmentManager()
                                .findFragmentById(R.id.fragment_games)).addGame(game);
                dialog.dismiss();
                showGames();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mFormDialog = builder.create();

        Window window = mFormDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        mFormDialog.show();
    }

    private void initializeFormlayout(final View layout) {
        Button upload = (Button) layout.findViewById(R.id.btn_form_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select file to upload "),
                        FILE_CHOSEN);
            }
        });
    }

    @Override
    public void onGameItemClick(final ListView l, final View v, final int position, final long id) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case FILE_CHOSEN:
                Uri imagePath = data.getData();
                onIconUpload(imagePath);
                break;
            default:
                Toast.makeText(this, "Could not save image.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void onIconUpload(final Uri imagePath) {
        EditText icon = (EditText) mFormDialog.findViewById(R.id.et_form_game_icon);

        // so users don't set default paths
        icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });

        icon.setText(imagePath.getPath());
        icon.setTag(Utils.getPath(imagePath, this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}