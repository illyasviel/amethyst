# Amethyst

[![Build Status](https://travis-ci.org/illyasviel/amethyst.svg?branch=master)](https://travis-ci.org/illyasviel/amethyst)
[![License](https://img.shields.io/github/license/illyasviel/amethyst.svg?maxAge=86400)](./LICENSE)
[![Version](https://img.shields.io/jetbrains/plugin/v/12731.svg)](https://plugins.jetbrains.com/plugin/12731-amethyst)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/12731.svg)](https://plugins.jetbrains.com/plugin/12731-amethyst)

Clean Clojure Namespace Declarations With An Opinionated Style Guide.

## Feature

This plugin is a port of the [`refactor-nrepl`](https://github.com/clojure-emacs/refactor-nrepl)'s `clean-ns` operation.

- Eliminate `:use` clauses in favor of `refer` `:all`.
- Sort required libraries, imports and vectors of referred symbols.
- Remove any duplication in the `:require` and `:import` form.

## Install

<kbd>Preferences(Settings)</kbd> ->
<kbd>Plugins</kbd> ->
<kbd>Marketplace</kbd> -> 
<kbd>Search "Amethyst"</kbd> -> 
<kbd>Install Plugin</kbd>

## Usage

- Right click mouse -> clean ns

![editor-right-click](./.github/editor-right-click.gif)

- <kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>A</kbd> -> clean ns

![search-action](./.github/search-action.gif)
