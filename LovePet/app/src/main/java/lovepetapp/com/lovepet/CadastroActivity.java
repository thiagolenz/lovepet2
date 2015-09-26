package lovepetapp.com.lovepet;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

import lovepetapp.com.lovepet.bean.Usuario;
import lovepetapp.com.lovepet.database.UsuarioDao;
import lovepetapp.com.lovepet.global.PetShoGlobalApplication;
import lovepetapp.com.lovepet.picasso.CircleTransform;

/**
 * Created by macbookpro on 12/08/15.
 */
public class CadastroActivity extends AppCompatActivity {
    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText aniversario;
    private String avatarUsuario;

    private static int RESULT_LOAD_IMAGE = 1;

    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_screen);
        initToolbar();

        aniversario = (EditText)findViewById(R.id.aniversario);
        aniversario.addTextChangedListener(tw);

        nome = (EditText)findViewById(R.id.nome);
        email = (EditText)findViewById(R.id.email);
        senha = (EditText)findViewById(R.id.senha);

        findViewById(R.id.imageSelectedCadastro)
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageSelectedCadastro);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            avatarUsuario = picturePath;
            Picasso.with(CadastroActivity.this).load(new File(picturePath) ).transform(new CircleTransform()).into(imageView);
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri, Intent data) {

        Uri _uri = data.getData();

        // this will be null if no image was selected...
        if (_uri != null) {
            // now we get the path to the image file
            Cursor cursor = getContentResolver().query(_uri, null, null, null, null);
            cursor.moveToFirst();
            String imageFilePath = cursor.getString(0);
            cursor.close();
            return imageFilePath;
        }
        return null;
    }


    public void onCriarCadastroClick (View view) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        usuario.setAniversario(aniversario.getText().toString());
        usuario.setAvatarUsuario(avatarUsuario);
        if (usuario.getAvatarUsuario() == null) {
            Toast toast = Toast.makeText(this, "Selecione uma imagem para o perfil", Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }

        if (usuario.getEmail().length() == 0
                || usuario.getNome().length() == 0
                || usuario.getSenha().length() == 0
                || usuario.getAniversario().length() == 0) {
            Toast toast = Toast.makeText(this, "Informe todos campos", Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }

        new UsuarioDao().salvar(usuario, this);

        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        final PetShoGlobalApplication globalVariable = (PetShoGlobalApplication) getApplicationContext();

        //Set name and email in global/application context
        globalVariable.setUserAvatar(usuario.getAvatarUsuario());
        globalVariable.setUserEmail(usuario.getEmail());
        globalVariable.setUserName(usuario.getNome());

        startActivity(intent);
        finish();
        System.gc();;
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    if (mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                aniversario.setText(current);
                aniversario.setSelection(sel < current.length() ? sel : current.length());
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

}


