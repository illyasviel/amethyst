;
; Copyright (c) 2019 the original author or authors.
; Licensed under the Eclipse Public License 2.0
; which is available at http://www.eclipse.org/legal/epl-2.0
;

(ns org.illyasviel.amethyst.CleanNsUtils
  (:gen-class :methods [^:static [initCaller [] void]
                        ^:static [getCleanedNsByFilePath [String] String]
                        ^:static [getReplaceOffset [String String] clojure.lang.PersistentVector]])
  (:require [clojure.string :as str]
            [refactor-nrepl.config :as config]
            [refactor-nrepl.core :refer [read-ns-form-with-meta]]
            [refactor-nrepl.ns.clean-ns :refer [clean-ns]]
            [refactor-nrepl.ns.pprint :refer [pprint-ns]])
  (:import (clojure.lang PersistentVector)))

(defn- import-form? [form]
  (and (seq? form)
       (= (first form) :import)))

(defn- cook-import
  "cursive import style"
  [[_ & imports]]
  (->> imports
       (map (fn [single-import]
              (if (vector? single-import)
                (seq single-import)
                (let [import-str (str single-import)
                      split-import (str/split import-str #"\.")]
                  (list
                    (->> (drop-last split-import)
                         (str/join ".")
                         (symbol))
                    (symbol (last split-import)))))))
       (apply list :import)))

(defn- clean-ns*
  "return nilable"
  [filepath]
  (some->> (try
             (config/with-config
               {:prune-ns-form false
                :prefix-rewriting false}
               (clean-ns {:path filepath}))
             (catch Exception _))
           (map (fn [form]
                  (if (import-form? form)
                    (cook-import form)
                    form)))
           (pprint-ns)
           (str/trim)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Static Methods
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -initCaller []
  (comment "do nothing"))

(defn ^String -getCleanedNsByFilePath
  "nilable"
  [^String filepath]
  (clean-ns* filepath))

(defn ^PersistentVector -getReplaceOffset
  "return [^Long? start-offset, ^Long? end-offset]"
  [^String filepath ^String content]
  (let [raw-ns-str (-> (read-ns-form-with-meta filepath)
                       (str)
                       (str/escape (fn [ch]
                                     (case ch
                                       \space "\\s+", \. "\\.", \( "\\(", \) "\\)", \[ "\\[", \] "\\]"
                                       (if-let [replacement (re-find #"[a-zA-Z0-9]" (str ch))]
                                         replacement
                                         \.))))
                       (re-pattern)
                       (re-find content))
        start-offset (when (some? raw-ns-str) (long (str/index-of content raw-ns-str)))
        end-offset (when (some? raw-ns-str) (+ start-offset (count raw-ns-str)))]
    [start-offset end-offset]))
