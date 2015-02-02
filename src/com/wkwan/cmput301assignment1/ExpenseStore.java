package com.wkwan.cmput301assignment1;

import java.util.ArrayList;
import java.io.Serializable;

//This class is designed to store any and all of the expenses that
//users create with the application. Users can then access, review,
//add, remove, etc. the stored expenses.

public class ExpenseStore implements Serializable {
	
	// Serialization code that is created automatically by Eclipse
	private static final long serialVersionUID = -1702512719785055120L;
	private ArrayList<ExpenseCreate> expenses = null;
	
	public ExpenseStore() {
		expenses = new ArrayList<ExpenseCreate>();
	}
	
	public ArrayList<ExpenseCreate> getExpenses() {
		return expenses;
	}
	
	public void add(ExpenseCreate expense) {
		expenses.add(expense);
	}
	
	public ExpenseCreate get(int index) {
	    return expenses.get(index);
    }

	public void removeExpense(ExpenseCreate expense) {
		expenses.remove(expense);
	}
	
	public ExpenseCreate getExpense(int index) {
		return expenses.get(index);
	}
	
	public boolean contains(ExpenseCreate expense) {
		return expenses.contains(expense);
	}
	
	public int amount() {
		return expenses.size();
	}

	public int orderOf(Object object) {
	    return expenses.indexOf(object);
    }
}