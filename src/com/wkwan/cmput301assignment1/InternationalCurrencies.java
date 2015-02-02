package com.wkwan.cmput301assignment1;

import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

// This class serves to acquire the official designations of the four example
// world currencies of CAD, USD, EUR, and GBP outlined in the assignment description.
// Once obtained from the built-in currency utility, the desginations are placed into
// the user currency selection view and alphabetized in order.

final public class InternationalCurrencies {

	private InternationalCurrencies() {}
	
	private static ArrayList<Currency> currencies = null;
	
	public static ArrayList<Currency> getAllCurrencies() {
		if (currencies == null) {
	        Set<Currency> sampleCurrencies = new HashSet<Currency>();
	        for (Locale locale : Locale.getAvailableLocales())
	        {
	        	try {
	        		Currency can_currency = Currency.getInstance(Locale.CANADA);
	        		Currency us_currency = Currency.getInstance(Locale.US);
	        		Currency euro_currency = Currency.getInstance(Locale.GERMANY);
	        		Currency gbp_currency = Currency.getInstance(Locale.UK);
	        		sampleCurrencies.add(can_currency);
	        		sampleCurrencies.add(us_currency);
	        		sampleCurrencies.add(euro_currency);
	        		sampleCurrencies.add(gbp_currency);
	        	}
	        	catch (IllegalArgumentException e) {
	        	}
	        }
	        
	        currencies = new ArrayList<Currency>(sampleCurrencies);
	        Collections.sort(currencies, new Comparator<Currency>() {
				@Override
	            public int compare(Currency lhs, Currency rhs) {
		            return lhs.getCurrencyCode().compareTo(rhs.getCurrencyCode());
	            }
			});
		}
        
        return currencies;
	}
}