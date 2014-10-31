(ns genarts.fifreen
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def colors
  "Color palette"
  [[230 229 15] [243 209 0] [246 132 0] [239 55 1] [219 29 3]
   [194 8 83] [117 26 123] [56 24 100] [43 38 161] [2 98 208]
   [0 153 179] [0 174 66] [39 185 22] [103 196 2] [173 214 0]])

(def xs
  "column widths"
  [3 2 1 4 2 1 4 2 1 4 2 1 4 2 1])
(def ys 
  "column color offsets"
  [0 10 14 8 11 6 9 4 7 2 5 12 4 13 1])

(defn draw-color-rectangle [color x y w h]
  ;(apply q/fill (conj (nth colors color) 100)) ; with alpha channel
  (apply q/fill (nth colors color))
  (q/rect x y w h))

(defn setup []
  (q/frame-rate 2)
  (q/color-mode :rgb)
  (q/rect-mode :corner)
  (q/no-stroke)
  {:frame 0})

(defn update [state]
  {:frame (mod (inc (:frame state)) 15)})

(defn draw [state]
  (q/background 240)
  (q/fill 0)
  (let [{frame :frame} state
        wx 14
        hx 30
        n 15
        xposs (->> xs (map #(* wx %)) (reductions + 0))]
    (doseq [x (range n)
            y (range n)]
      (let [width (* wx (nth xs x))
            xpos (nth xposs x)
            dy (nth ys x)
            k (if (odd? x) (- frame) frame)
            color (mod (+ n (+ y dy k)) n)]
        (draw-color-rectangle color xpos (* y hx) width hx)))))

(q/defsketch fifteen
  :title "15 lines"
  :size [476 450]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
