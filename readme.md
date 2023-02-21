# IMP

the `i` in `for i`
object scope: a field in an object whose context we are in

lexical scope: a variable that was declared in this scope

## Blocks

A block is syntactically any region enclosed between `{` and `}`.
Every block pushes its own frame on the stack

| Block                        | Frame     | Return            |
|------------------------------|-----------|-------------------|
| `If`::`ifTrue`/`ifFalse`     | Empty     | Can if parent can |
| `While`/`Repeat`::`block`    | Empty     | Can if parent can |
| `For`::`block`               | i         | Can if parent can |
| `FunctionDefinition`::`body` | Arguments | Must              |

They all push their own frame

They all can have

## TODO

- [ ] `break`/`continue`
- [ ] `switch`/`case`, `when`
- [ ] Exception handling: `try`, `except`, `catch`, `finally`
- [ ] Pythonic Elses: `for`/`else`, `try`/`else`

