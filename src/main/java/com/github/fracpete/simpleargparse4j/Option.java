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
 * Option.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import java.io.Serializable;

/**
 * Defines an option.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Option
  implements Serializable, Comparable<Option> {

  /** the types. */
  public enum Type {
    STRING,
    BOOLEAN,
    BYTE,
    SHORT,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
  }

  /** the destination (key in namespace). */
  protected String m_Dest;

  /** the commandline flag. */
  protected String m_Flag;

  /** the long commandline flag. */
  protected String m_SecondFlag;

  /** whether option has an argument. */
  protected boolean m_HasArgument;

  /** the default value. */
  protected Object m_DefaultValue;

  /** the help string. */
  protected String m_Help;

  /** whether the option is required or optional. */
  protected boolean m_Required;

  /** whether the option can be specified multiple times. */
  protected boolean m_Multiple;

  /** the type. */
  protected Type m_Type;

  /**
   * Initializes the option.
   *
   * @param flag	the flag (eg "-X" or "--XXX")
   */
  public Option(String flag) {
    this(flag.replace("-", ""), flag, true, null, flag.replace("-", ""), false, false, Type.STRING);
  }

  /**
   * Initializes the option.
   *
   * @param flag	the flag (eg "-X" or "--XXX")
   * @param secondFlag 	the second flag (eg a long version "--XXX")
   */
  public Option(String flag, String secondFlag) {
    this((secondFlag != null ? secondFlag.replace("-", "") : flag.replace("-", "")), flag, secondFlag, true, null, flag.replace("-", ""), false, false, Type.STRING);
  }

  /**
   * Initializes the option.
   *
   * @param dest	the destination (ie key in namespace)
   * @param flag	the flag (eg "-X" or "--XXX")
   * @param hasArg	true if the option has an argument
   * @param defValue	the default value
   * @param help	short help string
   * @param required	true if required, otherwise optional
   * @param multiple	true if can be occur multiple times
   * @param type	the type of the argument
   */
  public Option(String dest, String flag, boolean hasArg, String defValue, String help, boolean required, boolean multiple, Type type) {
    this(dest, flag, null, hasArg, defValue, help, required, multiple, type);
  }

  /**
   * Initializes the option.
   *
   * @param dest	the destination (ie key in namespace)
   * @param flag	the flag (eg "-X" or "--XXX")
   * @param secondFlag 	the second flag (eg a long version "--XXX")
   * @param hasArg	true if the option has an argument
   * @param defValue	the default value
   * @param help	short help string
   * @param required	true if required, otherwise optional
   * @param multiple	true if can be occur multiple times
   * @param type	the type of the argument
   */
  public Option(String dest, String flag, String secondFlag, boolean hasArg, String defValue, String help, boolean required, boolean multiple, Type type) {
    m_Dest         = dest;
    m_Flag         = flag;
    m_SecondFlag = secondFlag;
    m_HasArgument  = hasArg;
    m_DefaultValue = defValue;
    m_Help         = help;
    m_Required     = required;
    m_Multiple     = multiple;
    m_Type         = type;
  }

  /**
   * Sets the destination key (used in the namespace).
   *
   * @param value	the name of the key
   * @return		the option
   */
  public Option dest(String value) {
    m_Dest = value;
    return this;
  }

  /**
   * Sets whether the option requires an argument.
   *
   * @param value	true if argument required
   * @return		the option
   */
  public Option argument(boolean value) {
    m_HasArgument = value;
    if (!m_HasArgument)
      type(Type.BOOLEAN);
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(String value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(boolean value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(byte value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(short value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(int value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(long value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(float value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the default value.
   *
   * @param value	the default value
   * @return		the option
   */
  public Option setDefault(double value) {
    m_DefaultValue = value;
    return this;
  }

  /**
   * Sets the help string.
   *
   * @param value	the help string
   * @return		the option
   */
  public Option help(String value) {
    m_Help = value;
    return this;
  }

  /**
   * Sets whether the option is required.
   *
   * @param value	true if required, optional otherwise
   * @return		the option
   */
  public Option required(boolean value) {
    m_Required = value;
    return this;
  }

  /**
   * Sets whether the option can be specified multiple times.
   *
   * @param value	true if multiple occurrences
   * @return		the option
   */
  public Option multiple(boolean value) {
    m_Multiple = value;
    return this;
  }

  /**
   * Sets how to interpret the argument.
   *
   * @param value	the type
   * @return		the option
   */
  public Option type(Type value) {
    m_Type = value;
    if (m_Type == Type.BOOLEAN)
      m_HasArgument = false;
    return this;
  }

  /**
   * Returns the destination key name.
   *
   * @return		the key
   */
  public String getDest() {
    return m_Dest;
  }

  /**
   * Returns the commandline flag ("-X").
   *
   * @return		the flag
   */
  public String getFlag() {
    return m_Flag;
  }

  /**
   * Returns whether a second flag is present.
   *
   * @return		true if present
   */
  public boolean hasSecondFlag() {
    return (m_SecondFlag != null);
  }

  /**
   * Returns the second commandline flag ("--XXX").
   *
   * @return		the flag
   */
  public String getSecondFlag() {
    return m_SecondFlag;
  }

  /**
   * Returns whether the option requires an argument.
   *
   * @return		true if argument required
   */
  public boolean hasArgument() {
    return m_HasArgument;
  }

  /**
   * Returns the default value.
   *
   * @return		the default value
   */
  @SuppressWarnings("unchecked")
  public <E> E getDefault() {
    return (E) m_DefaultValue;
  }

  /**
   * Returns the help string.
   *
   * @return		the help string
   */
  public String getHelp() {
    return m_Help;
  }

  /**
   * Returns whether the option is required or optional.
   *
   * @return		true if required, optional otherwise
   */
  public boolean isRequired() {
    return m_Required;
  }

  /**
   * Returns whether the option can occur multiple times.
   *
   * @return		true if can occur multiple times
   */
  public boolean isMultiple() {
    return m_Multiple;
  }

  /**
   * Returns the type of the option.
   *
   * @return		the type
   */
  public Type getType() {
    return m_Type;
  }

  /**
   * Uses the type information to test the value.
   *
   * @param value	the string value to test
   * @return		true if valid according to type
   * @see		#getType()
   */
  public boolean isValid(String value) {
    try {
      switch (getType()) {
	case BOOLEAN:
	  Boolean.parseBoolean(value);
	  break;
	case BYTE:
	  Byte.parseByte(value);
	  break;
	case SHORT:
	  Short.parseShort(value);
	  break;
	case INTEGER:
	  Integer.parseInt(value);
	  break;
	case LONG:
	  Long.parseLong(value);
	  break;
	case FLOAT:
	  Float.parseFloat(value);
	  break;
	case DOUBLE:
	  Double.parseDouble(value);
	  break;
	case STRING:
	  return true;
	default:
	  throw new IllegalStateException("Unhandled type (for option '" + getDest() + "'): " + getType());
      }
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Uses the type information to parse the value.
   *
   * @param value	the string value to test
   * @return		true if valid according to type
   * @see		#getType()
   */
  public Object parse(String value) {
    switch (getType()) {
      case BOOLEAN:
	return Boolean.parseBoolean(value);
      case BYTE:
	return Byte.parseByte(value);
      case SHORT:
	return Short.parseShort(value);
      case INTEGER:
	return Integer.parseInt(value);
      case LONG:
	return Long.parseLong(value);
      case FLOAT:
	return Float.parseFloat(value);
      case DOUBLE:
	return Double.parseDouble(value);
      case STRING:
	return value;
      default:
	throw new IllegalStateException("Unhandled type (for option '" + getDest() + "'): " + getType());
    }
  }

  /**
   * Uses string comparison on the name.
   *
   * @param o		the other option to compare with
   * @return		less than, equal to or greater than zero
   */
  @Override
  public int compareTo(Option o) {
    return m_Dest.compareTo(o.getDest());
  }

  /**
   * Checks whether the option is the same (uses name).
   *
   * @param obj		the object/option to compare with
   * @return		true if option with the same name
   */
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Option) && (compareTo((Option) obj) == 0);
  }

  /**
   * Returns the hashcode.
   *
   * @return		the hashcode
   */
  @Override
  public int hashCode() {
    return m_Dest.hashCode();
  }

  /**
   * Returns a short description of the option.
   *
   * @return		the description
   */
  public String toString() {
    return "name=" + m_Dest + ", "
      + "flag=" + m_Flag + ", "
      + "longFlag=" + (m_SecondFlag == null ? "-no-" : m_SecondFlag) + ", "
      + "hasArg=" + m_HasArgument + ", "
      + "defValue=" + m_DefaultValue + ", "
      + "help=" + m_Help + ", "
      + "required=" + m_Required + ", "
      + "multiple=" + m_Multiple;
  }
}
