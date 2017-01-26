package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SenderId {
	@JsonProperty("gaia_id")
	private String gaiaId;
	@JsonProperty("chat_id")
	private String chatId;
	
	public SenderId() {}

	public String getGaiaId() {
		return gaiaId;
	}

	public void setGaiaId(String gaiaId) {
		this.gaiaId = gaiaId;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}	
}
