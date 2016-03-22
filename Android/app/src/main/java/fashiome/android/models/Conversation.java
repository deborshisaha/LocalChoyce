package fashiome.android.models;

import com.cloudinary.utils.StringUtils;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Conversation")
public class Conversation  extends ParseObject {

    public static final String CONVERSATION_IDENTIFIER = "fashiome.android.conversation.identifier";
    public static final String SPLITER = "#";
    private String lastMessage;


    private User participantOne;
    private User participantTwo;

    private String mConverstaionIdentifier;

    public User getParticipantTwo() {

        if (participantTwo == null) {
            participantTwo = (User) getParseObject("participantTwo");
        }
        return participantTwo;
    }

    public void setParticipantTwo(User participantTwo) {
        this.participantTwo = participantTwo;
        put("participantTwo", this.participantTwo);
    }

    public User getParticipantOne() {

        if (participantOne == null) {
            participantOne = (User) getParseObject("participantOne");
        }

        return participantOne;
    }

    public void setParticipantOne(User participantOne) {
        this.participantOne = participantOne;
        put("participantOne", this.participantOne);
    }

    public static String getIdentifierFromUserId(String userObjectIdOne, String userObjectIdTwo) {
        String identifier = null;
        if (userObjectIdOne.charAt(0) <= userObjectIdTwo.charAt(0)) {
            identifier = userObjectIdOne+SPLITER+userObjectIdTwo;
        } else {
            identifier = userObjectIdTwo+SPLITER+ userObjectIdOne;
        }

        return identifier;
    }

    public User getOtherUser() {

        if (getParticipantOne().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return getParticipantTwo();
        }

        return getParticipantOne();
    }

    public static void fetchConversations(FindCallback<Conversation> conversationFindCallback){

        ParseQuery<Conversation> query = ParseQuery.getQuery(Conversation.class);
        query.include("participantOne");
        query.include("participantTwo");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
        query.findInBackground(conversationFindCallback);
    }

    public static List<Conversation> filterMyConversations(List<Conversation> conversations) {

        List<Conversation> filteredConversations = new ArrayList<Conversation>();

        for (Conversation conversation: conversations) {
            if (conversation.meParticipant()) {
                filteredConversations.add(conversation);
            }

        }
        return filteredConversations;
    }

    private boolean meParticipant() {
        return (getParticipantOne().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ||
                getParticipantTwo().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()));
    }

    public String getConverstaionIdentifier() {

        if (StringUtils.isEmpty(mConverstaionIdentifier)) {
            mConverstaionIdentifier = getString("identifier");
        }

        return mConverstaionIdentifier;
    }

    public void setConverstaionIdentifier(String mConverstaionIdentifier) {
        this.mConverstaionIdentifier = mConverstaionIdentifier;
        put("identifier", this.mConverstaionIdentifier);
    }

    public String getLastMessage() {
        if (lastMessage == null) {
            lastMessage = getString("lastMessage");
        }
        return lastMessage;
    }

}
