# lein-hgnotes

A Leiningen plugin that writes mercurial commit messages to ./resources/hgnotes.edn.
Reads messages from the latest tagged revision to the tip revision in `develop` branch.

## Usage

[![Clojars Project](http://clojars.org/lupapiste/lein-hgnotes/latest-version.svg)](http://clojars.org/lupapiste/lein-hgnotes)

Put lupapiste/lein-hgnotes into the `:plugins` vector of your project.clj.

Run `lein hgnotes` before packaging your Clojure software.

You can read the edn file like this:
```clojure
(def hgnotes (read-string (slurp (clojure.java.io/resource "hgnotes.edn"))))
```
The data is a sequence of maps, that contain
* `:node` - short commit ID
* `:desc` - the commit message
* `:date` as string in ISO format
* `:author`.

## Requirements

`hg` and `tail` commands in PATH.

## License

Copyright © 2013-2015 [Solita Oy](http://www.solita.fi/)

Distributed under the Eclipse Public License, the same as Clojure.
