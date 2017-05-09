(ns jolien.toplevelcategorize
  "Create a categorie for a file based on its extention or import statements"
  (:import [jolien.ast AstCreation]))

(defn is-presentation-file [file-path]
  "Presentation file ends with .html or .css"
   (or 
     (.contains file-path ".html")
     (.contains file-path ".css")))

(defn is-build-file [file-path]
  ".xml-files are build files"
  (.contains file-path ".xml"))

(defn junit-import [file-path]
  "See if file imports JUnit-framework"
  (let [ast (. AstCreation createAstForFile file-path)]
    (let [import-statements (.imports ast)]
      (doall (map (fn [i]  (.contains (.toString i) "org.junit."))
                  import-statements)))))

(defn is-test-file [file-path]
  "Test file ends with .java and contains test in the path or imports JUnit"
 (or
   (junit-import file-path)
   (and 
     (.contains file-path ".java")
     (.contains (.toLowerCase file-path) "test"))))

(defn is-configuration-file [file-name] 
  "Config files ends with .config"
  (.contains file-path .config))

(defn is-source-file [file-path] 
  "Source files are .java files that are no test files"
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


