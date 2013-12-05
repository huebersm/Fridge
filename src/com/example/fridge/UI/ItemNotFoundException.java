package com.example.fridge.UI;

public class ItemNotFoundException extends Exception {
	public ItemNotFoundException(String text) {
		super("That item was not found:"+text);
	}
}
