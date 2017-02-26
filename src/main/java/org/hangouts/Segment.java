package org.hangouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Clase que se utiliza para el parseo de los datos exportados por Hangouts
 * @author martinmineo
 *
 */
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
