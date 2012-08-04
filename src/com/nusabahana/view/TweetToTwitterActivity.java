package com.nusabahana.view;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TweetToTwitterActivity extends Activity {

	private static final String TAG = "Blundell.TweetToTwitterActivity";

	/** Name to store the users access token */
	private static final String PREF_ACCESS_TOKEN = "accessToken";
	/** Name to store the users access token secret */
	private static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_KEY = "eu7WDgiIHAh3RJSNRcA";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
	private static final String CONSUMER_SECRET = "McMeKiOVRpHeD2RDvpPuVzoVpnWSTU7mJ9IciA3WWQ"; // XXX Encode in your app
	/** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	private static final String CALLBACK_URL = "tweet-to-twitter-blundell-01-android:///";
	/** Preferences to store a logged in users credentials */
	private SharedPreferences mPrefs;
	/** Twitter4j object */
	private Twitter mTwitter;
	/** The request token signifies the unique ID of the request you are sending to twitter  */
	private RequestToken mReqToken;

	private EditText twitterMessage;
	private Button tweet;
	private Button logout;
	private Button login;
//	private RadioButton mTweetStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Loading TweetToTwitterActivity");
		setContentView(R.layout.share_twitter);
		
		// Create a new shared preference object to remember if the user has
		// already given us permission
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		
		Log.i(TAG, "Got Preferences");
		
		// Load the twitter4j helper
		mTwitter = new TwitterFactory().getInstance();
		Log.i(TAG, "Got Twitter4j");
		
		// Tell twitter4j that we want to use it with our app
		mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		Log.i(TAG, "Inflated Twitter4j");
		
		
//		loginStatus = (RadioButton)findViewById(R.id.login_status);
        tweet = (Button) findViewById(R.id.btn_post);
        logout = (Button) findViewById(R.id.btn_logout);
        login = (Button) findViewById(R.id.btn_login);
        twitterMessage = (EditText) findViewById(R.id.twitter_message_box);
		
        
        updateLoginStatus();
        
        tweet.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
        		tweetMessage();
            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
        		loginNewUser();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
		//Check previous authentication status
//		if(mPrefs.contains(PREF_ACCESS_TOKEN)) {
//			Log.i(TAG, "User has logged in");
//			loginAuthorisedUser();
//			mLoginButton.setEnabled(false);
//		} else {
//			disableTweetButton();
//			mTweetMessage.setEnabled(false);
//		}
	}

	/**
	 * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br> 
	 * Checks if the user has given this app permission to use twitter
	 * before</br> If so login and enable tweeting</br> 
	 * Otherwise redirect to Twitter for permission
	 * 
	 * @param v the clicked button
	 */
//	public void buttonLogin(View v) {
//		Log.i(TAG, "Login Pressed");
//		if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
//			Log.i(TAG, "Repeat User");
//			loginAuthorisedUser();
//		} else {
//			Log.i(TAG, "New User");
//			loginNewUser();
//		}
//	}

	/**
	 * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br>
	 * 
	 * @param v the clicked button
	 */
//	public void buttonTweet(View v) {
//		Log.i(TAG, "Tweet Pressed");
//		tweetMessage();
//	}

	/**
	 * Create a request that is sent to Twitter asking 'can our app have permission to use Twitter for this user'</br> 
	 * We are given back the {@link mReqToken}
	 * that is a unique indetifier to this request</br> 
	 * The browser then pops up on the twitter website and the user logins in ( we never see this informaton
	 * )</br> Twitter then redirects us to {@link CALLBACK_URL} if the login was a success</br>
	 * 
	 */
	private void loginNewUser() {
		try {
			Log.i(TAG, "Request App Authentication");
			mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);

			Log.i(TAG, "Starting Webview to login to twitter");
			WebView twitterSite = new WebView(this);
			twitterSite.loadUrl(mReqToken.getAuthenticationURL());
			setContentView(twitterSite);

		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * The user had previously given our app permission to use Twitter</br> 
	 * Therefore we retrieve these credentials and fill out the Twitter4j helper
	 */
	private void loginAuthorisedUser() {
		String token = mPrefs.getString(PREF_ACCESS_TOKEN, null);
		String secret = mPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

		// Create the twitter access token from the credentials we got previously
		AccessToken at = new AccessToken(token, secret);

		mTwitter.setOAuthAccessToken(at);
		
		Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
		
//		enableTweetButton();
	}

	/**
	 * Catch when Twitter redirects back to our {@link CALLBACK_URL}</br> 
	 * We use onNewIntent as in our manifest we have singleInstance="true" if we did not the
	 * getOAuthAccessToken() call would fail
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(TAG, "New Intent Arrived");
		dealWithTwitterResponse(intent);
		updateLoginStatus();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "Arrived at onResume");
	}
	
	/**
	 * Twitter has sent us back into our app</br> 
	 * Within the intent it set back we have a 'key' we can use to authenticate the user
	 * 
	 * @param intent
	 */
	private void dealWithTwitterResponse(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");
			authoriseNewUser(oauthVerifier);
		}
	}

	/**
	 * Create an access token for this new user</br> 
	 * Fill out the Twitter4j helper</br> 
	 * And save these credentials so we can log the user straight in next time
	 * 
	 * @param oauthVerifier
	 */
	private void authoriseNewUser(String oauthVerifier) {
		try {
			AccessToken at = mTwitter.getOAuthAccessToken(mReqToken, oauthVerifier);
			mTwitter.setOAuthAccessToken(at);
			saveAccessToken(at);
			onCreate(null);
			// Set the content view back after we changed to a webview
//			setContentView(R.layout.share_twitter);
//			updateLoginStatus();
//			enableTweetButton();
		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Allow the user to Tweet
	 */
//	private void enableTweetButton() {
//		Log.i(TAG, "User logged in - allowing to tweet");
//		mLoginButton.setEnabled(false);
//		mLoginButton.setClickable(false);
//		
//		mTweetButton.setEnabled(true);
//		mTweetButton.setClickable(true);
//		
//		mTweetMessage.setEnabled(true);
//		mTweetMessage.setClickable(true);
//		
////		mTweetStatus.setChecked(true);
////		mTweetStatus.setText("Welcome, post your tweet ^^");
//	}
//	
//	private void disableTweetButton(){
//		Log.i(TAG, "Hasn't logged in. Must authenticate");
//		mLoginButton.setEnabled(true);
//		mLoginButton.setClickable(true);
//		
//		mTweetButton.setEnabled(false);
//		mTweetButton.setClickable(false);
//		
//		mTweetMessage.setEnabled(false);
//		mTweetMessage.setClickable(false);
//		
////		mTweetStatus.setChecked(false);
////		mTweetStatus.setText("Please login first ^^");
//	}

	/**
	 * Send a tweet on your timeline, with a Toast msg for success or failure
	 */
	private void tweetMessage() {
		try {
			String message = twitterMessage.getText().toString();
			if(message.equals("")){
				Toast.makeText(this, "Oops.. Please insert your message", Toast.LENGTH_SHORT).show();
			} else if(message.length() > 125){
				Toast.makeText(this, "Oops.. Maximum characters is 125", Toast.LENGTH_SHORT).show();
			}else {
				mTwitter.updateStatus(message+" #nusabahana");
				Toast.makeText(this, "Tweets success ^^", Toast.LENGTH_SHORT).show();
			}
		} catch (TwitterException e) {
			Toast.makeText(this, "Tweet error, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveAccessToken(AccessToken at) {
		String token = at.getToken();
		String secret = at.getTokenSecret();
		Editor editor = mPrefs.edit();
		editor.putString(PREF_ACCESS_TOKEN, token);
		editor.putString(PREF_ACCESS_TOKEN_SECRET, secret);
		editor.commit();
	}
	
	public void updateLoginStatus() {
		if(mPrefs.contains(PREF_ACCESS_TOKEN)){
//			loginStatus.setText("Welcome");
//			loginStatus.setClickable(false);
//			loginStatus.setSelected(true);
			
			tweet.setEnabled(true);
			login.setEnabled(false);
			logout.setEnabled(true);
			
			loginAuthorisedUser();
		} else {
//			loginStatus.setText("Please Log in");
//			loginStatus.setSelected(false);
//			loginStatus.setClickable(false);
			
			tweet.setEnabled(false);
			login.setEnabled(true);
			logout.setEnabled(false);
		}
	}
	
	private void clearCredentials() {
		Editor e = mPrefs.edit();
		e.remove(PREF_ACCESS_TOKEN);
		e.commit();
		mTwitter.setOAuthAccessToken(null);
	}
}