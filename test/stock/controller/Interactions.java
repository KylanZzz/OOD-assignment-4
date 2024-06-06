package stock.controller;


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