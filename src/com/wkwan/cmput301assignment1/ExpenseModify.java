package com.wkwan.cmput301assignment1;

import java.math.BigDecimal;
import java.util.Currency;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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

/*
 * Lets the user modify the settings for an expense.
 * 
 * CLAIM_INDEX must be passed as an integer extra. The claim in
 * ClaimListController at index CLAIM_INDEX will be edited.
 * 
 * If EXPENSE_INDEX is passed as an integer extra, then the expense
 * in the claim's list at index EXPENSE_INDEX will be edited rather
 * than creating a new one.
 */

public class ExpenseModify extends Activity {
	public static final String CLAIM_INDEX = "CLAIM_INDEX";
	public static final String EXPENSE_INDEX = "EXPENSE_INDEX";
	
	private View actionBarView;
	private Claim claim = null;
	private Expense expense = null;		// This is the original expense
	private Expense tempExpense = null;	// This will be copied back to expense when changes are saved
	
	private DatePickerController datePicker = null;
	private Spinner categorySpinner = null;
	private Spinner currencySpinner = null;
	private EditText amountEditText = null;
	private TextView descriptionEditText = null;
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBarView = getLayoutInflater().inflate(R.layout.menu_edit_expense, null);
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getMenuInflater().inflate(R.menu.edit_expense, menu);
        
        // Handle onClick for custom accept button
        actionBar.getCustomView().findViewById(R.id.accept_expense_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	        	saveChanges();
	        	finish();
			}
		});
        
        return true;
    }
	
	@Override
	public void onBackPressed() {
		discardAlert();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
        case R.id.action_discard_expense_changes:
        	discardAlert();
        	return true;
        	
        case R.id.action_delete_expense:
        	deleteAlert();
        	return true;
        	
    	default:
    		break;
        }
        
	    return super.onOptionsItemSelected(item);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        
        // Populate category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
        		R.array.expense_categories,
        		android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = (Spinner) findViewById(R.id.expense_category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        
        // Populate currency spinner
        ArrayAdapter<Currency> currencyAdapter = new ArrayAdapter<Currency>(this,
        		android.R.layout.simple_spinner_item);
        currencyAdapter.addAll(CurrencyHelper.getAllCurrencies());
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        currencySpinner = (Spinner) findViewById(R.id.expense_currency_spinner);
        currencySpinner.setAdapter(currencyAdapter);
		
		// Get the claim
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	int claimIndex = extras.getInt(CLAIM_INDEX, -1);
        	int expenseIndex = extras.getInt(EXPENSE_INDEX, -1);
        	
        	if (claimIndex != -1) {
        		claim = ClaimListController.getClaimList().getClaim(claimIndex);
            	if (expenseIndex != -1) expense = claim.getExpenseList().get(expenseIndex);
        	}
        }
        
        // Missing claim
        if (claim == null) {
        	throw new RuntimeException("Attempted to edit expense for nonexistent claim");
        }
        
        // Need to make a new expense
        if (expense == null) {
        	tempExpense = new Expense();
        	
    	// Existing expense so make a copy of it
        } else {
        	tempExpense = new Expense(expense);
        }
        
        // Set up the date field as it needs a special controller
        // We don't need a special listener to update tempExpense.getDate, as the controller handles it
        TextView dateText = (TextView) findViewById(R.id.expense_date_textview);
        datePicker = new DatePickerController(dateText, tempExpense.getDate(), claim.getFrom(), claim.getTo(), null);
        
    	// Get other fields
        amountEditText = (EditText) findViewById(R.id.expense_amount_edittext);
        descriptionEditText = (TextView) findViewById(R.id.expense_description_edittext);
        
        // Set listener
        categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
				
	            tempExpense.setCategory(categorySpinner.getSelectedItem().toString());
            }

			@Override
            public void onNothingSelected(AdapterView<?> parent) {}
		});
        
        currencySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
	            
				tempExpense.setCurrency((Currency) currencySpinner.getSelectedItem());
            }

			@Override
            public void onNothingSelected(AdapterView<?> parent) {}
		});
        
        amountEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				try {
					tempExpense.setAmount(new BigDecimal(s.toString()));
				}
				catch (NumberFormatException e) {
					amountFormatError();
				}
			}
		});
        
        descriptionEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				tempExpense.setDescription(s.toString());
			}
		});
        
        // Populate fields
        categorySpinner.setSelection(categoryAdapter.getPosition(tempExpense.getCategory()));
        currencySpinner.setSelection(currencyAdapter.getPosition(tempExpense.getCurrency()));
		descriptionEditText.setText(tempExpense.getDescription());
		amountEditText.setText(tempExpense.getAmount().toPlainString());
	}
	
	/*
	 * Displays a format error for the amount field
	 */
	private void amountFormatError() {
		amountEditText.setError(getString(R.string.amount_format_error));
	}
	
	/**
	 * Ask before deleting expense.
	 */
	private void deleteAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_expense_message)
			   .setPositiveButton(R.string.action_delete_expense, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteClaim();
						finish();
					}
			   })
			   .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			   });
		builder.create().show();
	}
	
	/**
	 * Delete the expense.
	 */
	private void deleteClaim() {
		claim.getExpenseList().removeExpense(expense);
		ClaimListController.save();
		
		finish();
	}
	
	// Ask before discarding changes
	private void discardAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.discard_message)
			   .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
			   })
			   .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			   });
		builder.create().show();
	}
	
	// Save changes from the temporary expense to the actual expense
	// If necessary, create a new expense in the claim
	private void saveChanges() {
		// Create the new expense to the claim
		if (expense == null) {
			expense = new Expense(tempExpense);
			claim.getExpenseList().add(expense);
		
		// Expense already exists
		} else {
			expense.copyFrom(tempExpense);
		}
		
		ClaimListController.save();
	}
}
