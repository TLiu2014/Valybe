package com.lybe;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

public class SettingActivity extends Activity {

	private Button rightBtn = null;
	private Button leftBtn = null;
	private CheckBox cbBoot = null;
	private CheckBox cbChargeDialog = null;
	private CheckBox cbCharge = null;
	private EditText etDelay = null;
	private Spinner spinner = null;
	private TextView tvReset = null;
	private TextView tvHelp = null;
	private TextView tvAbout = null;
	private AlertDialog.Builder resetDialog;
	private AlertDialog.Builder aboutDialog;
	private ArrayAdapter<CharSequence> adapter;  
	private boolean isOpen;
	private boolean cdMark;
	private boolean chargeMark;
	private int delay;
	private int delayInText;
	private int timeUnit;
	private int spinNum;
	private int count;
	private SharedPreferences.Editor editor = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
        rightBtn = (Button)findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new BackBtnListener());
        leftBtn = (Button)findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new SaveBtnListener());
        cbBoot = (CheckBox)findViewById(R.id.cbBoot);
        cbChargeDialog = (CheckBox)findViewById(R.id.cbChargeDialog);
        cbCharge = (CheckBox)findViewById(R.id.cbCharge);
        etDelay = (EditText)findViewById(R.id.etDelay);
        spinner = (Spinner)findViewById(R.id.spinner);
        tvReset = (TextView)findViewById(R.id.tvReset);
        tvReset.setOnClickListener(new ResetBtnListener());
        tvHelp = (TextView)findViewById(R.id.tvHelp);
        tvHelp.setOnClickListener(new HelpBtnListener());
        tvAbout = (TextView)findViewById(R.id.tvAbout);
        tvAbout.setOnClickListener(new AboutBtnListener());
        resetDialog = new AlertDialog.Builder(this);
        resetDialog.setTitle(R.string.dialog_reset_title);
        resetDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delay = 4000;
		        timeUnit = 1000;
		        spinNum = 0;
		        delayInText = 4;
		        isOpen = true;
		        cdMark = true;
		        chargeMark = true;
		        spinner.setSelection(spinNum);
		        etDelay.setText(""+delayInText); 
		        cbBoot.setChecked(isOpen);
		        cbChargeDialog.setChecked(!cdMark);
			}      	
        });
        resetDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}      	
        });
        aboutDialog = new AlertDialog.Builder(this);
        aboutDialog.setTitle(R.string.about_detail);
        aboutDialog.setPositiveButton(R.string.dialog_gotit, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}      	
        });
        adapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinner.setAdapter(adapter);  
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());  
        
        SharedPreferences sp = getSharedPreferences("delay",Activity.MODE_PRIVATE);  
        editor = getSharedPreferences("delay",Activity.MODE_PRIVATE).edit();
        isOpen = sp.getBoolean("ISOPEN", true);
        cdMark = sp.getBoolean("cdMark", true);
        chargeMark = sp.getBoolean("chargeMark", true);
        delay = sp.getInt("delay", 4000);
        timeUnit = sp.getInt("timeUnit", 1000);
        spinNum = sp.getInt("spinNum", 0);
        delayInText  = delay/timeUnit;
        cbBoot.setChecked(isOpen);
        cbChargeDialog.setChecked(!cdMark);
        cbCharge.setChecked(chargeMark);
        spinner.setSelection(spinNum);
        etDelay.setText(""+delayInText);     
        count = getIntent().getExtras().getInt("count");
         
        cbBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
            	PackageManager pm = SettingActivity.this.getPackageManager();  
                ComponentName name = new ComponentName(SettingActivity.this, BootReceiver.class);  
                if (isChecked) 
                {  
                	isOpen = true;  
                    pm.setComponentEnabledSetting(name,  
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,  
                            PackageManager.DONT_KILL_APP);  
                } 
                else 
                {  
                    isOpen = false;  
                    pm.setComponentEnabledSetting(name,  
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  
                            PackageManager.DONT_KILL_APP);  
                }  
            } 
        });
        cbChargeDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
                if (isChecked) 
                {  
                	cdMark = false;   
                } 
                else 
                {  
                    cdMark = true;   
                }  
            } 
        });
        cbCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
                if (isChecked) 
                {  
                	chargeMark = true;   
                } 
                else 
                {  
                	chargeMark = false;   
                }  
            } 
        });
	}
	class SpinnerSelectedListener implements OnItemSelectedListener{  
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
		{  
        	if(0 == position)
        	{
        		timeUnit = 1000;
        		spinNum = 0;
        	}
        	else if(1 == position)
        	{
        		timeUnit = 60000;
        		spinNum = 1;
        	}
        	else if(2 == position)
        	{
        		timeUnit = 3600000;
        		spinNum = 2;
        	}
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
              
        }  
          
    } 
	class BackBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		Intent intent = new Intent(SettingActivity.this, PreActivity.class);
            intent.putExtra("count", count);
            startActivity(intent);
    		SettingActivity.this.finish();
    	}   	
    }
	class SaveBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		delayInText = Integer.valueOf(etDelay.getText().toString());
    		delay = delayInText * timeUnit;
            editor.putBoolean("ISOPEN", isOpen);  
            editor.putBoolean("cdMark", cdMark);  
            editor.putBoolean("chargeMark", chargeMark); 
    		editor.putInt("delay", delay);
    		editor.putInt("timeUnit", timeUnit);
    		editor.putInt("spinNum", spinNum);
    		editor.commit();
            etDelay.setText(""+delayInText);
            cbBoot.setChecked(isOpen);
            cbChargeDialog.setChecked(!cdMark);
            cbCharge.setChecked(chargeMark);
            Intent intent = new Intent(SettingActivity.this, PreActivity.class);
            intent.putExtra("count", count);
            startActivity(intent);
    		SettingActivity.this.finish();
    	}   
    }
	class ResetBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		resetDialog.show();
    	}   
    }
	class HelpBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		startActivity(new Intent(SettingActivity.this, HelpActivity.class));
    	}   
    }
	class AboutBtnListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		aboutDialog.show();
    	}   
    }
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(SettingActivity.this, PreActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
		SettingActivity.this.finish();
	}
}
