package com.tiy.webapp.wrappers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class GameController {

    final String CORRECT_PASSWORD = "pass1234";

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login (HttpSession session, String username, String password) {
        if (password.equals(CORRECT_PASSWORD)) {
            session.setAttribute("username", username);
            session
            return "redirect:/game.html";
        } else {
            return "bad-pw";
        }
    }

    @RequestMapping(path = "/game", method = RequestMethod.GET)
    public String game () {
        return "game";
    }

    @RequestMapping(path = "/game", method = RequestMethod.GET)
    public String game2 (HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "game";
        } else {
            return "redirect:/login.html";
        }
    }

    @RequestMapping(path = "/settings", method = RequestMethod.GET)
    public String settings () {
        return "settings";
    }
}
