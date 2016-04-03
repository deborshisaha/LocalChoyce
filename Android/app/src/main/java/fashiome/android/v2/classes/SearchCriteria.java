package fashiome.android.v2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dsaha on 3/31/16.
 */
public class SearchCriteria implements Parcelable {

    private Gender gender;

    public HashMap<String, String> getSearchCriteriaItems() {
        return searchCriteriaItems;
    }

    private HashMap<String,String> searchCriteriaItems = new HashMap<String, String>();

    public enum Gender {
        GENDER_F, GENDER_M;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getGenderString(){
        if (gender == Gender.GENDER_F) {
            return "F";
        } else if (gender == Gender.GENDER_M) {
            return "M";
        }

        return null;
    }

    public SearchCriteria() {
    }

    public static String KEY = "SEARCH_CRITERIA_KEY";

    public void addSearchTerm(String string) {
        searchCriteriaItems.put(string, string);
    }

    public void removeSearchTerm(String string) {
        searchCriteriaItems.remove(string);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeSerializable(this.searchCriteriaItems);
    }

    protected SearchCriteria(Parcel in) {
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.searchCriteriaItems = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<SearchCriteria> CREATOR = new Creator<SearchCriteria>() {
        @Override
        public SearchCriteria createFromParcel(Parcel source) {
            return new SearchCriteria(source);
        }

        @Override
        public SearchCriteria[] newArray(int size) {
            return new SearchCriteria[size];
        }
    };
}
