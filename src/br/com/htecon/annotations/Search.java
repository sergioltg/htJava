package br.com.htecon.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Search(SearchPolicy.LIKE)
public @interface Search {
	
	SearchPolicy value();	
	
}
