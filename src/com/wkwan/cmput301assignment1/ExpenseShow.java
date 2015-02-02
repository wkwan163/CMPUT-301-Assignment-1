package com.wkwan.cmput301assignment1;

import java.util.Calendar;
import java.util.ArrayList;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// A class designed to display the user's created expenses and their details in an array list.

public class ExpenseShow extends ArrayAdapter<ExpenseCreate> {
	private ArrayList<ExpenseCreate> expenses;

	public ExpenseShow(Context context, int textViewResourceId, ArrayList<ExpenseCreate> expenses) {
	    super(context, textViewResourceId, expenses);
	    this.expenses = expenses;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    
	    if (convertView == null) {
	    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	view = inflater.inflate(R.layout.expenses_storage, null);
	    }
	    
	    ExpenseCreate expense = expenses.get(position);
	    
	    TextView mainTextView = (TextView) view.findViewById(R.id.main_textview);
	    if (mainTextView != null) {
	    	mainTextView.setText(expense.getcost().toString() + " " + expense.getCurrency().getCurrencyCode());
	    }
	    
	    TextView secondaryTextView = (TextView) view.findViewById(R.id.secondary_textview);
	    if (secondaryTextView != null) {
	    	Calendar datewidget = expense.getmmddyy();
	    	String dateStr = DateFormat.getMediumDateFormat(getContext()).format(datewidget.getTime());
	    	secondaryTextView.setText(dateStr + " - " + expense.getexpensetype());
	    }   
	    return view;
    }
}
