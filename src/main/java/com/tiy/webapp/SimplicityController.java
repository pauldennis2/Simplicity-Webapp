package com.tiy.webapp;

import com.tiy.webapp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.rmi.runtime.Log;

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
                user.setLobbyStatus(LobbyStatus.MAIN);
                users.save(user);
                session.setAttribute("user", user);
                return "redirect:/main-menu.html";
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

    @RequestMapping(path = "/game-lobby", method = RequestMethod.GET)
    public String gameLobby () {
        return "game-lobby";
    }

    @RequestMapping(path = "/main-menu", method = RequestMethod.GET)
    public String mainMenu () {
        return "main-menu";
    }

    @RequestMapping(path = "/empty-race-sel", method = RequestMethod.GET)
    public String raceSel () {
        return "empty-race-sel";
    }

    public void initializeUsers () {
        String[] emails = {"paul", "jon", "adrian", "amy", "conor", "maurice", "dom", "tj"};
        String[] handles = {"erro", "jonsnow", "adrian0615", "amypotter", "cut time", "$reece", "domiswrong", "coolshirtguy"};

        int index = 0;
        for (String email : emails) {
            User user = users.findFirstByEmail(email);
            if (user == null) {
                user = new User(email, "1234", handles[index]);
            }
            user.setLobbyStatus(LobbyStatus.LOGGED_OUT);
            users.save(user);
            index++;
        }
    }
}
