/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package w3;

import java.util.Calendar;

/**
 *
 * @author jambo
 */
public class CalanderTestDrive {

    public static void main(String[] args) {
        int month = 0;
        int i = 0;
        Calendar cal = Calendar.getInstance();
        String aChar = new Character((char) i).toString();
        int getdate = cal.get(Calendar.DATE);
        int getmonth = cal.get(Calendar.MONTH);
        System.out.println("The date Marthin Luther King was born: " + (cal.get(Calendar.MONTH) + 7) + "/" + (cal.get(Calendar.MONTH) + 26) + "/" + (cal.getWeekYear() - 86) + "G.C");
    }
}

--------------------

package george.rima;
    
import org.joda.time.*;
//This class is used to calculate the duration employment

import java.text.SimpleDateFormat;
import java.util.Date;
public class DurationOfEmployment {
	/*This class calculates the duration of months and years worked
	 * 
	 */
	LocalDate dt1;
	LocalDate dt2;
	private int years;
	private int months;
	
	public int NumMonths(LocalDate dateStart, LocalDate dateStop)
	
	{
		
		// working months
		Period period=new Period(dateStart,dateStop);
		months = period.getMonths();
		System.out.println(" Working months "+ months);
		return months;
		
		
				
	}
	public int NumYears(LocalDate dateStart, LocalDate dateStop)
	{
		// working years
		Period period=new Period(dateStart,dateStop);
		years = period.getYears();
		System.out.println(" Working Years "+ years);
		
		return years;
		
				
	}
	public Date dateFormat(String date)
	{
		        
		// change the string type to a date format
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		
		
		Date dt;
		dt=null;
		try {
		
			dt = format.parse(date);
			
			   
		}
	    catch (Exception e) {
		    e.printStackTrace();
	    }
		
		return dt;
	}
	
		
	
}


--------------------

package com.immiapp.immiapp;

/**
 * Created by Ben on 4/2/2015.
 */
public class Date {
    int day,
    month,
    year,
    hour,
    minute;

    public Date(int minute, int hour, int day, int month, int year){
        this.minute = minute % 60;
        this.hour = hour % 24;
        this.day = day % 31;
        this.month = month % 12;
        this.year = year;
    }

    public int GetMinute() {
        return minute;
    }

    public void SetMinute(int minute) {
        this.minute = minute % 60;
    }

    public int GetHour() {
        return hour;
    }

    public void SetHout(int hour) {
        this.hour = hour % 24;
    }

    public int GetDay(){
        return day;
    }

    public void SetDay(int day) {
        this.day = day % 31;
    }

    public int GetMonth() {
        return month;
    }

    public void SetMonth(int month) {
        this.month = month % 12;
    }

    public int GetYear() {
        return year;
    }

    public void SetYear(int year) {
        this.year = year;
    }
}

--------------------

