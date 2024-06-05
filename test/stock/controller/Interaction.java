package stock.controller;

public interface Interaction {
  void apply(StringBuilder in, StringBuilder out, StringBuilder model);
}

class Interactions {
  static Interaction prints(String... lines) {
    return (in, out, model) -> {
      for (String line : lines) {
        out.append(line).append('\n');
      }
    };
  }

  static Interaction inputs(String input) {
    return (in, out, model) -> {
      in.append(input).append('\n');
    };
  }

  static Interaction modelLog(String input) {
    return (in, out, model) -> {
      model.append(input).append('\n');
    };
  }
}