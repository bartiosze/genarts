(ns genarts.spiral
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def colors 
  "Colors to use in the spiral"
  [[251 226 0]   [246 180 24]  [240 81 51]
   [223 43  124] [178 36  120] [132 37 129]
   [18  37  132] [1   73  155] [1   134 199]
   [60  161 19]  [143 181 8]   [211 209 3]])

(def pattern0 
  "First pattern for cicrcle rotation"
  [[1  -2] [ 1 -1] [ 1  0] [1   1]
   [ 0  1]                 [-1  1]
   [-2  1]                 [-2  0] 
   [-2 -1] [-2 -2] [-1 -2] [0 -2]])

(def pattern1 
  "Second pattern for circle rotation"
  [ [-2 1 ] [-2  0] [-2 -1]
    [-2 -2] [-1 -2] [0  -2]
    [1  -2] [1  -1] [1   0] 
    [1   1] [0   1] [-1  1]])
(def pattern
  [[-2 -2] [-1 -2] [0  -2] [1  -2] 
   [-2 -1]                 [1  -1]
   [-2  0]                 [1   0]
   [-2  1] [-1  1] [0   1] [1   1]])

(defn draw-color-rectangle [color x y w h]
  "Utility function to draw a rectangle with given color index. Should
  be moved to genarts.util when it is created."
  ;(apply q/fill (conj (nth colors color) 100)) ; with alpha channel
  (apply q/fill (nth colors color))
  (q/rect x y w h))

(defn setup []
  (q/frame-rate 1)
  (q/color-mode :rgb)
  (q/rect-mode :corner)
  (q/no-stroke)
  {:frame 0})

(defn update [state]
  "Update the sketch state. It only contains a frame number. We have
  12 frames"
  {:frame (mod (inc (:frame state)) 12)})

(defn draw [state]
  (q/background 240)
  (q/fill 0)
  (let [{frame :frame} state
        frame (if (odd? frame) (- frame) frame)
        i 0 b 1
        pattern (if (odd? (* frame i)) pattern1 pattern0)
        width (q/width)
        size (/ width 4)]
    (doseq [[j [xx yy]]  (map-indexed vector pattern)]
      (let [color (mod (+ 12 j (Math/abs frame)) 12)
            x (+ 256 (* xx size))
            y (+ 256 (* yy size))]
        (q/with-fill [0]
          (q/text (format "%d,%d:\t%s" (Math/abs (+ 12 j frame)) (+ 12 j frame) (mod (+ 12 j frame) 12))
                  128 (+ (* 12 j)  140)))
        (draw-color-rectangle color x y size size)))
    ;; (doseq [[j [xx yy]]  (map-indexed vector pattern1)]
    ;;   (let [color (mod (+ 12 j frame) 12)
    ;;         size (/ size 2)
    ;;         x (+ 256 (* xx size))
    ;;         y (+ 256 (* yy size))]
    ;;     ;; (q/with-fill [0]
    ;;     ;;   (q/text (format "%d,%d:\t%s" frame color j) 128 (+ (* 12 j)  140)))
    ;;     (draw-color-rectangle color x y size size)))
    ))

(q/defsketch spiral
  :title "Spiral sketch"
  :size [512 512]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
