package com.mark.model.google.request;

import java.util.List;

public class GoogleFlightRequestDetail
{
	private List<Slice> slice;
	private String maxPrice;
	private String saleCountry;
	private boolean refundable;
	private Integer solutions;
	private Passengers passengers;
	
	public Passengers getPassengers() {
		return passengers;
	}
	public void setPassengers(Passengers passengers) {
		this.passengers = passengers;
	}
	public List<Slice> getSlice() {
		return slice;
	}
	public void setSlice(List<Slice> slice) {
		this.slice = slice;
	}
	public String getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getSaleCountry() {
		return saleCountry;
	}
	public void setSaleCountry(String saleCountry) {
		this.saleCountry = saleCountry;
	}
	public boolean isRefundable() {
		return refundable;
	}
	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}
	public Integer getSolutions() {
		return solutions;
	}
	public void setSolutions(Integer solutions) {
		this.solutions = solutions;
	}
	
}
--------------------

package com.example.zf_android.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChanelEntitiy implements Serializable{
//    "id": 1,
//    "service_rate": 30,
//    "description": "说明pcbc",
//    "name": "T+1"e 
	private String trade_value;
	private int terminal_rate;
	private int standard_rate;
	
	
	public int getStandard_rate() {
		return standard_rate;
	}
	public void setStandard_rate(int standard_rate) {
		this.standard_rate = standard_rate;
	}
	public int getTerminal_rate() {
		return terminal_rate;
	}
	public void setTerminal_rate(int terminal_rate) {
		this.terminal_rate = terminal_rate;
	}
	public String getTrade_value() {
		return trade_value;
	}
	public void setTrade_value(String trade_value) {
		this.trade_value = trade_value;
	}
	private int id;
	private int service_rate; 
	private String description;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getService_rate() {
		return service_rate;
	}
	public void setService_rate(int service_rate) {
		this.service_rate = service_rate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

--------------------

package ch.bfh.bti7081.s2015.green.DoctorsRegistry.entity;

public class PatientMedication {
	
	private int id = -1;
	private String name = "";
	private int dose = -1;
	private int appointmentId = -1;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDose() {
		return dose;
	}
	public void setDose(int dose) {
		this.dose = dose;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	

}

--------------------

