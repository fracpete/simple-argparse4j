/*
 * RequiredOptionMissingException.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import java.util.Collection;

/**
 * Gets thrown if required options weren't supplied.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RequiredOptionMissingException
  extends ArgumentParserException {

  /**
   * Initializes the exception.
   *
   * @param options	the required options that weren't supplied
   */
  public RequiredOptionMissingException(Collection<Option> options) {
    super("Required options not supplied: " + flatten(options));
  }

  /**
   * Flattens the options.
   *
   * @param options	the options to flatten
   * @return		the generated string
   */
  protected static String flatten(Collection<Option> options) {
    StringBuilder result;

    result = new StringBuilder();
    for (Option opt: options) {
      if (result.length() > 0)
	result.append(", ");
      result.append(opt.getFlag());
    }

    return result.toString();
  }
}
