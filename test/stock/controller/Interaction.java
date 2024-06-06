package stock.controller;

/**
 * Represents an interaction in the stock application that can
 * modify the input, output, or model logs.
 */
public interface Interaction {
  /**
   * Applies the interaction to the provided input, output, and model logs.
   *
   * @param in    the input log
   * @param out   the output log
   * @param model the model log
   */
  void apply(StringBuilder in, StringBuilder out, StringBuilder model);
}

/**
 * Utility class that provides various interactions for testing purposes.
 */
class Interactions {
  /**
   * Creates an interaction that appends the given lines to the output log.
   *
   * @param lines the lines to append to the output log
   * @return an interaction that appends the given lines to the output log
   */
  static Interaction prints(String... lines) {
    return (in, out, model) -> {
      for (String line : lines) {
        out.append(line).append('\n');
      }
    };
  }

  /**
   * Creates an interaction that appends the given input to the input log.
   *
   * @param input the input to append to the input log
   * @return an interaction that appends the given input to the input log
   */
  static Interaction inputs(String input) {
    return (in, out, model) -> {
      in.append(input).append('\n');
    };
  }

  /**
   * Creates an interaction that appends the given input to the model log.
   *
   * @param input the input to append to the model log
   * @return an interaction that appends the given input to the model log
   */
  static Interaction modelLog(String input) {
    return (in, out, model) -> {
      model.append(input).append('\n');
    };
  }
}