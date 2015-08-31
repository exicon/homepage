# homepage

A Hoplon project to implement the custom part of Exicon's public homepage.

## Dependencies

- [Java 1.8][1]
- [boot][2]

## Usage

1. Start the auto-compiler. In a terminal:

    ```bash
    $ cp config.example.sh config.sh
    $ source config.sh; boot dev
    ```

2. Go to [http://localhost:3103][3] in your browser.

Bootup takes around 60seconds *after* the dependencies are downloaded.
It's expected and will be improved, but the main reason for it is
this homepage is a multipage application, while Hoplon was primarily
meant to be used for creating Single Page Applications.

For production use we do precompilation which is part of the `prod` task.
The result will be in the `target/` directory, from where it can be served
with any webserver capable of serving static files.

    ```bash
    $ source config.sh; boot prod
    $ (cd target; python -mSimpleHTTPServer 3103)
    ```

## License

Copyright © 2014, **Excion Ltd**. All rights reserved.

The use and distribution terms for this software are covered by the [Eclipse
Public License 1.0](http://opensource.org/licenses/eclipse-1.0.php). By using
this software in any fashion, you are agreeing to be bound by the terms of
this license. You must not remove this notice, or any other, from this software.

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[2]: https://github.com/boot-clj/boot#install
[3]: http://localhost:3103
