(ns day-1
  (:require [clojure.string :as s]
            [clojure.java.io :as io]))

(def part1-sample
  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet")

(def part2-sample
  "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen")

(defn part1
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
  (part1 (-> "day-1.txt"
                  io/resource
                  slurp)))

(def ^:private word->digit {"one" 1
                            "two" 2
                            "three" 3
                            "four" 4
                            "five" 5
                            "six" 6
                            "seven" 7
                            "eight" 8
                            "nine" 9})

(def ^:private words (keys word->digit))

(def ^:private word-re (re-pattern (str "^(" (s/join "|" words) ")")))

(comment
  (re-find word-re "onetoothree"))

(defn ^:private first-digit-in-range
  [s idxs]
  (println "processing" s)
  (loop [i (first idxs)
         rem (rest idxs)]
    (let [s' (subs s i)]
      (when (seq s')
        (let [digit-match (re-find #"^[0-9]" s')
              word-match (re-find word-re s')]
          (cond
            digit-match
            (read-string digit-match)

            word-match
            (get word->digit (first word-match))

            :else
            (recur (first rem) (rest rem))))))))

(comment
  (let [s "eight2threefour"]
    (first-digit-in-range s (range (dec (count s)) -1 -1))))

(defn part2
  [s]
  (let [xs (s/split-lines s)]
    (reduce (fn [acc x]
              (let [n (count x)
                    first-digit (first-digit-in-range x (range n))
                    last-digit (first-digit-in-range x (range (dec n) -1 -1))]
                (+ acc (read-string (str first-digit last-digit)))))
            0
            xs)))

(comment
  (part2 
   "4skbhsbtqc"
))

(comment
  (part2 (-> "day-1-2.txt"
             io/resource
             slurp
             s/trim)))
