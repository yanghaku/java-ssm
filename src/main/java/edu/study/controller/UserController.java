package edu.study.controller;

import edu.study.model.Keyword;
import edu.study.model.User;
import edu.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 获取当前用户状态
     */
    @RequestMapping(value = "/status",method = RequestMethod.GET)
    @ResponseBody
    public String getStatus(HttpServletRequest request){
        // 返回session 里保存的当前用户
        return (String)request.getSession().getAttribute("current_user");
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request,@RequestBody User request_user){
        User user = userService.userSelectByPrimaryKey(request_user.getUsername());
        if(user == null){// 如果用户没有查到，返回用户不存在
            return "No Such User!";
        }
        if(user.getPassword().equals(request_user.getPassword())){//登录成功
            // 将session 里面添加入已登录的用户名
            request.getSession().setAttribute("current_user",user.getUsername());
            return "success";
        }
        return "password Error!";
    }

    /**
     * 用户注销
     */
    @RequestMapping(value = "/logout" ,method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpServletRequest request){
        // 删去当前登录用户
        request.getSession().removeAttribute("current_user");
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    @ResponseBody
    public String Register(HttpServletRequest request,@RequestBody User user){
        if(user.getUsername() == null || user.getUsername().equals("")){
            return "username could not be null";
        }
        if(userService.userSelectByPrimaryKey(user.getUsername()) == null){//用户名没有被注册
            userService.userInsert(user);
            request.getSession().setAttribute("current_user",user.getUsername());
            return "success";
        }
        else{
            return "this username is already registered";
        }
    }

    /**
     * 获取用户详细信息
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(HttpServletRequest request,@RequestParam(required = false) String username){
        if(username == null){// 如果没有指定用户，就返回当前登录的用户
            username = (String)request.getSession().getAttribute("current_user");
            if(username == null)return null;
        }
        return userService.userSelectByPrimaryKey(username);
    }

    /**
     * 获取用户的所有的喜好keyword
     */
    @RequestMapping(value = "/getKeyword", method = RequestMethod.GET)
    @ResponseBody
    public List<Keyword> getKeyword(@RequestParam String username) {
        if (username == null) return null;
        return userService.keywordSelectByUsername(username);
    }

    // 修改用户信息
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public int updateUser(HttpServletRequest request,@RequestBody User user){
        if(user.getUsername() == null || user.getUsername().equals(""))return 0;
        // 如果用户名不合法或者是 不是当前登录用户，就拒绝操作
        if(!user.getUsername().equals((String)request.getSession().getAttribute("current_user")))return 0;
        return userService.userUpdateByPrimaryKey(user);
    }


}