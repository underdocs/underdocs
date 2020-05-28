# The underdocs Format

> Or, how to comment in underdocs style.

This document serves as a reference on
  
  * how underdocs expects your codebase to be structured (modules. headers, top-level elements),
  * rules for doc comment styles, placement and structure,
  * how underdocs parses doc comments (attributes, sections, links).

**Table of Contents**

  * [High-Level Concepts](#high-level-concepts)
    * [Modules](#modules)
      * [module-info.h](#module-info.h)
    * [Headers](#headers)
    * [Top-Level Elements](#top-level-elements)
  * [Doc Comment Format](#doc-comment-format)
    * [Placement](#placement)
      * [Merging](#merging)
    * [Styles](#styles)
    * [Attributes](#attributes)
    * [Sections](#sections)
    * [Links](#links)
  * [Attributes](#attributes)
    * [Alias](#alias)
    * [Group](#group)
    * [Since](#since)
    * [Stability](#stability)
    * [Visibility](#visibility)
    * [Other Attributes](#other-attributes)
  * [Sections](#sections)
    * [Recognized Sections](#recognized-sections)
    * [Description](#description)
    * [Groups](#groups)
    * [Parameters](#parameters)
    * [Return Value](#return-value)
    * [Error Handling](#error-handling)
    * [Notes](#notes)
    * [Examples](#examples)
    * [See Also](#see-also)
    * [Other Sections](#other-sections)
  * [Links](#links)
    * [Codebase](#codebase)
    * [Resource](#resource)
    * [Page](#page)
    * [Version](#version)

## High-Level Concepts

Before detailing the actual format of comments, we will start with the introduction of some terminology used throughout this document.

### Modules

<details>
<summary><b>TL;DR</b></summary>

Each directory is treated as a module. Module documentation can be created by providing a `module-info.h`˙file in the directory. The actual documentation must be placed on an include guard. The root of the include directory tree is called the top-level module.

</details>

underdocs introduces the concept of *modules*. Even though C does not provide language-level support for namespaces or modules (the way, for example, C++ does), developers usually split their codebase into separate components with well-defined responsibilities and roles. These components usually live in their own directories. That way, the directory structure corresponds to the component hierarchy of the project.

Let's assume, that we are writing a compiler! In this case, we can immediately split our codebase into three components:

  * lexer,
  * parser,
  * codegen.

Thus, we will have a directory structure like this:

~~~~
/compiler-project
  /include
    /lexer
    /parser
    /codegen
~~~~

We can further divide these components into smaller ones, for example in the case of the `parser`:

~~~~
/compiler-project
  /include
    /lexer
    /parser
        /error-reporting
        /representation
        /type-checker
    /codegen
~~~~

For underdocs, each directory is a *module*. Modules may have their own documentation and they have sudmodules (subdirectories of the current directory) and headers (headers in the current directory). Submodules and headers will appear on the rendered documentation page of the module.

#### module-info.h

Module documentation can be added to a directory by creating a `module-info.h` header. For example, if we want to add documentation for our *top-level module* (which corresponds to the root of the include directory tree), then we can create the `/compiler-project/include/module-info.h` file:

~~~~
/compiler-project
  /include
    /lexer
    /parser
        /error-reporting
        /representation
        /type-checker
    /codegen
    module-info.h
~~~~

underdocs expects these files to contain a documented include guard, as follows:

~~~~C
/**
 * # Compiler
 *
 * ## Description
 *
 * The best compiler on this planet.
 */
#ifndef __TOP_LEVEL_MODULE_H
#define __TOP_LEVEL_MODULE_H
#endif
~~~~

The documentation attached to the include guard will be used as the module documentation.

**Note**: You may also use `#if !defined` and `#pragma once` as include guards.

### Headers

<details>
<summary><b>TL;DR</b></summary>

Header documentation must be placed on an include guard. Macros and other declarations are called *top-level elements*.

</details>

underdocs creates the documentation by parsing the header files of your project. Every header belongs to a module: the one which corresponds to the containing directory.

Headers may have their own documentation. The header documentation must be placed on an include guard, as follows:

~~~~C
/**
 * ## Description
 *
 * AST for the best compiler.
 */
#ifndef __AST_H
#define __AST_H

typedef struct {

} ast_t;

ast_t *init_ast();

#endif
~~~~

Macros and other declarations in the header are called *top-level elements*. These elements may have their own documentation and will appear on the rendered documentation page of the header.

### Top-Level Elements

<details>
<summary><b>TL;DR</b></summary>

Each top-level element gets their own rendered documentation page. Documentation for these elements must be placed on top of the element.

</details>

Headers contain so-called top-level elements. Each of these elements get their own rendered documentation page.

Top-level elements are the follows:

  * enums,
  * functions,
  * macro constants,
  * macro functions,
  * preprocessor conditionals,
  * structs,
  * typedefs (or in underdocs terminology: type aliases),
  * unions,
  * variables.

Example documentation for a macro constant:

~~~~C
/**
 * ## Description
 *
 * The mathematical constant $`\pi`$.
 */
#define PI 3.14
~~~~

## Doc Comment Format

In this section we detail the mechanics of doc comments: how they are recognized and parsed.

### Placement

<details>
<summary><b>TL;DR</b></summary>

  * Top-level elements: Above the element.
  * Members: Above or next to the member.

</details>

The required placement of documentation is rather straightforward. In the case of *top-level elements*, the documentation must appear on above element:

  * :white_check_mark:
    ~~~~C
    /**
    * ## Description
    * 
    * Boolean type for truth values.
    */
    typedef char Boolean;
    ~~~~
  * :x:
    ~~~~C
    typedef char Boolean; /**
                           * ## Description
                           * 
                           * Boolean type for truth values.
                           */
    ~~~~
    
In the case of members, the documentation may appear above or next to the member:

  * :white_check_mark:
    ~~~~C
    struct vec2d_t {
        /**
         * The $`x`$ coordinate.
         */
        float x;

        /**
         * The $`y`$ coordinate.
         */
        float y;
    };
    ~~~~

  * :white_check_mark:
    ~~~~C
    struct vec2d_t {
        float x; // The $`x`$
                 // coordinate. Yes, this comment
                 // can be multiline.

        float y; // The $`y`$ coordinate.
    };
    ~~~~

#### Merging

Multiple doc comments appearing above (or next to) the same top-level element (or member) are merged. For example:

~~~~C
/**
* ## Description
*
*/
/**
* Boolean type for truth values.
*/
typedef char Boolean;
~~~~

is the same as

~~~~C
/**
 * ## Description
 * 
 * Boolean type for truth values.
 */
typedef char Boolean;
~~~~

Doc comments of different styles are merged too. For example:

~~~~C
/// ## Description
///
/**
* Boolean type for truth values.
*/
typedef char Boolean;
~~~~

is the same as

~~~~C
/**
 * ## Description
 * 
 * Boolean type for truth values.
 */
typedef char Boolean;
~~~~

### Style

<details>
<summary><b>TL;DR</b></summary>

Dependens on the configuration, the following are supported:

  * `/* */`,
  * `/** */,`
  * `//`,
  * `///`.

</details>

underdocs supports four doc comment styles (where style refers to the delimiters of the doc comment):

  * SLASH_ASTERISK 
    * `/* */`
  * SLASH_DOUBLE_ASTERISK
    * `/** */`
  * DOULE_SLASH
    * `//`
  * TRIPLE_SLASH
    * `///`

You can configure the set of recognized doc comment styles, so in the `parse` phase underdocs will extract exactly those comments that you desire.

### Attributes

Documentation comments for

  * modules,
  * headers,
  * and top-level elements,

may begin with an optional YAML front-matter containing attributes. The front matter is a valid [YAML](https://yaml.org/) fragment placed between `---` fences.

The attributes in the front-matter specify metadata regarding the appropriate element. Any attribute can appear in the front-matter, but some attributes induce special processing:

  * [alias](#alias),
  * [group](#group),
  * [since](#since),
  * [stability](#stability),
  * [visibility](#visibility).

Example:

~~~~C
/**
 * ---
 * since: v1.0.0
 * ---
 * ## Description
 * 
 * The mathematical constant $`\pi`$.
 */
#define PI 42
~~~~

### Sections

<details>
<summary><b>TL;DR</b></summary>

The documentation of modules, headers and top-level elements is broken into sections. Each section starts with a second level heading (`##`).

</details>

Documentation comments for

  * modules,
  * headers,
  * and top-level elements

are broken into sections. The sections provide a base structure for the documentation and some of them have special parsing rules. Each section starts with a second level heading (`##`) with the section name (`## Description` for example).

Sections recognized by underdocs are as follows:

  * [Description](#description),
  * [Groups](#groups),
  * [Parameters](#parameters),
  * [Return Value](#return-value),
  * [Error Handling](#error-handling),
  * [Notes](#notes),
  * [Examples](#examples),
  * [See Also](#see-also).

Not every section is recognized for every type. Please refer to [Recognized Sections](#recognized-sections) for the list of recognized section for types.

### Links

In the `render` phase, underdocs parses and processes the Markdown contents of the doc comments. During the processing phase, so-called namespaced links are resolved and replaced.

Using namespace links, you can create links to different elements or pages without being aware of the actual output structure of the documentation site. For example, you can create a link to the `vec2d_t` type in the `linalg/vec2d.h` header using the following namespaced link:

~~~~Markdown
[vec2d_t](codebase://linalg/vec2d.h#vec2d_t)
~~~~

The following namespaces are recognized:

  * [`codebase://`](#codebase).
  * [`pages://`](#pages),
  * [`resources://`](#resources),
  * [`versions://`](#versions).
