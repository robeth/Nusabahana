package com.nusabahana.facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import com.nusabahana.view.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidFacebookSample extends Activity {

	private static final String FACEBOOK_APPID = "399011940130195";
	private static final String FACEBOOK_PERMISSION = "publish_stream";
	private static final String TAG = "FacebookSample";
	private final Handler mFacebookHandler = new Handler();
//	private RadioButton loginStatus;
	private FacebookConnector facebookConnector;
	private EditText facebookMessage;
	private Button tweet;
	private Button logout;
	private Button login;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Update success ^^", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_facebook);
        this.facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
        

//        loginStatus = (RadioButton)findViewById(R.id.login_status);
        tweet = (Button) findViewById(R.id.btn_post);
        logout = (Button) findViewById(R.id.btn_logout);
        login = (Button) findViewById(R.id.btn_login);
        facebookMessage = (EditText) findViewById(R.id.facebook_message_box);
        
        updateLoginStatus();
        
        tweet.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
        		postMessage();
            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
        		postMessage();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
		updateLoginStatus();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}
	
	public void updateLoginStatus() {
		if(facebookConnector.getFacebook().isSessionValid()){
//			loginStatus.setText("Welcome");
//			loginStatus.setClickable(false);
//			loginStatus.setSelected(true);
			
			tweet.setEnabled(true);
			login.setEnabled(false);
			logout.setEnabled(true);
		} else {
//			loginStatus.setText("Please Log in");
//			loginStatus.setSelected(false);
//			loginStatus.setClickable(false);
			
			tweet.setEnabled(false);
			login.setEnabled(true);
			logout.setEnabled(false);
		}
	}
	

	private String getFacebookMsg() {
		return facebookMessage.getText().toString()+"\n #nusabahana";
		
	}	
	
	public void postMessage() {
		
		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {
				
				@Override
				public void onAuthSucceed() {
					updateLoginStatus();
				}
				
				@Override
				public void onAuthFail(String error) {
					
				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
		    	
		    	try {
		    		if(facebookMessage.getText().toString().length() != 0){
			    		facebookConnector.postMessageOnWall(getFacebookMsg());
						mFacebookHandler.post(mUpdateFacebookNotification);
		    		} else {
		    			Toast.makeText(AndroidFacebookSample.this, "Please enter your message", Toast.LENGTH_SHORT);
		    		}
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}