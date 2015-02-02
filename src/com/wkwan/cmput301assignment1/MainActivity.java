package com.wkwan.cmput301assignment1;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

// This main activity class displays any and all claims that a user has
// previously created, allowing the user to review and modify any claims they wish.

public class MainActivity extends Activity {
	private ClaimShow claimListModifier = null;

    @Override
    protected void onCreate(Bundle persistentinfo) {
        super.onCreate(persistentinfo);
        setContentView(R.layout.activity_main);
        SummaryPersistence.initManager(this.getApplicationContext());
        
        ArrayList<ClaimCreate> claims = SummaryAccess.getClaimList().getClaims();
        
        claimListModifier = new ClaimShow(this, R.layout.claims_storage, claims);
         
        ListView listScreen = (ListView) findViewById(R.id.claim_list_view);
        listScreen.setAdapter(claimListModifier);
        listScreen.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
	            
				ClaimCreate claim = claimListModifier.getItem(position);
				int claimset = SummaryAccess.getClaimList().orderOf(claim);
				
		    	Intent intent = new Intent(MainActivity.this, ReviewAndChange.class);
		    	intent.putExtra(ReviewAndChange.claimOrder, claimset);
		    	
		    	startActivity(intent);
            }
		});
    }
    
    @Override
    protected void onResume() {
    	if (claimListModifier != null) claimListModifier.notifyDataSetChanged();
    	
        super.onResume();
    }

    public void recordClaim() {
    	Intent intent = new Intent(MainActivity.this, ClaimModify.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_claim) {
        	recordClaim();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
