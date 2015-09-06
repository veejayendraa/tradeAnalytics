package com.vij.spark.streaming.kafkaSparkStreaming.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;




import com.vij.spark.streaming.kafkaSparkStreaming.pojo.TradeData;
import com.vij.spark.streaming.kafkaSparkStreaming.pojo.TradeDataKey;

public class TradesConverter implements Serializable {
	
	final static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	public static TradeData convert(String str) throws NumberFormatException, ParseException
	{
		String[] arr = str.split(",");
		return new TradeData(ft.parse(arr[0]),Integer.parseInt(arr[1]),arr[2],arr[3],Double.parseDouble(arr[4]));
	}
	
	public static TradeDataKey getKey(String str) throws NumberFormatException, ParseException
	{
		String[] arr = str.split(",");
		return new TradeDataKey(Integer.parseInt(arr[1]),arr[2],arr[3]);
	}

}
