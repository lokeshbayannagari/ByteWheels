package com.bytewheels.dao;

import com.bytewheels.exception.ByteWheelsException;

/**
 *
 * @author lokesh
 */

public interface UserDaoActions 
{
    public String getUserAndCardDetails() throws ByteWheelsException;
    public String addUserDetails(String userData) throws ByteWheelsException;
    public String signUpUser(String userData) throws ByteWheelsException;
    public String updateUserDetails(Integer id, String userData) throws ByteWheelsException;
    public String deleteUserDetails(Integer id) throws ByteWheelsException;
	public String getUserDetails(Integer id) throws ByteWheelsException;
        
}
