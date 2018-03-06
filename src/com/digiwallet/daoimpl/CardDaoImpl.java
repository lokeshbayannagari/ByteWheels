/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiwallet.daoimpl;

import com.digiwallet.dao.CardDaoActions;
import com.digiwallet.exception.WalletException;
import javax.ejb.Stateless;

/**
 *
 * @author lokesh
 */
@Stateless
public class CardDaoImpl implements CardDaoActions{

    @Override
    public String getUserCardDetails(int userID) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addCardDetails(String userCardData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updateCardDetails(String userCardData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteCardDetails(String userCardData) throws WalletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
