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
 * ArgumentParser.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * For parsing commandline options.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ArgumentParser
  implements Serializable {

  /** the screen width. */
  public final static int SCREEN_WIDTH = 80;

  /** the tab width. */
  public final static int TAB_WIDTH = 8;

  /** the characters that can be used to break up a line. */
  public final static char[] BREAK_CHARS = " ,;!?".toCharArray();

  /** the description. */
  protected String m_Description;

  /** the managed options. */
  protected List<Option> m_Options;

  /** whether help got requested. */
  protected boolean m_HelpRequested;

  /** the screen width to use. */
  protected int m_ScreenWidth;

  /** the break characters to use for the help screen. */
  protected char[] m_BreakChars;

  /**
   * Initializes the parser.
   */
  public ArgumentParser(String description) {
    m_Description = description;
    m_Options     = new ArrayList<>();
    m_ScreenWidth = SCREEN_WIDTH;
    m_BreakChars  = BREAK_CHARS;
  }

  /**
   * Returns the description.
   *
   * @return		the description
   */
  public String getDescription() {
    return m_Description;
  }

  /**
   * Sets the screenwidth to use.
   *
   * @param value	the width
   * @return		the parser
   */
  public ArgumentParser screenWidth(int value) {
    m_ScreenWidth = value;
    return this;
  }

  /**
   * Returns the currently set screenwidth.
   *
   * @return		the width
   */
  public int getScreenWidth() {
    return m_ScreenWidth;
  }

  /**
   * Sets the break characters to use for breaking up help screen lines when
   * longer than the maximum screen width.
   *
   * @param value	the characters to use for breaking up long strings
   * @return		the parser
   */
  public ArgumentParser breakChars(char[] value) {
    m_BreakChars = value;
    return this;
  }

  /**
   * Returns the currently set break characters for breaking up the help
   * screen text into multiple lines.
   *
   * @return		the characters
   */
  public char[] getBreakChars() {
    return m_BreakChars;
  }

  /**
   * Adds the option.
   *
   * @param flag	the flag (eg "-X" or "--XXX")
   * @return		the option
   */
  public Option addOption(String flag) {
    Option	result;

    result = new Option(flag);
    m_Options.add(result);

    return result;
  }

  /**
   * Adds the option.
   *
   * @param flag	the flag (eg "-X" or "--XXX")
   * @param secondFlag 	the second flag (eg a long version "--XXX")
   * @return		the option
   */
  public Option addOption(String flag, String secondFlag) {
    Option	result;

    result = new Option(flag, secondFlag);
    m_Options.add(result);

    return result;
  }

  /**
   * Returns all options.
   *
   * @return		the options
   */
  public List<Option> getOptions() {
    return m_Options;
  }

  /**
   * Parses the options and returns the parsed associations.
   * Does not remove options from the supplied array.
   *
   * @param args	the options to parse
   * @return		the parsed options
   * @throws ArgumentParserException        if parsing fails, e.g., if options not supplied
   */
  public Namespace parseArgs(String[] args) throws com.github.fracpete.simpleargparse4j.ArgumentParserException {
    return parseArgs(args, false);
  }

  /**
   * Parses the options and returns the parsed associations.
   *
   * @param args	the options to parse
   * @param remove	true if to remove parsed options from the array
   * @return		the parsed options
   * @throws ArgumentParserException        if parsing fails, e.g., if options not supplied
   */
  public Namespace parseArgs(String[] args, boolean remove) throws com.github.fracpete.simpleargparse4j.ArgumentParserException {
    return parseArgs(args, remove, false);
  }

  /**
   * Parses the options and returns the parsed associations.
   *
   * @param args	the options to parse
   * @param remove	true if to remove parsed options from the array
   * @param noErrors 	if enabled, no exceptions are thrown
   * @return		the parsed options
   * @throws ArgumentParserException        if parsing fails, e.g., if options not supplied
   */
  public Namespace parseArgs(String[] args, boolean remove, boolean noErrors) throws com.github.fracpete.simpleargparse4j.ArgumentParserException {
    Namespace		result;
    Map<String,Option> 	mapped;
    Set<Option>		required;
    int			i;
    Option		option;

    // initialize parsing
    result   = new Namespace(m_Options);
    mapped   = new HashMap<>();
    required = new HashSet<>();
    for (Option opt: m_Options) {
      mapped.put(opt.getFlag(), opt);
      if (opt.hasSecondFlag())
        mapped.put(opt.getSecondFlag(), opt);
      if (opt.isRequired())
        required.add(opt);
    }

    // parse
    for (i = 0; i < args.length; i++) {
      // help?
      if (args[i].equals("--help")) {
        m_HelpRequested = true;
        if (!noErrors)
	  throw new com.github.fracpete.simpleargparse4j.HelpRequestedException();
      }

      // defined option?
      if (mapped.containsKey(args[i])) {
        option = mapped.get(args[i]);
        if (option.hasArgument()) {
	  if (i == args.length - 1)
	    throw new com.github.fracpete.simpleargparse4j.MissingArgumentException("No argument supplied: " + option.getFlag());
	  if (!option.isValid(args[i+1]))
	    throw new InvalidArgumentException(option.getFlag(), option.getType(), args[i+1]);
	  if (option.isMultiple())
	    result.addValue(option.getDest(), option.parse(args[i+1]));
	  else
	    result.setValue(option.getDest(), option.parse(args[i+1]));
          if (remove) {
	    args[i]   = "";
	    args[i+1] = "";
	    i++;
	  }
	}
	else {
          if (option.isMultiple())
            result.addValue(option.getDest(), result.flipDefault(option.getDest()));
          else
	    result.setValue(option.getDest(), result.flipDefault(option.getDest()));
          if (remove)
            args[i] = "";
	}
	required.remove(option);
      }
    }

    // required options missing?
    if (required.size() > 0) {
      if (!noErrors)
	throw new com.github.fracpete.simpleargparse4j.RequiredOptionMissingException(required);
    }

    return result;
  }

  /**
   * Generates and returns the help screen.
   *
   * @param requested 	true if actually requested
   * @return		the help screen
   */
  public String generateHelpScreen(boolean requested) {
    return generateHelpScreen(requested, true, true, true);
  }

  /**
   * Searches for the from the starting point to the left.
   *
   * @param s 		the string to search
   * @param c 		the character to look for
   * @param start 	the starting position
   * @return 		the position, -1 if not found
   */
  protected int leftIndex(String s, char c, int start) {
    int		result;
    int		i;

    result = -1;

    for (i = start; i >= 0; i--) {
      if (s.charAt(i) == c) {
        result = i;
        break;
      }
    }

    return result;
  }

  /**
   * Breaks up a string into multiple lines if longer than the specified
   * maximum length.
   *
   * @param line	the line to break up
   * @param max		the maximum length
   * @return		the generated lines
   */
  protected List<String> breakUp(String line, int max) {
    List<String>	result;
    int[]		pos;
    int			i;
    int			index;

    result = new ArrayList<>();
    pos    = new int[m_BreakChars.length];

    do {
      if (line.length() > max) {
        for (i = 0; i < m_BreakChars.length; i++)
          pos[i] = leftIndex(line, m_BreakChars[i], max);
        index = 0;
        for (i = 0; i < m_BreakChars.length; i++) {
          if ((pos[i] > -1) && (pos[i] > index))
            index = pos[i];
	}
	// soft-break?
	if (index > 0) {
          result.add(line.substring(0, index + 1).trim());
          line = line.substring(index + 1).trim();
	}
	else {
          result.add(line.substring(0, max).trim());
          line = line.substring(max).trim();
	}
      }
      else {
        result.add(line);
        line = "";
      }
    } while (line.length() > 0);

    return result;
  }

  /**
   * Generates and returns the help screen.
   *
   * @param requested 	true if actually requested
   * @param desc	true if to output description
   * @param usage	true if to output short usage section
   * @param options	true if to output detailed options
   * @return		the help screen
   */
  public String generateHelpScreen(boolean requested, boolean desc, boolean usage, boolean options) {
    StringBuilder	result;
    int			width;
    int			optwidth;
    String[]		lines;

    result = new StringBuilder();
    if (requested) {
      result.append("Help requested");
      result.append("\n\n");
    }

    if (desc) {
      lines = m_Description.split("\n");
      for (String line : lines) {
	for (String fitted: breakUp(line, m_ScreenWidth))
	  result.append("\t").append(fitted).append("\n");
      }
      result.append("\n\n");
    }

    // short usage
    if (usage) {
      result.append("Usage: [--help]");
      for (Option opt : m_Options) {
	width = result.length() - result.lastIndexOf("\n");

	// length of option string
	optwidth = opt.getFlag().length();
	if (opt.hasSecondFlag())
	  optwidth += 2 + opt.getSecondFlag().length();  // comma+blank=2
	if (!opt.isRequired())
	  optwidth += 2;  // surrounding brackets
	if (opt.hasArgument()) {
	  if (opt.hasMetaVar())
	    optwidth += 1 + opt.getMetaVar().length();
	  else
	    optwidth += 1 + opt.getDest().length();
	}
	if (opt.isMultiple())
	  optwidth += 3;
	if (width + optwidth + 1 > m_ScreenWidth)
	  result.append("\n").append("      ");

	// append option string
	result.append(" ");
	if (!opt.isRequired())
	  result.append("[");
	result.append(opt.getFlag());
	if (opt.hasArgument()) {
	  if (opt.hasMetaVar())
	    result.append(" ").append(opt.getMetaVar().toUpperCase());
	  else
	    result.append(" ").append(opt.getDest().toUpperCase());
	}
	if (opt.isMultiple())
	  result.append("...");
	if (!opt.isRequired())
	  result.append("]");
      }
      result.append("\n\n");
    }

    // help screen for options
    if (options) {
      result.append("Options:\n");
      for (Option opt : m_Options) {
	result.append(opt.getFlag());
	if (opt.hasSecondFlag())
	  result.append(", ").append(opt.getSecondFlag());
	if (opt.hasArgument()) {
	  if (opt.hasMetaVar())
	    result.append(" ").append(opt.getMetaVar().toUpperCase());
	  else
	    result.append(" ").append(opt.getDest().toUpperCase());
	}
	result.append("\n");
	lines = opt.getHelp().split("\n");
	for (String line : lines) {
	  for (String fitted: breakUp(line, m_ScreenWidth - TAB_WIDTH))
	    result.append("\t").append(fitted).append("\n");
	}
	result.append("\n");
      }
    }

    return result.toString();
  }

  /**
   * Returns whether help got requested.
   *
   * @return		true if help got requested
   */
  public boolean getHelpRequested() {
    return m_HelpRequested;
  }

  /**
   * Handles the parse exception.
   *
   * @param e		the exception
   * @see		#getHelpRequested()
   */
  public void handleError(com.github.fracpete.simpleargparse4j.ArgumentParserException e) {
    if (e instanceof com.github.fracpete.simpleargparse4j.HelpRequestedException) {
      System.out.println(generateHelpScreen(true));
      return;
    }

    System.err.println(e.toString());
    e.printStackTrace();
    System.err.println(generateHelpScreen(false));
  }
}
