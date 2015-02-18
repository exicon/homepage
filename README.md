# homepage

A Hoplon project to implement the custom part of Exicon's public homepage.

## Dependencies

- Java 1.7+ in theory but in practice, on a Mac it needs [Java 1.8][1]
  otherwise it throws an out of memory error because of some bug
- [boot][2] for development
- [leiningen][3] for production compilation

## Usage

1. Start the auto-compiler. In a terminal:

    ```bash
    $ boot development
    ```

2. Go to [http://localhost:3003][4] in your browser.

## License

Copyright © 2014, **Excion Ltd**. All rights reserved.

The use and distribution terms for this software are covered by the [Eclipse
Public License 1.0](http://opensource.org/licenses/eclipse-1.0.php). By using
this software in any fashion, you are agreeing to be bound by the terms of
this license. You must not remove this notice, or any other, from this software.

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[2]: https://github.com/tailrecursion/boot
[3]: https://github.com/technomancy/leiningen
[4]: http://localhost:3003
