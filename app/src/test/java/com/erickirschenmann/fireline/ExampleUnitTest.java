package com.erickirschenmann.fireline;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);
  }

  @Test
  public void stringSplit() {
    String units = "E63, MED665, E55";
    String[] split = units.split(", ");

    assertEquals("MED665", split[1]);
  }
}
