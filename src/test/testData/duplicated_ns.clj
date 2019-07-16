;
; Copyright (c) 2019 the original author or authors.
; Licensed under the Eclipse Public License 2.0
; which is available at http://www.eclipse.org/legal/epl-2.0
;

(ns duplicated-ns
  "doc string"
  (:require [clojure.tools.logging :refer :all]
            [clojure.core.async :as async :refer [>! <! >!! <!!]]))

(ns duplicated-ns
  "doc string"
  (:require [clojure.tools.logging :refer :all]
            [clojure.core.async :as async :refer [>! <! >!! <!!]]))

(defn some-func []
  (comment "do something"))
