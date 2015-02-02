package com.wkwan.cmput301assignment1;

import java.util.Calendar;
import java.util.Currency;
import java.io.Serializable;
import java.math.BigDecimal;

// This class permits users to create and modify individual expenses that
// occur within each claim made by the user.
public class ExpenseCreate implements Serializable {

	// Serialization code that is created automatically by Eclipse
	private static final long serialVersionUID = 6852807867907381015L;
    
	private Calendar mmddyy;
	private String expense_type;
	private BigDecimal cost;
	private Currency currency;
	private String expense_description;
	
	public ExpenseCreate() {
		setmmddyy(Calendar.getInstance());
		setexpensetype(expense_type);
		setcost(new BigDecimal(0));
		setCurrency(Currency.getInstance("CAD"));
		setexpensedescription("");
	}
	
	// We use the following duplication and clone methods to save and copy
	// expense parameters that the user has recorded.
	public ExpenseCreate(ExpenseCreate sample) {
		mirror(sample);
	}
	
	public void mirror(ExpenseCreate sample) {
		setmmddyy((Calendar) sample.mmddyy.clone());
		setexpensetype(new String(sample.expense_type));
		setexpensedescription(new String(sample.expense_description));
		setcost(new BigDecimal(sample.cost.toString()));
		
		// Don't need to deep copy as only one exists per currency type
		setCurrency(sample.currency);
	}
	
	public Calendar getmmddyy() {
		return mmddyy;
	}
	public void setmmddyy(Calendar mmddyy) {
		this.mmddyy = mmddyy;
	}
	public String getexpensetype() {
		return expense_type;
	}
	public void setexpensetype(String expensetype) {
		this.expense_type = expensetype;
	}
	public BigDecimal getcost() {
		return cost;
	}
	public void setcost(BigDecimal cost) {
		this.cost = cost;
		
		sum_individual_curr();
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
		
		sum_individual_curr();
	}
	public String getexpensedescription() {
		return expense_description;
	}
	public void setexpensedescription(String expensedescription) {
		this.expense_description = expensedescription;
	}
	
	// This function allows us to maintain the values of the different currency
	// amounts being accrued.
	private void sum_individual_curr() {
		if (cost == null) return;
		if (currency == null) return;
		
		cost = cost.setScale(currency.getDefaultFractionDigits(), BigDecimal.ROUND_DOWN);
	}
}
