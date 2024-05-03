(ns day-1
  (:require [clojure.string :as s]
            [clojure.java.io :as io]))

(def data
  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet")

(defn input->sum
  [s]
  (let [xs (s/split-lines s)]
    (reduce (fn [acc x]
              (let [first-digit (->> x
                                     (re-find #"^\D*(\d)")
                                     second)
                    last-digit (->> x
                                    (re-find #"(\d)\D*$")
                                    second)]
                (+ acc (read-string (str first-digit last-digit)))))
            0
            xs)))

(comment
  (input->sum (-> "day-1.txt"
                  io/resource
                  slurp)))
