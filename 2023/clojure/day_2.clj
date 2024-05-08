(ns day-2
  (:require [clojure.string :as s]
            [clojure.java.io :as io]))

(def ^:private colour->limit {"red" 12, "green" 13, "blue" 14})

(def ^:private part1-sample
  "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")

(def ^:private part2-sample
  "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")

(defn part1
  [xs]
  (let [invalid? (fn [[_ n c]]
                   (> (read-string n) (get colour->limit c)))]
    (reduce (fn [acc x]
              (let [[_ id] (re-find #"Game (\d+)" x)]
                (if (some invalid? (re-seq #"(\d+) (blue|red|green)" x))
                  acc
                  (+ acc (read-string id)))))
            0
            xs)))

(defn part2
  [xs]
  (apply +
         (for [x xs
               :let [mins
                     (reduce
                      (fn mins
                        [c->m [_ n c]]
                        (let [m (get c->m c)
                              v (read-string n)]
                          (if (> v m)
                            (assoc c->m c v)
                            c->m)))
                      {"red" 0, "green" 0, "blue" 0}
                      (re-seq #"(\d+) (blue|red|green)" x))

                     {:strs [red green blue]} mins]]
           (* red green blue))))

(comment
  (= 2268
     (-> (io/resource "day-2-1.txt")
         slurp
         s/split-lines
         part1)))

(comment
  (= 63542
     (-> (io/resource "day-2-2.txt")
         slurp
         s/split-lines
         part2)))

