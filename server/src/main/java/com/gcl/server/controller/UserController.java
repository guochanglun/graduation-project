package com.gcl.server.controller;

import com.gcl.server.bean.User;
import com.gcl.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public @ResponseBody String addNewUser (@RequestParam String name,
                                            @RequestParam String username,
                                            @RequestParam String password) {

		User n = new User();
        n.setName(name);
        n.setUsername(username);
        n.setPassword(password);
        userRepository.save(n);
        return "ok";
	}

    @PostMapping("/update")
    public @ResponseBody String updateUser (@RequestParam Integer id,
                                            @RequestParam String name,
                                            @RequestParam String username,
                                            @RequestParam String password) {

        User n = new User();
        n.setId(id);
        n.setName(name);
        n.setUsername(username);
        n.setPassword(password);
        userRepository.save(n);
        return "ok";
    }

    @PostMapping(path = "/adminlogin")
    public String adminLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session){
	    // todo admin login
        return "index";
    }
	@PostMapping(path = "/login")
    public @ResponseBody User login(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session){
        List<User> userList = userRepository.findByUsernameAndPassword(username, password);
        if(userList.size() == 0){
            User user = new User();
            // 同样是返回的user，但是如果user的id是-1，则表示登录不成功
            user.setId(-1);
            user.setName("");
            user.setUsername("");
            user.setPassword("");
            return user;
        }else { // 登陆成功
            User user = userList.get(0);
            System.out.println(user);
            // session用户
            session.setAttribute("user", user);
            return user;
        }
    }

    /**
     * 退出登陆
     * @param session
     */
    @GetMapping(path = "/logout")
    public @ResponseBody String logOut(HttpSession session){
        session.removeAttribute("user");
        return "ok";
    }

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

    @GetMapping(path="/delete/{id}")
    public @ResponseBody int delete(@PathVariable("id") int id) {
        userRepository.delete(id);
        return id;
    }
}