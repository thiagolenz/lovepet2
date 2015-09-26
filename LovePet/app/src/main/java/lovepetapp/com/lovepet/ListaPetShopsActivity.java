package lovepetapp.com.lovepet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 02/09/15.
 */
public class ListaPetShopsActivity extends AppCompatActivity {
    private List<PetItemBean> petList = new ArrayList<>();
    private String avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.lista_pet_layout);
        initToolbar();

        this.avatarFile = getIntent().getStringExtra("userAvatar");

        final ListView listview = (ListView) findViewById(R.id.pet_list_view);

        petList.add(new PetItemBean("Animal 10", "Petshop", true, "10km", "10min - 30min", R.drawable.logo_animal_10));
        petList.add(new PetItemBean("Cães e cia", "Petshop", false, "10km", "10min - 30min", R.drawable.logo_caes_e_cia));
        petList.add(new PetItemBean("Center Pet", "Petshop", true, "10km", "10min - 50min", R.drawable.logo_center_pet));
        petList.add(new PetItemBean("Louco Por bixo", "Petshop Estética", true, "10km", "30min - 30min", R.drawable.logo_loucoporbicho));
        petList.add(new PetItemBean("Penelope", "Petshop", false, "10km", "10min - 30min", R.drawable.logo_penelope));
        petList.add(new PetItemBean("PetPolly", "Petshop", false, "10km", "10min - 10min", R.drawable.logo_pet_polly));
        petList.add(new PetItemBean("Univet PetShop", "Petshop", true, "10km", "10min - 20min", R.drawable.logo_univet_pet_shop));
        petList.add(new PetItemBean("Bem Estar Animal PetShop", "Petshop", true, "10km", "10min - 20min", R.drawable.logo_bem_estar_petshop));

        setListViewHeightBasedOnChildren(listview);
        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, petList.toArray(new PetItemBean[petList.size()]));
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ListaPetShopsActivity.this, PetSingleInfoActivity.class);
            intent.putExtra("userAvatar", ListaPetShopsActivity.this.avatarFile);
            startActivity(intent);
            System.gc();
            }
        });

    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListaPet);
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

    class MySimpleArrayAdapter extends ArrayAdapter<PetItemBean> {
        private final Context context;
        private final PetItemBean[] values;

        public MySimpleArrayAdapter(Context context, PetItemBean[] values) {
            super(context, R.layout.pet_list_view_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.pet_list_view_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.pet_name_label);
            TextView textOpen = (TextView) rowView.findViewById(R.id.pet_status_open);
            TextView textPetType = (TextView) rowView.findViewById(R.id.pet_type);
            TextView textPetTime = (TextView) rowView.findViewById(R.id.pet_time_minutes);
            TextView textPetDistance = (TextView) rowView.findViewById(R.id.pet_distance_km);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.pet_logo);

            PetItemBean pet = values [position];
            textView.setText(pet.name);
            textPetType.setText(pet.type);
            textPetTime.setText(pet.distanceTime);
            textPetDistance.setText(pet.distanceKm);
            if (pet.open) {
                textOpen.setText("Aberto");
                textOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_aberto, 0, 0, 0);
            } else {
                textOpen.setText("Fechado");
                textOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fechado, 0, 0, 0);
            }
            imageView.setImageResource(pet.iconId);
            System.gc();
            return rowView;
        }
    }

    class PetItemBean {
        String name;
        String type;
        boolean open;
        String distanceKm;
        String distanceTime;
        int iconId;

        public PetItemBean(String name, String type, boolean open, String distanceKm, String distanceTime, int iconId) {
            this.name = name;
            this.type = type;
            this.open = open;
            this.distanceKm = distanceKm;
            this.distanceTime = distanceTime;
            this.iconId = iconId;
        }
    }
}
