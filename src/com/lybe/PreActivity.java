package com.lybe;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class PreActivity extends BaseActivity {

	
	private String cachePath =  Environment.getExternalStorageDirectory().getPath()+"/valybe/cache/";
	private File dir = null;
	private boolean chargeMark;
	private boolean cdMark;//chargeDialogMark

	protected SharedPreferences.Editor editor = null;
	protected SharedPreferences sp = null;
	
	private AlertDialog.Builder chargeDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		chargeDialog = new AlertDialog.Builder(this);
		chargeDialog.setTitle(R.string.dialog_charge_title);
		chargeDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				exitMark = false;
				handler.postDelayed(thread, delay);
			}
        });
		chargeDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PreActivity.super.toSettingActivity();
				exitMark = false;
			}      	
        });
		CharSequence[] items = {PreActivity.this.getString(R.string.no_remind), };
		boolean [] itemChosen = {cdMark, };
		chargeDialog.setMultiChoiceItems(items, itemChosen, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,	boolean isChecked) {
				 if (isChecked)  
				 {
					 cdMark = false;
				 } 
				 else 
				 {  
					 cdMark = true;
				 }  			
				editor.putBoolean("cdMark", cdMark);
				editor.commit();
			} 	
        });
		
		chargeMark = getIntent().getBooleanExtra("chargeMark", false);
		//Initiate count, the counter of file/image numbers
		count = getIntent().getIntExtra("count", 1);
		//Get configuration values
		sp = getSharedPreferences("delay",Activity.MODE_PRIVATE);  
        editor = getSharedPreferences("delay",Activity.MODE_PRIVATE).edit();
        delay = sp.getInt("delay", 4000); 
        cdMark = sp.getBoolean("cdMark", true); 
        //Create the root directory
		dir = new File (imgPath);
        if(!dir.exists())
        {
         dir.mkdirs();
        }
        //Update images, get the number of images
        super.num = updateImgForNum();
        //If there is no image in the directory, redirect to help panel.
        if(0 == super.num)
        {
        	exitMark=true;
        	Intent intent = new Intent(PreActivity.this, HelpActivity.class);
        	intent.putExtra("fromPreActivity", true);
        	startActivity(intent);
        	PreActivity.this.finish();
        }
        //Display images
		showImg();
		
		if(chargeMark)
		{
			if(cdMark)
			{
				chargeDialog.show();
				exitMark = true;
			}
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "SimpleTimer");
            mWakelock.acquire();
            mWakelock.release();

            Window window = getWindow();  
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);  
            /*
            //Deprecated methods, which would influence the original screen locking if used.
            KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
            KeyguardLock keyguardLock = newKeyguardLock(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD );
            keyguardLock.disableKeyguard();   
            */
		}
		//Jump to another activity with delay
		mesIntent.setClass(PreActivity.this,OneActivity.class);
		thread = new Runnable(){ 
		     public void run(){ 
		    	 putToIntent();
                 startActivity(mesIntent);
                 PreActivity.this.finish();
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
	/**
	 * updateImg: rename images/files
	 */
	/*
	private void updateImg()
	{
		File files[] = dir.listFiles();
		File cache = new File (cachePath);
        cache.mkdirs();
		int i = 1;
		if(files != null)  
		{
			for(File f:files)
	        {  
	        	f.renameTo(new File(cachePath+i+".jpg"));//The path must be an absolute path 2013.4.6
	        	i++;
	        }
			files = cache.listFiles();
			i = 1;
			for(File f:files)
	        {  
	        	f.renameTo(new File(imgPath+i+".jpg"));//The path must be an absolute path 2013.4.6
	        	f.delete();
	        	i++;	        	
	        }
			cache.delete();
		}        
		else 
		{
			
		}
	}
	*/
	/**
	 * updateImgForNum: rename images/files, delete non-image files and images in illegal formats.
	 */
	private int updateImgForNum()
	{
		File files[] = dir.listFiles();
		File cache = new File (cachePath);
        cache.mkdirs();
		int i = 1;
		if(files != null)  
		{
			for(File f:files)
	        {  
				if(findRegx(f.getName(), ".jpg$") || findRegx(f.getName(), ".png$"))
				{
					f.renameTo(new File(cachePath+i+".jpg"));//The path must be an absolute path 2013.4.6
		        	i++;
				} 
				else
				{
					f.delete();
				}
	        }
			files = cache.listFiles();
			i = 1;
			for(File f:files)
	        {  
	        	f.renameTo(new File(imgPath+i+".jpg"));//The path must be an absolute path 2013.4.6
	        	f.delete();
	        	i++;	        	
	        }
			cache.delete();
			return i-1;
		}        
		else 
		{
			return 0;
		}
	}
	/**
	 * Find substrings matching the rule of "regEx" in "src".
	 */
	public boolean findRegx(String src, String regEx) 
	{
		Pattern pat = Pattern.compile(regEx);
	    Matcher matcher = pat.matcher(src);
	    if (matcher.find()) 
	    {
	    	return true;
	    } 
	    else 
	    {
	    	return false;
	    }	    
	}
}
