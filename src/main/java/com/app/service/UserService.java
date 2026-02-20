package com.app.service;

import com.app.exceptions.UserException;
import com.app.pojos.User;

public interface UserService {
	
	public User findUserById(Long id) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;

}
