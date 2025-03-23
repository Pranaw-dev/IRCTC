package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.User;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "app/src/main/java/org/example/localDb/users.json";

    public UserBookingService(User user1) throws IOException
    {
        this.user = user1;
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            Object UserServiceUtil;
            return user1.getName().equals(User.getName()) && UserServiceUtil.checkPassword(user.getPassword(),),
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListtoFile();
            return Boolean.TRUE;
        }
        catch (IOException){
            return Boolean.FALSE;
        }
    }

    private void saveUserListtoFile() {
    }
}
