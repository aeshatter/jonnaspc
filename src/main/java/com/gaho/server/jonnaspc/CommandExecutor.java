package com.gaho.server.jonnaspc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor {
	
	private final static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
	public static void execute(Command cmd) throws NoSuchCommandException {
		logger.info("Executing command {}", cmd.getCommand());
		Runtime r = Runtime.getRuntime();
		try {
			switch(cmd.getCommand()) {
				case "hibernate":
				//hibernate
					r.exec("shutdown /h");
					break;
				case "shutdown":
					r.exec("shutdown /s /t 0");
				//shutdown
					break;
				default:
					throw new NoSuchCommandException("No such command: " + cmd.getCommand());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

class NoSuchCommandException extends RuntimeException {
	private static final long serialVersionUID = 1124598633941130818L;

	NoSuchCommandException(String message) {
		super(message);
	}
}