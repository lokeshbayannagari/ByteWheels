package com.digiwallet.dao;

import javax.ejb.Local;

import com.digiwallet.exception.WalletException;

/**
 *
 * @author lokesh
 */

@Local
public interface UserDaoActions 
{
    public String getUserCardDetails() throws WalletException;
    public String addUserDetails(String userData) throws WalletException;
    public String signUpUser(String userData) throws WalletException;
    public String updateUserDetails(String userData) throws WalletException;
    public String deleteUserDetails(String userData) throws WalletException;
        
}
