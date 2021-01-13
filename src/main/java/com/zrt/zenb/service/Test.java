package com.zrt.zenb.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.common.JxlsUtils;

public class Test {
	public static void main(String[] args) {

//		exportToExcel2();

		Calendar calendar = Calendar.getInstance();
		int weekName = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println(weekName);
		
		Random random = new Random(System.currentTimeMillis());			
		for(int i = 0; i < 10; i ++) {
			int randomInt = random.nextInt(8000) + 1000;
			System.out.println(randomInt);
//			System.out.println(ByteUtils.arrayBytesToString(String.valueOf(randomInt).getBytes()));
			System.out.println(ByteUtils.arrayBytesToString(ByteUtils.int2bytes(randomInt)));
			
			System.out.println(Integer.toHexString(0xab));
			
//			byte[] sss = String.valueOf(randomInt).getBytes();
//			byte[] seedBytes = new byte[4];
//    		for(int k = 0; k < 4; k ++) {
//    			seedBytes[k] = sss[k];
//    		}
//    		System.out.println(ByteUtils.arrayBytesToString(seedBytes));
    		
			System.out.println("");
		}
		
		String testContain = "123456abc";
		System.out.println(testContain.contains("*"));
		
		int msgLen = 67789;
		System.out.println(ByteUtils.arrayBytesToString(ByteUtils.int2bytes(msgLen)));
		
		List<Integer> testList = new ArrayList<>();
		testList.add(0);
		testList.add(1);
		testList.add(2);
		testList.add(3);
		testList.add(4);
		
		testList.add(5, 7);
		
		System.out.println(testList.size());
		
//		int index = 0;
//		while(true) {
//			System.out.println("-------------");
//			if(index >= testList.size()) {
//				break;
//			}
//			int temp = testList.get(index);
//
//			index ++;
//			
//			switch (temp) {
//			case 0:
//				System.out.println("00000000000");
//				break;
//			case 1:
//				System.out.println("11111111111");
//				break;
//			case 2:
//				System.out.println("2222222222222");
//				if(temp % 2 == 0) {
//					continue;
//				}
//				break;
//			case 3:
//				System.out.println("33333333333");
//				break;
//			default:
//				break;
//			}
//			
//			try {
//				Thread.sleep(1000);
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}

		System.out.println("=================");
		String testPwd = "";
		char[] testPwdCharA = testPwd.toCharArray();
		for(int i = 0; i < testPwdCharA.length; i ++){
			System.out.println(testPwdCharA[i]);
		}

	}
}
