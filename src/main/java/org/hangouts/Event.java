package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	@JsonProperty("conversation_id")
	private ConversationId conversationId;
	@JsonProperty("sender_id")
	private SenderId senderId;
	@JsonProperty("chat_message")
	private ChatMessage chatMessage;
	
	public Event() {}

	public ConversationId getConversationId() {
		return conversationId;
	}

	public void setConversationId(ConversationId conversationId) {
		this.conversationId = conversationId;
	}

	public SenderId getSenderId() {
		return senderId;
	}

	public void setSenderId(SenderId senderId) {
		this.senderId = senderId;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
}
