package com.vij.spark.streaming.kafkaSparkStreaming.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


public class TradeData implements Serializable {
	
	private Date businessdate;
	
	private int bookid;
	
	private String utid;
	
	private String risktype;

	private double amt;
	
	public Date getbusinessdate() {
		return businessdate;
	}
	public void setbusinessdate(Date businessdate) {
		this.businessdate = businessdate;
	}
	public int getbookid() {
		return bookid;
	}
	public void setbookid(int bookid) {
		this.bookid = bookid;
	}
	public String getUtid() {
		return utid;
	}
	public void setUtid(String utid) {
		this.utid = utid;
	}
	
	public String getrisktype() {
		return risktype;
	}
	public void setrisktype(String risktype) {
		this.risktype = risktype;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((businessdate == null) ? 0 : businessdate.hashCode());
		result = prime * result + bookid;
		result = prime * result + ((risktype == null) ? 0 : risktype.hashCode());;
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
		TradeData other = (TradeData) obj;
		if (businessdate == null) {
			if (other.businessdate != null)
				return false;
		} else if (!businessdate.equals(other.businessdate))
			return false;
		if (bookid != other.bookid)
			return false;
		if (risktype != other.risktype)
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
		return "TradeData [businessdate=" + businessdate + ", bookid="
				+ bookid + ", utid=" + utid +  ", risktype=" + risktype + ", amt=" + amt + "]";
	}
	
	
	public TradeData(Date businessdate, int bookid, String utid,
			 String risktype,double amt) {
		super();
		this.businessdate = businessdate;
		this.bookid = bookid;
		this.utid = utid;
		this.risktype = risktype;
		this.amt = amt;
	}
	
	
	

}


