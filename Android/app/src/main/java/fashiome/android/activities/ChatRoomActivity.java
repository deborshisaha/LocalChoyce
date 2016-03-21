package fashiome.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fashiome.android.R;
import fashiome.android.fragments.MessagesFragment;
import fashiome.android.fragments.ProductsRecyclerViewFragment;

/**
 * Created by dsaha on 3/21/16.
 */
public class ChatRoomActivity extends AppCompatActivity {

    MessagesFragment messagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messagesFragment = (MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentChatroom);
    }
}
