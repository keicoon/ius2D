package ius;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {

	final int MAX_PLAYSOUND_COUNT = 10;
	Context mContext;
	SoundPool m_soundpool;
	MediaPlayer background;
	HashMap<String, Integer> SoundMap;
	
	@SuppressWarnings("deprecation")
	public SoundManager(Context context){
		mContext = context;
		m_soundpool = new SoundPool(MAX_PLAYSOUND_COUNT, AudioManager.STREAM_MUSIC, 0);
		SoundMap = new HashMap<String, Integer>();
	}
	public void LoadSound(String name, int sound_id, boolean isBGM){
		
		//int sound_id = m_soundpool.load(mContext, mContext
		//		.getResources().
		//		getIdentifier(name, "raw", mContext.getPackageName()), 1);
		
		if(isBGM)
		{
			background = MediaPlayer.create(mContext, sound_id);
			background.setLooping(true);
			background.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		else
		{
			SoundMap.put(name, m_soundpool.load(mContext,sound_id,1));
		}
	}
	public void PlayBGMSound(){
		background.start();
	}
	public void PauseBGMSound(boolean isPlaying){
		if(background == null)return ;
		
		if(isPlaying)
			background.start();
		else
			background.pause();
	}
	public void ReleaseBGMSound(){
		background.stop();
		background.release();
	}
	public void PlaySound(String name, boolean loop){
		int sound_id = SoundMap.get(name);
		m_soundpool.play(sound_id, 1f, 1f, 0, (loop == true)?1:0, 1f);
	}
	public void StopSound(String name){
		int sound_id = SoundMap.get(name);
		m_soundpool.stop(sound_id);
	}
}
