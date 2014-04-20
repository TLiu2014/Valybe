package com.lybe;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	private Button backBtn = null;
	private Button sdiBtn = null; //set default image button
	protected String imgPath =  Environment.getExternalStorageDirectory().getPath()+"/valybe/image/";
	private boolean fromPreMark;
	private AlertDialog.Builder noImgDialog;
	private AlertDialog.Builder sdiDialog; //set default image dialog
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		backBtn = (Button)findViewById(R.id.rightBtn);
		backBtn.setOnClickListener(new BackBtnListener());
		sdiBtn = (Button)findViewById(R.id.rightBtn2);
		sdiBtn.setOnClickListener(new SdiBtnListener());
		noImgDialog = new AlertDialog.Builder(this);
        noImgDialog.setTitle(R.string.dialog_no_img);
        noImgDialog.setPositiveButton(R.string.dialog_to_know_how, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}      	
        });
        sdiDialog = new AlertDialog.Builder(this);
        sdiDialog.setTitle(R.string.dialog_sdi);
        sdiDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {		
	    		int [] images = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3
	    				, R.drawable.image_4, R.drawable.image_5};
	    		for(int count = 0; count < 5;)
	    		{
	    			Bitmap bmp = BitmapFactory.decodeResource(getResources(), images[count]);
	        		count++;
	                try { 
	                	FileOutputStream fOut = new FileOutputStream(
	                			Environment.getExternalStorageDirectory().getPath()+"/valybe/image/"+ count + ".jpg"); 
	                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut); 
	                    fOut.flush(); 
	                    fOut.close(); 
	                } catch (IOException e) { 
	                    e.printStackTrace(); 
	                }
	    		}
			}      	
        });
        sdiDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}      	
        });
		fromPreMark = getIntent().getBooleanExtra("fromPreActivity", false);
		if(fromPreMark)
		{
			noImgDialog.show();
		}
	}
	class BackBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		onBackPressed();
    	}   	
    }
	class SdiBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		sdiDialog.show();
    	}   	
    }
	@Override
	public void onBackPressed(){
		if(fromPreMark)//redirected from PreActivity
		{
			startActivity(new Intent(HelpActivity.this, PreActivity.class));
		}
		else//redirected from SettingActivity
		{
			//to do
		}
		HelpActivity.this.finish();
	}
}
