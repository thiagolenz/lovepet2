package lovepetapp.com.lovepet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lovepetapp.com.lovepet.bean.Usuario;

/**
 * Created by macbookpro on 14/08/15.
 */
public class UsuarioDao {

    public boolean salvar (Usuario usuario, Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("aniversario", usuario.getAniversario());

        long resultado = db.insert("usuario", null, values);
        db.close();
        return resultado != -1;
    }

    public Usuario findLogin (String email, String senha, Context context) {
       try {
           DatabaseHelper helper = new DatabaseHelper(context);
           SQLiteDatabase db = helper.getReadableDatabase();
           String sql = "SELECT _id, nome, email, senha, aniversario FROM usuario  " +
                   " where email = ? and senha = ?";

           String[] selectionArgs = new String[]{email, senha};
           Cursor cursor = db.rawQuery(sql, selectionArgs);
           cursor.moveToFirst();

           return montaUsuario(cursor);
       }catch (RuntimeException e) {
           e.printStackTrace();
       }
        return null;
    }

    public Usuario montaUsuario (Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        usuario.setAniversario(cursor.getString(cursor.getColumnIndex("aniversario")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));

        return usuario;
    }
}
