# Commands

This document provides a description of the commands available in the underdocs CLI:

  * [**init**](#init),
  * [**parse**](#parse),
  * [**render**](#render),
  * [**staple**](#staple).

## init

~~~~shell
java -jar underdocs.jar init
~~~~

Initializes a new underdocs configuration file (see [Configuration Reference](configuration-reference.md) for a reference on the contents of this file) in the current working directory. The contents of the file are queried interactively, or the defaults can be automatically accepted by passing the `-y` option.

Thus

~~~~shell
java -jar underdocs.jar init -y
~~~~

will create a new default configuration file in the current working directory.

If you omit the `-y` option, then underdocs will interactively ask a few questions regarding the configuration.

## parse

~~~~shell
java -jar underdocs.jar parse
~~~~

Parses the header files in [`parse.input.codebaseDirectory`](configuration-reference.md#codebasedirectory) and outputs a JSON representation of the codebase into the `parse.outputFile` file.

Please see the [Configuration Reference](configuration-reference.md) on how this command can be configured.

The inner workings of this command are documented in [Pipeline: Parse](pipeline.md#1-parse).

## render

~~~~shell
java -jar underdocs.jar render
~~~~

Renders a single-version site from the JSON representation located in the file [`render.input.codebaseFile`](configuration-reference.md#codebasefile). This file is the output of a previous `parse` command execution.

Please see the [Configuration Reference](configuration-reference.md) on how this command can be configured.

The inner workings of this command are documented in [Pipeline: Render](pipeline.md#2-render).

## staple

> TBD
