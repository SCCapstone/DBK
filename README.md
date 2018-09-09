# DBK Apps

Android and iOS Applications supporting DBK Drymatic.

## Installing / Getting started

> *Pending design decisions*

## Developing

> *Pending design decisions*

### Deploying / Publishing

> *Pending design decisions*

## Features

### Complete

> *There isn't anything here yet*

### Road Map

- iOS application
- Android application
- Bluetooth integration with DBK hardware

## Contributing

### Policies

#### Don't work yourself to death.

We can't work harder in an unexpected crunch if we're already regularly putting in as many hours as we possibly can.

#### Don't commit code directly to master.

Our goal is "master always passes build". Obviously that won't be the case until we actually have something in `master` to compile, and have set up TravisCI to perform automated testing.

When the repository is set up or we begin writing code (whichever comes first), we will lock the master branch so that at least one person must review the changes before they may be merged to `master`. If changes pass review but cause compilation to fail or cause a test that previously passed to fail, they must be fixed somehow. Ideally this should be checked by building locally before submitting a pull request.

Non-code changes (i.e., updating English text files) may be done while it remains possible, but will not be possible after `master` is locked.

#### Follow the styleguide.

We don't know what styleguides we're using yet because we don't know what *languages* we're using. We do know that we are using Java for the Android application, and in that case we will use the [Google Java Styleguide](https://google.github.io/styleguide/javaguide.html). Other styleguide decisions have yet to be made by the group.
