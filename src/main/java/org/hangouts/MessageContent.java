package org.hangouts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageContent {
	@JsonProperty("segment")
	private List<Segment> segments;
	
	public MessageContent() {}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segment) {
		this.segments = segment;
	}
	
	

}
