package demo.rhasan.games.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by rhasan on 8/25/2014.
 */
public class DialogUtils {
    /**
     * Shows the standard Android dialog with custom parameters with title &
     * message controls.
     *
     * @param context    The context containing the dialog.
     * @param title      The title of the dialog.
     * @param message    The message of the dialog.
     * @param buttonText The message of the dialog.
     */
    public static void showAlertDialog(final Context context,
                                       final String title, final String message, final String buttonText) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {
                    }
                });

        alertDialog.show();
    }
}
