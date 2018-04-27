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

  @Test
  public void toTitleCase() {
    String string = "624 martha DRIVE";
    String[] parts = string.toLowerCase().split(" ");

    // loop through array of characters
    for (int i = 0; i < parts.length; i++) {
      parts[i] =
          parts[i].replaceFirst(
              parts[i].charAt(0) + "", Character.toTitleCase(parts[i].charAt(0)) + "");
    }

    StringBuilder builder = new StringBuilder();

    for (String part : parts) {
      builder.append(part).append(" ");
    }

    string = builder.toString().trim();

    assertEquals("624 Martha Drive", string);
  }
}
