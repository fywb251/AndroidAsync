package com.koushikdutta.async.sample;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

//import com.koushikdutta.async.http.AsyncHttpClient;
//import com.koushikdutta.async.http.AsyncHttpGet;
//import com.koushikdutta.async.http.AsyncHttpPost;
//import com.koushikdutta.async.http.AsyncHttpResponse;
//import com.koushikdutta.async.http.body.UrlEncodedFormBody;
//import com.koushikdutta.async.http.cache.ResponseCacheMiddleware;
//import com.koushikdutta.async.http.socketio.Acknowledge;
//import com.koushikdutta.async.http.socketio.ConnectCallback;
//import com.koushikdutta.async.http.socketio.EventCallback;
//import com.koushikdutta.async.http.socketio.JSONCallback;
//import com.koushikdutta.async.http.socketio.SocketIOClient;
//import com.koushikdutta.async.http.socketio.StringCallback;

public class MainActivity extends Activity {
//    static ResponseCacheMiddleware cacher;
    
    ImageView rommanager;
    ImageView tether;
    ImageView desksms;
    ImageView chart;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (cacher == null) {
//            try {
//                cacher = ResponseCacheMiddleware.addCache(AsyncHttpClient.getDefaultInstance(), getFileStreamPath("asynccache"), 1024 * 1024 * 10);
//                cacher.setCaching(false);
//            }
//            catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "unable to create cache", Toast.LENGTH_SHORT).show();
//            }
//        }
        setContentView(R.layout.activity_main);
        
        Button b = (Button)findViewById(R.id.go);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                refresh();
                new AsyncTask<Void, Void,Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						newChat();
						return null;
					}
				}.execute();
                
            }
        });
        
        rommanager = (ImageView)findViewById(R.id.rommanager);
        tether = (ImageView)findViewById(R.id.tether);
        desksms = (ImageView)findViewById(R.id.desksms);
        chart = (ImageView)findViewById(R.id.chart);
        
//        showCacheToast();
    }
    
    private void newChat(){

        try {
			IO.Options op = new IO.Options();
			op.forceNew = true;
			final Socket socket = IO.socket("http://192.168.1.203:9093", op);
			socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
			    @Override
			    public void call(Object... objects) {
			    	System.out.println("ERROR="+objects);
			    }
			});
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			    @Override
			    public void call(Object... objects) {
			    	System.out.println("EVENT_CONNECT="+objects);
			    	JSONObject json = new JSONObject();
			    	try {
						json.put("userName", "hello");
						json.put("message", "nihao");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	socket.emit("ackevent1", json, new Ack() {
						
						@Override
						public void call(Object... arg0) {
							System.out.println(arg0);
							System.out.println("ackevent1="+arg0);
						}
					});

			    }
			});
			socket.on("message", new Emitter.Listener() {
			    @Override
			    public void call(Object... objects) {
			    	
			        System.out.println("message="+objects);
			    }
			});
			socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					System.out.println("EVENT_DISCONNECT="+arg0);
					
				}
			});
			socket.on("ackevent3", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					System.out.println("ackevent3="+arg0);
				}
			});
			socket.once("ackevent2", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					System.out.println("ackevent2="+arg0);
					
				}
			});
			socket.connect();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
    

//    void showCacheToast() {
//        boolean caching = cacher.getCaching();
//        Toast.makeText(getApplicationContext(), "Caching: " + caching, Toast.LENGTH_SHORT).show();
//    }
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("Toggle Caching").setOnMenuItemClickListener(new OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                cacher.setCaching(!cacher.getCaching());
//                showCacheToast();
//                return true;
//            }
//        });
//        return true;
//    }
    
//    private void assignImageView(final ImageView iv, final BitmapDrawable bd) {
//        iv.getHandler().post(new Runnable() {
//          @Override
//          public void run() {
//              iv.setImageDrawable(bd);
//          }
//        });
//    }

//    private void getFile(final ImageView iv, String url, final String filename) {
//        AsyncHttpClient.getDefaultInstance().executeFile(new AsyncHttpGet(url), filename, new AsyncHttpClient.FileCallback() {
//            @Override
//            public void onCompleted(Exception e, AsyncHttpResponse response, File result) {
//                if (e != null) {
//                    e.printStackTrace();
//                    return;
//                }
//                Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                result.delete();
//                if (bitmap == null)
//                    return;
//                BitmapDrawable bd = new BitmapDrawable(bitmap);
//                assignImageView(iv, bd);
//            }
//        });
//    }

//    private void getChartFile() {
//        final ImageView iv = chart;
//        final String filename = getFileStreamPath(randomFile()).getAbsolutePath();
//        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("cht", "lc"));
//        pairs.add(new BasicNameValuePair("chtt", "This is a google chart"));
//        pairs.add(new BasicNameValuePair("chs", "512x512"));
//        pairs.add(new BasicNameValuePair("chxt", "x"));
//        pairs.add(new BasicNameValuePair("chd", "t:40,20,50,20,100"));
//        UrlEncodedFormBody writer = new UrlEncodedFormBody(pairs);
//        try {
//            AsyncHttpPost post = new AsyncHttpPost("http://chart.googleapis.com/chart");
//            post.setBody(writer);
//            AsyncHttpClient.getDefaultInstance().executeFile(post, filename, new AsyncHttpClient.FileCallback() {
//                @Override
//                public void onCompleted(Exception e, AsyncHttpResponse response, File result) {
//                    if (e != null) {
//                        e.printStackTrace();
//                        return;
//                    }
//                    Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                    result.delete();
//                    if (bitmap == null)
//                        return;
//                    BitmapDrawable bd = new BitmapDrawable(bitmap);
//                    assignImageView(iv, bd);
//                }
//            });
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    
//    @SuppressWarnings("deprecation")
//	private void chat(){
//    	
//    	SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://192.168.1.203:9093", new ConnectCallback() {
//    	    @Override
//    	    public void onConnectCompleted(Exception ex, SocketIOClient client) {
//    	        if (ex != null) {
//    	            ex.printStackTrace();
//    	            return;
//    	        }
//    	        client.setStringCallback(new StringCallback() {
//
//					@Override
//					public void onString(String string, Acknowledge acknowledge) {
//						System.out.println(string);
//						
//					}
//    	        });
//    	        client.on("message", new EventCallback() {
//					@Override
//					public void onEvent(JSONArray argument,
//							Acknowledge acknowledge) {
//						System.out.println("args: " + argument.toString());
//						
//					}
//    	        });
//    	        client.setJSONCallback(new JSONCallback() {
//    	           
//					@Override
//					public void onJSON(JSONObject json, Acknowledge acknowledge) {
//						System.out.println("json: " + json.toString());
//						
//					}
//    	        });
//    	        client.on("connect", new EventCallback() {
//					
//					@Override
//					public void onEvent(JSONArray argument, Acknowledge acknowledge) {
//						System.out.println("args: " + argument.toString());
//					}
//				});
//    	        client.on("disconnect", new EventCallback(){
//
//					@Override
//					public void onEvent(JSONArray argument,
//							Acknowledge acknowledge) {
//						System.out.println("args: " + argument.toString());
//						
//					}});
//    	        client.on("ackevent2", new EventCallback(){
//
//					@Override
//					public void onEvent(JSONArray argument,
//							Acknowledge acknowledge) {
//						System.out.println("args: " + argument.toString());
//						
//					}});
//    	        client.on("ackevent3", new EventCallback(){
//
//					@Override
//					public void onEvent(JSONArray argument,
//							Acknowledge acknowledge) {
//						System.out.println("args: " + argument.toString());
//						
//					}});
//    	        JSONArray args = new JSONArray();
//    	        JSONObject json = new JSONObject();
//    	        try {
//    	        	json.put("username", "201");
//					json.put("message","ddddddd");
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    	        args.put(json);
//    	        client.emit("ackevent1", args);
//    	        
//    	        
//    	    }
//    	});
//    	
//    }
    
    
    private String randomFile() {
        return ((Long)Math.round(Math.random() * 1000)).toString() + ".png";
    }
    
//    private void refresh() {
//        rommanager.setImageBitmap(null);
//        tether.setImageBitmap(null);
//        desksms.setImageBitmap(null);
//        chart.setImageBitmap(null);
//        
//        getFile(rommanager, "https://raw.github.com/koush/AndroidAsync/master/rommanager.png", getFileStreamPath(randomFile()).getAbsolutePath());
//        getFile(tether, "https://raw.github.com/koush/AndroidAsync/master/tether.png", getFileStreamPath(randomFile()).getAbsolutePath());
//        getFile(desksms, "https://raw.github.com/koush/AndroidAsync/master/desksms.png", getFileStreamPath(randomFile()).getAbsolutePath());
//        getChartFile();
//        
//        Log.i(LOGTAG, "cache hit: " + cacher.getCacheHitCount());
//        Log.i(LOGTAG, "cache store: " + cacher.getCacheStoreCount());
//        Log.i(LOGTAG, "conditional cache hit: " + cacher.getConditionalCacheHitCount());
//        Log.i(LOGTAG, "network: " + cacher.getNetworkCount());
//    }
//    
    private static final String LOGTAG = "AsyncSample";
}
