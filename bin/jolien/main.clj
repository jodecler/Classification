(ns ast.main
  (:require [qwalkeko.clj.functionalnodes :as functionalnodes]) 
  (:import [jolien AstCreation]))

(defn differences-two-files [first-filepath second-filepath]
  (let [first-ast (. AstCreation createAstForFile first-filepath)
        second-ast (. AstCreation createAstForFile second-filepath)
        differencer (functionalnodes/make-differencer first-ast second-ast)]
    (.difference differencer)
    
    (inspector-jay.core/inspect (.getOperations differencer))
    ))



(defn test []
  (differences-two-files "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile"
                         "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile2"))
  
;"/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile"
;"/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile2"
;(ast.main/make-ast-from-file  "/Users/joliendeclerck/Documents/workspace/classification-java/testfile")

;;(def diff (new changenodes.Differencer left-compilation-unit right-compilation-unit))
;;(.difference diff)
;;(.getOperations diff)
