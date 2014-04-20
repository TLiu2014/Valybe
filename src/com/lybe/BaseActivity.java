package com.lybe;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class BaseActivity extends Activity {

	protected int delay;
	protected boolean exitMark;
	protected boolean isPreMark;
	protected int count;
	//Image numbers
	protected int num;
	protected boolean showBtnMark;
	protected boolean dialogMark;
	protected String imgPath =  Environment.getExternalStorageDirectory().getPath()+"/valybe/image/";
	protected Runnable thread = null;
	protected Handler handler = null;
	protected Intent mesIntent = null;
	
	private Button touchBtn = null;
	private Button leftBtn = null;
	private Button rightBtn = null;
	private Button midBtn = null;
	private AlertDialog.Builder dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Initiate widgets
		widgetInit();
        
        if(!"PreActivity".equals(this.getLocalClassName()))
        {
    		exitMark = getIntent().getExtras().getBoolean("exitMark");
    		showBtnMark = getIntent().getExtras().getBoolean("showBtnMark");
    		count = getIntent().getExtras().getInt("count");
    		num = getIntent().getExtras().getInt("num");
    		dialogMark = getIntent().getExtras().getBoolean("dialogMark");
    		delay = getIntent().getExtras().getInt("delay");
    		showImg();
        }
        
		handler = new Handler();
		if(dialogMark)
			dialog.show();
		if(showBtnMark)
			showBtn();
		mesIntent = new Intent();
	}
	class TouchBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		showBtn();
    		if(!showBtnMark){
                leftBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom));
                rightBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom));
                midBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom));
                showBtnMark=true;
    		}
    		//If the buttons at the bottom have appeared, click the button again, 
    		//the buttons would appear again with a decreased offset.
    		//If the "else" part below were commented, click the button again, 
    		//the buttons would appear again with the same offset as before.
    		else{
    			leftBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom_half));
                rightBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom_half));
                midBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.from_bottom_half));
    		}
    	}   	
    }
	class HideBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		leftBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.to_bottom));
            rightBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.to_bottom));
            midBtn.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.to_bottom));
			leftBtn.setVisibility(View.INVISIBLE);
    		rightBtn.setVisibility(View.INVISIBLE);
    		midBtn.setVisibility(View.INVISIBLE);
    		showBtnMark=false;
    	}   	
    }
	class SettingBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		toSettingActivity();
    	}   	
    }
	class ExitBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		dialog.show();
    		dialogMark = true;
    	}   	
    }
	@Override
	public void onBackPressed()
	{
		dialog.show();
		dialogMark = true;
	}
	private void showBtn()
	{
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.VISIBLE);
		midBtn.setVisibility(View.VISIBLE);
	}
	/**
	 * Initiate widgets
	 */
	private void widgetInit()
	{
		touchBtn = (Button)findViewById(R.id.touchBtn);
        touchBtn.setOnClickListener(new TouchBtnListener());
        leftBtn = (Button)findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new HideBtnListener());
        rightBtn = (Button)findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new ExitBtnListener());
        midBtn = (Button)findViewById(R.id.midBtn);
        midBtn.setOnClickListener(new SettingBtnListener());
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_exit_title);
        dialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				exitMark = true;
				dialogMark = false;
				handler.removeCallbacks(thread);
				BaseActivity.this.finish();
			}      	
        });
        dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialogMark = false;
			}      	
        });
	}
	protected void putToIntent()
	{
        mesIntent.putExtra("exitMark", BaseActivity.this.exitMark);
        mesIntent.putExtra("showBtnMark", BaseActivity.this.showBtnMark);
        mesIntent.putExtra("count", BaseActivity.this.count);
        mesIntent.putExtra("num", BaseActivity.this.num);
        mesIntent.putExtra("dialogMark", BaseActivity.this.dialogMark);
        mesIntent.putExtra("delay", BaseActivity.this.delay);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	 	getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		if(R.id.menu_settings == item.getItemId())
		{
			toSettingActivity();
			return true;
		}
		else
		{
			return super.onOptionsItemSelected(item);
		}
	}
	protected void toSettingActivity()
	{
		exitMark=true;
		Intent intent = new Intent(BaseActivity.this,SettingActivity.class);
		intent.putExtra("count", BaseActivity.this.count - 1);
        startActivity(intent);
        handler.removeCallbacks(thread);
        BaseActivity.this.finish();
	}
	/**
	 * showImg: set background image, modify the value of the variable "count"
	 */
	protected void showImg()
	{
		//set background image
		findViewById(R.id.main).setBackground(Drawable.createFromPath(imgPath+count+".jpg"));
		if(count < num)
			count++;
		else
			count=1;	
	}
}
