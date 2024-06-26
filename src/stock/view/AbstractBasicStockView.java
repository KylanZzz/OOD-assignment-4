package stock.view;

import java.io.IOException;
import java.util.List;

/**
 * Provides an abstract base class for stock view implementations.
 * This class handles basic output functionalities to an Appendable object,
 * allowing derived classes to focus on specific interactions and user interface logic.
 */
public abstract class AbstractBasicStockView {
  protected final Appendable out;

  /**
   * The Appendable object used for all output operations.
   */
  protected AbstractBasicStockView(Appendable out) {
    this.out = out;
  }

  protected void println(String message) {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Unable to append message: " + message + " to view.");
    }
  }

  protected void printOptionsPrompt() {
    println("Please type the number that corresponds with the choice you would like to pick, "
            + "or type " + BasicMenuOptions.exitKeyword() + " to return/exit");
  }

  protected void printMenu(List<String> options) {
    for (int i = 0; i < options.size(); i++) {
      println(String.format("%d. %s", i + 1, options.get(i)));
    }
  }

  protected void printList(List<String> list) {
    for (String item : list) {
      println(item);
    }
  }
}
