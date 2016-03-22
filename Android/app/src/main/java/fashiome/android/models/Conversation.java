package fashiome.android.models;

import com.parse.ParseObject;

public class Conversation  extends ParseObject {
    public static final String CONVERSATION_IDENTIFIER = "fashiome.android.conversation.identifier";
    public static final String SPLITER = "#";

    public static String getIdentifierFromUserId(String userObjectIdOne, String userObjectIdTwo) {
        String identifier = null;
        if (userObjectIdOne.charAt(0) <= userObjectIdTwo.charAt(0)) {
            identifier = userObjectIdOne+SPLITER+userObjectIdTwo;
        } else {
            identifier = userObjectIdTwo+SPLITER+ userObjectIdOne;
        }

        return identifier;
    }
}
