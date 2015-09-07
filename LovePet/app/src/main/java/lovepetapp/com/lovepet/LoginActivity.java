package lovepetapp.com.lovepet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import lovepetapp.com.lovepet.bean.Usuario;
import lovepetapp.com.lovepet.database.UsuarioDao;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initToolbar();

        email = (EditText) findViewById(R.id.login_email);
        senha = (EditText) findViewById(R.id.login_senha);
    }

    public void onCadastrarClick (View view) {
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
    }

    public void onEntrarClick (View view) {
        Usuario usuario = new UsuarioDao().findLogin(email.getText().toString(), senha.getText().toString(), this);
        System.out.println(usuario);
        if (usuario == null) {
            Toast toast = Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish ();
        }

    }


    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
