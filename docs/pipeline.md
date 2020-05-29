# Pipeline

> From headers to HTML.

This document details how the pipeline of transformations applied to the input header files.

**Overview**

  1. [Parse](#1.-parse)
     1. [Collector](#1.1.-collector)
     1. [C Parser](#1.2.-c-parser)
     1. [Representation Transformer](#1.3.-representation-transformer)
     1. [Output Writer](#1.4.-output-writer)
  1. [Render](#2.-render)
     1. [Module Grouper](#2.1-module-grouper)
     1. [Module Parser](#2.2-header-parser)
     1. [Header Parser](#2.3-header-parser)
     1. [HTML Renderer](#2.4-html-renderer)
  1. [Staple](#3-staple)

## 1. Parse

This stage can be invoked using the [`parse`](commands.md#parse) command.

### 1.1. Collector

  * Input
    * [`parse.input.codebaseDirectory`](configuration-reference.md#codebasedirectory)
      * The root directory containing header files.
    * [`parse.input.includeGlobs`](configuration-reference.md#includeglobs)
      * Glob patterns controlling which header files to include in the parsing process.
    * [`parse.input.excludeGlobs`](configuration-reference.md#excludeglobs)
      * Glob patterns controlling which header files to exclude from the parsing process.
  * Output
    * An array of header file paths.
  * Codebase Location
    * [underdocs-collector](../underdocs-collector) maven module

The first phase of the pipeline is the **Collector**. This phase directly maps to the `underdocs-collector` maven module.

The **Collector** phase walks the `codebaseDirectory` and collects all files matching at least one of the `includeGlobs`. Afterwards, it discards every file which matches at least one of the `excludeGlobs`.

The remaining files (their paths to be precise) are passed to the next phase.

### 1.2. C Parser

  * Input
    * An array of header file paths.
  * Output
    * A compound representation of each header with the AST, comments and preprocessor statements.
  * Codebase Location
    * [underdocs.parser.EclipseHeaderParser.kt](../underdocs-parser/src/main/kotlin/underdocs/parser/EclipseHeaderParser.kt)

After collecting the header files to be processed the **C parser** takes over the process. Each header file is parsed into an Abstract Syntax Tree for further processing. Comment nodes and preprocessor statements are also parsed and stored.

This phase uses the [Eclipse CDT C Parser](https://www.eclipse.org/cdt/) under the hood.

### 1.3. Representation Transformer

  * Input
    * Parsed header representations.
    * [`parse.commentStyles`](configuration-reference.md#commentstyles)
  * Output
    * Each header in [underdocs-representation](../underdocs-representation).
  * Codebase Location
    * [underdocs.parser.EclipseHeaderParser.kt](../underdocs-parser/src/main/kotlin/underdocs/parser/EclipseHeaderParser.kt).
    * [underdocs.parser.comment](../underdocs-parser/src/main/kotlin/underdocs/parser/comment) package.
    * [underdocs-representation](../underdocs-representation) maven module.

The output of **C parser** phase depends on the underlying tool, thus, further processing is necessary to create a stable representation that can be used the `render` stage. This is the very task of this phase.

The **Representation Transformer** takes  every

  * C declaration (enums, functions, structs, typedefs, unions, variables),
  * macro definition (macro constants and macro functions),
  * preprocessor conditional (`#ifdef`, `#ifndef`, `#if`)

and

  1. searches for comments attached to the entity,
  2. discards comments written in undesired style,
  3. processes and merges comment contents,
  4. emits a new appropriate object in `underdocs-representation` format.

It is the task of the **Representation Transformer** to identify the include guards in the header files. Comments attached to the include guards are treated as the header documentation.

**IMPORTANT**: The representation emitted by this step contains the comments in unparsed format: they are flat strings. It is the task of the renderer to actually parse the comments into attributes, sections and such.

### 1.4. Output Writer

  * Input
    * Headers in [underdocs-representation](../underdocs-representation).
    * [`parse.outputFile`](configuration-reference.md#outputfile)
  * Output
    * The header contents in a single JSON file.
  * Codebase Location
    * [underedocs.cli.ParseCommand.kt](../underdocs/src/main/kotlin/underdocs/cli/commands/parse/ParseCommand.kt)

The last phase of the `parse` stage is the **Output Writer**. This step will collect the header representations and put them into a `Codebase` object along with `parse.input.codebaseDirectory`. Afterwards, this object is serialized into JSON and written into `parse.outputFile`.

The JSON file output of this phase can be used to write custom tools against the codebase.

## 2. Render

This stage can be invoked using the [`render`](comamnds.md#render) command.

### 2.1. Module Grouper

  * Input
    * The codebase as output by the Parse stage.
  * Output
    * The module tree.
  * Codebase Location
    * [underdocs.renderer.parser.DefaultCodebaseParser.kt](../underdocs-renderer/src/main/kotlin/underdocs/renderer/parser/DefaultCodebaseParser.kt)

The codebase representation written into JSON by the Parse stage has no notion of [modules](#format.md#modules). It's a flat list of headers with no hierarchy applied.

This phase takes this flat list of headers and organizes it into a module tree based on the directories each resides in. The output of the phase is this tree where the root corresponds to the top-level module (which, in turn, corresponds to the `parse.input.codebaseDirectory`).

### 2.2 Module Parser

  * Input
    * The module tree.
  * Output
    * The module tree with parsed modules.
  * Codebase Location
    * [underdocs.renderer.parser.module.DefaultModuleParser.kt](../underdocs-renderer/src/main/kotlin/underdocs/renderer/parser/module/DefaultModuleParser.kt)

The module parser takes a module tree node (which is a module) and passes each header in the module to the header parser.

### 2.3. Header Parser

  * Input
    * A header in the module tree.
    * Lots of configuration from the [`render`](#configuration-reference.md#render) object.
  * Output
    * A header in the module tree with parsed declarations. Header contents are transformed into instances of classes in the [underdocs.renderer.representation](../underdocs-renderer/src/main/kotlin/underdocs/renderer/representation) package
  * Codebase Location
    * [underdocs.renderer.parser.module.DefaultHeaderParser.kt](../underdocs-renderer/src/main/kotlin/underdocs/renderer/parser/module/DefaultHeaderParser.kt)
    * [underdocs.renderer.parser.documentation](../underdocs-renderer/src/main/kotlin/underdocs/renderer/parser/documentation) package
    * [underdocs.renderer.parser.section](../underdocs-renderer/src/main/kotlin/underdocs/renderer/parser/section) package
    * [underdocs.renderer.representation](../underdocs-renderer/src/main/kotlin/underdocs/renderer/representation) package

The header objects in the original module tree contain unparsed documentation attached to each element. The **Header Parser** phase takes every entity in the header and parses the attributes and sections in the attached documentation string. Thus, at the end of this phase, the each entity will have a structured documentation object attached to it (if documentation comment was present).

The module tree with parsed headers is written into the [`render.outputDirectory`](#configuration-reference.md#outputdirectory) as a JSON file (`{render.outputDirectory}/data.json`) for further machine processing (including multiversion deployments by the [3. Staple](#staple) phase). This JSON file can be used to build additional tooling around the parsed documentation.

### 2.4. HTML Renderer

  * Input
    * A module tree with parsed headers in [underdocs.renderer.representation](../underdocs-renderer/src/main/kotlin/underdocs/renderer/representation).
    * Lots of configuration from the [`render`](#configuration-reference.md#render) object.
  * Output
    * HTML pages and copied resources.
  * Codebase Location
    * [underdocs.renderer.output.html](../underdocs-renderer/src/main/kotlin/underdocs/renderer/output/html) package
    * [underdocs.renderer.writer](../underdocs-renderer/src/main/kotlin/underdocs/renderer/writer) package

The **HTML Renderer** phase descends the parsed module tree and emits a new HTML page

  * for each module,
  * for each header
  * and for each header entity.

The nesting of the created directory structure directly corresponds to the module tree.

This phase is also responsible for copying the built-in resources and the user-provided resources into the `{render.outputDirectory}/_static` folder.

## 3. Staple

> TBD.

This stage can be invoked using the [`staple`](comamnds.md#staple) command.
