package org.commons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.hangouts.ChatMessage;
import org.hangouts.ConversationStateRoot;
import org.hangouts.Event;
import org.hangouts.HangoutsJSON;
import org.hangouts.MessageContent;
import org.hangouts.ParticipantData;
import org.hangouts.Segment;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Convierte un JSON de Google Hangouts a archivos ARFF 
 * 
 */
public class GoogleHangoutsJsonParser {

	public GoogleHangoutsJsonParser(String fileName) {
		
		parseJson(fileName);
	}
	
	private String getARFFHeader() {
		String header;
		
		header = 	"@relation chat" + '\n' + 
					'\n' + 
					"@attribute Conducta {1,2,3,4,5,6,7,8,9,10,11,12}" + '\n' + 
					"@attribute nombre string" + '\n' + 
					"@attribute message string" + '\n' + 
					'\n' + 
					"@data" + '\n';
		return header;
	}
	
	private void saveToFile(String fileName, String fileContent) {
		
		File file = new File(fileName);
		if(!file.exists())
			try {
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(fileContent);
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


    private void parseJson(String fileName) {
    
    	ObjectMapper mapper = new ObjectMapper();

		HangoutsJSON hangoutsJSON;
		try {
			hangoutsJSON = mapper.readValue(new File(fileName), HangoutsJSON.class);
			
			List<ConversationStateRoot> conversationStateRoots = hangoutsJSON.getConversationStatesRoot();
			
			Hashtable<String, String> names = new Hashtable<String, String>();
			for (ConversationStateRoot conversationStateRoot : conversationStateRoots) {
				for (ParticipantData participants : conversationStateRoot.getConversationState().getConversation().getParticipantDataList()) {
					String name = participants.getFallbackName() == null ? participants.getParticipantId().getGaiaId(): participants.getFallbackName();
					if (!names.containsKey(participants.getParticipantId().getGaiaId()))
						names.put(participants.getParticipantId().getGaiaId(), name);
				}
			}
			
			for (ConversationStateRoot conversationStateRoot : conversationStateRoots) {
				
				String fileContent;
				fileContent = getARFFHeader();
				for (Event event : conversationStateRoot.getConversationState().getEvents()) {
					ChatMessage chatMessage;
					MessageContent messageContent;
					
					if (event.getChatMessage() != null) {
						chatMessage = event.getChatMessage();
						if (chatMessage.getMessageContent() != null) {
							messageContent = chatMessage.getMessageContent();
							if (messageContent.getSegments() != null) {
								for(Segment segment : messageContent.getSegments()) {
									fileContent += "?,'" + names.get(event.getSenderId().getGaiaId()) + "','" + addEscapeChar(segment.getText()) + "'" + '\n';
								}
							}
						}
					}
				}
				
				String newFileName = Constants.HANGOUTS_FOLDER + conversationStateRoot.getConversationId().getId() + Constants.ARFF_FILE;
				saveToFile(newFileName, fileContent);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private String addEscapeChar(String str) {
		
		if (str.contains("'"))
			str = str.replace("'", "\\'");
		str = str.replace('\n', ' ');
			
		return str;
	}
    
    
    
    
    
    
    
}
