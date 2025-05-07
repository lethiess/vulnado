package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

  private Cowsay() {
public class Cowsay {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());
    throw new UnsupportedOperationException("Utility class");
  public static String run(String input) {
  }
    ProcessBuilder processBuilder = new ProcessBuilder();
    String sanitizedInput = input.replaceAll("[^a-zA-Z0-9 ]", "");
    String cmd = "/usr/games/cowsay '" + sanitizedInput + "'";
    LOGGER.info(cmd);
    processBuilder.command("bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      LOGGER.severe("An error occurred: " + e.getMessage());
      // Log the stack trace for debugging purposes if needed
    }
    return output.toString();
  }
}
