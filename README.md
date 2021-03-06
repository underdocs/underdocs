<div align="center">
  <a href="https://github.com/underdocs/underdocs">
    <img alt="underdocs" src="docs/img/logo.png" width="400" style="padding-top:20px">
  </a>
</div>

<h3 align="center">
  Modern documentation generator for C.
</h3>

<div align="center">
  <a href="https://github.com/underdocs/underdocs/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/underdocs/underdocs" alt="underdocs uses the Apache License 2.0.">
  </a>
  <a href="https://github.com/underdocs/underdocs/actions?query=workflow%3A%22Build+Master%22">
    <img src="https://github.com/underdocs/underdocs/workflows/Build%20Master/badge.svg" alt="Build Master status.">
  </a>
  <a href="https://discord.gg/YKyVhMg">
    <img src="https://img.shields.io/badge/discord-join-7289DA.svg?logo=discord&longCache=true&style=flat" alt="Join our Discord Server!">
  </a>
</div>


<h3 align="center">
  <a href="docs/quickstart.md">Quickstart</a>
  <span> · </span>
  <a href="examples">Example Projects</a>
  <span> · </span>
  <a href="docs/README.md">Documentation</a>
</h3>

---

> **IMPORTANT**: This README and the documentation reflect what we *want* and not what is *available*. Please check the [2020 Summer Intership](https://github.com/underdocs/underdocs/projects/1) project board for the current progress.  

underdocs is a modern, performant documentation generator for C. Notable features include the following:

  * **Only C, No C++.** Is this even a feature? In our opinion, absolutely. underdocs is not another C++ documentation generator with accidental support for C. We only support C18 and do not plan to ever add support for C++.
  * **Inspired by CppReference.** If you know [CppReference](https://en.cppreference.com/w/c/string/byte/memcpy), then you'll feel right at home. The documentation format and the generated sites are heavily inspired by CppReference.
  * **Modern, performant HTML output with support for custom styling.** With optional build-time pre-rendering (needs [GraalVM](https://www.graalvm.org/) to be enabled), underdocs generates fast and stylish HTML output with support for customization. Pre-rendering allows us to ship minimal client-side JavaScript to keep your site as fast as possible.
  * **Markdown-based documentation format.** underdocs comes with its own doc comment format, based on Markdown. You are free to use arbitrary Markdown formatting in your docs, including support for images, code snippets with syntax highlighting (via [Prism](https://prismjs.com/)) and mathematical expressions (via [KaTeX](https://katex.org/)).
  * **On-Site Search.** A huge project may contain thousands of headers, structs, functions and such. underdocs makes it easy to navigate such projects by providing on-site search capabilities using a precomputed index.
  * **Multiversion Sites.** If you support multiple versions of your project simultaneously, then the need naturally arises to host the documentation of each supported version. underdocs makes this a breeze with out of the box support for such scenarios.

In no particular order, we also have:

  * Out of the box night mode.
  * Client-side preprocessor conditional evaluation.
  * Remote repository link integration (for code files and tags).
  * Mobile-ready output.
  * Module and file level commitlogs between versions.
  * Easy-to-extend architecture.

Check out the [Documentation](a) for detailed information on these features.

## Table of Contents

  * [Up and Running](#up-and-running)
  * [Learning underdocs](#learning-underdocs)
  * [Contribution Guidelines](#contribution-guidelines)
  * [License](#license)

## Up and Running

For the impatient, we offer a "quicker than [Quickstart](docs/quickstart.md) guide" here.

  0. **Ensure some JRE.**

     Our only dependency is the Java Runtime Environment (version 11 or above). If you want to enjoy the complete underdocs experience with pre-rendering, then make sure to use [GraalVM](https://www.graalvm.org/). An easy solution to obtain GraalVM is [jabba](https://github.com/shyiko/jabba) or [jdk-via-jabba](https://github.com/battila7/jdk-via-jabba) if you're on GitHub Actions.

  1. **Initialize underdocs.**
     
     Move into your project's directory:

     ~~~~shell
     cd my-c-project
     ~~~~

     Download underdocs and create a new configuration file:

     ~~~~shell
     wget -q https://underdocs.dev/underdocs.jar
     java -jar underdocs init
     ~~~~
  
  2. **Parse and render the codebase.**

     Generating documentation with underdocs is a two-step process. First you parse the codebase into a JSON file, then render it to HTML.

     ~~~~shell
     java -jar underdocs.jar parse
     java -jar underdocs.jar render
     ~~~~
  
  3. **Visit the documentation site.**

     Fire up an HTTP-server (for example [http-party/http-server](https://github.com/http-party/http-server)) serving the output directory (set with the `init` command) and check out your new documentation site!

If you did not use the underdocs documentation style throughout your headers, then the generated site will look pretty boring. Make sure to check the next section, [Learning underdocs](#learning-underdocs) on how to get the most out of underdocs!

## Learning underdocs

The full documentation for underdocs lives in the [docs](docs) folder. The following resources might be of most interest:

  * **[Quickstart Guide.](docs/quickstart.md)** A few-minute introduction to the underdocs flow.
  * **[The underdocs Format.](docs/format.md)** An in-depth description of the underdocs doc comment style and its capabilities.
  * **[Configuration Reference.](docs/configuration-reference.md)** Detailed description of the available configuration options.
  * **[Example Projects.](examples)** Multiple example projects showcasing different features of underdocs.

## Contribution Guidelines

Contributions are always welcome in underdocs! Take a look at the [Contributing Guide](CONTRIBUTING.md) for more information (including setup and development related stuff).

## License

Licensed under the [Apache License 2.0](LICENSE.md).
