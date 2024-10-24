package comp4342.grp15.gem.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ProfileViewModel extends ViewModel {
    private String userName;
    private Boolean isLogin;


    public ProfileViewModel() {
        isLogin =false;
        userName="null";
    }

    public Boolean getIsLogin(){
        return this.isLogin;
    }

    public void setLogin(boolean status){
        isLogin = status;
    }

    public String getUsername(){
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

}