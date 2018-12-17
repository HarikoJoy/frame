package com.frame.hariko.web.base.views;

import java.util.ArrayList;
import java.util.List;

public class PageWrapper {

	private int total;
	private List<?> list;

	public PageWrapper(List<?> list, int total) {
		this.total = total;
		this.list = (list == null ? new ArrayList<>() : list);
	}

	public int getTotal() {
		return total;
	}

	public List<?> getList() {
		return list;
	}

}
