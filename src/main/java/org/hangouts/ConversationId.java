package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationId {
	private String id;
	
	public ConversationId() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
