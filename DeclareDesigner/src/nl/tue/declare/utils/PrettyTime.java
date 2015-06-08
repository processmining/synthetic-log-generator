package nl.tue.declare.utils;
/*
 * This class is a util-class to make handling time easier.
 * For example, it extracts a month, day, year, hour, minute from a timestamp.
 * Also, it prints a time in a nice format  
 * 
 * */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class PrettyTime{
	
	private Calendar calendar = new GregorianCalendar();
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS"); 	

	public PrettyTime(long milisec) {
		this();
		setTimeInMillis(milisec);
	}
	
	public PrettyTime() {
		super();
		setCurrentTime();
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
   
   public long getTimeInMillis(){
		return calendar.getTimeInMillis();
	}
   
   public void setTimeInMillis(long msec){
	   calendar.setTimeInMillis(msec);
   }
	
   public void setCurrentTime(){
		this.setTimeInMillis(System.currentTimeMillis());
	}
   
   public int getDay(){
		return calendar.get(Calendar.DAY_OF_MONTH); // 0..31		
	}
	
	public void setDay(int day){
		calendar.set(Calendar.DAY_OF_MONTH, day); 		
	}
	
	public int getMonth(){
		return calendar.get(Calendar.MONTH) +1; // 0..11		
	}

	public void setMonth(int month){
		calendar.set(Calendar.MONTH,month); 		
	}	
	
	public int getYear(){
		return calendar.get(Calendar.YEAR); // 0..31		
	}

	public void setYear(int year){
		calendar.set(Calendar.YEAR,year); 		
	}	
	
	public int getHour(){
		return calendar.get(Calendar.HOUR_OF_DAY); // 0..24	
	}
	
	public void setHour(int hour){
		calendar.set(Calendar.HOUR_OF_DAY,hour); 		
	}	
	
	public int getMinute(){
		return calendar.get(Calendar.MINUTE); // 0..59		
	}
	
	public void setMinute(int minute){
		calendar.set(Calendar.MINUTE,minute); 		
	}	

	public int getSecond(){
		return calendar.get(Calendar.SECOND); // 0..59		
	}
	
	public void setSecond(int sec){
		calendar.set(Calendar.SECOND,sec); 		
	}

	public int getMiliSecond(){
		return calendar.get(Calendar.MILLISECOND); // 0..59		
	}
	
	public void setMilisecond(int msec){
		calendar.set(Calendar.MILLISECOND,msec); 		
	}	
	
	public void setTime(PrettyTime t){
		this.setTimeInMillis(t.getTimeInMillis());
	}

	public String toString(){
	 	return format.format(new Date(getTimeInMillis())); 
	}
	
	public void parse(String s){
	 	try {
			Date date = format.parse(s);			
			calendar.setTimeInMillis(date.getTime());
		} catch (ParseException e) {
			// DO NOTHING	
			e.printStackTrace();
		} 
	}	
}
