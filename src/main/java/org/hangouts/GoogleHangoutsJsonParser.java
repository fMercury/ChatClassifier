package org.hangouts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.commons.Constants;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Convierte un JSON de Google Hangouts a archivos ARFF
 * @author martinmineo
 *
 */
public class GoogleHangoutsJsonParser {

	public GoogleHangoutsJsonParser() {}
	
	/**
	 * Crea el encabezado de los archivos ARFF 
	 * @return
	 */
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
	
	/**
	 * Guarda en un archivo el contenido pasado por parámetro
	 * @param fileName Nombre del archivo donde se guardará la información
	 * @param fileContent Contenido que se desea guardar
	 */
	private void saveToFile(String fileName, String fileContent) {
		
		File file = new File(fileName);
		if(!file.exists()) {
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
		else {
		    try {
		    FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent);
            fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
	}


	/**
	 * Parséa el archivo JSON pasado por parámetro, genera un archivo ARFF para cada conversación y guarda cada archivo ARFF por separado
	 * @param fileName Nombre del archivo a parsear
	 * @return Devuelve un String con la lista de todos los archivos generados separados por coma
	 */
    public String parseJson(String fileName) {
    
    	String parsedFiles = "";
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
								    
									fileContent += "?,'" + addEscapeChar(names.get(event.getSenderId().getGaiaId())) + "','" + addEscapeChar(segment.getText()) + "'" + '\n';
								}
							}
						}
					}
				}
				
				String newFileName;
				if (conversationStateRoot.getConversationState().getConversation().getName() != null) {
				    newFileName = Constants.HANGOUTS_FOLDER + conversationStateRoot.getConversationState().getConversation().getName() + Constants.ARFF_FILE;
				}
				else
				    newFileName	= Constants.HANGOUTS_FOLDER + conversationStateRoot.getConversationId().getId() + Constants.ARFF_FILE;
				
				parsedFiles += newFileName + ", ";
				saveToFile(newFileName, fileContent);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		parsedFiles = parsedFiles.substring(0, parsedFiles.lastIndexOf(", "));
		return parsedFiles;
    }
    
    /**
     * Agrega al String pasado por parámetro los caracteres de escape necesarios
     * @param str Texto para agregarle los caracteres de escape
     * @return String con los caracteres de escape agregados
     */
	private String addEscapeChar(String str) {
		
	    if (str != null)
	    {
   			str = str.replace("'", "\\'");
    		str = str.replace('\n', ' ');
    		str = str.replace('\u0001', ' ');
    		str = str.replace('\u0002', ' ');
	    }
	    else
	        return "";
			
		return str;
	}
}
