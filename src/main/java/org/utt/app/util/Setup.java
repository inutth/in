package org.utt.app.util;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class Setup {
	public static boolean checkEmptyTextField(JTextField t){
		String id ="";
		id = t.getText();
		if(id.equals(""))
			return true;
		else return false;
	}
	public static String ShowThaiDate(String date){
		String DateThai="",day="";
		String Date_from=date;
		String Month_from=date;
		String Year_from=date;
		String year =Year_from.substring(0, 4);
		String month=Month_from.substring(5, 7);
		String day1=Date_from.substring(8).trim();
		if(day1.substring(0,1).equals("0")){
			day=day1.substring(1);
		}else{
			day=day1;
		}
		DateThai=day+" "+getMonthThaiName2(month)+" พ.ศ. "+year;
		
		return DateThai;
	}
	public static String getMonthThaiName2(String month){
		 
		String currentMonth=month;
		String currentMonthThaiName="";
		    if(currentMonth.equals("01")){
		    	currentMonthThaiName="มกราคม";
		    }
		    else if(currentMonth.equals("02")){
		    	currentMonthThaiName="กุมภาพันธ์";
		    }
		    else if(currentMonth.equals("03")){
		    	currentMonthThaiName="มีนาคม";
		    }
		    else if(currentMonth.equals("04")){
		    	currentMonthThaiName="เมษายน";
		    }
		    else if(currentMonth.equals("05")){
		    	currentMonthThaiName="พฤษภาคม";
		    }
		    else if(currentMonth.equals("06")){
		    	currentMonthThaiName="มิถุนายน";
		    }
		    else if(currentMonth.equals("07")){
		    	currentMonthThaiName="กรกฎาคม";
		    }
		    else if(currentMonth.equals("08")){
		    	currentMonthThaiName="สิงหาคม";
		    }
		    else if(currentMonth.equals("09")){
		    	currentMonthThaiName="กันยายน";
		    }
		    else if(currentMonth.equals("10")){
		    	currentMonthThaiName="ตุลาคม";
		    }
		    else if(currentMonth.equals("11")){
		    	currentMonthThaiName="พฤศจิกายน";
		    }
		    else if(currentMonth.equals("12")){
		    	currentMonthThaiName="ธันวาคม";
		    }
		return currentMonthThaiName;
	}
	public static void SetUnderline(JLabel label){
		Font font = label.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		 
		label.setFont(font.deriveFont(attributes));
	}

	public static String DateInDBMSSQL(String date){
		String dateInDBMSSQL="";
		String Date_from=date;
		String Month_from=date;
		String Year_from=date;
		String year =Year_from.substring(0, 4);
		String month=Month_from.substring(5, 7);
		String day=Date_from.substring(8);
		
		int db_year=Integer.parseInt(year)-543 ;
		dateInDBMSSQL=db_year+"-"+month+"-"+day;
		
		return dateInDBMSSQL;
		
	}
	public static String DateInDBMSSQLno(String date){
		String dateInDBMSSQL="";
		String Date_from=date;
		String Month_from=date;
		String Year_from=date;
		String year =Year_from.substring(0, 4);
		String month=Month_from.substring(4, 6);
		String day=Date_from.substring(6);
		
		int db_year=Integer.parseInt(year)-543 ;
		dateInDBMSSQL=db_year+"-"+month+"-"+day;
		
		return dateInDBMSSQL;
		
	}
	public static String AgeInAll(String birthday){
		String age="";
		LocalDate birthdate = new LocalDate (birthday);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int age_year=period.getYears();
        int age_month=period.getMonths();
        int age_day=period.getDays();
		return age=age_year+" ปี "+age_month+" เดือน "+age_day+" วัน";
	}
	public static String AgeInYear(String birthday){
		String age="";
		LocalDate birthdate = new LocalDate (birthday);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int age_year=period.getYears();
		return age=age_year+"";
	}
	public static String getMonthShortThaiName(String month){
		 
		String currentMonth=month;
		String currentMonthThaiName="";
		    if(currentMonth.equals("01")){
		    	currentMonthThaiName="ม.ค.";
		    }
		    else if(currentMonth.equals("02")){
		    	currentMonthThaiName="ก.พ.";
		    }
		    else if(currentMonth.equals("03")){
		    	currentMonthThaiName="มี.ค.";
		    }
		    else if(currentMonth.equals("04")){
		    	currentMonthThaiName="เม.ย.";
		    }
		    else if(currentMonth.equals("05")){
		    	currentMonthThaiName="พ.ค.";
		    }
		    else if(currentMonth.equals("06")){
		    	currentMonthThaiName="มิ.ย.";
		    }
		    else if(currentMonth.equals("07")){
		    	currentMonthThaiName="ก.ค.";
		    }
		    else if(currentMonth.equals("08")){
		    	currentMonthThaiName="ส.ค.";
		    }
		    else if(currentMonth.equals("09")){
		    	currentMonthThaiName="ก.ย.";
		    }
		    else if(currentMonth.equals("10")){
		    	currentMonthThaiName="ต.ค.";
		    }
		    else if(currentMonth.equals("11")){
		    	currentMonthThaiName="พ.ย.";
		    }
		    else if(currentMonth.equals("12")){
		    	currentMonthThaiName="ธ.ค.";
		    }
		return currentMonthThaiName;
	}
	public static String ConvertDateTimePrint(Calendar time){
		int date = time.get(time.DAY_OF_MONTH );
		int month = time.get(time.MONTH)+1;
		int  year = time.get(time.YEAR) + 543;
		String currentDate = date + " " + getMonthThaiName(time) + " " + year;
		
		SimpleDateFormat TimeFormat_now = new SimpleDateFormat("HH:mm:ss");
		Calendar cal_time= Calendar.getInstance();
		String time_now = TimeFormat_now.format(cal_time.getTime());
		
		return currentDate+" เวลา "+time_now;
	}
	public static String getMonth(Calendar time){
		int month=time.get(time.MONTH)+1;
		String currentMonth=month+"";
		return currentMonth;
	}
	public static String getMonthThaiName(Calendar time){
		 
		String currentMonth=getMonth(time);
		String currentMonthThaiName="";
		    if(currentMonth.equals("1")){
		    	currentMonthThaiName="มกราคม";
		    }
		    else if(currentMonth.equals("2")){
		    	currentMonthThaiName="กุมภาพันธ์";
		    }
		    else if(currentMonth.equals("3")){
		    	currentMonthThaiName="มีนาคม";
		    }
		    else if(currentMonth.equals("4")){
		    	currentMonthThaiName="เมษายน";
		    }
		    else if(currentMonth.equals("5")){
		    	currentMonthThaiName="พฤษภาคม";
		    }
		    else if(currentMonth.equals("6")){
		    	currentMonthThaiName="มิถุนายน";
		    }
		    else if(currentMonth.equals("7")){
		    	currentMonthThaiName="กรกฎาคม";
		    }
		    else if(currentMonth.equals("8")){
		    	currentMonthThaiName="สิงหาคม";
		    }
		    else if(currentMonth.equals("9")){
		    	currentMonthThaiName="กันยายน";
		    }
		    else if(currentMonth.equals("10")){
		    	currentMonthThaiName="ตุลาคม";
		    }
		    else if(currentMonth.equals("11")){
		    	currentMonthThaiName="พฤศจิกายน";
		    }
		    else if(currentMonth.equals("12")){
		    	currentMonthThaiName="ธันวาคม";
		    }
		return currentMonthThaiName;
	}
	public static String ShowThaiDate1(String date){
		String DateThai="",day1="";
		String Date_from=date;
		String Month_from=date;
		String Year_from=date;
		String year =Year_from.substring(0, 4);
		String month=Month_from.substring(5, 7);
		String day=Date_from.substring(8).trim();
		if(day.substring(0,1).equals("0")){
			day1=day.substring(1);
		}
		else{
			day1=day;
		}
		int Thai_year=Integer.parseInt(year)+543 ;
		DateThai=day1+" "+getMonthThaiName2(month)+" พ.ศ. "+Thai_year;
		
		return DateThai;
		
	}
	public static String GetDateNow(){
		DateTime now = new DateTime();
		String date_in=now.toLocalDate().toString();
		return date_in;
	}
	public static String ConverttoScanDate(String _visitdate){
		String date =_visitdate.substring(_visitdate.length()-2);
		String month =_visitdate.substring(_visitdate.length()-5,_visitdate.length()-3);
		int  year = Integer.parseInt(_visitdate.substring(0,4))+543;
			String visitDate = ""+year+month+date;
		return visitDate;
	}
}
