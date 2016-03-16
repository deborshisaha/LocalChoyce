package fashiome.android.utils;

import android.content.Context;


/**
 * Created by dsaha on 3/11/16.
 */
public class ImageURLGenerator {

    private static ImageURLGenerator ourInstance = new ImageURLGenerator();

    private Context mContext;

    private final String BASE_URL = "http://res.cloudinary.com/fashiome/image/upload/";

    public static ImageURLGenerator getInstance(Context c) {
        ourInstance.mContext = c;
        return ourInstance;
    }

    private ImageURLGenerator() {
    }

    public String URLForImageWithCloudinaryPublicId (String publicId, int pixels) {

        if (pixels == -1) {
            return BASE_URL + publicId + ".jpg";
        }

        return BASE_URL+"r_4/w_"+ Integer.toString(pixels) + "/" + publicId + ".png";
    }

}
