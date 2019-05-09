package fr.sushi.app.ui.objectbox;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.UUID;

import fr.sushi.app.SushiApp;
import io.objectbox.Box;
import io.objectbox.android.ObjectBoxLiveData;

public class TestViewModel extends ViewModel {
    private Box<User> userBox;

    public TestViewModel(){
        //userBox = SushiApp.getBoxStore().boxFor(User.class);
    }

    public ObjectBoxLiveData<User> getUserList(){
        return new ObjectBoxLiveData<>(userBox.query().build());
    }

    public void saveUser(){
        String name = "UserName";
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setName(name);
        user.setUserId(userId);
        userBox.put(user);
    }

    public void deleteItem(User item) {
        userBox.remove(item);
    }
}
