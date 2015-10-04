package ius;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeManager {

	private HashMap<String, Integer> TimeIndexMap;
	private ArrayList<Time> TimeArray; 
	
	public class Time
	{
		private float setTime;
		private float currentTime;
		private boolean active;
		private boolean option;
		Time(float time, boolean pOption){
			setTime = time;
			currentTime = 0f;
			active = true;
			option = pOption;
		}
		public boolean isOn(){
			if(currentTime >= setTime){
				if(!option)
					currentTime = 0f;
				return true;
			}
			else 
				return false;
		}
		public void Stop(){
			active = false;
		}
		public void Start(){
			active = true;
		}
		public void Add(float delta){
			if(active)
				currentTime += delta;
		}
		public void OptionReset(){
			if(option) currentTime = 0f;
		}
	}
	private static TimeManager instance;

	public static TimeManager getInstance() {
		if (instance == null) {
			instance = new TimeManager();
		}
		return instance;
	}
	private TimeManager(){
		TimeIndexMap = new HashMap<String, Integer>();
		TimeArray = new ArrayList<Time>();
	}
	public void AddTimer(String timerName, float time){
		AddTimer(timerName, time, false);
	}
	public Time AddTimer(float time, boolean option){
		Time temp_time = new Time(time, option);
		TimeArray.add(temp_time);
		return temp_time;
	}
	public void AddTimer(String timerName, float time, boolean option){
		TimeIndexMap.put(timerName, TimeArray.size());
		TimeArray.add(new Time(time, option));
	}
	public boolean EvnetOnTimer(String timerName){
		return TimeArray.get(TimeIndexMap.get(timerName)).isOn();
	}
	//need call Function
	public void time(float delta){
		for(int i=0;i<TimeArray.size();i++){
			Time tmp = TimeArray.get(i);
			tmp.OptionReset();
			tmp.Add(delta);
		}
	}
	public void ClearTime(){
		TimeIndexMap.clear();
		TimeArray.clear(); 
	}
}
