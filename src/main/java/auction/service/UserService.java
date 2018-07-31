package auction.service;

import auction.model.User;

public interface UserService {
	public User getUserById(int id);
	public User getUserByName(String name);
	public int addUser(User user);
	public int updateUserpasswordByName(User user);
	
}
