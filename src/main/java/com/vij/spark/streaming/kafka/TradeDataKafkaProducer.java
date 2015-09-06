package com.vij.spark.streaming.kafka;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TradeDataKafkaProducer {
	 final static String TOPIC = "tradesTopic";

	public static void main(String[] args) throws InterruptedException {
		Properties properties = new Properties();
        properties.put("metadata.broker.list","localhost:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("groupid","trades");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String,String> producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
        String[] riskTypes = new String[]{"PV01","CR01"};
        for(int i=1;i<61;i++)
        {
        	if(i%10==0)
        	{
        		System.out.println("Going to sleep for");
        		Thread.sleep(30*1000);
        		
        	}
	        for(String str:riskTypes)
	        {
	        	KeyedMessage<String, String> message =new KeyedMessage<String, String>(TOPIC,getTradeInfo(str));
	        	producer.send(message);
	        }
        }
        producer.close();

	}
	
	
	static String getTradeInfo(String riskType)
	{
		try{
		Random r = new Random();
		int bookId = r.nextInt(8500-8495) + 8495;
		int dealNum = r.nextInt(15000-14990) + 14990;
		double amt =  1.0 + (1000.0 - 1.0) * r.nextDouble();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tradeDate = formatter.format(new Date());
		String tradeData = tradeDate+","+bookId+",ICE@"+dealNum+","+riskType+","+amt;
		System.out.println(tradeData);
		return tradeData;
		}catch(Exception ex){return null;}
		
	}

}
