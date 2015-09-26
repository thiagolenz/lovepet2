package lovepetapp.com.lovepet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macbookpro on 13/08/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "PetLove3";

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuario (_id INTEGER PRIMARY KEY," +
                " nome TEXT, email TEXT, aniversario TEXT, senha TEXT, avatarUsuario TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
