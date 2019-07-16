;
; Copyright (c) 2019 the original author or authors.
; Licensed under the Eclipse Public License 2.0
; which is available at http://www.eclipse.org/legal/epl-2.0
;

(ns normal-ns
  "Some doc string"
  (:refer-clojure :exclude [send set!])
  (:require [clojure.core.async :as async :refer [<! <!! >! >!!]]
            [clojure.tools.logging :refer :all]
            com.example.my-application.base
            [com.example.my-application.server.sse :as server.sse]
            [io.pedestal.http :as http]
            [io.pedestal.http.sse :as http.sse]
            [riemann.common :refer :all]
            [riemann.instrumentation :refer :all]
            [riemann.time :refer :all]
            [ring.util.response :as response])
  (:import (java.nio.file Files LinkOption)
           (org.apache.commons.io FileUtils)))

(defn some-func []
  (comment "do something"))
