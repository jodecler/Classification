(ns ast.main
  (:require [jolien.classification :as classification])
  (:require [jolien.toplevelcategorize :as categorize]))


(defn algemene-loop [first-file second-file]
  (let [categorie (categorize/top-level first-file)]
    (categorize/pretty-print-type-file first-file categorie)
    (if (or (= categorie 'test-layer)
            (= categorie 'source-layer))
        (classification/differences-two-files first-file second-file)
        )))




(defn try [] 
  (algemene-loop
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestAfter.java")
  (algemene-loop
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/CurrenciesTestBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/CurrenciesTestAfter.java")
  (algemene-loop
    "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsAfter.java")
  (algemene-loop
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/NamedRunnableBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/NamedRunnableAfter.java")
  (algemene-loop
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/JedisClusterBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/JedisClusterAfter.java")
   (algemene-loop
     "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/RouteSelectorBefore.java"
     "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/RouteSelectorAfter.java")
   (algemene-loop ;test
    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/PresentationExampleAfter.java"
    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/PresentationExampleAfter.java")
  (algemene-loop ;test
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java"
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java")
  (algemene-loop ;pres
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/CssTestFile.css"
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/CssTestFile.css")
  (algemene-loop ;pres
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/HtmlTestFile.html"
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/HtmlTestFile.html")
   (algemene-loop ;build
    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/XmlTestFile.xml"
    "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/XmlTestFile.xml")
  (algemene-loop ;source
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/Source.java"
   "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/Source.java"))  

      

