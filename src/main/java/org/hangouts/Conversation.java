package org.hangouts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation {
	@JsonProperty("participant_data")
	private List<ParticipantData> participantData;
	
	public Conversation() {}

	public List<ParticipantData> getParticipantDataList() {
		return participantData;
	}

	public void setParticipantData(List<ParticipantData> participantData) {
		this.participantData = participantData;
	}

}
