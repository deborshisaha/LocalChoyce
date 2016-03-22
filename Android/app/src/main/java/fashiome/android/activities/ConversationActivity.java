package fashiome.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cloudinary.utils.StringUtils;

import fashiome.android.R;
import fashiome.android.fragments.ConversationFragment;
import fashiome.android.fragments.MessagesFragment;
import fashiome.android.models.Conversation;

/**
 * Created by dsaha on 3/22/16.
 */
public class ConversationActivity extends AppCompatActivity{

    private ConversationFragment mConversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        getSupportActionBar().setTitle("Conversations");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mConversationFragment =  ConversationFragment.newInstance();
        ft.replace(R.id.fragment_placeholder, mConversationFragment);
        ft.commit();
    }
}
