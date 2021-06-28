package se.swedbank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Links {
 private Next next;

public Next getNext() {
	return next;
}

public void setNext(Next next) {
	this.next = next;
}
 
}
