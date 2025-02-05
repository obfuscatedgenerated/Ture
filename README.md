# Turing Machine
> An Antlr4 grammar for a formal description of Turing Machine transition rules.

## Usage

I highly recommend using [Terence Parr's Antlr4 plugin for IntelliJ IDEA](https://plugins.jetbrains.com/plugin/7358-antlr-v4-grammar-plugin) to use the grammar as it has a lot of useful features and handles multibyte characters (such as the empty symbol ⬚) more reliably than using `grun` or the VScode extension.

Open the `Turing.g4` file in IntelliJ IDEA, right click the `program` parse rule and select `Test parser rule Program` to test out the grammar.

Eventually there will be a functioning Turing Machine simulator that uses the grammar to parse the transition rules and simulate the machine.

## Example programs

### Replace last letter of tape with the first letter

```
Store the first letter in the word as a state
(qInit, a) -> (qA, a, right)
(qInit, b) -> (qB, b, right)

Move to the end of the tape
(qA, a) -> (qA, a, right)
(qA, b) -> (qA, b, right)
(qB, a) -> (qB, a, right)
(qB, b) -> (qB, b, right)

Backtrack once when we reach the end of the tape
(qA, ⬚) -> (qEndA, ⬚, left)
(qB, ⬚) -> (qEndB, ⬚, left)

Replace the last letter with the first letter and enter the trap state
(qEndA, a) -> (qHalt, a, right)
(qEndA, b) -> (qHalt, a, right)
(qEndB, a) -> (qHalt, b, right)
(qEndB, b) -> (qHalt, b, right)
```
