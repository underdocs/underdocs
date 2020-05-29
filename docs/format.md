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
    * [Versions](#versions)
      * [version-info.h](#version-info.h)
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
    * [Recognized Attributes](#recognized-attributes)
    * [Alias](#alias)
    * [Group](#group)
    * [Since](#since)
    * [Stability](#stability)
    * [Visibility](#visibility)
    * [Other Attributes](#other-attributes)
  * [Sections](#sections)
    * [Recognized Sections](#recognized-sections)
    * [Title](#title)
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
    * [Pages](#pages)
    * [Resources](#resources)
    * [Versions](#versions)

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

**Note**: The actual name of the macro constant does not matter.

**Note**: You may also use `#if !defined` and `#pragma once` as include guards.

**Configuration**: 

  * [parse.input.codebaseDirectory](configuration-reference.md#codebasedirectory)

### Versions

**Note**: If you're not interested in multiversion deployments, you can skip this section.

The `parse` and `render` phases of underdocs are completely version-agnostic and enable single-version deployments, but the `staple` phase allows for multiversion deployments. With multiversion deployments you can host the documentation of multiple library versions at the same place.

#### version-info.h

Version documentation can be added by creating a `version-info.h` header in the *top-level module*. For example, if we want to add version documentation to our compiler project, then we can create the `/compiler-project/include/version-info.h` file:

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
    version-info.h
~~~~

underdocs expects this file to contain a documented include guard, as follows:

~~~~C
/**
 * # v1.0.0
 *
 * ## Description
 *
 * The first public release of the best compiler.
 */
#ifndef __VERSION_INFO_H
#define __VERSION_INFO_H
#endif
~~~~

The documentation attached to the include guard will be used as the version documentation.

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

**Configuration**: 

  * [parse.input.codebaseDirectory](configuration-reference.md#codebasedirectory)
  * [parse.input.includeGlobs](configuration-reference.md#includeglobs)
  * [parse.input.excludeGlobs](configuration-reference.md#excludeglobs)

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

**Configuration**: 

  * [parse.commentStyles](configuration-reference.md#commentstyles)

### Attributes

Documentation comments for

  * modules (in `module-info.h`),
  * the current version (in `version-info.h`),
  * headers,
  * and top-level elements,

may begin with an optional YAML front-matter containing attributes. The front matter is a valid [YAML](https://yaml.org/) fragment placed between `---` fences.

The attributes in the front-matter specify metadata regarding the appropriate element. Any attribute can appear in the front-matter, but some attributes induce special processing:

  * [alias](#alias),
  * [group](#group),
  * [since](#since),
  * [stability](#stability),
  * [visibility](#visibility).

Not every attribute is recognized for every entity. Please refer to [Recognized Attributes](#recognized-attributes) for the list of recognized section by entities.

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

  * modules (in `module-info.h`),
  * the current version (in `version-info.h`),
  * headers,
  * and top-level elements

are broken into sections. The sections provide a base structure for the documentation and some of them have special parsing rules. Each section starts with a second level heading (`##`) with the section name (`## Description` for example). The only exception is the `Title` section which is a single top-level heading (`#`).

Sections recognized by underdocs are as follows:

  * [Title](#title),
  * [Description](#description),
  * [Groups](#groups),
  * [Parameters](#parameters),
  * [Return Value](#return-value),
  * [Error Handling](#error-handling),
  * [Notes](#notes),
  * [Examples](#examples),
  * [See Also](#see-also).

Not every section is recognized for every entity. Please refer to [Recognized Sections](#recognized-sections) for the list of recognized section for entities.

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

## Attributes

### Recognized Attributes

Recognized attributes are the ones with special meaning for underdocs. You may use any attribute anywhere, this is just additional semantics.

The following attributes are recognized for each entity:

  * **module**
    * [since](#since),
    * [stability](#stability),
    * [visibility](#visibility)
  * **the current version**
    * none
  * **header**
    * [since](#since),
    * [stability](#stability),
    * [visibility](#visibility)
  * **top-level elements**
    * [alias](#alias),
    * [group](#group),
    * [since](#since),
    * [stability](#stability),
    * [visibility](#visibility)

### Alias

The `alias` attribute can be used to set the exported name of the symbol it's applied to. This can be best seen through an example. Assume, that you've created an anonymous enum:

~~~~C
/**
 * ## Description
 *
 * Enumeration of truth values.
 */
enum {
    TRUE,
    FALSE
}
~~~~

Because this enum is anonymous

  * underdocs can only display it as `anonymous enum` on the header page,
  * you cannot link to the enum using the `codebase://` namespace.

These issues can be solved by adding the `alias` attribute:

~~~~C
/**
 * ---
 * alias: boolean
 * ---
 * ## Description
 *
 * Enumeration of truth values.
 */
enum {
    TRUE,
    FALSE
}
~~~~

Now underdocs will use `boolean` as the exported name of the enum.

**Note**: The `alias` must be a valid C identifier.

**Note**: Any top-level element can be given an alias, not just anonymous ones.

### Group

The `group` attribute can be used to group top-level elements together, so that they will appear together on the header page.

Let's assume, that you have a type `vec3d_t`, representing a three-dimensional vector:

~~~~C
typedef struct {
    float x, y, z;
} vec3d_t;
~~~~

It is pretty common, to have multiple "overloads" of addition with different types. Then you can create groups for different vector operations to make the header easier to navigate:

~~~~C
/**
 * ---
 * group: Addition
 * ---
 */
void vec3d_addf(vec3d_t *result, const vec3d_t *a, const float b);

/**
 * ---
 * group: Addition
 * ---
 */
void vec3d_addv3(vec3d_t *result, const vec3d_t *a, const vec3d_t *b);
~~~~

A group may include top-level elements of any type.

Groups can be documented using the [Groups](#groups) section.

### Since

The `since` attribute can be used to indicate the first version in which the entity appeared. 

If the [`render.linkTemplates.remoteRepositoryTag`](configuration-reference.md#remoterepositorytag) configuration property is set, then the value of the attribute will be substituted into the passed template.

If a multiversion deployment is performed with `staple`, then the value of the attribute will be used to create a link to the page of the appropriate version.

Example:

~~~~C
/**
 * ---
 * since: v1.0.0
 * ---
 */
typedef struct {
    float x, y, z;
} vec3d_t;
~~~~

**Configuration**: 

  * [`render.linkTemplates.remoteRepositoryTag`](configuration-reference.md#remoterepositorytag)

### Stability

The `stability` attribute can be used to indicate the stability/maturity of an entity.

Stability values are arbitrary (for example: alpha, beta, stable, deprecated) and can be color-coded using the [`render.stabilityColors`](configuration-reference.md#stabilitycolors) configuration property.

Example:

~~~~C
/**
 * ---
 * stability: deprecated
 * ---
 */
typedef struct {
    float x, y, z;
} vec3d_t;
~~~~

**Configuration**: 

  * [`render.stabilityColors`](configuration-reference.md#stabilitycolors)

### Visibility

The `visibility` attribute can be used to control whether an entity appears in the generated documentation.

Visibility values are aribitrary but ordered and can be set using the [`render.visibilityLevels`](configuration-reference.md#visibilitylevels) configuration property.

The visibility level used during the `render` phase can be set using the [`render.visibility`](configuration-reference.md#visibility) configuration property. If the visibility level of an entity is less than the render visibility, then the entity will not be rendered. 

**Note**:

  * If no visibility is set on an entity, then the highest is assumed. 
  * If no render visibility is set, then the highest is assumed. 

For example, let's assume, that you've defined three levels: private, protected and public.

If the render visibility is set to protected, then

  * Entities with private visibility will not be rendered.
  * Entities with protected, public or no visibility will be rendered.

Example:

~~~~C
/**
 * ---
 * visibility: protected
 * ---
 */
typedef struct {
    float x, y, z;
} vec3d_t;
~~~~

**Configuration**: 

  * [`render.visibility`](configuration-reference.md#visibility)
  * [`render.visibilityLevels`](configuration-reference.md#visibilitylevels)

### Other Attributes

Every attribute which is not recognized for a given entity (refer to [Recognized Attributes](#recognized-attributes)) is treated as an "other" attribute with no attached semantics.

By default, other attributes will appear at the bottom of the page rendered for the entity. This behavior can be changed using the [`render.otherAttributes`](configuration-reference.md#otherattributes) configuration property.

**Configuration**: 

  * [`render.otherAttributes`](configuration-reference.md#otherattributes)

## Section

### Recognized Sections

Recognized sections are the ones with special meaning for underdocs. You may use any section anywhere, this is just additional semantics.

The following sections are recognized for each entity:

  * **module**
    * [Title](#title)
    * [Description](#description)
    * [Examples](#examples)
    * [See Also](#see-also)
  * **the current version**
    * [Title](#title)
    * [Description](#description)
  * **header**
    * [Description](#description)
    * [Groups](#groups)
    * [Examples](#examples)
    * [See Also](#see-also)
  * **enum, macro constant, struct, type synonym, union, variable**
    * [Description](#description)
    * [Examples](#examples)
    * [See Also](#see-also)
  * **macro function**
    * [Description](#description)
    * [Parameters](#parameters)
    * [Examples](#examples)
    * [See Also](#see-also)
  * **function**
    * [Description](#description)
    * [Parameters](#parameters)
    * [Return Value](#return-value)
    * [Error Handling](#return-value)
    * [Notes](#return-value)
    * [Examples](#examples)
    * [See Also](#see-also)

### Title

The Title section is only recognized for modules and the current version (`version-info.h` header). The section itself is not really a section, but a single top-level heading (`#`) with arbitrary text.

In the case of modules the Title section contains the name of the module.

Example:

~~~~C
/**
 * # Module Name
 *
 * ## Description
 *
 * Module description.
 */
#pragma once
~~~~

For the current version, the Title section is used as the tag.

Example:

~~~~C
/**
 * # v1.0.0
 *
 * ## Description
 *
 * Version description.
 */
#pragma once
~~~~

### Description

The Description section is a free text section with arbitrary Markdown contents. The first sentence of the section will be used as the *excerpt* (short description) of the entity.

Example:

~~~~C
/**
 * ## Description
 *
 * Multiplies two integers. Will return `0` on overflow.
 */
int muli(const int a, const int b);
~~~~

In this case, the excerpt of the `muli` function will be `Multiplies two integers.`.

### Groups

The Groups section is only recognized for headers. In this context, it is used to document groups introduced by the `group` attribute values in the said header.

Consider the following example:

~~~~C
/**
 * ## Groups
 *
 * ### Addition
 *
 * Addition operation on different types.
 *
 * ### Multiplication
 *
 * Multiplication operation on different types.
 */
#pragma once

/**
 * ---
 * group: Addition
 * ---
 */
int addi(const int a, const int b)

/**
 * ---
 * group: Multiplication
 * ---
 */
int muli(const int a, const int b)
~~~~

Here, two groups are introduced by the `group` attribute values: Addition and Multiplication. By using the same names as third level headings (`###`) in the Groups section, we can create corresponding documentation for these groups.

### Parameters

The Parameters section is recognized for macro functions and ordinary functions. It is used to document the parameters of such entities.

Consider the following example:

~~~~C
/**
 * ## Parameters
 * 
 *   * result
 *     * The result of the scaling as an out parameter. Must be allocated and thus freed by the caller.
 *   * vec
 *     * The vector to scale.
 *   * factor
 *     * The scaling factor.
 */
void vec3d_scalef(vec3d_t *result, const vec3d_t *vec, const float factor);
~~~~

The Parameters section may only contain a single bullet list. Each item in this bullet list is the name of a (macro) function parameter. Under each item, there is another, nested bullet list with a single item. The contents of this item will be used as the parameter documentation.

**Note**: The order of items does not matter, underdocs will use the order in the (macro) function parameter list when rendering.

### Return Value

The Return Value section is only recognized for ordinary functions (macro functions are out of the game). It can be used to document the possible return values of the function.

The section must be of the following form:

~~~~C
/**
 * ## Return Value
 *
 * ### Success
 *
 *   * value
 *     * description
 *   * value
 *     * description
 *   ...
 *
 * ### Error
 *
 *   * value
 *     * description
 *   * value
 *     * description
 *   ...
 */
~~~~

Where the `Error` part is optional.

Consider the following example:

~~~~C
/**
 * ## Return Value
 *
 * ### Success
 *
 *   * A pointer to the newly allocated memory chunk.
 *     * If everything is okay. The chunk will have the size requested.
 *
 * ### Error
 *
 *   * `NULL`
 *     * If the amount of available memory was not enough to fulfill the allocation request.
 */
void *malloc(size_t size);
~~~~

Thus, the Success part can be used to document function return values in case of success. The Success section  may only contain a single bullet list. Each item in this bullet list is a return value. Under each item, there is another, nested bullet list with a single item. The contents of this item will be used as the return value documentation.

In a same fashion, the Error part can be used to document function return values in case of some error or failure.

### Error Handling

The Error Handling section is only recognized for ordinary functions (macro functions are out of the game). It can be used to document how errors arising during the function execution are handled.

The section must be of the following form:

~~~~C
/**
 * ## Error Handling
 *
 *   * action/state
 *     * condition
 *   * action/state
 *     * condition
 *   ...
 */
~~~~

Consider the following example:

~~~~C
/**
 * ## Error Handling
 *
 *   * The [errno](codebase://file.h#errno) variable is set to `ENOENT`.
 *     * If the file does not exist or the privileges are insufficient.
 */
FILE *fopen(char *path);
~~~~

The section may only contain a single bullet list. Each item in this bullet list is an action which is done or a new state of the system. Under each item, there is another, nested bullet list with a single item. The contents of this item will be used as the condition leading to the specified action or state.

### Notes

The Notes section is a free text section with arbitrary Markdown contents.

Example:

~~~~C
/**
 * ## Notes
 *
 * Actually implemented in platform-dependent assembly, so is pretty fast.
 */
int muli(const int a, const int b);
~~~~

### Examples

The Examples section can be used to list example usages of some entity. 

The section must be of the following form:

~~~~C
/**
 * ## Examples
 *
 * ### Example 1
 *
 * Description of Example 1.
 *
 * ~~~~C
 * // Code  for Example 1.
 * ~~~~
 *
 * ~~~~
 * Possible output for Example 1.
 * Optional.
 * ~~~~
 *
 * ### Example 2
 *
 * ...
 */
~~~~

The sections of several smaller sections, each introducing a separate example. Each example starts with a third level heading (`###`) containing the name of the example. It is followed by the description of the example. Next there must be a fenced code block which contains the actual example code. Optionally, another fenced code block can be added with the possible output of the example code.

Consider the following example:

~~~~C
/**
 * ## Examples
 *
 * ### Handling Overflow
 *
 * In this example we showcase how overflow is handled. As stated, this
 * function returns `0` on overflow. This example prints `Overflow occurred` in the
 * case of an overflow.
 *
 * ~~~~C
 * #include <stdio.h>
 * #include "custom_maths.h"
 *
 * int main(int argc, char **argv) {
 *   int a = 87787878787878, b = 7987138927987198789;
 *
 *   int result = muli(a, b);   
 *   if (result == 0 && a != 0 && b != 0) {
 *     printf("Overflow occurred!\n");
 *   }
 *
 *   return 0;
 * }
 * ~~~~
 *
 * ~~~~
 * Overflow occurred!
 * ~~~~
 */
int muli(const int a, const int b);
~~~~

### See Also

The See Also section can be used to refer the reader of the documentation to other resources.

The section must be of the following form:

~~~~C
/**
 * ## See Also
 * 
 *   * Free text or codebase namespaced link.
 *   * Free text or codebase namespaced link.
 *   ...
 */
~~~~

Thus, the section may only contain a single bullet list. Each item in this section is a See Also item. Items consisting only of `codebase` namespaced links are handled specially: they will be substituted by the name and excerpt of the linked entity.

Consider the following example:

~~~~C
/**
 * ## See Also
 *
 *   * codebase://custom_maths.h#mulf
 *   * The [section](link-to-the-section) in Uncle Joe's excellent textbook on multiplication.
 */
int muli(const int a, const int b);
~~~~

### Other Sections

Every section which is not recognized for a given entity (refer to [Recognized Sections](#recognized-section)) is treated as an "other" section with no attached semantics.

By default, other sections will appear before the See Also section on the rendered documentation page of the entity. This behavior can be changed using the [`render.otherSections`](configuration-reference.md#othersections) configuration property.

**Configuration**: 

  * [`render.otherSections`](configuration-reference.md#othersections)

## Links

### Codebase

Codebase namespaced links can be used to create links to:

  * **modules**,
    * `codebase://path/to/the/module`
  * **headers**
    * `codebase://path/to/the/module/header.h`
  * **groups in headers**
    * `codebase://path/to/the/module/header.h#Name of the group`
  * **top-level elements**
    * `codebase://path/to/the/module/header.h#element`
  * **members of enums, structs and unions**
    * `codebase://path/to/the/module/header.h#element.member`
  * **parameters of macro functions and ordinary functions**
    * `codebase://path/to/the/module/header.h#element.parameter`

The path must start from the root of your include directory (set using the [`parse.input.codebaseDirectory`](configuration-reference.md#codebasedirectory) configuration parameter).

If an element has an alias set using the `alias` attribute, then the alias can be used.

**Configuration**: 

  * [`parse.input.codebaseDirectory`](configuration-reference.md#codebasedirectory)

### Pages

Pages namespaced links can be used to create links to Markdown documentation pages in the configured pages directory:

  * `pages://path/to/the/page.md`

The path must start from the root of your pages directory (set using the [`render.input.pagesDirectory`](configuration-reference.md#pagesdirectory) configuration parameter).

**Configuration**: 

  * [`render.input.pagesDirectory`](configuration-reference.md#pagesdirectory)

### Resources

Resources namespaced links can be used to create links to static resources in the configured resources directory:

  * `resources://path/to/some/image.png`

The path must start from the root of your resources directory (set using the * [`render.input.resourcesDirectory`](configuration-reference.md#resourcesdirectory) configuration parameter).

**Configuration**: 

  * [`render.input.resourcesDirectory`](configuration-reference.md#resourcesdirectory)

### Versions

Versions namespaced links can be used to create links to version pages. The link should contain the name of the version:

  * `versions://v1.0.0`
