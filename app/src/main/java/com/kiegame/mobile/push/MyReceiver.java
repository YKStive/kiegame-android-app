package com.kiegame.mobile.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kiegame.mobile.Game;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyPush";
	private SpeechSynthesizer mTts;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {


			Bundle bundle = intent.getExtras();
			ttsPlay("新订单来了");
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");



			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.get(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Log.e(TAG,"[processCustomMessage] "+message+"-----"+extras);
	}



	private void ttsPlay(String tts) {
		if(mTts==null){
			mTts = SpeechSynthesizer.createSynthesizer(Game.ins(), new InitListener() {
				@Override
				public void onInit(int i) {
					if (i != ErrorCode.SUCCESS) {
						android.util.Log.i("TTS", "语音初始化失败,错误码：" + i);
					} else {
						android.util.Log.i("TTS", "语言初始化成功");
						mTts.startSpeaking(tts,playListener);
					}
				}

			});
		}else {
			mTts.startSpeaking(tts,playListener);
		}

	}


	private PlayListener playListener  = new PlayListener();
	class PlayListener implements SynthesizerListener {
		@Override
		public void onSpeakBegin() {
			android.util.Log.i("TTS", "语言初始化成功");
		}

		@Override
		public void onBufferProgress(int i, int i1, int i2, String s) {
			android.util.Log.i("TTS", "语言初始化成功");
		}

		@Override
		public void onSpeakPaused() {
			android.util.Log.i("TTS", "语言初始化成功");
		}

		@Override
		public void onSpeakResumed() {
			android.util.Log.i("TTS", "语言初始化成功");
		}

		@Override
		public void onSpeakProgress(int i, int i1, int i2) {
			android.util.Log.i("TTS", "语言初始化成功");
		}

		@Override
		public void onCompleted(SpeechError speechError) {
			android.util.Log.i("TTS", "播放失败--"+speechError.getErrorDescription());
		}

		@Override
		public void onEvent(int i, int i1, int i2, Bundle bundle) {
			Log.i("TTS", "播放失败--"+i);
		}
	}
}
