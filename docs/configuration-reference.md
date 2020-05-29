# Configuration Reference

This document describes the configuration options that can be used on the command line or in the configuration file.

**Table of Contents**

  * [Configuration File](#configuration-file)
  * [Options](#options)
    * [configurationFile](#configurationfile)
    * [log](#log)
      * [level](#level)
    * [parse](#parse)
      * [input](#input)
        * [codebaseDirectory](#codebasedirectory)
        * [includeGlobs](#includeglobs)
        * [excludeGlobs](#excludeglobs)
      * [outputFile](#outputfile)
    * [render](#render)
      * [features](#features)
        * [nightMode](#nightMode)
        * [prerendering](#prerendering)
        * [search](#search)
      * [headFragment](#headFragment)
      * [input](#input-1)
        * [codebaseFile](#codebasefile)
        * [resourcesDirectory](#resourcesdirectory)
        * [pagesDirectory](#pagesdirectory)
      * [linkTemplates](#linktemplates)
        * [remoteRepositoryLine](#remoterepositoryline)
        * [remoteRepositoryTag](#remoterepositorytag)
      * [otherAttributes](#otherattributes)
      * [otherSections](#othersections)
      * [outputDirectory](#outputdirectory)
      * [stabilityColors](#stabilityColors)
      * [visibility](#visibility)
      * [visibilityLevels](#visibilityLevels)

## Configuration File

A text file in [YAML](https://yaml.org/) format, holding the configuration values. This is the preferred way of configuring underdocs, as not every value can be set from the command line.

The location of the configuration file can be set via the command line using the [`configurationFile`](#configurationfile) option. By default, underdocs will search for the following files (in this order) in the current working directory:

  * `underdocs.yml`,
  * `underdocs.yaml`.

## Options

### configurationFile

  * Setting:
    * CLI :white_check_mark:
      * `--configurationFile`
    * File: :x:
  * Default Value
     * `underdocs.yml` and `underdocs.yaml` (in this order).

Sets the location of the configuration file. Overrides the default locations (`underdocs.yml` and `underdocs.yaml`). Must point to a text file in [YAML](https://yaml.org/) format.

~~~~shell
java -jar underdocs.jar parse --configurationFile=./someFile.yml
~~~~

### log

#### level

  * Default Value
    * `INFO`
  * Accepted Values
    * `QUIET`
    * `WARN`
    * `INFO`
    * `DEBUG`
  * Setting
    * CLI :white_check_mark:
      * `--log.level=INFO`
    * File: :white_check_mark:
      ~~~~yaml
      log:
        level: INFO
      ~~~~

Sets the verbosity of logging. Can be one of the following values:

  * `QUIET`
    * No logging output. Only errors will be reported.
  * `WARN`
    * Output warnings and errors only.
  * `INFO`
    * Output informative progress messages as well as warnings and errors.
  * `DEBUG`
    * Output detailed debug messages as well as progress messages, warnings and errors.

### parse

#### input

##### codebaseDirectory
  
  * Required.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      parse:
        input:
          codebaseDirectory: ./include
      ~~~~

The root directory of the header files.

For example, if set to `./include`, then `codebase://header.h` will link to the rendered documentation page of the `./include/header.h` header.

##### includeGlobs

  * Default Value
    * Single-item list of `*.h`.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      parse:
        input:
          includeGlobs:
            - '*.h'
      ~~~~

A list of [glob patterns](https://en.wikipedia.org/wiki/Glob_(programming)) to indicate which header files to include in the parsing process.

##### excludeGlobs

  * Default Value
    * Empty list.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      parse:
        input:
          excludeGlobs:
            - '**/config.h'
      ~~~~

A list of [glob patterns](https://en.wikipedia.org/wiki/Glob_(programming)) to indicate which header files to exclude from the parsing process.

Only paths matching the [includeGlobs](#includeglobs) will be tested against the exclude globs.

#### outputFile

  * Required.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      parse:
        outputFile: codebase.json
      ~~~~

The destination of the `parse` phase output. Will be overwritten if exists.

### render

#### features

##### nightMode

  * Default value
    * `true`
  * Accepted values
    * `true` or `false` (or any equivalent in the YAML syntax)
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        features:
          nightMode: true
      ~~~~

If set to `true` then a night mode switch will be displayed on the rendered pages. Otherwise no switch will be displayed and the cooresponding JS and CSS code will not be shipped.

The night mode styling can be overridden by placing an `underdocs-night.css` in the resources directory (can be set using the [resourcesDirectory](#resourcesdirectory) option).

##### prerendering

  * Default value
    * `true`
  * Accepted values
    * `true` or `false` (or any equivalent in the YAML syntax)
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        features:
          prerendering: true
      ~~~~

If set to `true` then underdocs will try to prerender the TeX formulae and the code snippets during build time. Note, that this is only possible if underdocs is executed by the [GraalVM](https://graalvm.org). Otherwise the rendering of such objects will happen on the client when the pages are loaded.

##### search

  * Default value
    * `true`
  * Accepted values
    * `true` or `false` (or any equivalent in the YAML syntax)
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        features:
          search: true
      ~~~~

If set to `true` then underdocs will

  * compute a search index during build time,
  * place a search box and corresponding JS code on every page.


#### headFragment

  * Default Value.
    * Empty String.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        input:
          headFragment: ''
      ~~~~

HTML code snippet, that will be included at the bottom of the `<head>` element on every rendered page.

#### input

##### codebaseFile

  * Required.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        input:
          codebaseFile: true
      ~~~~

The output file of the `parse` phase. Should be set to the same value as the [parse.outputFile](#outputfile) option.

##### resourcesDirectory

  * Default Value.
    * `null`.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        input:
          resourcesDirectory: null
      ~~~~

The root directory of custom resource files.

Files in this directory will be simply copied to the static resource directory of the site. Thus, files in this directory can override the built-in resources of underdocs.

For example, if set to `./resources`, then `resources://uml-diagram.svg` will link the copied instance of `./resources/uml-diagram.svg` in the static resources output folder.

##### pagesDirectory

  * Default Value.
    * `null`.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        input:
          pagesDirectory: null
      ~~~~

The root directory of custom documentation page files.

For example, if set to `./pages`, then `pages://complex-example.md` will to the rendered output page of `./pages/complex-example.md`.

#### linkTemplates

##### remoteRepositoryLine

  * Default Value.
    * `null`.
  * Setting
    * CLI :white_check_mark:
      * `--linkTemplates.remoteRepositoryLine`
    * File :white_check_mark:
      ~~~~yaml
      render:
        linkTemplates:
          remoteRepositoryLine: null
      ~~~~

Remote repository (GitHub, BitBucket, GitLab and friends) link template which will be used when creating links to the declarations in the code files.

Consider the following:

  * The template is set to the following value:
    ~~~~
    https://github.com/org/repo/tree/master/include/${path}#L${line}
    ~~~~

  * The [codebaseDirectory](#codebasedirectory) option was set to `./include`.

  * underdocs is rendering some declaration at line 10 of the file `./include/module/module/header.h`.

In this case, the result of the substitution will be as follows:

~~~~
https://github.com/org/repo/tree/master/include/module/module/header.h#10
~~~~

Thus:

  * `${path}` will be substituted with the path to the header with the `codebaseDirectory` chopped off.
  * `${line}` will be substituted with the line the declaration starts at in the header.

##### remoteRepositoryTag

  * Default Value.
    * `null`.
  * Setting
    * CLI :white_check_mark:
      * `--linkTemplates.remoteRepositoryTag`
    * File :white_check_mark:
      ~~~~yaml
      render:
        linkTemplates:
          remoteRepositoryTag: null
      ~~~~

Remote repository (GitHub, BitBucket, GitLab and friends) tag template which will be used when creating links to tags in [`since attributes`](format.md#since) in the case of single-version deployments.

Consider the following:

  * The template is set to the following value:
    ~~~~
    https://github.com/cryptid-org/cryptid-native/releases/tag/${tag}
    ~~~~

  * underdocs is rendering a `since` attribute with the value `v1.0.0`.

In this case, the result of the substitution will be as follows:

~~~~
https://github.com/org/repo/releases/tag/v1.0.0
~~~~

Thus:

  * `${tag}` will be substituted with the name of the tag.

#### otherAttributes

  * Default Value
    * `true`
  * Accepted values
    * `true` or `false` (or any equivalent in the YAML syntax)
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        otherAttributes: true
      ~~~~

If set to `true` then unrecognized attributes will be rendered on the documentation pages. Please refer to [The underdocs Format – Recognized Attributes](format.md#recognized-attributes) for the list of recognized attributes and to [The underdocs Format – Other Attributes](format.md#other-attributes) on how such attributes are handled.

#### otherSections

  * Default Value
    * `true`
  * Accepted values
    * `true` or `false` (or any equivalent in the YAML syntax)
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        otherSections: true
      ~~~~

If set to `true` then unrecognized sections will be rendered on the documentation pages before the `See Also` section. Please refer to [The underdocs Format – Recognized Sections](format.md#recognized-section) for the list of recognized sections and to [The underdocs Format – Other Sections](format.md#other-sections) on how such sections are handled.

#### outputDirectory

  * Required.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        outputDirectory: ./docs-site
      ~~~~

The directory into which underdocs will put the outputs of the `render` phase. Will be created if missing. Will be overwritten (emptied and rewritten) of exists.

#### stabilityColors

  * Default Value.
    * Empty Map
  * Accepted Values.
    * A mapping of strings to hex color strings (`#RRGGBB`).
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        stabilityColors:
          alpha: '#FFFFFF'
          beta: '#FFFFFF'
          stable: '#FFFFFF'
          deprecated: '#FFFFFF'
      ~~~~

Colors for the values of the [`stability attribute`](format.md#stability).

Consider the following example:

  * The color for the `deprecated` stability level is set to `#FF0000` as follows
    ~~~~YAML
    deprecated: '#FF0000`
    ~~~~

  * The `stability` attribute is present on some entity with the `deprecated` value:
    ~~~~YAML
    stability: deprecated
    ~~~~

In this case, underdocs will render this attribute with the color `#FF0000`.

Note, that the stbility levels are completely arbitrary and user-defined.

If no color is specified for a given stability level, then the default attribute color will be used.

#### visibility

  * Default Value.
    * `null`
  * Accepted Values.
    * `null`, or an item in the [`visibilityLevels`](#visibilitylevels) list.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        visibility: null
      ~~~~

Sets the visibility level which controls whether an entity will appear in the rendered output or not.

Please refer to the documentation of the [`visibility attribute`](format.md#visibility) for further details.

#### visibilityLevels

  * Default Value.
    * Empty list.
  * Accepted Values.
    * A list of strings.
  * Setting
    * CLI :x:
    * File: :white_check_mark:
      ~~~~yaml
      render:
        visibilityLevels:
          - private
          - protected
          - public
      ~~~~

Sets the available visibility levels and their ordering from lowest to highest.

Please refer to the documentation of the [`visibility attribute`](format.md#visibility) for further details.
