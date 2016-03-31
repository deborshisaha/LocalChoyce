package fashiome.android.v2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dsaha on 3/31/16.
 */
public class SearchCriteria implements Parcelable {

    private Gender gender;

    public enum Gender {
        GENDER_F, GENDER_M
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    }


    protected SearchCriteria(Parcel in) {
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
    }

    public static final Parcelable.Creator<SearchCriteria> CREATOR = new Parcelable.Creator<SearchCriteria>() {
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
