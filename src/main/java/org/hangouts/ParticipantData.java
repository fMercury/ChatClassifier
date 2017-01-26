package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantData {
	@JsonProperty("id")
	private ParticipantId participantId;
	@JsonProperty("fallback_name")
	private String fallbackName;
	
	public ParticipantData() {}

	public ParticipantId getParticipantId() {
		return participantId;
	}

	public void setParticipantId(ParticipantId participantId) {
		this.participantId = participantId;
	}

	public String getFallbackName() {
		return fallbackName;
	}

	public void setFallbackName(String fallbackName) {
		this.fallbackName = fallbackName;
	}
}
