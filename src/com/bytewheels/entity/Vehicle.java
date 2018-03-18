package com.bytewheels.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vehicles database table.
 * 
 */
@Entity
@Table(name="vehicles")
@NamedQuery(name="Vehicle.findAll", query="SELECT v FROM Vehicle v")
public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int vehicleid;

	private String vehicleno;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="vehicle")
	private List<Booking> bookings;

	//bi-directional many-to-one association to VehicleCategory
	@ManyToOne
	@JoinColumn(name="vehiclecategoryid")
	private VehicleCategory vehiclecategory;

	//bi-directional many-to-one association to VehicleModel
	@ManyToOne
	@JoinColumn(name="vehiclemodelid")
	private VehicleModel vehiclemodel;

	public Vehicle() {
	}

	public int getVehicleid() {
		return this.vehicleid;
	}

	public void setVehicleid(int vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getVehicleno() {
		return this.vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setVehicle(this);

		return booking;
	}

	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setVehicle(null);

		return booking;
	}

	public VehicleCategory getVehiclecategory() {
		return this.vehiclecategory;
	}

	public void setVehiclecategory(VehicleCategory vehiclecategory) {
		this.vehiclecategory = vehiclecategory;
	}

	public VehicleModel getVehiclemodel() {
		return this.vehiclemodel;
	}

	public void setVehiclemodel(VehicleModel vehiclemodel) {
		this.vehiclemodel = vehiclemodel;
	}

}