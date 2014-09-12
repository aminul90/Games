package demo.rhasan.games.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import demo.rhasan.games.R;
import demo.rhasan.games.fragments.GamesFragment;
import demo.rhasan.games.models.Game;


public class ListGamesActivity extends Activity implements GamesFragment.OnFragmentInteractionListener {

    private static final int FILE_CHOSEN = 1;
    private static final int FILE_CHOSEN_KIT_KAT = 2;
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

    @Override
    protected void onResume() {
        super.onResume();
        showGames();
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
        final View layout = LayoutInflater.from(this).inflate(R.layout.form_add_game, (ViewGroup) findViewById(R.id.fl_list_games_root), false);
        initializeFormlayout(layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText icon = (EditText) layout.findViewById(R.id.et_form_game_icon);
                EditText name = (EditText) layout.findViewById(R.id.et_form_game_name);
                Spinner console = (Spinner) layout.findViewById(R.id.spin_form_game_console);
                CheckBox finished = (CheckBox) layout.findViewById(R.id.cb_form_game_finished);
                RatingBar rating = (RatingBar) layout.findViewById(R.id.rb_form_game_rating);


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

                addGameToFragment(game);
                dialog.dismiss();

                showGames();
            }


        });

        mFormDialog = builder.create();
        mFormDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialogInterface) {
                showGames();
            }
        });

        Window window = mFormDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        mFormDialog.show();
    }

    private void addGameToFragment(final Game game) {
        ((GamesFragment)
                getFragmentManager()
                        .findFragmentById(R.id.fragment_games)).addGame(game);
    }

    private void updateGameFromFragment(final Game game) {
        ((GamesFragment)
                getFragmentManager()
                        .findFragmentById(R.id.fragment_games)).updateGame(game);
    }

    private void initializeFormlayout(final View layout) {
        Button upload = (Button) layout.findViewById(R.id.btn_form_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // tested on nexus 5, 4.4
                // system dialog way should be loaded
                // or URI won't persist right in the database
                // the rights go away
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select file to upload "),
                            FILE_CHOSEN);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, FILE_CHOSEN_KIT_KAT);
                }
            }
        });
    }

    @Override
    public void onGameItemClick(final AdapterView<?> l, final View v, final int position, final long id) {
        Game game = (Game) l.getItemAtPosition(position);
        updateGameFromFragment(game);
    }

    @Override
    public void onLongGameItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        showEditGameForm((Game) adapterView.getItemAtPosition(i));
    }

    private void showEditGameForm(final Game game) {
        final View layout = LayoutInflater.from(this).inflate(R.layout.form_add_game, (ViewGroup) findViewById(R.id.fl_list_games_root), false);
        initializeFormlayoutData(layout, game);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText icon = (EditText) layout.findViewById(R.id.et_form_game_icon);
                EditText name = (EditText) layout.findViewById(R.id.et_form_game_name);
                Spinner console = (Spinner) layout.findViewById(R.id.spin_form_game_console);
                CheckBox finished = (CheckBox) layout.findViewById(R.id.cb_form_game_finished);
                RatingBar rating = (RatingBar) layout.findViewById(R.id.rb_form_game_rating);

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

                addGameToFragment(game);
                dialog.dismiss();

                showGames();
            }
        });

        mFormDialog = builder.create();
        mFormDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialogInterface) {
                showGames();
            }
        });

        Window window = mFormDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        mFormDialog.show();
    }

    private void initializeFormlayoutData(final View layout, Game game) {
        EditText icon = (EditText) layout.findViewById(R.id.et_form_game_icon);
        EditText name = (EditText) layout.findViewById(R.id.et_form_game_name);
        Spinner console = (Spinner) layout.findViewById(R.id.spin_form_game_console);
        CheckBox finished = (CheckBox) layout.findViewById(R.id.cb_form_game_finished);
        RatingBar rating = (RatingBar) layout.findViewById(R.id.rb_form_game_rating);

        if (game.getIcon() != null)
            icon.setText(game.getIcon().getPath());

        if (game.getName() != null)
            name.setText(game.getName());

        if (game.getConsole() != null) {
            String[] spinnerArr = getResources().getStringArray(R.array.spinner_game_console_entries);
            if (game.getConsole().equals(spinnerArr[0])) {
                console.setSelection(0);
            } else if (game.getConsole().equals(spinnerArr[1])) {
                console.setSelection(1);
            } else {
                console.setSelection(2);
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case FILE_CHOSEN:
            case FILE_CHOSEN_KIT_KAT:
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
        icon.setFocusable(true);
        icon.setText(imagePath.getPath());
        icon.setActivated(false);

        icon.setTag(imagePath);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
