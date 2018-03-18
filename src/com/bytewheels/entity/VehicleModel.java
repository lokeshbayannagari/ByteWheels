package com.bytewheels.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vehiclemodels database table.
 * 
 */
@Entity
@Table(name="vehiclemodels")
@NamedQuery(name="VehicleModel.findAll", query="SELECT v FROM VehicleModel v")
public class VehicleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int vehiclemodelid;

	private int vehiclemodelcount;

	private String vehiclemodelname;

	//bi-directional many-to-one association to Vehicle
	@OneToMany(mappedBy="vehiclemodel")
	private List<Vehicle> vehicles;

	public VehicleModel() {
	}

	public int getVehiclemodelid() {
		return this.vehiclemodelid;
	}

	public void setVehiclemodelid(int vehiclemodelid) {
		this.vehiclemodelid = vehiclemodelid;
	}

	public int getVehiclemodelcount() {
		return this.vehiclemodelcount;
	}

	public void setVehiclemodelcount(int vehiclemodelcount) {
		this.vehiclemodelcount = vehiclemodelcount;
	}

	public String getVehiclemodelname() {
		return this.vehiclemodelname;
	}

	public void setVehiclemodelname(String vehiclemodelname) {
		this.vehiclemodelname = vehiclemodelname;
	}

	public List<Vehicle> getVehicles() {
		return this.vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public Vehicle addVehicle(Vehicle vehicle) {
		getVehicles().add(vehicle);
		vehicle.setVehiclemodel(this);

		return vehicle;
	}

	public Vehicle removeVehicle(Vehicle vehicle) {
		getVehicles().remove(vehicle);
		vehicle.setVehiclemodel(null);

		return vehicle;
	}

}