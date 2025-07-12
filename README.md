# Ture - Turing Machine
> An Antlr4 interpreter for a formal description of Turing Machine transition rules.

## Usage

The project uses Maven for build and dependency management. You can create a shaded JAR containing all Antlr4 dependencies with `mvn package`.

You can run a script by passing it to the compiled program:

```bash
java -jar ./target/Ture.jar LastFirst.ture
```

## Inspecting grammars

The `Turing.g4` grammar has the quality of life features used by the interpreter: [comments](#comments) and the [INIT statement](#init-declaration) whereas `Turing_RulesOnly.g4` parses only the transition rules.

I highly recommend using [Terence Parr's Antlr4 plugin for IntelliJ IDEA](https://plugins.jetbrains.com/plugin/7358-antlr-v4-grammar-plugin) to use the grammar as it has a lot of useful features and handles multibyte characters (such as the empty symbol ⬚) more reliably than using `grun` or the VScode extension.

Open the `Turing.g4` file in IntelliJ IDEA, right click the `program` parse rule and select `Test parser rule Program` to test out the grammar.

Eventually there will be a functioning Turing Machine simulator that uses the grammar to parse the transition rules and simulate the machine.

## Syntax

See `LastFirst.ture` for an example program.

### Rules

Rules are defined in the following format:

```
(state, letter) -> (state, letter, direction)
```

Where `state` is the name of the state, `letter` is the symbol on the tape at the current position, `direction` is either `left` or `right` and `state` is the name of the next state.

When the pointer is over the given letter whilst in the given state on the LHS, it overwrites the letter with the new letter, changes to the new state, and moves the pointer in the direction specified on the RHS.

If no applicable rule is found, the machine halts.

The empty letter is represented by the symbol `⬚`.

The state names must not contain any of `,()⬚` or whitespace and letters must be a single character that is not `,()` or whitespace. It is convention to start state names with `q` but this is not enforced.

It is recommended to add a newline at the end of each rule, but this is optional. Whitespace is also allowed before and after rules.

Whitespace is permitted between tokens of the rule, but not within tokens such as state names or letters.

### Init declaration

The initial state is declared at the start of the file, before any rules:

```
INIT state
```

Where `state` is the name of the initial state.

### Comments

Comments start with `%` and can be placed anywhere in the program. Comments run from the `%` to the end of the line.

You can escape with `\%`.
