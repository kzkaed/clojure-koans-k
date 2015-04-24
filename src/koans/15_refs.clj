(ns koans.15-refs
  (:require [koan-engine.core :refer :all]))
;most data structures are mutable, but refs allows for safe mutability

(def the-world (ref "hello"))
(def bizarro-world (ref {}))



(meditations
  "In the beginning, there was a word"
  (= "hello" (deref the-world))

  "You can get the word more succinctly, but it's the same"
  (= "hello" @the-world)

  "You can be the change you wish to see in the world."
  (= "better" (do
                 (dosync (ref-set the-world "better"));set (reset)value of ref 
                 @the-world))

  "Alter where you need not replace";;because it was ref-set above so saved globally?
  (= "better!!!" (let [exclamator (fn [x] (str x "!"))] 
                  (dosync
                   (alter the-world exclamator) ;;alter allows function to change value of ref
                   (alter the-world exclamator)
                   (alter the-world exclamator))
                  @the-world))

  "Don't forget to do your work in a transaction!"
  (= 0 (do (dosync (ref-set the-world 0)) ;;dosync ref-set
           @the-world))

  "Functions passed to alter may depend on the data in the ref"
  (= 20 (do (let [change (fn [x] (+ x 20))]
              (dosync (alter the-world change)))));look at partial also
  
  ;;k adds example
  ;;(do (dosync (alter the-world + 20)))
 

  "Two worlds are better than one"
  (= ["Real Jerry" "Bizarro Jerry"]
       (do
         (dosync
          (ref-set the-world {})
          (alter the-world assoc :jerry "Real Jerry")
          (alter bizarro-world assoc :jerry "Bizarro Jerry")
          [(:jerry @the-world) (:jerry @bizarro-world)])))
  )
