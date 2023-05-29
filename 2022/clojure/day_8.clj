(ns day-8
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [portal.api :as portal]))

(defn visible
  [xs idxs]
  (loop [x (first idxs)
         rem (rest idxs)
         max Float/NEGATIVE_INFINITY
         visible-idxs #{}]
    (if x
      (let [v (nth xs x)]
        (if (> v max)
          (recur (first rem)
                 (rest rem)
                 v
                 (conj visible-idxs x))
          (recur (first rem)
                 (rest rem)
                 max
                 visible-idxs)))
      visible-idxs)))

(defn- input->rows [data]
  (vec (for [line (str/split-lines data)
             :let [chars (str/split line #"")]]
         (mapv edn/read-string chars))))

(defn- rows->visible [rows x-idxs]
  (for [y (range (count rows))
        :let [visible-idxs (visible (nth rows y) x-idxs)]]
    (mapv #(vector % y) visible-idxs)))

(defn- cols->visible [rows y-idxs]
  (for [x (range (count (first rows)))
        :let [visible-idxs (visible (mapv #(get-in rows [% x]) (range (count rows))) y-idxs)]]
    (mapv #(vector x %) visible-idxs)))

(defn- scenic-score
  [[x y] forest]
  (let [v (get-in forest [y x])
        col-vals (map #(get % x) forest)
        row-vals (get forest y)
        right-of-v (->> row-vals
                        (drop (inc x)))
        left-of-v (->> row-vals
                       (take x)
                       reverse)
        above-v (->> col-vals
                     (take y)
                     reverse)
        below-v (drop (inc y) col-vals)
        score (fn [ts]
                (reduce (fn [acc t]
                          (if (>= t v)
                            (reduced (inc acc))
                            (inc acc)))
                        0
                        ts))]
    (apply * (map score [right-of-v left-of-v above-v below-v]))))

(comment
  (let [rows (input->rows (slurp (io/resource "day-8.txt")))
        part-2-answer (apply max (for [y (range (count rows))
                                       x (range (count (first rows)))]
                                   (scenic-score [x y] rows)))]
    (assert (= 536625 part-2-answer) "part 2")))

(comment
  (let [data (slurp (io/resource "day-8.txt"))
        rows (input->rows data)
        from-left (rows->visible rows (range (count (first rows))))
        from-right (rows->visible rows (range (dec (count (first rows))) -1 -1))
        from-top (cols->visible rows (range (count rows)))
        from-bottom (cols->visible rows (range (dec (count rows)) -1 -1))

        part-1-count (count (reduce (fn [acc xs]
                                      (into acc (apply concat xs)))
                                    #{}
                                    [from-left from-right from-top from-bottom]))]
    (assert (= 1679 part-1-count) "part 1")))
