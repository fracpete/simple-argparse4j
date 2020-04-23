/*
 * ArgumentParserException.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

/**
 * Ancestor for parsing exceptions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class ArgumentParserException
  extends Exception {

  /**
   * Initializes the exception.
   *
   * @param reason	the reason
   */
  public ArgumentParserException(String reason) {
    super(reason);
  }
}
