(ns gedcom.core-test
  (:require [clojure.test :refer :all]
            [gedcom.core :refer :all]))

(deftest parser
  (testing "gedcom-line"
    (are [input expected] (= expected (into {} (gedcom-line input)))
      "0 FOO"           {:level 0 :label nil :tag "FOO" :suffix nil :data nil}
      "0 @I1@ FOO data" {:level 0 :label "@I1@" :tag "FOO" :suffix nil :data "data"}
      "0 FOO data"      {:level 0 :label nil :tag "FOO" :suffix nil :data "data"}
      "0 FOO @I1@"      {:level 0 :label nil :tag "FOO" :suffix nil :data "@I1@"}
      "0 @I1@ FOO__EN data" {:level 0 :label "@I1@" :tag "FOO" :suffix "EN" :data "data"}))

  (testing "gedcom-line-seq"
    (are [input expected] (= expected (->> input
                                           (map gedcom-line)
                                           gedcom-line-seq
                                           first
                                           (into {})))
         ["0 FOO one" "1 CONT two" "1 CONC  three"] {:level 0 :label nil :tag "FOO" :suffix nil :data "one\ntwo three"})))
