package learn2crack.listview;

import java.util.ArrayList;
import java.util.HashMap;

import learn2crack.listview.library.JSONParser;
import learn2crack.insert.InsertActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	ListView list;
	TextView user;
	TextView ver;
	TextView name;
	TextView api;
	TextView url1;
	Button Btngetdata;
	Button Insert;
	
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	//URL to get JSON Array
	private static String url = "http://cofilter.huray.net/cofilters.json";
	
	//JSON Node Names 
	private static final String TAG_OS = "comments";
	private static final String TAG_USER = "user";
	private static final String TAG_CONTENT = "content";
	//private static final String TAG_ID = "id";
	private static final String TAG_CREATE = "created_at";
	//private static final String TAG_URL = "url";
	
	JSONArray android = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.list_title);
        oslist = new ArrayList<HashMap<String, String>>();
        
        Btngetdata = (Button)findViewById(R.id.getdata);
        
        
        Btngetdata.setOnClickListener(new View.OnClickListener() {
			
        	
			@Override
			public void onClick(View view) {
				
				new JSONParse().execute();
		        
		       }
			
		});
        
       Insert = (Button)findViewById(R.id.insert);
       
       Insert.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,InsertActivity.class);
		startActivity(intent);
		}
	});
    }


    
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
    	 private ProgressDialog pDialog;
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
             user = (TextView)findViewById(R.id.user);
             //ver = (TextView)findViewById(R.id.vers);
			 name = (TextView)findViewById(R.id.name);
			 api = (TextView)findViewById(R.id.api);
			// url1 = (TextView)findViewById(R.id.url);
			 
			//list.invalidate();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);//게이지가 올라가거나 화살표가 돌아가는 것이 작업이 완료될때까지 멈추기 않게.
            pDialog.setCancelable(true);//back key 막기
            pDialog.show();
            
            
            
    	}
    	
    
    	
    	@Override
        protected JSONObject doInBackground(String... args) {
    		
    		JSONParser jParser = new JSONParser();

    		// Getting JSON from URL
    		JSONObject json = jParser.getJSONFromUrl(url);
    		return json;
    	}
    	 @Override
         protected void onPostExecute(JSONObject json) {
    		 pDialog.dismiss();
    		 try {
    				// Getting JSON Array from URL
    				android = json.getJSONArray(TAG_OS);
    				
    				for(int i = 0; i < android.length(); i++){
    				JSONObject c = android.getJSONObject(i);
    				
    				// Storing  JSON item in a Variable
    				String user = c.getString(TAG_USER);
    				String ver = c.getString(TAG_CONTENT);
    				//String name = c.getString(TAG_ID);
    				String api = c.getString(TAG_CREATE);
    				//String url1 = c.getString(TAG_URL);
    			
    				// Adding value HashMap key => value
    				
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put(TAG_USER, user);
    				map.put(TAG_CONTENT, ver);
    				//map.put(TAG_ID, name);
    				map.put(TAG_CREATE, api);
    				//map.put(TAG_URL, url1);
    				
    				oslist.add(map);
    				list=(ListView)findViewById(R.id.list);
    				
    					
    					ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
    						R.layout.list_v,
    						new String[] { TAG_USER,TAG_CONTENT, TAG_CREATE}, new int[] {
    								R.id.user,R.id.name, R.id.api});
    				
    				
    				list.setAdapter(adapter);
    				
    				
    				
    				
    				}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	
    	 }
    	
    }
    
}
