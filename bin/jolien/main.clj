(ns ast.main
  (:require [jolien.classification :as classification])
  (:require [jolien.toplevelcategorize :as toplevel])
  (:require [qwalkeko.experiments.automated :as automated]))

(defn before []
    (automated/clone-projects automated/PROJECT_FOLDER
             automated/PROJECTS))
(defn after-before []
    (automated/import-projects automated/PROJECT_FOLDER))

(def multiple-file-information nil)
(def multiple-changes nil) 

(defn classification []
  (let [project-list (list (first (automated/find-all-changes
                                    automated/BREAKERFIXER)))]
        (doseq [item project-list]
          (let [change-list (:changes item)
                files (:changed item)
                project (:project item)
                breaking (:breaking item)
                fixing (:fixing item)
                breaking_tr (:breaking_tr item)
                fixing_tr (:fixing_tr item)]
            (inspector-jay.core/inspect item)
            (doseq [file files]
              (let [file-name (:file file)
                    file-information {
                                      :file-name filename
                                      :file-classification (toplevel/top-level file-name)
                                      }] 
                (alter-var-root #'multiple-file-information (conj multiple-file-information file-information))
               ))
            (doseq [i change-list]
              (let [sequence (first (first i))
                    changes (first sequence)
                    qwalkekochanges (first (rest changes))
                    classified-changes (classification/classify-changes qwalkekochanges)]
                
               (print classified-changes)
               (print (type classified-changes))
               (newline)))
            (let [all-information {
                                   :project project
                                   :breaking breaking
                                   :fixing fixing
                                   :breaking_tr breaking_tr
                                   :fixing_tr fixing_tr
                                   :file-information multiple-file-information}]
              (print all-information))
            ))))

; {
;     :project (nth change 0),
;     :breaking (nth change 1),
;     :fixing (nth change 2),
;     :breaking_tr (nth change 3),
;     :fixing_tr (nth change 4),
;     :changed (first changes),
;     :changes (second changes)
;    }))







;(defn algemene-loop [first-file second-file]
;  (let [categorie (categorize/top-level first-file)]
;    (categorize/pretty-print-type-file first-file categorie)
;    (if (or (= categorie 'test-layer)
;            (= categorie 'source-layer))
;        (classification/differences-two-files first-file second-file)
;        )))
;
;(defn try [] 
;  (algemene-loop
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestBefore.java"
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestAfter.java")
;  (algemene-loop
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/CurrenciesTestBefore.java"
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/CurrenciesTestAfter.java")
;  (algemene-loop
;    "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsBefore.java"
;    "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsAfter.java")
;  (algemene-loop
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/NamedRunnableBefore.java"
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/NamedRunnableAfter.java")
;  (algemene-loop
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/JedisClusterBefore.java"
;    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/JedisClusterAfter.java")
;   (algemene-loop
;     "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/RouteSelectorBefore.java"
;     "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/RouteSelectorAfter.java")
;   (algemene-loop ;test
;    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/PresentationExampleAfter.java"
;    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/PresentationExampleAfter.java")
;  (algemene-loop ;test
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java"
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java")
;  (algemene-loop ;pres
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/CssTestFile.css"
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/CssTestFile.css")
;  (algemene-loop ;pres
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/HtmlTestFile.html"
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/HtmlTestFile.html")
;   (algemene-loop ;build
;    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/XmlTestFile.xml"
;    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/XmlTestFile.xml")
;  (algemene-loop ;source
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/Source.java"
;   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/Source.java"))  

