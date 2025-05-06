package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.util.logging.Logger;
import java.io.InputStreamReader;

  private Cowsay() { }
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String sanitizedInput = input.replaceAll("[\\'\\"\\`\\$]", "");
    String cmd = "/usr/games/cowsay '" + sanitizedInput + "'";
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command("/bin/bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      Logger.getLogger(Cowsay.class.getName()).severe(e.getMessage());
    }
    return output.toString();
  }
}
