package com.thefuntasty.tfileautouploader.request;

import java.util.ArrayList;

public class AddItemsRequest {

	ArrayList<AddableItem> items = new ArrayList<>();

	public void add(AddableItem item) {
		items.add(item);
	}

	public ArrayList<AddableItem> getItems() {
		return items;
	}

	public int size() {
		return items.size();
	}
}
