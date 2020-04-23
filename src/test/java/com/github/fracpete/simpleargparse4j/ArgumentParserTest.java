/*
 * ArgumentParserTest.java
 * Copyright (C) 2018-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.simpleargparse4j;

import com.github.fracpete.simpleargparse4j.Option.Type;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Testing the ArgumentParser class.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ArgumentParserTest
  extends TestCase {

  /**
   * Constructor.
   *
   * @param name	the name of the test
   */
  public ArgumentParserTest(String name) {
    super(name);
  }

  /**
   * Tests help request.
   */
  public void testHelp() {
    ArgumentParser parser = new ArgumentParser("test");
    try {
      parser.parseArgs(new String[]{
        "--help",
      });
    }
    catch (Exception e) {
      assertTrue("help request", parser.getHelpRequested());
    }
  }

  /**
   * Tests string options.
   */
  public void testStrings() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt1").dest("opt1");
    parser.addOption("--opt2").dest("opt2");
    parser.addOption("--opt3").dest("opt3");
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt1",
	"v1",
        "--opt2",
	"v2",
        "--opt3",
	"v3",
      });
      assertEquals("opt1", "v1", ns.getString("opt1"));
      assertEquals("opt2", "v2", ns.getString("opt2"));
      assertEquals("opt3", "v3", ns.getString("opt3"));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests boolean options.
   */
  public void testBooleans() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt1").dest("opt1").argument(false);
    parser.addOption("--opt2").dest("opt2").argument(false).setDefault(true);
    parser.addOption("--opt3").dest("opt3").argument(false);
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt1",
        "--opt2",
        "--opt3",
      });
      assertEquals("opt1", true, ns.getBoolean("opt1"));
      assertEquals("opt2", false, ns.getBoolean("opt2"));
      assertEquals("opt3", true, ns.getBoolean("opt3"));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests just string and numeric options.
   */
  public void testMixed() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--str1").dest("str1");
    parser.addOption("--int2").dest("int2");
    parser.addOption("--float3").dest("float3");
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--str1",
	"v1",
        "--int2",
	"2",
        "--float3",
	"3.1415",
      });
      assertEquals("str1", "v1", ns.getString("str1"));
      assertEquals("int2", 2, ns.getByte("int2"));
      assertEquals("int2", 2, ns.getShort("int2"));
      assertEquals("int2", 2, ns.getInt("int2"));
      assertEquals("int2", 2L, ns.getLong("int2"));
      assertEquals("float3", 3.1415f, ns.getFloat("float3"));
      assertEquals("float3", 3.1415d, ns.getDouble("float3"));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests invalid argument type.
   */
  public void testInvalid() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--int").dest("int").type(Type.INTEGER);
    try {
      parser.parseArgs(new String[]{
        "--int",
	"3.123",
      });
      fail("Should have failed!");
    }
    catch (InvalidArgumentException e) {
      // correct behavior
    }
    catch (Throwable e2) {
      fail("Failed to parse!\n" + e2);
    }
  }

  /**
   * Tests string list.
   */
  public void testStringList() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt").dest("opt").multiple(true);
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt",
	"v1",
        "--opt",
	"v2",
        "--opt",
	"v3",
      });
      assertEquals("opt", 3, ns.getList("opt").size());
      assertEquals("opt[0]", "v1", ns.getList("opt").get(0));
      assertEquals("opt[1]", "v2", ns.getList("opt").get(1));
      assertEquals("opt[2]", "v3", ns.getList("opt").get(2));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests int list.
   */
  public void testIntList() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt").dest("opt").multiple(true).type(Type.INTEGER);
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt",
	"1",
        "--opt",
	"2",
        "--opt",
	"3",
      });
      assertEquals("opt", 3, ns.getList("opt").size());
      assertEquals("opt[0]", 1, ns.getList("opt").get(0));
      assertEquals("opt[1]", 2, ns.getList("opt").get(1));
      assertEquals("opt[2]", 3, ns.getList("opt").get(2));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests double list.
   */
  public void testDoubleList() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt").dest("opt").multiple(true).type(Type.DOUBLE);
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt",
	"1.1",
        "--opt",
	"2.1",
        "--opt",
	"3.1",
      });
      assertEquals("opt", 3, ns.getList("opt").size());
      assertEquals("opt[0]", 1.1, ns.getList("opt").get(0));
      assertEquals("opt[1]", 2.1, ns.getList("opt").get(1));
      assertEquals("opt[2]", 3.1, ns.getList("opt").get(2));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests boolean list.
   */
  public void testBooleanList() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("--opt").dest("opt").multiple(true).type(Type.BOOLEAN);
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt",
        "--opt",
        "--opt",
      });
      assertEquals("opt", 3, ns.getList("opt").size());
      assertEquals("opt[0]", true, ns.getList("opt").get(0));
      assertEquals("opt[1]", true, ns.getList("opt").get(1));
      assertEquals("opt[2]", true, ns.getList("opt").get(2));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Tests supplying short/long flags.
   */
  public void testShortLong() {
    ArgumentParser parser = new ArgumentParser("test");
    parser.addOption("-o", "--opt").dest("opt");
    try {
      Namespace ns = parser.parseArgs(new String[]{
        "--opt",
        "yo",
      });
      assertEquals("opt", "yo", ns.getString("opt"));
      ns = parser.parseArgs(new String[]{
        "-o",
        "yo",
      });
      assertEquals("opt", "yo", ns.getString("opt"));
    }
    catch (Exception e) {
      fail("Failed to parse!\n" + e);
    }
  }

  /**
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(ArgumentParserTest.class);
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    TestRunner.run(suite());
  }
}
