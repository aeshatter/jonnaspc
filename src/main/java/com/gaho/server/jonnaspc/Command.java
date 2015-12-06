package com.gaho.server.jonnaspc;

public class Command extends Filterable {
	private String command;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return "Command: " + command;
	}
}
