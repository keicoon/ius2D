package ius;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {

	final int MAX_PLAYSOUND_COUNT = 10;
	Context mContext;
	SoundPool m_soundpool;
	MediaPlayer m_background;
	HashMap<String, MediaPlayer> BackgroundMap;
	HashMap<String, Integer> SoundMap;

	private static SoundManager instance;

	public static SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}

	private SoundManager() {

	}

	@SuppressWarnings("deprecation")
	public void SoundLoad(Context context) {
		mContext = context;
		m_soundpool = new SoundPool(MAX_PLAYSOUND_COUNT,
				AudioManager.STREAM_MUSIC, 0);
		SoundMap = new HashMap<String, Integer>();
		BackgroundMap = new HashMap<String, MediaPlayer>();
	}

	public void LoadSound(String filepath, boolean isBGM) {

		// int sound_id = m_soundpool.load(mContext, mContext
		// .getResources().
		// getIdentifier(name, "raw", mContext.getPackageName()), 1);

		if (isBGM) {
			try {
				AssetFileDescriptor afd = mContext.getAssets().openFd(
						"sound/" + filepath);
				MediaPlayer background = new MediaPlayer();
				background.setAudioStreamType(AudioManager.STREAM_MUSIC);
				background.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				afd.close();
				// background = MediaPlayer.create(mContext, sound_id);
				background.setLooping(true);
				background.prepare();
				BackgroundMap.put(filepath, background);
			} catch (IOException e) {
			}
		} else {
			try {
				AssetFileDescriptor afd = mContext.getAssets().openFd(
						"sound/" + filepath);
				SoundMap.put(filepath, m_soundpool.load(afd, 1));
				afd.close();
			} catch (IOException e) {
			}
		}
	}

	public void PlayBGMSound(String filepath) {
		if (m_background != null)
			m_background.pause();
		
		m_background = BackgroundMap.get(filepath);
		m_background.seekTo(0);
		m_background.start();
	}

	public void PauseBGMSound(boolean isPlaying) {
		if (m_background == null)
			return;

		if (isPlaying)
			m_background.start();
		else
			m_background.pause();
	}

	private void ReleaseBGMSound(MediaPlayer background) {
		background.pause();
		background.stop();
		background.release();
		background = null;
	}

	public void PlaySound(String name, boolean loop) {
		int sound_id = SoundMap.get(name);
		m_soundpool.play(sound_id, 1f, 1f, 0, (loop == true) ? 1 : 0, 1f);
	}

	public void StopSound(String name) {
		int sound_id = SoundMap.get(name);
		m_soundpool.stop(sound_id);
	}

	@SuppressWarnings("deprecation")
	public void ClearSound() {
		for (Entry<String, MediaPlayer> entry : BackgroundMap.entrySet()) {
			MediaPlayer value = entry.getValue();
			ReleaseBGMSound(value);
		}
		BackgroundMap.clear();
		SoundMap.clear();
		m_soundpool.release();
		m_soundpool = null;
		/* TODO 임시방편 */
		m_soundpool = new SoundPool(MAX_PLAYSOUND_COUNT,
				AudioManager.STREAM_MUSIC, 0);
	}
}
