package com.tiy.webapp;

import com.tiy.webapp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by Paul Dennis on 2/10/2017.
 */
@Controller
public class SimplicityController {

    @Autowired
    UserRepo users;

    boolean initialized = false;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home (HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "login";
        }
        return "redirect:/main.html";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login (HttpSession session, String email, String password) {
        if (!initialized) {
            initializeUsers();
            initialized = true;
        }
        User user = users.findFirstByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                user.setLoggedIn(true);
                users.save(user);
                session.setAttribute("user", user);
                return "redirect:/simple-lobby.html";
            }
        }
        return "bad-pw";
    }

    @RequestMapping(path = "/lobby", method = RequestMethod.GET)
    public String lobby () {
        return "game-lobby";
    }

    @RequestMapping(path = "/simple-lobby", method = RequestMethod.GET)
    public String simpleLobby () {
        return "simple-lobby";
    }

    public void initializeUsers () {
        User paul = users.findFirstByEmail("paul");
        User jon = users.findFirstByEmail("jon");
        User adrian = users.findFirstByEmail("adrian");
        if (paul == null) {
            paul = new User("paul", "1234");
        }
        if (jon == null) {
            jon = new User("jon", "1234");
        }
        if (adrian == null) {
            adrian = new User("adrian", "1234");
        }
        paul.setLoggedIn(false);
        users.save(paul);
        jon.setLoggedIn(false);
        users.save(jon);
        adrian.setLoggedIn(false);
        users.save(adrian);
    }
}
