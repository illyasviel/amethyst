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
            [clojure.tools.namespace.parse :as parse]
            [clojure.tools.reader.reader-types :as readers]
            [refactor-nrepl.config :as config]
            [refactor-nrepl.core :refer [read-ns-form-with-meta]]
            [refactor-nrepl.ns.clean-ns :refer [clean-ns]]
            [refactor-nrepl.ns.pprint :refer [pprint-ns]])
  (:import (clojure.lang PersistentVector)
           (java.io FileReader PushbackReader)))

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

(defn- read-ns-form [^String path]
  (with-open [file-reader (FileReader. path)]
    (let [indexing-reader (readers/indexing-push-back-reader (PushbackReader. file-reader))]
      {:ns-form (parse/read-ns-decl indexing-reader)
       :line-number (readers/get-line-number indexing-reader)
       :column-number (readers/get-column-number indexing-reader)})))

(defn- get-replace-offset [^String filepath ^String content]
  (let [{:keys [ns-form line-number column-number]} (read-ns-form filepath)
        end-offset (->> content
                        (str/split-lines)
                        (take (dec line-number))
                        (map count)
                        (map inc)
                        (reduce +)
                        (+ (dec column-number)))
        before-ns-content (subs content 0 end-offset)
        ns-regex (-> (str "\\(\\s*ns\\s+" (name (second ns-form)))
                     (str/replace "." "\\.")
                     (re-pattern))
        [ns-declare, :as matched-seq] (re-seq ns-regex before-ns-content)]
    (when (= 1 (count matched-seq))
      [(long (str/index-of content ns-declare)) end-offset])))

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
  (get-replace-offset filepath content))
