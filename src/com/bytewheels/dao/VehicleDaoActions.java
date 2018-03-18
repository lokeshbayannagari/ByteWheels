package com.bytewheels.dao;

import javax.ejb.Local;

import com.bytewheels.exception.ByteWheelsException;

/**
 *
 * @author lokesh
 */
@Local
public interface VehicleDaoActions {
	public String getCarDetails(Long fromDate, Long toDate) throws ByteWheelsException;

	public String bookVehicle(Integer categoryId, Integer modelId, Long fromDate, Long toDate, String emailId)
			throws ByteWheelsException;

}
