/*
 * HelpRequestedException.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

/**
 * Gets thrown when then help flag is used: --help
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class HelpRequestedException
  extends ArgumentParserException {

  /**
   * Initializes the exception.
   */
  public HelpRequestedException() {
    super("Help requested");
  }
}
