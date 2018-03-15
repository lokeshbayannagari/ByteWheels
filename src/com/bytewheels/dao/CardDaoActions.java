/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytewheels.dao;

import javax.ejb.Local;

import com.bytewheels.exception.ByteWheelsException;

/**
 *
 * @author lokesh
 */
@Local
public interface CardDaoActions 
{
    public String getUserCardDetails(int userID) throws ByteWheelsException;
    public String addCardDetails(String userCardData) throws ByteWheelsException;
    public String updateCardDetails(Integer id, String userCardData) throws ByteWheelsException;
    public String deleteCardDetails(Integer userId, Integer cardId) throws ByteWheelsException;
}
