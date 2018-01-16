/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * InvalidArgumentException.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
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
