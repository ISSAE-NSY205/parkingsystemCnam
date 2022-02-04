package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {
         
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

       // int inHour = ticket.getInTime().getHours();
        //int outHour = ticket.getOutTime().getHours();
        Date inDate = ticket.getInTime();
        Date outDate = ticket.getOutTime();
        
        long inDateMilli = inDate.getTime();
        long outDateMilli = outDate.getTime();
        //TODO: Some tests are failing here. Need to check if this logic is correct
        
        
        int duration = (int)((outDateMilli - inDateMilli)/1000/60);
        boolean recurrence=ticket.getRecurrent();
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                //if duration >24h only the 24h are charged
                if (duration/60>24)
                    //if this is a recurrent customer apply 5% discount
                    if (recurrence==true)
                     ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR*0.95); 
                    else
                       ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR); 
                  
                //if duration <30minutes the parking is for free
                else if (duration<=30)
                ticket.setPrice(0); 
                else
                   //if this is a recurrent customer apply 5% discount
                    if (recurrence==true)  
                      ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR*0.95/60 );
                    else 
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR/60 );
                break;
            }
            case BIKE: {
                //if duration >24h only the 24h are charged
                if (duration/60>24)
                     //if this is a recurrent customer apply 5% discount
                    if (recurrence==true)
                       ticket.setPrice(24 * Fare.BIKE_RATE_PER_HOUR *0.95);
                    else
                        ticket.setPrice(24 * Fare.BIKE_RATE_PER_HOUR);
                //if duration <30minutes the parking is for free
                else if (duration<=30)
                ticket.setPrice(0); 
                else
                     if (recurrence==true)
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR*0.95/60 ); 
                     else
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR/60 ); 
                break;
            }
            
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}