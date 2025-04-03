package com.scalesec.vulnado;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cowsay {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

  private Cowsay() {}

  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.environment().put("PATH", "/usr/games");
    String cmd = "/usr/games/cowsay '" + sanitizeInput(input) + "'";
    LOGGER.info(cmd);
    processBuilder.command("/bin/bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error occurred: ", e);
    }
    return output.toString();
  }

  private static String sanitizeInput(String input) {
    return input.replaceAll("[^a-zA-Z0-9 ]", "");
  }
}
