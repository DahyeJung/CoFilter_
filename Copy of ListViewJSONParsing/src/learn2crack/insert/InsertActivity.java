package learn2crack.insert;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import learn2crack.listview.MainActivity;
import learn2crack.listview.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertActivity extends Activity{
	
	String answer;
	TextView tvIsConnected, counter;
	EditText name,comment;
	Button btn;
	Person person;
	
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.insert);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.insert_title);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		name = (EditText)findViewById(R.id.name);
		comment = (EditText)findViewById(R.id.comment);
		btn = (Button)findViewById(R.id.ok);
		counter = (TextView)findViewById(R.id.count);
		btn.setEnabled(false);
		btn.setBackgroundColor(Color.GRAY);
		
		 /*if(isConnected()){
	            tvIsConnected.setBackgroundColor(0xFF00CC00);
	            tvIsConnected.setText("You are conncted");
	        }
	        else{
	            tvIsConnected.setText("You are NOT conncted");
	        }*/
	
		 final String[] not = getResources().getStringArray(R.array.not);
				 
		comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				counter.setText(String.valueOf(s.length()));//글자수
				if(s.length()>=5){
					btn.setEnabled(true);
					btn.setBackgroundColor(Color.RED);
				}else{
					btn.setEnabled(false);
					btn.setBackgroundColor(Color.GRAY);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				filter();
				
				if(!validate())
	                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
	                // call AsynTask to perform network operation on separate thread
	                new HttpAsyncTask().execute("http://cofilter.huray.net/cofilters.json");
	         
	            
			Intent intent = new Intent(InsertActivity.this,MainActivity.class);
			startActivity(intent);
			}

			private void filter() {
				
				String name1 = name.getText().toString();//name 값 
				String comment1 = comment.getText().toString();//edittext에서 가져온 값 
				
				for(int i=0 ;i<not.length;i++){
					
					if(name1.contains(not[i])){
						
						answer = not[i];
						String star = "";
						for(int j=0;j<not[i].length();j++){
							star +="*";
						}
						name1 = name1.replaceAll(answer,star);
						
						name.setText(name1);
					}
					
					if(comment1.contains(not[i])){
						//answer = 글자 뽑아오기
						
						//answer = comment.substring(comment.indexOf(not[i].charAt(0)),comment.lastIndexOf(not[i].charAt(not[i].length()-1))+1);
						answer = not[i];
						String star = "";
						for(int j=0;j<not[i].length();j++){
							star += "*";
						}
						comment1 = comment1.replaceAll(answer, star);
						/*Toast t = Toast.makeText(InsertActivity.this,comment1,Toast.LENGTH_LONG);
						t.show();*/
						comment.setText(comment1);
					}	
					
			}
			}
		
		});
		
		

		}//onCreate
		
		 public static String POST(String url, Person person) {
			   InputStream inputStream = null;
		        String result = "";
		        try {
		 
		            // 1. create HttpClient
		            HttpClient httpclient = new DefaultHttpClient();
		 
		            // 2. make POST request to the given URL
		            HttpPost httpPost = new HttpPost(url);
		 
		            String json = "";
		 
		            // 3. build jsonObject
		            JSONObject jsonObject = new JSONObject();
		            jsonObject.accumulate("user", person.getUser());
		            jsonObject.accumulate("content", person.getContent());
		           // jsonObject.accumulate("twitter", person.getTwitter());
		 
		            // 4. convert JSONObject to JSON to String
		            json = jsonObject.toString();
		 
		            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
		            // ObjectMapper mapper = new ObjectMapper();
		            // json = mapper.writeValueAsString(person); 
		 
		            // 5. set json to StringEntity
		            StringEntity se = new StringEntity(json,"utf-8");
		 
		            // 6. set httpPost Entity
		            httpPost.setEntity(se);
		 
		            // 7. Set some headers to inform server about the type of the content   
		            httpPost.setHeader("Accept", "application/json");
		            httpPost.setHeader("Content-type", "application/json");
		            httpPost.setHeader("Accept-Language", "utf-8");
		            // 8. Execute POST request to the given URL
		            HttpResponse httpResponse = httpclient.execute(httpPost);
		 
		            // 9. receive response as inputStream
		            inputStream = httpResponse.getEntity().getContent();
		 
		            // 10. convert inputstream to string
		            if(inputStream != null)
		                result = convertInputStreamToString(inputStream);
		            else
		                result = "Did not work!";
		 
		        } catch (Exception e) {
		            Log.d("InputStream", e.getLocalizedMessage());
		        }
		 
		        // 11. return result
		        return result;
			}
		/*
			public boolean isConnected() {
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
	            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	            if (networkInfo != null && networkInfo.isConnected()) 
	                return true;
	            else
	                return false;    
			}
		
		*/
			   private class HttpAsyncTask extends AsyncTask<String, Void, String> {
			        @Override
			        protected String doInBackground(String... urls) {
			 
			            person = new Person();
			            person.setUser(name.getText().toString());
			            person.setContent(comment.getText().toString());
			           // person.setTwitter(etTwitter.getText().toString());
			 
			            return POST(urls[0],person);
			        }
			       
					// onPostExecute displays the results of the AsyncTask.
			        @Override
			        protected void onPostExecute(String result) {
			            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
			       }
			    }
		
		private boolean validate() {
			 if(name.getText().toString().trim().equals(""))
		            return false;
		        else if(comment.getText().toString().trim().equals(""))
		            return false;
		       /* else if(etTwitter.getText().toString().trim().equals(""))
		            return false;*/
		        else
		            return true;    
			}

	

		private static String convertInputStreamToString(InputStream inputStream) throws IOException {
			   BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		        String line = "";
		        String result = "";
		        while((line = bufferedReader.readLine()) != null)
		            result += line;
		 
		        inputStream.close();
		        return result;
	}
		
			 }



	
	
	


