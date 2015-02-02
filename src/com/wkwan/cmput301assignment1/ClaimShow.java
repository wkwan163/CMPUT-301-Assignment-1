package com.wkwan.cmput301assignment1;

import android.content.Context;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// This class is primarily for displaying claims in an array list persistently.

public class ClaimShow extends ArrayAdapter<ClaimCreate> {
	private ArrayList<ClaimCreate> userclaims;

	public ClaimShow(Context context, int textViewResourceId, ArrayList<ClaimCreate> claims) {
	    super(context, textViewResourceId, claims);
	    this.userclaims = claims;
    }

	@Override
    public View getView(int position, View changeScreen, ViewGroup parent) {
	    View screen = changeScreen;
	    
	    if (changeScreen == null) {
	    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	screen = inflater.inflate(R.layout.claims_storage, null);
	    }
	    
	    ClaimCreate nu_claim = userclaims.get(position);
	    
	    TextView labelText = (TextView) screen.findViewById(R.id.name_textview);
	    if (labelText != null) {
	    	labelText.setText(nu_claim.getClaimLabel());
	    }
	    
	    TextView summaryText = (TextView) screen.findViewById(R.id.status_textview);
	    if (summaryText != null) {
	    	summaryText.setText(SummaryClaimLine.getTotalString(nu_claim, getContext()));
	    }
	    
	    
	    return screen;
    }
	
	
}