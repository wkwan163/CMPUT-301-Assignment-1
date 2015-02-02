package com.wkwan.cmput301assignment1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

// This class allows users to select a month/day/year date from the
// built-in date-picker widget and to have this selected date appear
// on a text view in the program layout.

public class UserDateSelection {
	private TextView dateLine;
	private Calendar datewidget;
	private Calendar earliestDate;
	private Calendar futureDate;
	private DatePickerDialog.OnDateSetListener observer;
	private boolean assist = false;
	
	public UserDateSelection(TextView dateLine, Calendar datewidget, Calendar earliestDate, Calendar futureDate,
			DatePickerDialog.OnDateSetListener observer) {
	    this.dateLine = dateLine;
	    this.datewidget = datewidget;
	    this.observer = observer;
	    this.earliestDate = earliestDate;
	    this.futureDate = futureDate;
	    
	    dateLine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayMessage();
			}
		});
	    
	    if (this.earliestDate != null) {
	    	this.earliestDate.set(Calendar.MILLISECOND, this.earliestDate.getMinimum(Calendar.MILLISECOND));
	    }
	    if (this.futureDate != null) {
	    	this.futureDate.set(Calendar.MILLISECOND, this.futureDate.getMaximum(Calendar.MILLISECOND));
	    }
	    
	    currentdateLine();
    }
	
	public TextView getdateLine() {
		return dateLine;
	}

	public Calendar getdatewidget() {
		return datewidget;
	}

	public void setmmddyy(int year, int month, int day) {
		datewidget.set(year, month, day);
		currentdateLine();
	}
	
	public void currentdateLine() {
		String dateStr = DateFormat.getMediumDateFormat(dateLine.getContext()).format(datewidget.getTime());
		dateLine.setText(dateStr);
	}
	
	private void displayMessage() {
		assist = true;
		
		DatePickerDialog message = new DatePickerDialog(dateLine.getContext(),
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
					        int dayOfMonth) {
						
						if (!assist) {
							setmmddyy(year, monthOfYear, dayOfMonth);
							if (observer != null) observer.onDateSet(view, year, monthOfYear, dayOfMonth);
						}
					}
				},
				this.datewidget.get(Calendar.YEAR),
				this.datewidget.get(Calendar.MONTH),
				this.datewidget.get(Calendar.DAY_OF_MONTH));
		
		DatePicker selector = message.getDatePicker();
		if (earliestDate != null) selector.setMinDate(earliestDate.getTimeInMillis());
		if (futureDate != null) selector.setMaxDate(futureDate.getTimeInMillis());

		Context cntx = dateLine.getContext();
		
		message.setButton(Dialog.BUTTON_POSITIVE, cntx.getString(R.string.user_select), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				assist = false;
			}
		});
		
		message.setButton(Dialog.BUTTON_NEGATIVE, cntx.getString(R.string.user_undo), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				assist = true;
			}
		});
		
		message.show();
	}
}
