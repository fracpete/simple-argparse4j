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
 * Copyright (C) 2017-2018 University of Waikato, Hamilton, NZ
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
  public final int SCREEN_WIDTH = 80;

  /** the description. */
  protected String m_Description;

  /** the managed options. */
  protected List<Option> m_Options;

  /** whether help got requested. */
  protected boolean m_HelpRequested;

  /**
   * Initializes the parser.
   */
  public ArgumentParser(String description) {
    m_Description = description;
    m_Options     = new ArrayList<>();
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
   * Adds the option.
   *
   * @param flag	the flag to use (eg "--blah")
   * @return		the option
   */
  public Option addOption(String flag) {
    Option	result;

    result = new Option(flag);
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
      if (opt.isRequired())
        required.add(opt);
    }

    // parse
    for (i = 0; i < args.length; i++) {
      // help?
      if (args[i].equals("--help")) {
        m_HelpRequested = true;
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
    if (required.size() > 0)
      throw new com.github.fracpete.simpleargparse4j.RequiredOptionMissingException(required);

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
      result.append(m_Description);
      result.append("\n\n");
    }

    // short usage
    if (usage) {
      result.append("Usage: [--help]");
      for (Option opt : m_Options) {
	width = result.length() % SCREEN_WIDTH;
	optwidth = opt.getFlag().length();
	if (!opt.isRequired())
	  optwidth += 2;  // surrounding brackets
	if (opt.hasArgument())
	  optwidth += 1 + opt.getDest().length();
	if (width + optwidth + 1 > SCREEN_WIDTH)
	  result.append("\n").append("      ");
	result.append(" ");
	if (!opt.isRequired())
	  result.append("[");
	result.append(opt.getFlag());
	if (opt.hasArgument())
	  result.append(" ").append(opt.getDest().toUpperCase());
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
	if (!opt.hasArgument())
	  result.append(" ").append(opt.getDest().toUpperCase());
	result.append("\n");
	lines = opt.getHelp().split("\n");
	for (String line : lines)
	  result.append("\t").append(line).append("\n");
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

    System.err.println(e);
    e.printStackTrace();
    System.err.println(generateHelpScreen(false));
  }
}
