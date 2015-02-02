package com.wkwan.cmput301assignment1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import java.math.BigDecimal;
import java.util.Currency;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

// This class allows users to access, review, and modify any expenses that
// they have previously made in a particular claim. The use of the variable
// expenseOrder is similar to how claimOrder is used in the SummaryAccess class
// in which it is used loosely as an index that allows the app to know
// whether it is working on a new expense or a pre-existing expense in a list of
// pre-existing expenses.

public class ExpenseModify extends Activity {
	public static final String claimOrder = "CLAIM_ORDER";
	public static final String expenseOrder = "EXPENSE_ORDER";
	
	private View top_action_bar;
	private ClaimCreate userclaim = null;
	private ExpenseCreate userexpense = null;
	private ExpenseCreate toolexpense = null;	
	
	private UserDateSelection dateSelect = null;
	private Spinner triptypeSpinner = null;
	private Spinner moneySpinner = null;
	private EditText costText = null;
	private TextView tripdescriptionText = null;
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        top_action_bar = getLayoutInflater().inflate(R.layout.expense_modification, null);
        ActionBar screentopheader = getActionBar();
        screentopheader.setCustomView(top_action_bar);
        screentopheader.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getMenuInflater().inflate(R.menu.expense_change, menu);
        
        screentopheader.getCustomView().findViewById(R.id.accept_expense_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	        	keepEdits();
	        	finish();
			}
		});
        
        return true;
    }
	
	@Override
	public void onBackPressed() {
		removeWarning();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem option) {
        int face = option.getItemId();
        
        switch (face) {
        case R.id.action_discard_expense_changes:
        	removeWarning();
        	return true;
        	
        case R.id.action_delete_expense:
        	removeCaution();
        	return true;
        	
    	default:
    		break;
        }
        
	    return super.onOptionsItemSelected(option);
	}

	@Override
	public void onCreate(Bundle persistentinfo) {
	    super.onCreate(persistentinfo);
        setContentView(R.layout.write_expense);
        
        ArrayAdapter<CharSequence> categoryLister = ArrayAdapter.createFromResource(this,
        		R.array.type_of_expense,
        		android.R.layout.simple_spinner_item);
        categoryLister.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        triptypeSpinner = (Spinner) findViewById(R.id.expense_category_spinner);
        triptypeSpinner.setAdapter(categoryLister);
        
        ArrayAdapter<Currency> currencyLister = new ArrayAdapter<Currency>(this,
        		android.R.layout.simple_spinner_item);
        currencyLister.addAll(InternationalCurrencies.getAllCurrencies());
        currencyLister.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        moneySpinner = (Spinner) findViewById(R.id.expense_currency_spinner);
        moneySpinner.setAdapter(currencyLister);
		
        Bundle enteredinfo = getIntent().getExtras();
        if (enteredinfo != null) {
        	int claimset = enteredinfo.getInt(claimOrder, -1);
        	int expenseset = enteredinfo.getInt(expenseOrder, -1);
        	
        	if (claimset != -1) {
        		userclaim = SummaryAccess.getClaimList().getClaim(claimset);
            	if (expenseset != -1) userexpense = userclaim.getExpenseList().get(expenseset);
        	}
        }
        
        if (userclaim == null) {
        	throw new RuntimeException("There is no claim for which to review any expenses");
        }
        
        if (userexpense == null) {
        	toolexpense = new ExpenseCreate();
        	
        } else {
        	toolexpense = new ExpenseCreate(userexpense);
        }
        
        TextView mmddyyText = (TextView) findViewById(R.id.expense_date_textview);
        dateSelect = new UserDateSelection(mmddyyText, toolexpense.getmmddyy(), userclaim.getStartTime(), userclaim.getEndTime(), null);
        
        costText = (EditText) findViewById(R.id.expense_amount_edittext);
        tripdescriptionText = (TextView) findViewById(R.id.expense_description_edittext);
        
        triptypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
				
	            toolexpense.setexpensetype(triptypeSpinner.getSelectedItem().toString());
            }

			@Override
            public void onNothingSelected(AdapterView<?> parent) {}
		});
        
        moneySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
	            
				toolexpense.setCurrency((Currency) moneySpinner.getSelectedItem());
            }

			@Override
            public void onNothingSelected(AdapterView<?> parent) {}
		});
        
        costText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				try {
					toolexpense.setcost(new BigDecimal(s.toString()));
				}
				catch (NumberFormatException e) {
					entercostError();
				}
			}
		});
        
        tripdescriptionText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				toolexpense.setexpensedescription(s.toString());
			}
		});
        
        triptypeSpinner.setSelection(categoryLister.getPosition(toolexpense.getexpensetype()));
        moneySpinner.setSelection(currencyLister.getPosition(toolexpense.getCurrency()));
		tripdescriptionText.setText(toolexpense.getexpensedescription());
		costText.setText(toolexpense.getcost().toPlainString());
	}
	
	private void entercostError() {
		costText.setError(getString(R.string.cost_display_caution));
	}
	
	private void removeCaution() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.remove_expense_caution)
			   .setPositiveButton(R.string.user_delete_expense, new DialogInterface.OnClickListener() {
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
		userclaim.getExpenseList().removeExpense(userexpense);
		SummaryAccess.save();
		
		finish();
	}
	
	private void removeWarning() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.undo_edits_caution)
			   .setPositiveButton(R.string.user_remove, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
	
	private void keepEdits() {
		if (userexpense == null) {
			userexpense = new ExpenseCreate(toolexpense);
			userclaim.getExpenseList().add(userexpense);
		
		} else {
			userexpense.mirror(toolexpense);
		}
		
		SummaryAccess.save();
	}
}
