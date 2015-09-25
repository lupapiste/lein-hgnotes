(ns leiningen.hgnotes
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(defn split [line] (rest (re-find #"^(.*?);(.*?);(.*?);(.*)" line)))

(defn parse [row] (zipmap [:node :date :author :desc] row))

(defn log [revision]
  (let [rrange (str (or revision "") ":tip")
        result (sh "hg" "log" "--template" "{node|short};{date|isodatesec};{author|user};{desc|strip|firstline}\n" "-b" "develop" "-r" rrange)
        output (:out result)
        lines  (s/split output #"\n")]
    (->> lines (map split) (map parse) reverse)))

(defn latest-revision []
  (let [output   (:out (sh "tail" "-1" ".hgtags"))
        revision (last (re-find #" (.*)\n" output))]
    revision))

(defn hgnotes [project & args]
  (spit "./resources/hgnotes.edn" (log (latest-revision))))
