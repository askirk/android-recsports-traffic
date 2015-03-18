package com.umrecsports;

public class UserTimeSelection {
	int hour = -1;
	int day = -1;
	int gym = -1;
	
	private String[] gym_array = {"IMSB", "CCRB", "NCRB"};
	private String[] days_array = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"};
	
	
	public boolean setHour(String h, String ap){
		hour = Integer.parseInt(h);
		if(ap.equals("PM")){
			if(hour != 12)
				hour += 12;
			//check if open
			return isGymOpen();
		}else if(ap.equals("AM")){
			//check if open
			return isGymOpen();
		}else
			return false;
	};
	
	public boolean setDay(String d){
		for(int i = 0; i < 7; i++){
			if(d.equals(days_array[i])){
				day = i;
				return true;
			}
		}
		return false;
	};
	
	public boolean setGym(String g){
		for(int i = 0; i < 3; i++){
			if(g.equals(gym_array[i])){
				gym = i;
				return true;
			}
		}
		return false;
	};
	
	public boolean isGymOpen(){
		boolean res = false;
		switch(day){
		case 0:
			if((hour >= 6)&&(hour < 24))
				res = true;
			break;
		case 1:
			if((hour >= 6)&&(hour < 24))
				res = true;
			break;
		case 2:
			if((hour >= 6)&&(hour < 24))
				res = true;
			break;
		case 3:
			if((hour >= 6)&&(hour < 23))
				res = true;
			break;
		case 4:
			if((hour >= 6)&&(hour < 23))
				res = true;
			break;
		case 5:
			if((hour >= 8)&&(hour < 23))
				res = true;
			break;
		case 6:
			if((hour >= 8)&&(hour < 24))
				res = true;
			break;
		default:
			res = false;
			break;
		}
		return res;
	}
	
	public int getHour(){
		return hour;
	};
	
	public int getDay(){
		return day;
	};
	
	public int getGym(){
		return gym;
	};
	
}
