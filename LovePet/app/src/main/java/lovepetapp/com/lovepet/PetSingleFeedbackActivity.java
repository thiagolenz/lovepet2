package lovepetapp.com.lovepet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lovepetapp.com.lovepet.R;
import lovepetapp.com.lovepet.global.PetShoGlobalApplication;
import lovepetapp.com.lovepet.picasso.CircleTransform;

/**
 * Created by macbookpro on 07/09/15.
 */
public class PetSingleFeedbackActivity extends AppCompatActivity {

    private List<PetFeedback> listFeedback = new ArrayList<>();

    private GoogleMap mMap;
    private String avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        this.setContentView(R.layout.petshop_single_feedback_layout);

        final PetShoGlobalApplication globalVariable = (PetShoGlobalApplication) getApplicationContext();

        this.avatarFile = globalVariable.getUserAvatar();

        System.gc();
        ImageView imageView = (ImageView) findViewById(R.id.currentUserAvatarFeedback);
        Picasso.with(PetSingleFeedbackActivity.this).load(new File(avatarFile)).transform(new CircleTransform()).into(imageView);

        System.gc();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(Color.parseColor("#e3e6e8"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#e3e6e8"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.parseColor("#f2d50e"), PorterDuff.Mode.SRC_ATOP);
        initToolbar();
    }
    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void finalizeFeedbackClick (View view) {
        TextView comment = (TextView) findViewById(R.id.commentTextEdit);


        String name = getIntent().getStringExtra("userName");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);

        listFeedback.add(0, new PetFeedback(avatarFile, comment.getText().toString(), name, ratingBar.getRating() + ""));

        comment.setText("");
        ratingBar.setRating(0);

        final ListView listview = (ListView) findViewById(R.id.feedbackList);
        listview.removeAllViewsInLayout();

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listFeedback.toArray(new PetFeedback[listFeedback.size()]));
        listview.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listview);

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

    class MySimpleArrayAdapter extends ArrayAdapter<PetFeedback> {
        private final Context context;
        private final PetFeedback[] values;

        public MySimpleArrayAdapter(Context context, PetFeedback[] values) {
            super(context, R.layout.pet_list_view_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.petshop_single_comment_item, parent, false);
            TextView textRating = (TextView) rowView.findViewById(R.id.scoreItemId);
            TextView textComment = (TextView) rowView.findViewById(R.id.commentText);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.commentProfilePicture);

            PetFeedback pet = values [position];
            textRating.setText(pet.nota);
            textComment.setText(pet.feedback);
            Picasso.with(PetSingleFeedbackActivity.this).load(new File(pet.imagem)).transform(new CircleTransform()).into(imageView);
            return rowView;
        }
    }

    class PetFeedback {
        String imagem;
        String feedback;
        String name;
        String nota;

        public PetFeedback(String imagem, String feedback, String name, String nota) {
            this.imagem = imagem;
            this.feedback = feedback;
            this.name = name;
            this.nota = nota;
        }
    }
}
