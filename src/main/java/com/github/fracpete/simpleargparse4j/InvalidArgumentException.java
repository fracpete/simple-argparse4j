/*
 * InvalidArgumentException.java
 * Copyright (C) 2018-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import com.github.fracpete.simpleargparse4j.Option.Type;

/**
 * Gets thrown if an option had an unparseable value supplie.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class InvalidArgumentException
  extends ArgumentParserException {

  /**
   * Initializes the exception.
   *
   * @param flag 	the flag that had no argument supplied
   */
  public InvalidArgumentException(String flag, Type type, String value) {
    super("Invalid argument for '" + flag + "': expected " + type + ", but encountered '" + value + "'");
  }
}
