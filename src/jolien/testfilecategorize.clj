(ns jolien.testfilecategorize
  (:require [jolien.changes :as changes]))

;We know that we are dealing with a test file, so we need to find out witch type of change has occurede
;This could be an assert statement that has been changed, or
;an annotation of a test has changed (@Test)
(defn type-of-test-file-change [file-name-before file-name-after]
  (let [changes (changes/get-changes file-name-before file-name-after)]
    (doseq [change changes]
      (assert-statement-changed change)
      (test-annotation-changed change))))

(defn assert-statement-changed [change]
  (let [left-parent (:left-parent change)
        parent (.getParent left-parent)]
    (if (= (type parent) org.eclipse.jdt.core.dom.MethodInvocation)
      (let [name (.toString (.getName parent))]
        (if (.startsWith name "assert")
          (do (print 'assert-statement-changed)
            (newline)))))))
        ;parent -> method invocation
        ;name -> assertEquals starts with
         
(defn test-annotation-changed [change]
  ;org.eclipse.jdt.core.dom.MarkerAnnotation
  (if (= (type change ) qwalkeko.clj.functionalnodes.CListInsert)
    (let [copy (:copy change)]
      (if (= (type copy) org.eclipse.jdt.core.dom.MarkerAnnotation)
        (do
          (print 'test-annotation-changed)
          (newline))))))



; Test during development

(defn test-assert-statement []
  (let [changes (changes/get-changes "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestBefore.java"
                             "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestAfter.java")]
 (doseq [change changes]
      (assert-statement-changed change))))

(defn test-annotation []
  (let [changes (changes/get-changes
                 "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java"
                 "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFileAfter.java")]
    (doseq [change changes]
      (test-annotation-changed change))))

(defn test-general []
  (type-of-test-file-change
     "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFile.java"
      "/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/files/TestFileAfter.java")
  (type-of-test-file-change
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestBefore.java"
    "/Users/joliendeclerck/Documents/THESIS/Files/WithClassification/GzipSourceTestAfter.java"))


