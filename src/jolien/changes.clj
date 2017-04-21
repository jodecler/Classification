(ns jolien.changes
  (:require [qwalkeko.clj.functionalnodes :as functionalnodes])
   (:import [jolien.ast AstCreation]))

(defn get-changes [first-filepath second-filepath]
  (let [first-ast (. AstCreation createAstForFile first-filepath)
        second-ast (. AstCreation createAstForFile second-filepath)
        differencer (functionalnodes/get-ast-changes first-ast second-ast)
        changes (:changes differencer)]
    changes))