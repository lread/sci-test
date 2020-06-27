# sci-test

## What

You want to verify that your Clojure libary works when compiled with GraalVM's native-image.

You run native-image on your library and your tests successfully on your development computer, but
the native-image consumes more RAM than is available on the free tier of the CI service you want
to use.

## How

I don't know yet.

Baby steps.

One of @borkdude's ideas was to compile your library and sci and clojure.test to produce
a native image. This native-image would take, as input, your library test suite.

## Status

Stolen enough code from rewrite-cljc and babashka to:

- evaluate the following string `"(require '[clojure.test :as t]) (t/is (= 1 2))"`

Next up:

- load and run a test from a file
- load and run tests from files
- include a dummy library in native image to test against
- update tests to hit dummy library

Later:

- incorporate test.check
- incorporate my target library: rewrite-cljc
