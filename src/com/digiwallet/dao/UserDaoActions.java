package com.digiwallet.dao;

import com.digiwallet.exception.WalletException;

/**
 *
 * @author lokesh
 */

public interface UserDaoActions 
{
    public String getUserCardDetails() throws WalletException;
    public String addUserDetails(String userData) throws WalletException;
    public String signUpUser(String userData) throws WalletException;
    public String updateUserDetails(Integer id, String userData) throws WalletException;
    public String deleteUserDetails(Integer id) throws WalletException;
	public String getUserDetails(Integer id) throws WalletException;
        
}
