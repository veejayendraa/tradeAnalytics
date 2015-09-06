package com.vij.spark.streaming.kafkaSparkStreaming.aggregators;

import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.spark.connector.cql.CassandraConnector;

public class BookDataFromTradeAggregator implements Runnable {

	private  CassandraConnector connector;
	private transient JavaSparkContext sc;
	public BookDataFromTradeAggregator( JavaSparkContext sc)
	{
		connector = CassandraConnector.apply(sc.getConf());
	}
	
	@Override
	public void run() {
		
		while(true)
		{
			
		}
		
	}
	
}
