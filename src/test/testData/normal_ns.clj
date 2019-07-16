;
; Copyright (c) 2019 the original author or authors.
; Licensed under the Eclipse Public License 2.0
; which is available at http://www.eclipse.org/legal/epl-2.0
;

(ns normal-ns
  "Some doc string"
  (:import
    (org.apache.commons.io FileUtils)
    (java.nio.file Files LinkOption))
  (:use [riemann.time :only [unix-time]]
        [riemann.common :only [deprecated localhost event]]
        clojure.tools.logging
        [riemann.instrumentation :only [Instrumented]])
  (:require
    [io.pedestal.http.sse :as http.sse]
    [io.pedestal.http :as http]
    [clojure.core.async :as async :refer [>! <! >!! <!!]]
    [com.example.my-application.server.sse :as server.sse]
    [com.example.my-application.base]
    [ring.util.response :as response])
  (:refer-clojure :exclude [send set!]))

(defn some-func []
  (comment "do something"))
