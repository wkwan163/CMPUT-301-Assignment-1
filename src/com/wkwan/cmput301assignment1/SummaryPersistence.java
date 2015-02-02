package com.wkwan.cmput301assignment1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

// This class handles the access, storage, serialization, and deserialization of lists of
// claims created by the user.

public class SummaryPersistence {
	private static final String prefFile = "ClaimList";
	private static final String clKey = "claimList";
	private static SummaryPersistence claimListManager = null;
	
	private Context context;
	
	public static void initManager(Context context) {
		if (context == null) {
			throw new RuntimeException("Context required for ClaimListManager");
		}
		
		claimListManager = new SummaryPersistence(context);
	}
	
	public static SummaryPersistence getManager() {
		if (claimListManager == null) {
			throw new RuntimeException("ClaimListManager not initialized");
		}
		
		return claimListManager;
	}
	
	public void saveClaimList(ClaimStore cl) throws IOException {
		SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString(clKey, claimListToString(cl));
		editor.commit();
	}

	public ClaimStore loadClaimList() throws ClassNotFoundException, IOException {
		SharedPreferences settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
		String studentListData = settings.getString(clKey, "");
		if (studentListData.equals("")) {
			return new ClaimStore();
		} else {
			return claimListFromString(studentListData);
		}
	}
	
	public String claimListToString(ClaimStore cl) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(cl);
		oo.close();
		
		byte bytes[] = bo.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	public ClaimStore claimListFromString(String claimListData) throws ClassNotFoundException, IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream(Base64.decode(claimListData, Base64.DEFAULT));
		ObjectInputStream oi = new ObjectInputStream(bi);
		
		return (ClaimStore)oi.readObject();	
	}
	
	private SummaryPersistence(Context context) {
		this.context = context;
	}
}