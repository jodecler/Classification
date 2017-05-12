(ns ast.main
  (:require [jolien.classification :as classification])
  (:require [jolien.toplevelcategorize :as toplevel])
  (:require [qwalkeko.experiments.automated :as automated])
  (:import [jolien WriteResults]))

(defn before []
  "Clone projects from GitHub"
    (automated/clone-projects automated/PROJECT_FOLDER
             automated/PROJECTS))

(defn after-before []
  "Import projects"
    (automated/import-projects automated/PROJECT_FOLDER))

(def multiple-file-information nil)
(def multiple-changes nil) 
(def new-information-list nil)

(defn classification []
  "Calculate changes and classify changes"
  (let [project-list (automated/find-all-changes
                        automated/BREAKERFIXER)]
     (alter-var-root #'new-information-list (constantly nil))
        (doseq [item project-list]
          (let [change-list (:changes item)
                files (:changed item)
                project (:project item)
                breaking (:breaking item)
                fixing (:fixing item)
                breaking_tr (:breaking_tr item)
                fixing_tr (:fixing_tr item)]
            (doseq [file files]
              (let [file-name (:file file)
                    file-information {
                                      :file-name file-name
                                      :file-classification (toplevel/top-level file-name)
                                      }] 
                (alter-var-root #'multiple-file-information (constantly  (conj multiple-file-information (list file-information))))))
            (doseq [i change-list]
              (let [sequence (first (first i))
                    changes (first sequence)
                    qwalkekochanges (first (rest changes))
                    classified-changes (classification/classify-changes qwalkekochanges)]
               (alter-var-root #'multiple-changes (constantly (conj multiple-changes (list classified-changes))))))
             (let [new-information {
                                    :project project
                                    :breaking breaking
                                    :fixing fixing
                                    :breaking_tr breaking_tr
                                    :fixing_tr fixing_tr
                                    :file-information multiple-file-information
                                    :changes-information multiple-changes}]
               (alter-var-root #'new-information-list (constantly (conj new-information-list (list new-information))))))))
   new-information-list)

(defn write-test []
  "Write results to file"
  (do
    (. WriteResults createFile)
    (. WriteResults writeString "project,breaking,fixing,breaking_tr,fixing_tr,filename,filecategorie,classified")
    (. WriteResults writeString "\n"))
  (let [classified (classification)]
    (doseq [item classified]
      (let [file-info (:file-information (first item))
            changes-info (:changes-information (first item))
            combined-info (map list file-info changes-info)
                project (:project (first item))
                breaking (:breaking (first item))
                fixing (:fixing (first item))
                breaking_tr (:breaking_tr (first item))
                fixing_tr (:fixing_tr (first item))]
        (doseq [combined combined-info]
          (let [file-name (:file-name (first (first combined)))
                file-categorie (:file-classification (first (first combined)))
                changes (first (nth (rest combined) 0))]
            (doseq [change changes]
              (let [change-keyword (key change)]
                    (. WriteResults writeString (.toString project))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString breaking))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString fixing))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString breaking_tr))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString fixing_tr))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString file-name))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString file-categorie))
                    (. WriteResults writeString ",")
                    (. WriteResults writeString (.toString (first change-keyword)))
                    (. WriteResults writeString "\n")
                    (. WriteResults flushWriting))))))))
(do
  (. WriteResults stopWriting)))

