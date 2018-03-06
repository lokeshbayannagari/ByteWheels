package com.digiwallet.daoimpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.exception.WalletException;

@Stateless
public class UserDaoImpl implements UserDaoActions{
	
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String getUserCardDetails() throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addUserDetails(String userData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updateUserDetails(String userData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteUserDetails(String userData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
