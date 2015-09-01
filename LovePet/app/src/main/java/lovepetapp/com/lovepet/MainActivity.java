package lovepetapp.com.lovepet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lovepetapp.com.lovepet.picasso.CircleTransform;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initRecyclerView();
        //initFab();
        initToolbar();
        setupDrawerLayout();

        content = findViewById(R.id.content);

        final ImageView avatar = (ImageView) findViewById(R.id.avatar);
       // Picasso.with(this).load(R.drawable.ic_user_avatar).transform(new CircleTransform()).into(avatar);
//        loadBitmapAvatar(R.drawable.ic_user_avatar, avatar);

        final ImageView avatarDog = (ImageView) findViewById(R.id.avatarDog);
       // Picasso.with(this).load(R.drawable.cachorro).transform(new CircleTransform()).into(avatarDog);

//        avatarDog.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(), R.id.avatarDog, 100, 100));
        loadBitmapDog(R.id.avatarDog, avatarDog, avatar);

        final ListView listview = (ListView) findViewById(R.id.actionList);
        String[] values = new String[]{"Consultas", "Vacinações", "Medicamentos",
                "Banho", "Ache um PET"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.main_list_item_view, R.id.custom_item_label, values);
        listview.setAdapter(adapter);
//
//        GPSTracker gps = new GPSTracker(this);
//        if (gps.canGetLocation()) {
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        }
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

    public void loadBitmapDog(int resId, ImageView imageViewDog, ImageView imageViewAvatar) {
        BitmapWorkerTaskDog task = new BitmapWorkerTaskDog(imageViewDog, imageViewAvatar);
        task.execute(resId);
    }

    public void loadBitmapAvatar(int resId, ImageView imageView) {
        BitmapWorkerTaskUser task = new BitmapWorkerTaskUser(imageView);
        task.execute(resId);
    }

    @Override
    public void onItemClick(View view, ViewModel viewModel) {
//        Intent intent = new Intent(MainActivity.this, PetMapActivity.class);
//        startActivity(intent);
    }

    class BitmapWorkerTaskDog extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;
        private ImageView imageViewAvatar;
        private ImageView imageView;

        public BitmapWorkerTaskDog(ImageView imageView, ImageView imageViewAvatar) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.imageView = imageView;
            this.imageViewAvatar = imageViewAvatar;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return Utils.decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Picasso.with(MainActivity.this).load(R.drawable.cachorro).transform(new CircleTransform()).into(imageView);

            try{
                Thread.sleep(2000);
            }catch (Exception e) {
                e.printStackTrace();;
            }

            BitmapWorkerTaskUser task = new BitmapWorkerTaskUser(imageViewAvatar);
            task.execute(R.drawable.ic_user_avatar);
        }
    }

    class BitmapWorkerTaskUser extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;
        private ImageView imageView;

        public BitmapWorkerTaskUser(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.imageView = imageView;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return Utils.decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Picasso.with(MainActivity.this).load(R.drawable.ic_user_avatar).transform(new CircleTransform()).into(imageView);
        }
    }
}



