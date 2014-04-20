package com.lybe;

import android.os.Bundle;

public class OneActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mesIntent.setClass(OneActivity.this,OtherActivity.class);
		thread = new Runnable(){ 
		     public void run(){ 
		    	 putToIntent();
		    	 //It is crucial to use "putExtras" at the right time. 2013.4.16
		    	 //Using "putExtras" here equals inserting values in the last step.
                 startActivity(mesIntent);
                 OneActivity.this.finish();
                 finish();
		     } 
		} ;
		if(!exitMark){
			handler.postDelayed(thread, delay);
		}
		else {
			//to do 
		}
	}
}
