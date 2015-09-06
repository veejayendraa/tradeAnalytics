package com.vij.spark.streaming.kafkaSparkStreaming.cassandra;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import java.io.Serializable;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.*;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.vij.spark.streaming.kafkaSparkStreaming.pojo.TradeData;



public class TradesDAO implements Serializable {
	
	//JavaStreamingContext jsc;
	public TradesDAO()
	{
		//this.jsc = jsc;
	}
	public void save(JavaStreamingContext jsc,JavaDStream<TradeData> tradeDataRDD)
	{
		 CassandraConnector connector = CassandraConnector.apply(jsc.sparkContext().getConf());
		 try (Session session = connector.openSession()) {
			 javaFunctions(tradeDataRDD).writerBuilder("tradesensstorage", "tradelevelstorage", mapToRow(TradeData.class)).saveToCassandra();
	         System.out.println("*******TRADE LEVEL STORAGE DONE***********");
		 }
	 }
	
	  

}
