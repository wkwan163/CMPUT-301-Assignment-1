package com.wkwan.cmput301assignment1;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

// This class allows users of the applicaiton to make changes to claims that have
// already been created. The claimOrder variable is used loosely as an index with
// which claims can either be accessed as new claims or existing claims in the
// storage list of pre-existing user created claims.

public class ClaimModify extends Activity {
	public static final String claimOrder = "CLAIM_ORDER";
	
	private View top_action_bar;
	private ClaimCreate userclaim = null;
	private boolean newClaim = false;
	private UserDateSelection startDate = null;
	private UserDateSelection endDate = null;
	private Calendar fromDate;
	private Calendar toDate;
	
	@Override
	public void onBackPressed() {
		removecaution();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        top_action_bar = getLayoutInflater().inflate(R.layout.claim_modification, null);
        ActionBar screentopheader = getActionBar();
        screentopheader.setCustomView(top_action_bar);
        screentopheader.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getMenuInflater().inflate(R.menu.claim_change, menu);
        
        screentopheader.getCustomView().findViewById(R.id.accept_claim_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View page) {
	        	keepEdits();
	        	finish();
			}
		});
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem option) {
        int face = option.getItemId();
        
        switch (face) {
        case R.id.action_discard_claim_changes:
        	removecaution();
        	return true;
        	
    	default:
    		break;
        }
        
	    return super.onOptionsItemSelected(option);
	}

	// User made claims are entered into a growing and persistent claims list.
	// If/else cases handle whether user is making new or editing pre-existing claims.
	@Override
	public void onCreate(Bundle persistentinfo) {
	    super.onCreate(persistentinfo);
        setContentView(R.layout.write_claim);
        
        Bundle enteredinfo = getIntent().getExtras();
        if (enteredinfo != null) {
        	int claim_order = enteredinfo.getInt(claimOrder, -1);
        	
        	if (claim_order != -1) userclaim = SummaryAccess.getClaimList().getClaim(claim_order);
        }
        
        if (userclaim == null) {
        	userclaim = new ClaimCreate();
        	SummaryAccess.getClaimList().addClaim(userclaim);
        	
        	newClaim = true;
        	
        } else {
        	newClaim = false;
        }
        	
    	((TextView) findViewById(R.id.claim_name_edittext)).setText(userclaim.getClaimLabel());
    	((TextView) findViewById(R.id.claim_destination_edittext)).setText(userclaim.getTravelLocation());
    	((TextView) findViewById(R.id.claim_reason_edittext)).setText(userclaim.getTripDescription());
    	fromDate = (Calendar) userclaim.getStartTime().clone();
    	toDate = (Calendar) userclaim.getEndTime().clone();
        
        // The built-in date picker widget is used to allow users to select the date
    	// on which their trip starts and the date on which it ends
        TextView fromText = (TextView) findViewById(R.id.claim_from_date_textview);
        startDate = new UserDateSelection(fromText, fromDate, null, null,
        		new android.app.DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
					        int dayOfMonth) {
						
		        		// Make sure start date comes before end date
						if (fromDate.after(toDate)) {
							endDate.setmmddyy(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
						}
					}});
        TextView convertwords = (TextView) findViewById(R.id.claim_to_date_textview);
        endDate = new UserDateSelection(convertwords, toDate, null, null,
        		new android.app.DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
					        int dayOfMonth) {

		        		// Make sure start date comes before end date
						if (fromDate.after(toDate)) {
							startDate.setmmddyy(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH), toDate.get(Calendar.DAY_OF_MONTH));
						}
					}});
	}
	
	// A cautionary dialogue box appears when the user selects to not proceed with any edits they made.
	// The throwing away of changes is permanent.
	private void removecaution() {
		AlertDialog.Builder warning = new AlertDialog.Builder(this);
		warning.setMessage(R.string.undo_edits_caution)
			   .setPositiveButton(R.string.user_remove, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface caution, int here) {
						removeEdits();
						finish();
					}
			   })
			   .setNegativeButton(R.string.user_undo, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface caution, int here) {
					}
			   });
		warning.create().show();
	}
	
	// This handles the permanent removal of edits made, should users proceed with formally undoing all edits
	private void removeEdits() {
    	if (newClaim) {
    		SummaryAccess.getClaimList().removeClaim(userclaim);
    	}
	}
	
	// This method saves the various modifications users may make any number of times to a claim they have started.
	private void keepEdits() {
    	userclaim.setCLaimLabel(((TextView) findViewById(R.id.claim_name_edittext)).getText().toString());
    	userclaim.setTravelLocation(((TextView) findViewById(R.id.claim_destination_edittext)).getText().toString());
    	userclaim.setTripDescription(((TextView) findViewById(R.id.claim_reason_edittext)).getText().toString());
    	userclaim.setStartTime(fromDate);
    	userclaim.setEndTime(toDate);
    	
    	SummaryAccess.save();
	}
}
