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
 * Namespace.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For storing parsed options.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Namespace
  implements Serializable {

  /** for storing the options (name - value). */
  protected Map<String,Object> m_Values;

  /**
   * Initializes the namespace.
   *
   * @param options	the options to initialize with
   */
  public Namespace(List<Option> options) {
    m_Values = new HashMap<>();
    init(options);
  }

  /**
   * Initializes the namespace with the given options.
   *
   * @param options	the options to initialize with
   */
  protected void init(List<Option> options) {
    for (Option option: options) {
      if (option.getDefault() != null)
	m_Values.put(option.getDest(), option.getDefault());

      if (!m_Values.containsKey(option.getDest())) {
	if (option.isMultiple()) {
	  switch (option.getType()) {
	    case BOOLEAN:
	      setDefault(option.getDest(), new ArrayList<Boolean>());
	      break;
	    case BYTE:
	      setDefault(option.getDest(), new ArrayList<Byte>());
	      break;
	    case SHORT:
	      setDefault(option.getDest(), new ArrayList<Short>());
	      break;
	    case INTEGER:
	      setDefault(option.getDest(), new ArrayList<Integer>());
	      break;
	    case LONG:
	      setDefault(option.getDest(), new ArrayList<Long>());
	      break;
	    case FLOAT:
	      setDefault(option.getDest(), new ArrayList<Float>());
	      break;
	    case DOUBLE:
	      setDefault(option.getDest(), new ArrayList<Double>());
	      break;
	    case STRING:
	      setDefault(option.getDest(), new ArrayList<String>());
	      break;
	    case FILE:
	    case DIRECTORY:
	    case EXISTING_FILE:
	    case EXISTING_DIR:
	    case NONEXISTING_FILE:
	    case NONEXISTING_DIR:
	      setDefault(option.getDest(), new ArrayList<File>());
	      break;
	    default:
	      throw new IllegalStateException("Unhandled list type (for option '" + option.getDest() + "'): " + option.getType());
	  }
	}
	else if (option.hasDefaultValue()) {
	  switch (option.getType()) {
	    case BOOLEAN:
	      setDefault(option.getDest(), false);
	      break;
	    case BYTE:
	      setDefault(option.getDest(), 0);
	      break;
	    case SHORT:
	      setDefault(option.getDest(), 0);
	      break;
	    case INTEGER:
	      setDefault(option.getDest(), 0);
	      break;
	    case LONG:
	      setDefault(option.getDest(), 0L);
	      break;
	    case FLOAT:
	      setDefault(option.getDest(), 0.0f);
	      break;
	    case DOUBLE:
	      setDefault(option.getDest(), 0.0);
	      break;
	    case STRING:
	      setDefault(option.getDest(), "");
	      break;
	    case FILE:
	    case DIRECTORY:
	    case EXISTING_FILE:
	    case EXISTING_DIR:
	    case NONEXISTING_FILE:
	    case NONEXISTING_DIR:
	      setDefault(option.getDest(), new File("."));
	      break;
	    default:
	      throw new IllegalStateException("Unhandled list type (for option '" + option.getDest() + "'): " + option.getType());
	  }
	}
      }
    }
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, String value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, boolean value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, byte value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, short value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, int value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, long value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, float value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, double value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public void setDefault(String name, File value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the default value for the named option.
   *
   * @param name	the name
   * @param value	the default value
   */
  public <E> void setDefault(String name, List<E> value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the "parsed" value for the named option.
   *
   * @param name	the name
   * @param value	the "parsed" value
   */
  public void setValue(String name, Object value) {
    m_Values.put(name, value);
  }

  /**
   * Sets the "parsed" value for the named option.
   *
   * @param name	the name
   * @param value	the "parsed" value
   */
  public void addValue(String name, Object value) {
    getList(name).add(value);
  }

  /**
   * Returns the flipped default value.
   *
   * @param name	the name
   * @return		the flipped value
   */
  public boolean flipDefault(String name) {
    return !(m_Values.get(name).equals(true));
  }

  /**
   * Returns the string value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public String getString(String name) {
    return (String) m_Values.get(name);
  }

  /**
   * Returns the boolean value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public boolean getBoolean(String name) {
    return Boolean.parseBoolean("" + m_Values.get(name));
  }

  /**
   * Returns the byte value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public byte getByte(String name) {
    return Byte.parseByte("" + m_Values.get(name));
  }

  /**
   * Returns the short value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public short getShort(String name) {
    return Short.parseShort("" + m_Values.get(name));
  }

  /**
   * Returns the int value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public int getInt(String name) {
    return Integer.parseInt("" + m_Values.get(name));
  }

  /**
   * Returns the long value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public long getLong(String name) {
    return Long.parseLong("" + m_Values.get(name));
  }

  /**
   * Returns the float value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public float getFloat(String name) {
    return Float.parseFloat("" + m_Values.get(name));
  }

  /**
   * Returns the double value associated with an option name.
   *
   * @param name	the name
   * @return		the associated value
   */
  public double getDouble(String name) {
    return Double.parseDouble("" + m_Values.get(name));
  }

  /**
   * Returns the file associated with an option name.
   *
   * @param name	the name
   * @return		the associated value, null if not available
   */
  public File getFile(String name) {
    if (m_Values.get(name) == null)
      return null;
    return new File("" + m_Values.get(name));
  }

  /**
   * Returns the list of values associated with an option name.
   *
   * @param name 	the name
   * @param <E>		the type of list
   * @return
   */
  @SuppressWarnings("unchecked")
  public <E> List<E> getList(String name) {
    return (List<E>) m_Values.get(name);
  }

  /**
   * Returns the stored options in a string representation.
   *
   * @return		the string representation
   */
  public String toString() {
    return m_Values.toString();
  }
}
