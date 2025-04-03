package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.DoublePredicate;
import java.util.function.LongPredicate;
import java.util.function.IntPredicate;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.LongSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.IntSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

  private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    processBuilder.environment().put(\"PATH\", \"/usr/games\");
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = \"/usr/games/cowsay '\" + sanitizeInput(input) + \"'\";
    LOGGER.info(cmd);
    processBuilder.command(\"/bin/bash\", \"-c\", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      LOGGER.severe(\"An error occurred: \" + e.getMessage());
    }
    return output.toString();
  private static String sanitizeInput(String input) {
  }
    return input.replaceAll(\"[^a-zA-Z0-9 ]\", \"\");
}
  }
