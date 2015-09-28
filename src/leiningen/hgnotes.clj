(ns leiningen.hgnotes
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(defn ensure-dir! [dir-name]
  (let [dir (clojure.java.io/file dir-name)]
    (if (.exists dir)
      (.isDirectory dir)
      (.mkdir dir))))

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

(def filename "hgnotes.edn")

(defn hgnotes [project & args]
  (let [target-dir-name "resources"
        separator (java.io.File/separator)]
    (if (ensure-dir! target-dir-name)
      (spit (str target-dir-name separator filename) (log (latest-revision)))
      (throw (Exception. (str target-dir-name " is not a directory!"))))))
