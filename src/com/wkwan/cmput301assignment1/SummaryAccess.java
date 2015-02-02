package com.wkwan.cmput301assignment1;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.Log;

// This class stores an overall global list of claims. It coordinates with the ClaimStore
// and SummaryPersistence classes to act on claims.

public class SummaryAccess {
	private static ClaimStore claimList = null;
	
	Context context = null;
	
	public static ClaimStore getClaimList() {
		if (claimList == null) {
			load();
		}
		
		return claimList;
	}
	
	public static void save() {
		try {
			SummaryPersistence.getManager().saveClaimList(claimList);
		}
		catch (IOException e) {
			throw new RuntimeException("List of claims could not be saved");
		}
	}
	
	public static void load() {
		try {
			claimList = SummaryPersistence.getManager().loadClaimList();
		}
		catch (IOException e) {
			throw new RuntimeException("List of claims could not be loaded");
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("List of claims could not be loaded");
		}
		
		if (claimList == null) claimList = new ClaimStore();
	}
}