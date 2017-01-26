package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Segment {
	private String text;

	public Segment() {}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
