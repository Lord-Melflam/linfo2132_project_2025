# LINFO2132 Compiler Project

This project involves implementing our own compiler for an imperative language from scratch. The project is structured around several phases corresponding to the components of the compiler pipeline.

## Project Overview

The goal of our project is to create a compiler featuring the following phases:

1. **Lexer**  
   The lexer reads the input source file and converts it into a sequence of tokens. It handles:
   - Comments starting with a specific symbol.
   - Identifiers that may include letters, digits, and underscores (with the first character being a letter or underscore).
   - Reserved keywords.
   - Numerical values for integers and floats.
   - Strings enclosed in double quotation marks with support for escaped characters.

2. **Parser**  
   This component converts the token sequence into an Abstract Syntax Tree (AST). Our implementation uses recursive-descent parsing with special handling for parts of the grammar that require it. Syntax errors are reported if the token sequence does not match the expected grammar.

3. **Semantic Analysis**  
   After obtaining a syntactically correct AST, the semantic analysis phase performs static type checking and ensures correct usage throughout the program. This phase reports any semantic errors, such as type mismatches, in our strongly typed language.

4. **Code Generation**  
   In the final phase, the compiler translates our semantically verified AST into executable code. Detailed instructions and guidelines for this phase are available in our project documentation and presentation slides.

## Project Structure

The repository is organized as a Gradle project with the following directory structure:

├── build.gradle.kts <br>
├── gradle <br>
├── gradlew <br>
├── gradlew.bat <br>
├── settings.gradle.kts <br>
├── src <br>
└── test <br>


- **src**: Contains the main source code.
- **test**: Contains tests for verifying the various phases.

Gradle wrapper scripts and configuration files are provided for building and running the project.

## Getting Started

### Prerequisites

- JDK (version 8 or higher)
- Gradle (or use the Gradle wrapper provided)

### Building the Project

To compile the project, run:

```bash
./gradlew build

