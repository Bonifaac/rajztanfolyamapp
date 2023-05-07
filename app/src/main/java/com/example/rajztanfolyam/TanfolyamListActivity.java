package com.example.rajztanfolyam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.rajztanfolyam.R.anim.shake;

public class TanfolyamListActivity extends AppCompatActivity {
    private static final String LOG_TAG = TanfolyamListActivity.class.getName();
    private FirebaseUser user;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;
    private Integer itemLimit = 10;
    private RecyclerView mRecyclerView;
    private ArrayList<TanfolyamItem> mItemsData;
    private TanfolyamItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private SharedPreferences preferences;
    private NotificationHandler mHandler;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanfolyam_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mHandler = new NotificationHandler(this);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mItemsData = new ArrayList<>();

        mAdapter = new TanfolyamItemAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        queryData();
    }


    private void initializeData() {
        String[] itemsName = getResources()
                .getStringArray(R.array.names);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.prices);
        String[] itemsDiff = getResources()
                .getStringArray(R.array.diffs);
        String[] itemsEvent = getResources()
                .getStringArray(R.array.events);

        for (int i = 0; i < itemsName.length; i++) {
            mItems.add(new TanfolyamItem(
                    itemsName[i],
                    itemsEvent[i],
                    itemsPrice[i],
                    itemsDiff[i]
            ));
        }

    }

    private void queryData() {
        mItemsData.clear();
        mItems.orderBy("name").limit(itemLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                TanfolyamItem item = document.toObject(TanfolyamItem.class);
                mItemsData.add(item);
            }

            if (mItemsData.size() == 0) {
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.tanfolyam_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }


    public void subcribeTanfolyam() {

        String msg = "Sikeresen feliratkoztál a tanfolyamra!";
        ViewFlipper mFlipper = (ViewFlipper) findViewById(R.id.card);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);

        mFlipper.setAnimation(animation);
        mFlipper.showNext();

        mHandler.sendNotification(msg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
