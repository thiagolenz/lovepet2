package lovepetapp.com.lovepet.global;

import android.app.Application;

/**
 * Created by macbookpro on 26/09/15.
 */
public class PetShoGlobalApplication extends Application {
    private String userAvatar;
    private String userName;
    private String userEmail;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
