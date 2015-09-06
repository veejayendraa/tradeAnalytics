package com.vij.spark.streaming.kafkaSparkStreaming.pojo;

import java.io.Serializable;

public class TradeDataKey implements Serializable{
	private int bookid;
	
	private String utid;
	
	private String risktype;

	

	public TradeDataKey(int bookid, String utid, String risktype) {
		super();
		this.bookid = bookid;
		this.utid = utid;
		this.risktype = risktype;
		
	}

	public int getBookid() {
		return bookid;
	}

	public void setBookid(int bookid) {
		this.bookid = bookid;
	}

	public String getUtid() {
		return utid;
	}

	public void setUtid(String utid) {
		this.utid = utid;
	}

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bookid;
		result = prime * result
				+ ((risktype == null) ? 0 : risktype.hashCode());
		result = prime * result + ((utid == null) ? 0 : utid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeDataKey other = (TradeDataKey) obj;
		if (bookid != other.bookid)
			return false;
		if (risktype == null) {
			if (other.risktype != null)
				return false;
		} else if (!risktype.equals(other.risktype))
			return false;
		if (utid == null) {
			if (other.utid != null)
				return false;
		} else if (!utid.equals(other.utid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TradeDataKey [bookid=" + bookid + ", utid=" + utid
				+ ", risktype=" + risktype + "]";
	}

}
