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