package fashiome.android.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Product")
public class Message extends ParseObject {

    public static final int MESSAGE_TYPE_SENT = 9182;
    public static final int MESSAGE_TYPE_RECEIVED = 7364;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public int getType() {

        if (this.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return MESSAGE_TYPE_SENT;
        }

        return MESSAGE_TYPE_RECEIVED;
    }

    public static void fetchMessagesOfConversation(String conversationId, FindCallback<Message> callback) {

    }

    private String text;
    private User fromUser;
    private Conversation conversation;

}
