/*
 * MissingArgumentException.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

/**
 * Gets thrown if an option is missing its argument.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MissingArgumentException
  extends ArgumentParserException {

  /**
   * Initializes the exception.
   *
   * @param flag 	the flag that had no argument supplied
   */
  public MissingArgumentException(String flag) {
    super("No argument supplied: " + flag);
  }
}
