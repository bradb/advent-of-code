(ns day-9
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(declare up down right left)

(def ^:private direction->fn
  {"R" #'right
   "U" #'up
   "D" #'down
   "L" #'left})

(defn- up
  ([[x y]] (up [x y] 1))
  ([[x y] n] [x (+ y n)]))

(defn- down
  ([[x y]]
   (down [x y] 1))
  ([[x y] n]
   (up [x y] (- n))))

(defn- right
  ([[x y]]
   (right [x y] 1))
  ([[x y] n] [(+ x n) y]))

(defn- left
  ([[x y]]
   (left [x y] 1))
  ([[x y] n]
   (right [x y] (- n))))

(defn- touching?
  [[x1 y1 :as p1] [x2 y2 :as p2]]
  (let [adjacent? (->> [up down left right (comp left up) (comp right up) (comp left down) (comp right down)]
                       (map #(% p1))
                       (into #{}))]
    (or (= p1 p2)
        (adjacent? p2))))

(defn- move
  [p & directions]
  (assert (even? (count directions)))
  (reduce (fn move*
            [new-p [f n]]
            (f new-p n))
          p
          (partition-all 2 directions)))

(defn- next-tail-pos [[hx hy :as head-pos] [tx ty :as tail-pos]]
  (let [delta-x (- hx tx)
        delta-y (- hy ty)]
    (cond
      (touching? head-pos tail-pos)
      tail-pos

      (and (zero? delta-x) (= -2 delta-y))
      (move tail-pos down 1)

      (and (zero? delta-x) (= 2 delta-y))
      (move tail-pos up 1)

      (and (zero? delta-y) (= -2 delta-x))
      (move tail-pos left 1)

      (and (zero? delta-y) (= 2 delta-x))
      (move tail-pos right 1)

      (and (pos? delta-x) (pos? delta-y))
      (move tail-pos (comp up right) 1)

      (and (neg? delta-x) (pos? delta-y))
      (move tail-pos (comp up left) 1)

      (and (pos? delta-x) (neg? delta-y))
      (move tail-pos (comp down right) 1)

      (and (neg? delta-x) (neg? delta-y))
      (move tail-pos (comp down left) 1)

      :else
      (throw (Exception. (str "unexpected postions. head: " head-pos ", tail: " tail-pos))))))

(defn- count-tail-visits [rope directions]
  (loop [rope rope
         dir-fn (first directions)
         rest-dir (rest directions)
         visited #{(last rope)}]
    (if (nil? dir-fn)
      (count visited)
      (let [moved-rope (reductions next-tail-pos (dir-fn (first rope)) (rest rope))]
        (recur
         moved-rope
         (first rest-dir)
         (rest rest-dir)
         (conj visited (last moved-rope)))))))

(comment
  (let [data (slurp (io/resource "day-9.txt"))
        directions (->> data
                        str/split-lines
                        (map #(str/split % #"\s+"))
                        (mapcat (fn [[d n]]
                                  (repeat (edn/read-string n) (direction->fn d)))))]
    (assert (= 6026 (count-tail-visits (repeat 2 [0 0]) directions)) "part 1")
    (assert (= 2273 (count-tail-visits (repeat 10 [0 0]) directions)) "part 2")))