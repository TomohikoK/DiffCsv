package com.hoge;

import java.util.Calendar;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("AAAAA");
		Calendar cal = Calendar.getInstance();
		String dayStr = "A";
		int dispDay = Integer.valueOf(dayStr);
		System.out.println(cal.getTime().toString());;
		cal.add(Calendar.DATE, dispDay - 1);
		System.out.println(cal.getTime().toString());;

		
	}

}
