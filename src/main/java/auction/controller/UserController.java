package auction.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import auction.model.User;
import auction.service.UserService;

@Controller  
@RequestMapping("/user") 
public class UserController {


	@Resource
	private UserService userService;

	 @RequestMapping("/showUser")  
	    public String showUser(HttpServletRequest request,Model model){  
	        int userId = Integer.parseInt(request.getParameter("id"));  
	        User user = this.userService.getUserById(userId);  
	        model.addAttribute("user", user);  
	        return "showUser";  
} 

	 @RequestMapping("/index")  
	    public ModelAndView toIndex(HttpServletRequest request){  
		 ModelAndView view = new ModelAndView("login");
		return view;
}

	@RequestMapping("/login")
	 public ModelAndView login(HttpServletRequest request,Model model){
		 String  username = request.getParameter("name");
		 String password = request.getParameter("password");
		 User user = this.userService.getUserByName(username);
		 String url="login";
		 if(user!=null&&password.equals(user.getpassword())){
			 url="showUser";
		 }else{
			 url="fail";
		 }
		 ModelAndView view = new ModelAndView("showUser");
		 view.addObject(user);
		 return view;
	 } 

	

}
