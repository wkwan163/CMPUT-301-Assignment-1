package com.wkwan.cmput301assignment1;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import android.content.Context;

// This class formats all the individual expenses of a claim into
// one or more properly punctuated lines to summarize the claim and
// its current and ongoing expenses.

public class SummaryClaimLine {

	static public final String getTotalString(ClaimCreate claim, Context context) {
		
		HashMap<Currency, BigDecimal> totals = claim.getTotals();
		
		StringBuilder builder = new StringBuilder();
		
		if (claim.getExpenseList().amount() == 0) {
			builder.append(context.getString(R.string.header_no_expenses));
			
		} else {
			for (Currency currency : totals.keySet()) {
				BigDecimal amount = totals.get(currency);
				
				builder.append(amount.toPlainString() +
							   " " +
							   currency.getCurrencyCode() +
							   ", ");
			}
			
			builder.delete(builder.length() - 2, builder.length());
		}
		
		return builder.toString();
	}
}