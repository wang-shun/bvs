package com.bizvisionsoft.bruiengine.app.sysman;

import java.util.Date;

import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class LicenseItem {
	
	@ReadValue
	@WriteValue
	private String prodId;
	
	@ReadValue
	@WriteValue
	private String prodName;
	
	@ReadValue
	@WriteValue
	private String version;
	
	@ReadValue
	@WriteValue
	private Date expiredDate;
	
	@ReadValue
	@WriteValue
	private int qty;
	
	@ReadValue
	@WriteValue
	private String usage;
	
	@ReadValue
	@WriteValue
	private String contractNo;
	
	@ReadValue
	@WriteValue
	private String ownerName;
	
	@ReadValue
	@WriteValue
	private String remark;
	
	@ReadValue
	@WriteValue
	private String unit;
	

	public String getProdId() {
		return prodId;
	}

	public LicenseItem setProdId(String prodId) {
		this.prodId = prodId;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public LicenseItem setVersion(String version) {
		this.version = version;
		return this;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public LicenseItem setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
		return this;
	}

	public int getQty() {
		return qty;
	}

	public LicenseItem setQty(int qty) {
		this.qty = qty;
		return this;
	}

	public String getUsage() {
		return usage;
	}

	public LicenseItem setUsage(String usage) {
		this.usage = usage;
		return this;
	}

	public String getContractNo() {
		return contractNo;
	}

	public LicenseItem setContractNo(String contractNo) {
		this.contractNo = contractNo;
		return this;
	}
	
	public LicenseItem setOwnerName(String ownerName) {
		this.ownerName = ownerName;
		return this;
	}
	
	public String getProdName() {
		return prodName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public LicenseItem setProdName(String prodName) {
		this.prodName = prodName;
		return this;
	}
	
	public LicenseItem setRemark(String remark) {
		this.remark = remark;
		return this;
	}
	
	public LicenseItem setUnit(String unit) {
		this.unit = unit;
		return this;
	}
	
	
}
