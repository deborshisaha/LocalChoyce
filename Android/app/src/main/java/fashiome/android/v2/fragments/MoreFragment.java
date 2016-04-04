package fashiome.android.v2.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.BuildConfig;
import fashiome.android.R;
import fashiome.android.activities.ChatRoomActivity;
import fashiome.android.activities.IntroAndLoginActivity;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Conversation;
import fashiome.android.models.User;
import fashiome.android.v2.activities.UserProfileActivity;
import fashiome.android.v2.adapters.BasicRecyclerViewAdapter;
import fashiome.android.v2.adapters.SectionedRecyclerViewAdapter;
import fashiome.android.v2.adapters.UserProfileTabsAdapter;
import fashiome.android.v2.classes.DividerItemDecoration;

/**
 * Created by dsaha on 4/3/16.
 */
public class MoreFragment extends Fragment {

    @Bind(R.id.rvMoreMenu)
    RecyclerView rvMoreMenu;

    private LogOutCallback mLogOutCallback = null;

    private final String LOG_IN = "Log in";
    private final String GENDER = "Gender";
    private final String RADIUS = "Radius";
    private final String INTERESTED_IN = "Items Interested In";
    private final String CU = "Contact Us";
    private final String RB = "Report A Bug";
    private final String TnC = "Terms & Conditions";
    private final String PP = "Privacy Policy";
    private final String OSL = "Open Source Libraries";
    private final String FAQ = "FAQs";
    private final String LOG_OUT = "Log out";

    private final String[] loggedOutMoreOptions = {LOG_IN, RADIUS, GENDER, INTERESTED_IN, CU,RB, FAQ, TnC, PP, OSL, buildInformation()};
    private final String[] loggedInMoreOptions ={((ParseUser.getCurrentUser() != null )?ParseUser.getCurrentUser().getUsername():""), RADIUS, INTERESTED_IN, CU,RB, FAQ, TnC, PP, OSL, LOG_OUT, buildInformation()};

    private int[] loggedOutMoreOptionsIcon = {R.drawable.contacts,0,0,0, R.drawable.contact_us, R.drawable.report_bug, R.drawable.faq, R.drawable.terms, R.drawable.privacy};
    private int[] loggedInMoreOptionsIcon = {0,0,0, R.drawable.contact_us, R.drawable.report_bug, R.drawable.faq, R.drawable.terms, R.drawable.privacy,0};

    private BasicRecyclerViewAdapter mBasicRecyclerViewAdapter;

    private List<SectionedRecyclerViewAdapter.Section> sections;

    public MoreFragment(LogOutCallback logoutCallback) {
        super();
        this.mLogOutCallback = logoutCallback;
    }

    public MoreFragment () {super();}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_more_v2, container, false);

        ButterKnife.bind(this, view);

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {

        rvMoreMenu.setHasFixedSize(true);
        rvMoreMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMoreMenu.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mBasicRecyclerViewAdapter = new BasicRecyclerViewAdapter(getActivity(), getMoreOptions(), getIcons());

        //This is the code to provide a sectioned list

        if (sections == null) {
            sections = new ArrayList<SectionedRecyclerViewAdapter.Section>();
        }

        //Sections
        addSections(sections);

        //Add your adapter to the sectionAdapter
        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
        SectionedRecyclerViewAdapter mSectionedAdapter = new
                SectionedRecyclerViewAdapter(getActivity(),R.layout.section_v2, R.id.tvSectionHeader, mBasicRecyclerViewAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        rvMoreMenu.setAdapter(mSectionedAdapter);

        ItemClickSupport.addTo(rvMoreMenu).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        String key = null;

                        int offsetPositionDueToSection = numberOfSectionBeforeThisPosition(position);

                        if (getMoreOptions().length > offsetPositionDueToSection) {
                            key = getMoreOptions()[offsetPositionDueToSection];
                        }

                        if (key.equals(LOG_IN)) {
                            // Go to log in screen
                            Intent launchIntent = new Intent(getActivity(), IntroAndLoginActivity.class);
                            launchIntent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                            startActivity(launchIntent);
                        } else if (key.equals(LOG_OUT)) {
                            // Log out
                            ParseUser.logOutInBackground(mLogOutCallback);
                        } else if (ParseUser.getCurrentUser()!= null && key.equals(ParseUser.getCurrentUser().getUsername())) {
                            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                            intent.putExtra(User.USER_KEY, (User) ParseUser.getCurrentUser());
                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.stay);
                            startActivity(intent);
                        }

                    }
                });
    }

    private int numberOfSectionBeforeThisPosition(int position) {

        for (SectionedRecyclerViewAdapter.Section section: sections) {
            int firstPosition = section.getFirstPosition();
            if (firstPosition < position) {
                position--;
            } else {
                break;
            }
        }
        return position;
    }

    private void addSections(List<SectionedRecyclerViewAdapter.Section> sections) {
        if (ParseUser.getCurrentUser() == null) {
            sections.add(new SectionedRecyclerViewAdapter.Section(0,"Account"));
            sections.add(new SectionedRecyclerViewAdapter.Section(1,"Search Filter"));
            sections.add(new SectionedRecyclerViewAdapter.Section(4,"Help & Feedback"));
            sections.add(new SectionedRecyclerViewAdapter.Section(7,"Legal"));
            sections.add(new SectionedRecyclerViewAdapter.Section(9,"Thank you"));
            sections.add(new SectionedRecyclerViewAdapter.Section(10,""));

        } else {
            sections.add(new SectionedRecyclerViewAdapter.Section(0,"Account"));
            sections.add(new SectionedRecyclerViewAdapter.Section(1,"Search Filter"));
            sections.add(new SectionedRecyclerViewAdapter.Section(3,"Help & Feedback"));
            sections.add(new SectionedRecyclerViewAdapter.Section(6,"Legal"));
            sections.add(new SectionedRecyclerViewAdapter.Section(8,"Thank you"));
            sections.add(new SectionedRecyclerViewAdapter.Section(9,""));
            sections.add(new SectionedRecyclerViewAdapter.Section(10,""));
            sections.add(new SectionedRecyclerViewAdapter.Section(11,""));
        }
    }

    public String[] getMoreOptions() {
        if (ParseUser.getCurrentUser() == null) {
            return loggedOutMoreOptions;
        } else {
            return loggedInMoreOptions;
        }
    }

    private String buildInformation (){
        return BuildConfig.VERSION_NAME;
    }

    public int[] getIcons() {
        if (ParseUser.getCurrentUser() == null) {
            return loggedOutMoreOptionsIcon;
        } else {
            return loggedInMoreOptionsIcon;
        }
    }
}
