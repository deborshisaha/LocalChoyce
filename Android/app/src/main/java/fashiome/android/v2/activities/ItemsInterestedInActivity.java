package fashiome.android.v2.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fashiome.android.R;
import fashiome.android.activities.ChatRoomActivity;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Conversation;
import fashiome.android.v2.adapters.ItemsInterestedInAdapter;
import fashiome.android.v2.classes.SearchCriteria;

/**
 * Created by dsaha on 4/2/16.
 */
public class ItemsInterestedInActivity extends AppCompatActivity {

    @Bind(R.id.rvItemsInterestedIn)
    RecyclerView rvItemsInterestedIn;

    @Bind(R.id.btnSkip)
    TextView btnSkip;

    @Bind(R.id.btnNext)
    TextView btnNext;

    private ItemsInterestedInAdapter itemsInterestedInAdapter;
    private SearchCriteria searchCriteria;

    public ItemsInterestedInActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_interested_in_v2);

        ButterKnife.bind(this);

        searchCriteria = (SearchCriteria) getIntent().getExtras().getParcelable(SearchCriteria.KEY);

        if (searchCriteria!= null) {
            itemsInterestedInAdapter = new ItemsInterestedInAdapter(this, searchCriteria.getGender());
        }

        setupUserProfileGrid();

        setUpClickSupport();
    }

    private void setUpClickSupport() {
        ItemClickSupport.addTo(rvItemsInterestedIn).setOnItemClickListener(

                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        ItemsInterestedInAdapter.PhotoViewHolder viewHolder = (ItemsInterestedInAdapter.PhotoViewHolder) recyclerView.getChildViewHolder(v);
                        viewHolder.tapped(position);

                        if (viewHolder.isSelected()) {
                            searchCriteria.addSearchTerm(itemsInterestedInAdapter.getCategory(searchCriteria.getGender())[position]);
                        } else {
                            searchCriteria.removeSearchTerm(itemsInterestedInAdapter.getCategory(searchCriteria.getGender())[position]);
                        }
                    }
                });
    }

    private void setupUserProfileGrid() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItemsInterestedIn.setLayoutManager(layoutManager);
        rvItemsInterestedIn.setAdapter(itemsInterestedInAdapter);
        rvItemsInterestedIn.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                itemsInterestedInAdapter.setLockedAnimations(true);
            }
        });
    }

    @OnClick(R.id.btnSkip)
    public void onSkip() {
        Intent launchIntent = new Intent(ItemsInterestedInActivity.this, PanacheHomeActivity.class);
        startActivity(launchIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @OnClick(R.id.btnNext)
    public void onNext() {
        animateDismiss();
    }

    private void animateDismiss() {
        View child;

        // goto next screen
        AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    Intent launchIntent = new Intent(ItemsInterestedInActivity.this, PanacheHomeActivity.class);
                    launchIntent.putExtra(SearchCriteria.KEY, searchCriteria);
                    startActivity(launchIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

        };

        for (int i = 0; i < rvItemsInterestedIn.getChildCount(); i++) {
            child = rvItemsInterestedIn.getChildAt(i);
            ItemsInterestedInAdapter.PhotoViewHolder photoViewHolder = (ItemsInterestedInAdapter.PhotoViewHolder) rvItemsInterestedIn.getChildViewHolder(child);

            if (i== rvItemsInterestedIn.getChildCount()) {
                photoViewHolder.dismiss(animatorListenerAdapter);
            } else {
                photoViewHolder.dismiss(null);
            }
        }
    }
}
