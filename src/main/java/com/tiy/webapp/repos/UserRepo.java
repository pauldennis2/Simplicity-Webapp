package com.tiy.webapp.repos;

import com.tiy.webapp.LobbyStatus;
import com.tiy.webapp.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 2/9/2017.
 */
public interface UserRepo extends CrudRepository<User, Integer> {
    User findFirstByEmail (String email);
    List<User> findByLobbyStatus (LobbyStatus status);
}
