package com.lybe;

import android.os.Bundle;

public class OtherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mesIntent.setClass(OtherActivity.this,OneActivity.class);
		thread = new Runnable(){ 
		     public void run(){ 
		    	 putToIntent();
                 startActivity(mesIntent);
                 OtherActivity.this.finish();
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
