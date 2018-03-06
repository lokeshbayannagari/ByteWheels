/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiwallet.dao;

import com.digiwallet.exception.WalletException;
import javax.ejb.Local;

/**
 *
 * @author lokesh
 */
@Local
public interface CardDaoActions {
    public String getUserCardDetails(int userID) throws WalletException;
        public String addCardDetails(String userCardData) throws WalletException;
        public String updateCardDetails(String userCardData) throws WalletException;
        public String deleteCardDetails(String userCardData) throws WalletException;
}
