# simple-argparse4j
Very simple argument parser, inspired by argparse4j (https://argparse4j.github.io/).

Normally, I use *argparse4j* for all my little command-line based tools. 
But for one project, I needed to be able to have unparsed options that resemble
options, as I needed to pass on these options to processes that get launched.
I didn't find a solution to get *argparse4j* to be more lenient, hence I 
implemented a very simple version of a command-line option parser inspired
by *argparse4j*.


## Supported data types

The parser supports the following data types:

* string
* boolean
* byte
* short
* int
* long
* float
* double
* file (any file/dir, existing file/dir, non-existing file/dir)

Apart from *boolean* all data types require an argument. 

## Defining options

Here is an example for defining an option:

```java
import com.github.fracpete.simpleargparse4j.ArgumentParser;
...
ArgumentParser parser = new ArgumentParser(getName());
parser.addOption("--name")
  .dest("name")
  .help("the name of the environment")
  .required(true);
```

The parser will look for an option that starts with a flag of `--name`
and stores the associated argument that it will find (using the key specified
with `dest(...)`). `help(...)` specifies the help string. 
`required(true)` specifies that the user *has* to supply the argument (by default, 
options are optional). 

It is also possible to supply two flags, e.g., a short form and a long version:
```java
import com.github.fracpete.simpleargparse4j.ArgumentParser;
...
ArgumentParser parser = new ArgumentParser(getName());
parser.addOption("-n", "--name")
  .dest("name")
  .help("the name of the environment")
  .required(true);
``` 

Other settings:

* `argument(boolean)` -- specifies whether the option is a flag (`false`) or 
  requires an argument (`true`)
* `multiple(boolean)` -- specifies whether the option can occur multiple times
* `setDefault(...)` -- sets the default value (if not a required option);
  in conjunction with `multiple(true)`, you have to set a `java.util.List` object
* `type(Type)` -- sets the type to enforce when parsing the options rather than 
  when retrieving them from the `Namespace` object, e.g., double


## Parsing options

The following snippet parses the options (`args`) and returns them as
`Namespace` object (`parseArgs(String[])` method). 
In case of invalid options, missing required options
or help being request, an `ArgumentParserException` is thrown.
Using the parser's `handleError(ArgumentParserException)` you can output
a help screen generated from the defined options.

```java
import com.github.fracpete.simpleargparse4j.ArgumentParserException;
import com.github.fracpete.simpleargparse4j.Namespace;
...
Namespace ns;
try {
  ns = parser.parseArgs(args);
}
catch (ArgumentParserException e) {
  parser.handleError(e);
  return !parser.getHelpRequested();  // help request is valid operation
}
```

Using `parseArgs(String[],true)` you can remove the all the parsed options
from the provided string array. `parseArgs(String[])` does not remove them
by default.


## Retrieving parsed values

Once the options have been parsed, you can retrieve (typed) from the
`Namespace` object:

* `getString(String)` -- returns the string associated with the provided key
* `getBoolean(String)` -- returns the boolean associated with the provided key; 
  whether a `true` or `false` gets returned is determine by the default value.
  Without specifying an explicit default value, `false` gets returned if the
  flag is present, otherwise `true`. You can invert this by simply using 
  `setDefault(true)` when defining the option.
* `getByte(String)` -- returns the byte associated with the provided key
* `getShort(String)` -- returns the short associated with the provided key
* `getInt(String)` -- returns the integer associated with the provided key
* `getLong(String)` -- returns the long associated with the provided key
* `getFloat(String)` -- returns the float associated with the provided key
* `getDouble(String)` -- returns the double associated with the provided key
* `getFile(String)` -- returns the double associated with the provided key
* `getList(String)` -- returns the list associated with the provided key


## Example

The following example configures several parameters, not all of them required.
It then parses the arguments provided to the application and outputs the
parsed values.

```java
import com.github.fracpete.simpleargparse4j.ArgumentParser;
import com.github.fracpete.simpleargparse4j.ArgumentParserException;
import com.github.fracpete.simpleargparse4j.Namespace;

public static void main(String[] args) {
  // define the options
  ArgumentParser parser = new ArgumentParser("create");
  parser.addOption("--name")
    .dest("name")
    .help("the name of the environment")
    .required(true);
  parser.addOption("--java")
    .dest("java")
    .help("the full path of the java binary to use for launching Weka")
    .setDefault("");
  parser.addOption("--memory")
    .dest("memory")
    .help("the heap size to use for launching Weka (eg '1024m' or '2g')")
    .setDefault("");
  parser.addOption("--weka")
    .dest("weka")
    .help("the full path to the weka.jar to use")
    .required(true);
  parser.addOption("--wekafiles")
    .dest("wekafiles")
    .help("the full path to the 'wekafiles' directory to initialize the environment with")
    .setDefault("");
  parser.addOption("--envvar")
    .dest("envvar")
    .help("optional environment variables to set (key=value)")
    .multiple(true);
  
  // parse the options
  Namespace ns;
  try {
    ns = parser.parseArgs(args, true);
  }
  catch (ArgumentParserException e) {
    parser.handleError(e);
    return false;
  }

  // output the parsed values
  System.out.println("Name: " + ns.getString("name"));
  System.out.println("Jav: " + ns.getString("java"));
  System.out.println("Memory: " + ns.getString("memory"));
  System.out.println("Weka jar: " + ns.getString("weka"));
  System.out.println("Env. vars: " + ns.getList("envvar"));
}
```

## Maven

Add the following dependency to your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>simple-argparse4j</artifactId>
      <version>0.0.5</version>
    </dependency>
```
