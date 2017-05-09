(ns jolien.toplevelcategorize
  (:import [jolien.ast AstCreation]))

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
    'build-layer
    (is-source-file file-name-before)
    'source-layer 
    (is-test-file file-name-before)
    'test-layer 
    (is-configuration-file file-name-before)
    'configuration-layer
    :else
    'unclassified)) 


