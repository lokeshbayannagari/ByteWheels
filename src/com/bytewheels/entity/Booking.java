package com.bytewheels.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the booking database table.
 * 
 */
@Entity
@Table(name = "bookings")
@NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int bookingid;

	private String emailid;

	@Temporal(TemporalType.DATE)
	private Date enddate;

	@Temporal(TemporalType.DATE)
	private Date startdate;

	// bi-directional many-to-one association to Vehicle
	@ManyToOne
	@JoinColumn(name = "vehicleid")
	private Vehicle vehicle;

	public Booking() {
	}

	public int getBookingid() {
		return this.bookingid;
	}

	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}

	public String getEmailid() {
		return this.emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

}