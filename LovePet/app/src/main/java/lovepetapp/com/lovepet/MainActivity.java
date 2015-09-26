package lovepetapp.com.lovepet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import lovepetapp.com.lovepet.global.PetShoGlobalApplication;
import lovepetapp.com.lovepet.picasso.CircleTransform;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.activity_main);

        final PetShoGlobalApplication globalVariable = (PetShoGlobalApplication) getApplicationContext();

        TextView name = (TextView) findViewById(R.id.txt_userName);
        name.setText(globalVariable.getUserName());

        TextView email = (TextView) findViewById(R.id.email);
        email.setText(globalVariable.getUserEmail());

        String avatarFile = globalVariable.getUserAvatar();

        ImageView imageViewPrincipal = (ImageView) findViewById(R.id.avatarDog);
        final ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Picasso.with(MainActivity.this).load(new File(avatarFile)).transform(new CircleTransform()).into(imageViewPrincipal);
        Picasso.with(MainActivity.this).load(new File(avatarFile)).transform(new CircleTransform()).into(avatar);

        //initRecyclerView();
        //initFab();
        initToolbar();
        setupDrawerLayout();

        content = findViewById(R.id.content);

        final ListView listview = (ListView) findViewById(R.id.actionList);
        String[] values = new String[]{"Consultas", "Vacinações", "Medicamentos",
                "Banho", "Ache um Pet", "Tosa"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.main_list_item_view, R.id.custom_item_label, values);
        listview.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 4 ) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//                    alert.setTitle("Local Atual");
                    TextView title = new TextView(MainActivity.this);
                    title.setTextColor(Color.parseColor("#f276a0"));
                    title.setPadding(40, 20, 10, 10);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setAllCaps(true);
                    title.setTextSize(16);
                    title.setText("Local Atual");
                    alert.setCustomTitle(title);
                    alert.setMessage("Rua Carlos Frederico Campos, 630 - Ouro Preto");

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(MainActivity.this, ListaPetShopsActivity.class);
                            startActivity(intent);
                            System.gc();
                        }
                    });

                    alert.show();

                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, VacinacaoActivity.class);
                    startActivity(intent);
                    System.gc();;
                }
            }
        });
//
//        GPSTracker gps = new GPSTracker(this);
//        if (gps.canGetLocation()) {
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void initRecyclerView() {
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
//        adapter.setOnItemClickListener(this);
//        recyclerView.setAdapter(adapter);
    }

    private void initFab() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, ViewModel viewModel) {
//        Intent intent = new Intent(MainActivity.this, PetMapActivity.class);
//        startActivity(intent);
    }
}



