package com.bytewheels.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vehiclecategory database table.
 * 
 */
@Entity
@Table(name="vehiclecategories")
@NamedQuery(name="VehicleCategory.findAll", query="SELECT v FROM VehicleCategory v")
public class VehicleCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int vehiclecategoryid;

	private int categorycost;

	private String categoryname;

	//bi-directional many-to-one association to Vehicle
	@OneToMany(mappedBy="vehiclecategory")
	private List<Vehicle> vehicles;

	public VehicleCategory() {
	}

	public int getVehiclecategoryid() {
		return this.vehiclecategoryid;
	}

	public void setVehiclecategoryid(int vehiclecategoryid) {
		this.vehiclecategoryid = vehiclecategoryid;
	}

	public int getCategorycost() {
		return this.categorycost;
	}

	public void setCategorycost(int categorycost) {
		this.categorycost = categorycost;
	}

	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public List<Vehicle> getVehicles() {
		return this.vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public Vehicle addVehicle(Vehicle vehicle) {
		getVehicles().add(vehicle);
		vehicle.setVehiclecategory(this);

		return vehicle;
	}

	public Vehicle removeVehicle(Vehicle vehicle) {
		getVehicles().remove(vehicle);
		vehicle.setVehiclecategory(null);

		return vehicle;
	}

}