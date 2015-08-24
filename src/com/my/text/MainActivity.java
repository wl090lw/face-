package com.my.text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.my.text.FaceDetect.CallBack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private ImageView iv;
	private TextView tv;
	private Button bt, bt2;
	private static final String KEY = "382a490141f1999929d37b479dcf53c0";
	private static final String APISECRET = "JjI9aD2aB5-qMgDL5dCgbSYek6tQwKVs";
	private String mcurrentPhotoStr;
	private Bitmap mphotoImg;
	private ProgressBar pbBar;
	protected int SUCCESS;
	protected String gender;
	protected int age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pbBar.setVisibility(View.VISIBLE);
				FaceDetect.detect(mphotoImg, new CallBack() {

					@Override
					public void success(JSONObject result) {
						Message message = Message.obtain();
						message.what = SUCCESS;
						message.obj = result;
						handler.sendMessage(message);

					}

					@Override
					public void error(FaceppParseException exception) {

					}
				});

			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == SUCCESS) {
				pbBar.setVisibility(View.INVISIBLE);
				JSONObject rs = (JSONObject) msg.obj;
				try {
					JSONArray faces = rs.getJSONArray("face");
					int faceCount = faces.length();
					tv.setText("一共找到"+faceCount+"张脸");
//					for (int i = 1; i < faceCount; i++) {
//
//						JSONObject face = faces.getJSONObject(i);// 拿到单独的一个脸
//						 gender = face.getJSONObject("attribute")
//								.getJSONObject("gender").getString("value");
//						 age = face.getJSONObject("attribute")
//								.getJSONObject("age").getInt("value");
//						
//					}
                          
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
	};

	private void initview() {
		iv = (ImageView) findViewById(R.id.imageView1);
		tv = (TextView) findViewById(R.id.textView1);
		bt = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		pbBar = (ProgressBar) findViewById(R.id.progressbar1);
	}

	public void clickbt(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		if (requestCode == 100) {
			if (intent != null) {
				Uri uri = intent.getData();
				Cursor cursor = getContentResolver().query(uri, null, null,
						null, null);
				cursor.moveToFirst();
				int idx = cursor
						.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				mcurrentPhotoStr = cursor.getString(idx);
				cursor.close();
				// 加载图片是消耗内存的操作，图片过大，压缩图片防止内存溢出（oom）
				resizePhoto();
				iv.setImageBitmap(mphotoImg);

			}

		}

	}

	private void resizePhoto() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(mcurrentPhotoStr, options);

		double ratio = Math.max(options.outWidth * 1.0d / 1024f,
				options.outHeight * 1.0d / 1024f);// 缩放比例

		options.inSampleSize = (int) Math.ceil(ratio);

		options.inJustDecodeBounds = false;
		mphotoImg = BitmapFactory.decodeFile(mcurrentPhotoStr, options);
	}

}
