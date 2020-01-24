package edu.study.controller;

import edu.study.dao.UserMapper;
import edu.study.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    UserMapper userService;

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
        User user = userService.selectByPrimaryKey(request_user.getUsername());
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
        if(userService.selectByPrimaryKey(user.getUsername()) == null){//用户名没有被注册
            System.out.println("username: "+user.getUsername()
            +"birthday: "+user.getBirthday() + "level: "+user.getLevel());

            userService.insert(user);
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
    public User getUser(@RequestParam String username){
        return userService.selectByPrimaryKey(username);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<User> all(){return userService.selectAll(); }
}

/*



     * 更改用户的基本信息
     * @param user 用户实例
     *
    @RequestMapping(value = "/updateUserInfo" ,method = RequestMethod.POST)
    @ResponseBody
    public int update(@RequestBody User user){
        return UserInfoService.updateUser(user);
    }




     * 处理管理员登录
     * @param  administrator 管理员实例
     * @return  number(int)
     *
    @RequestMapping(value = "/logInAdmin" ,method = RequestMethod.POST)
    @ResponseBody
    public int logInAdmin(HttpServletRequest request,@RequestBody Administrator administrator){
        //获取session
        HttpSession session = request.getSession();
        //获取用户ID
        String adminID = administrator.getAdministratorId();
        String adminPassWord = administrator.getPassword();
        //根据登录用户ID获取真实用户信息
        Administrator trueAdmin = UserInfoService.getAdministrator(adminID);
        //number标识符,1代码验证通过,2代表验证失败 默认失败
        int number = 2;
        String truePassWord = trueAdmin.getPassword();
        if(truePassWord.equals(adminPassWord) ){
            String name = trueAdmin.getName();
            number = 1;
            session.setAttribute("adminID",adminID);
            session.setAttribute("adminName",name);
            return number;
        }
        else{
            return number;
        }
    }


     * 批量查询收藏单词
     *
     *
    @RequestMapping(value = "/getCollectionWords" ,method = RequestMethod.POST)
    @ResponseBody
    public List<WordCollection> getCollectionWords(@RequestBody Map<String,Object> map){
        return UserInfoService.getCollectionWords(map);
    }

     * 批量查询已背诵单词
     *
     *
    @RequestMapping(value = "/getRecitedWords" ,method = RequestMethod.POST)
    @ResponseBody
    public List<WordRecited> getRecitedWords(@RequestBody Map<String,Object> map){
        return UserInfoService.getRecitedWords(map);
    }


     * 批量查询收藏的文章
     *
    @RequestMapping(value = "/getCollectionArticles" ,method = RequestMethod.POST)
    @ResponseBody
    public List<ArticleCollection> getCollectionArticles(@RequestBody Map<String,Object> map){
        return UserInfoService.getCollectionArticles(map);
    }
}
*/