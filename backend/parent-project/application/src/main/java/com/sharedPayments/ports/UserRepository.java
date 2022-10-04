package com.sharedPayments.ports;

import java.util.List;

import com.sharedPayments.model.User;

public interface UserRepository {
	
	public User save(User user);
	
	public User findById(Long id);
	
	public List<User> findAll();
	
	public User update(User user);

	public int count();

}
