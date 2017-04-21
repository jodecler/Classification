(ns jolien.toplevelcategorize
  (:import [jolien.ast AstCreation]))

 ; (:require [jolien.testfilecategorize :as testfilecategorize])
 ; (:require [jolien.sourcefilecategorize :as sourcefilecategorize]))


(defn pretty-print-type-file [filename filetype]
  (print filename)
  (print " is of the type ")
  (print filetype)
  (newline))

(defn is-presentation-file [file-path]
   (or 
     (.contains file-path ".html")
     (.contains file-path ".css")))

(defn is-build-file [file-path]
  (.contains file-path ".xml"))

(defn junit-import [file-path]
  (let [ast (. AstCreation createAstForFile file-path)]
    (let [import-statements (.imports ast)]
      (doall (map (fn [i]  (.contains (.toString i) "org.junit."))
                  import-statements)))))

(defn is-test-file [file-path]
 (or
   (junit-import file-path)
   (and 
     (.contains file-path ".java")
     (.contains (.toLowerCase file-path) "test"))))

(defn is-configuration-file [file-name] 
  (print 'TODO)
  )

(defn is-source-file [file-path] 
  (and (.contains file-path ".java")
       (not (.contains (.toLowerCase file-path) "test"))
        (= '() (junit-import file-path))))

(defn top-level [file-name-before]
  (cond 
    (is-presentation-file file-name-before)
    'presentation-layer
    (is-build-file file-name-before)
    'build-layer ;(pretty-print-type-file file-name-before "Build Layer")
    (is-source-file file-name-before)
    'source-layer ;(pretty-print-type-file file-name-before "Source Layer"))
      ;(sourcefilecategorize/type-of-source-file-change file-name-before file-name-after))
    (is-test-file file-name-before)
    'test-layer ;(pretty-print-type-file file-name-before "Test Layer"))
      ;(testfilecategorize/type-of-test-file-change file-name-before file-name-after))
    (is-configuration-file file-name-before)
    'configuration-layer ;(pretty-print-type-file file-name-before "Configuration Layer")
    
    :else
    'unclassified )) ;(pretty-print-type-file file-name-before "Unclassified Layer")))


