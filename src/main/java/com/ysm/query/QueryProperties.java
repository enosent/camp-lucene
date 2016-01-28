package com.ysm.query;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="query")
public class QueryProperties {

	private String selectDeals;
	
	public String getSelectDeals() {
		return selectDeals;
	}
	public void setSelectDeals(String selectDeals) {
		this.selectDeals = selectDeals;
	}
	
}