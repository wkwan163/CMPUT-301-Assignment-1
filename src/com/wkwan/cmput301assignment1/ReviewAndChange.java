package com.wkwan.cmput301assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import java.util.ArrayList;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

// This class is, as its title suggests, designed to allow users to
// review all claims made thus far and to review all expenses that comprise
// each individual claim. When accessing either a claim or an expense, the
// user is allowed to modify and change the details of the claim or expense
// as much as they wish.

public class ReviewAndChange extends Activity {
	public static final String claimOrder = "CLAIM_ORDER";
	
	private ClaimCreate userclaim = null;
	ExpenseShow expenseStorageAid = null;

	@Override
	protected void onCreate(Bundle persistentinfo) {
		super.onCreate(persistentinfo);
		setContentView(R.layout.display_expenses);
		
        Bundle enteredinfo = getIntent().getExtras();
        if (enteredinfo != null) {
        	int claim_order = enteredinfo.getInt(claimOrder, -1);
        	
        	if (claim_order != -1) userclaim = SummaryAccess.getClaimList().getClaim(claim_order);
        }
        
        if (userclaim == null) {
        	throw new RuntimeException("There is no claim for which to display expenses");
        }
        
        ArrayList<ExpenseCreate> expenses = userclaim.getExpenseList().getExpenses();
        expenseStorageAid = new ExpenseShow(this, R.layout.expenses_storage, expenses);
        ListView listscreen = (ListView) findViewById(R.id.expense_listview);
        listscreen.setAdapter(expenseStorageAid);
        listscreen.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
	            
				ExpenseCreate expense = (ExpenseCreate) expenseStorageAid.getItem(position);
				int claimset = SummaryAccess.getClaimList().orderOf(userclaim);
				int expenseset = userclaim.getExpenseList().orderOf(expense);
				
		    	Intent intent = new Intent(ReviewAndChange.this, ExpenseModify.class);
		    	intent.putExtra(ExpenseModify.claimOrder, claimset);
		    	intent.putExtra(ExpenseModify.expenseOrder, expenseset);
		    	
		    	startActivity(intent);
            }
		});
	}
	
	@Override
	protected void onResume() {
    	if (expenseStorageAid != null) expenseStorageAid.notifyDataSetChanged();

    	currentTotal();
    	
    	setTitle(userclaim.getClaimLabel());
    	
        super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.action_edit_claim:
			modifyClaim();
			return true;
			
		case R.id.action_add_expense:
			createExpense();
			return true;
			
		case R.id.action_delete_claim:
			deleteCaution();
			return true;
	    	
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void modifyClaim() {
		int claimset = SummaryAccess.getClaimList().orderOf(userclaim);
		
    	Intent intent = new Intent(ReviewAndChange.this, ClaimModify.class);
    	intent.putExtra(ClaimModify.claimOrder, claimset);
    	
    	startActivity(intent);
	}
	
	private void createExpense() {
		int claimset = SummaryAccess.getClaimList().orderOf(userclaim);
		
    	Intent intent = new Intent(ReviewAndChange.this, ExpenseModify.class);
    	intent.putExtra(ExpenseModify.claimOrder, claimset);
    	
    	startActivity(intent);
	}
	
	private void deleteCaution() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.remove_claim_caution)
			   .setPositiveButton(R.string.user_delete_claim, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeClaim();
						finish();
					}
			   })
			   .setNegativeButton(R.string.user_undo, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			   });
		builder.create().show();
	}
	
	private void removeClaim() {
		SummaryAccess.getClaimList().removeClaim(userclaim);
		SummaryAccess.save();
		
		finish();
	}
	
	private void currentTotal() {
		TextView totalView = (TextView) findViewById(R.id.expense_total_textview);
		StringBuilder builder = new StringBuilder();
		builder.append(getString(R.string.header_cost_total) + " =\n");
		builder.append(SummaryClaimLine.getTotalString(userclaim, this));
		
		totalView.setText(builder.toString());
	}
}
