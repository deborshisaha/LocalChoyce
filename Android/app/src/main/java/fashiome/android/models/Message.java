package fashiome.android.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final int MESSAGE_TYPE_SENT = 9182;
    public static final int MESSAGE_TYPE_RECEIVED = 7364;
    public static final String MESSAGE_NOTIFICATION_KEY = "fashiome.android.message.notification.key";

    public void saveInBackground(String conversationIdentifierString, SaveCallback saveCallback) {
        setConversationIdentifier(conversationIdentifierString);
        super.saveInBackground(saveCallback);
    }

    public String getText() {

        if (text == null) {
            text = getString("text");
        }

        return text;
    }

    public void setText(String text) {
        this.text = text;
        put("text", text);
    }

    public User getFromUser() {

        if (fromUser == null) {
            fromUser = (User) getParseUser("user");
        }

        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
        put("user", this.fromUser);
    }

    public int getType() {

        if (this.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return MESSAGE_TYPE_SENT;
        }

        return MESSAGE_TYPE_RECEIVED;
    }

    public static void fetchMessagesOfConversationIdentifier( String identifier, FindCallback<Message> messageFindCallback) {

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("identifier", identifier);
        query.include("user");
        query.include("conversation");
        query.findInBackground(messageFindCallback);
    }

    private String text;
    private User fromUser;
    private Conversation conversation;

    public void setConversationIdentifier(String conversationIdentifier) {
        this.conversationIdentifier = conversationIdentifier;
        put("identifier", conversationIdentifier);
    }

    private String conversationIdentifier;

}
