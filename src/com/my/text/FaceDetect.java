package com.my.text;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.graphics.Bitmap;
import android.util.Log;

public class FaceDetect {
	
	 public interface CallBack{
		 void success(JSONObject  result);
		 void error(FaceppParseException exception);
	 }
	 
	public static void detect(final Bitmap bm,final CallBack callBack){
		
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						
						 
						 try {
							 HttpRequests requests=new HttpRequests("382a490141f1999929d37b479dcf53c0", "JjI9aD2aB5-qMgDL5dCgbSYek6tQwKVs", true, true);
								
								Bitmap bmsmall =Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
								ByteArrayOutputStream stream =new ByteArrayOutputStream();
								bmsmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
								 byte[] bytes =  stream.toByteArray();
								 
								 PostParameters parameters =new PostParameters();
								 parameters.setImg(bytes);
							JSONObject jObject =  requests.detectionDetect(parameters);
							Log.e("TAG", jObject.toString());
							
							callBack.success(jObject);
						} catch (FaceppParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							callBack.error(e);
						}
						
					}
				}).start();
		
		
	}

}
