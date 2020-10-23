package showman0.amoor.showman.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String password;
    private String id;
    private String user_place;
    private String privilege; //admin , user , sales , makhzan

    public User() {

    }

    public User(String name, String id, String privilege)
    {
        this.name = name;
        this.id = id;
        this.privilege = privilege;
    }

    public String getUser_place() {
        return user_place;
    }

    public void setUser_place(String user_place) {
        this.user_place = user_place;
    }


    public User(String name, String id, String privilege, String user_place) {
        this.name = name;
        this.id = id;
        this.privilege = privilege;
        this.user_place = user_place;
    }


    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        id = in.readString();
        privilege = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrivilege() {
        return privilege;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(id);
        dest.writeString(privilege);
        dest.writeString(user_place);
    }
}
