package com.wkwan.cmput301assignment1;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.io.Serializable;

// This class allows users to create claims. The claims have various attributes like
// claim label, start and end trip dates, trip location and descriptions, and expenses that
// users can record and/or edit. 

public class ClaimCreate implements Serializable {
	
	// Serialization code automatically generated by Eclipse
	private static final long serialVersionUID = -4249904803645582937L;
    
	private String claim_label = "";
	private Calendar start_time = Calendar.getInstance();
	private Calendar end_time = Calendar.getInstance();
	private String travel_location = "";
	private String trip_description = "";
	private ExpenseStore expenses = new ExpenseStore();

	public String getClaimLabel() {
		return claim_label;
	}

	public void setCLaimLabel(String claimlabel) {
		this.claim_label = claimlabel;
	}
	
	public String getTravelLocation() {
		return travel_location;
	}

	public void setTravelLocation(String travellocation) {
		this.travel_location = travellocation;
	}

	public String getTripDescription() {
		return trip_description;
	}

	public void setTripDescription(String tripdescription) {
		this.trip_description = tripdescription;
	}

	public Calendar getStartTime() {
		return start_time;
	}

	public void setStartTime(Calendar starttime) {
		this.start_time = starttime;
	}

	public Calendar getEndTime() {
		return end_time;
	}

	public void setEndTime(Calendar endtime) {
		this.end_time = endtime;
	}

	public ExpenseStore getExpenseList() {
		return expenses;
	}
	
	// A hashmap is used to determine totals for different currencies from each of the expenses made
	// in a certain claim.
	public HashMap<Currency, BigDecimal> getTotals() {
		HashMap<Currency, BigDecimal> totals = new HashMap<Currency, BigDecimal>();
		
		// Determine amount for each currency and aggregate into totals unique to each currency
		for (ExpenseCreate expense : getExpenseList().getExpenses()) {
			Currency currency = expense.getCurrency();
			BigDecimal amount = totals.get(currency);
			
			// Setting up new currencies used
			if (amount == null) {
				amount = new BigDecimal(expense.getcost().toString());
				
			// Summing pre-existing currency totals
			} else {
				amount = amount.add(expense.getcost());
			}
			
			totals.put(currency, amount);
		}
		
		return totals;
	}
}
