package auction.serviceImp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import auction.dao.UserMapper;
import auction.model.User;
import auction.service.UserService;

@Service("userService")
public class UserServiceImp implements UserService {
	@Resource
	private UserMapper userMapper;
	
	public User getUserById(int id) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(id);
	}

	public int addUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.insert(user);
	}

	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		return userMapper.selectByName(name);
	}
	public int updateUserpasswordByName(User user) {
		// TODO Auto-generated method stub
		return userMapper.updateUserpasswordByName(user);
	}

}
