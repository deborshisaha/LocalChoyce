package fashiome.android.activities;

import org.parceler.Parcels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fashiome.android.R;
import fashiome.android.fragments.MapListFragment;
import fashiome.android.models.Item;


public class MapListActivity extends AppCompatActivity implements  MapListFragment.OnMarkerClickedListener_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        if (savedInstanceState == null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.fragment, MapListFragment.newInstance(null));
            trans.commit();
        }

    }
    @Override
    public void OnClick(Item item) {
        //Toast.makeText(TimelinesActivity.this, "Inside onclick in activity", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,ItemDetailsActivity.class);
        i.putExtra("item", Parcels.wrap(item));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items,menu);
        MenuItem searchItem =menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                //fetch new results
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login_fb:
                Intent i =new Intent(fashiome.android.activities.MapListActivity.this,LoginActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
