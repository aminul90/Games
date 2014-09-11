package demo.rhasan.games.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by rhasan on 9/10/2014.
 */
public class Utils {
    private Utils() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton!");
    }

    public static Drawable getImageFromPath(final String imageFilePath) {
        return null;
    }

    public static boolean itob(final int anInt) {
        return Boolean.parseBoolean(String.valueOf(anInt));
    }

    public static float stof(final String aString) {
        return Float.parseFloat(String.valueOf(aString));
    }

    public static final String getPath(final Uri uri, final Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
