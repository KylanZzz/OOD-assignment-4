package stock.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Interactions class that ensures that input,
 * output, and model logs are correctly written to.
 */
public class InteractionsTest {
  private StringBuilder in;
  private StringBuilder out;
  private StringBuilder model;

  @Before
  public void setup() {
    in = new StringBuilder();
    out = new StringBuilder();
    model = new StringBuilder();
  }

  @Test
  public void testPrints() {
    Interaction prints = Interactions.prints("aaaa", "bbbb");
    prints.apply(in, out, model);
    assertEquals("aaaa\nbbbb\n", out.toString());
  }

  @Test
  public void testInputs() {
    Interaction inputs = Interactions.inputs("cccc");
    inputs.apply(in, out, model);
    assertEquals("cccc\n", in.toString());
  }

  @Test
  public void testModelLog() {
    Interaction modelLog = Interactions.modelLog("tttt");
    modelLog.apply(in, out, model);
    assertEquals("tttt\n", model.toString());
  }
}