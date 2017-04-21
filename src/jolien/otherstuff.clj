(ns jolien.otherstuff
  (:require [qwalkeko.clj.functionalnodes :as functionalnodes])
  (:require [qwalkeko.experiments.phd :as experiments])
  (:require [clojure.core.logic :as logic])
    (:require [qwalkeko.clj.functionalnodes :as changes])
      (:require [damp.ekeko.jdt
             [ast :as jdt]])
  (:import [jolien AstCreation]))

(def if-statement-condition-changes 0)
(def if-statement-alternative-changes 0)
(def if-statement-consequence-changes 0)
(def method-invocation-rename-changes 0)

(def variable-declaration-changes 0)


(defn iets-dat-het-niet-is [affected-node]
  (if (= true affected-node)
  (print 'nee)))

(defn variable-declaration-affected [node change]
  (let [type-node (type node)]
    (if (= org.eclipse.jdt.core.dom.StringLiteral type-node)
      (let []
        ; old en new value
        (def variable-declaration-changes (+ variable-declaration-changes 1))
;  (inspector-jay.core/inspect node)
;  
;  (inspector-jay.core/inspect change)
        )))
  )

(defn if-statement-condition-affected [node change]
  (let [type-change (type change)]
    (cond 
      (= type-change changenodes.operations.Insert)
      (let [right-node (.getRightNode change)
            location (.getLocationInParent right-node)]
        (if (= location org.eclipse.jdt.core.dom.IfStatement/EXPRESSION_PROPERTY)
          (def if-statement-condition-changes (+ if-statement-condition-changes 1))))
      (= type-change changenodes.operations.Move)
      (print 'move-if-statement-condition-affected)
      (= type-change changenodes.operations.Delete)
      (let [left-node (.getLeftParent change)
            location (.getLocationInParent left-node)]
       (if (= location org.eclipse.jdt.core.dom.IfStatement/EXPRESSION_PROPERTY)
           (def if-statement-condition-changes (+ if-statement-condition-changes 1)))))))

(defn if-statement-consequence-affected [node change]
  (let [type-change (type change)]
    (cond
      (= type-change changenodes.operations.Insert)
      (let [right-node (.getRightNode change)
            location (.getLocationInParent right-node)]
        (if (= location org.eclipse.jdt.core.dom.IfStatement/THEN_STATEMENT_PROPERTY)
          (def if-statement-consequence-changes (+ if-statement-consequence-changes 1))))
      (= type-change changenodes.operations.Move)
      (print 'move-if-statement-consequence-affected)
      (= type-change changenodes.operations.Delete)
      (let [left-node (.getLeftParent change)
            location (.getLocationInParent left-node)]
        (if (= location org.eclipse.jdt.core.dom.IfStatement/THEN_STATEMENT_PROPERTY)
          (def if-statement-consequence-changes (+ if-statement-consequence-changes 1)))))))

(defn if-statement-alternative-affected [node change]
  (let [type-change (type change)]
    (cond
      (= type-change changenodes.operations.Insert)
      (let [right-node (.getRightNode change)
            location (.getLocationInParent right-node)]
        (if (= location org.eclipse.jdt.core.dom.IfStatement/ELSE_STATEMENT_PROPERTY)
          (def if-statement-alternative-changes (+ if-statement-alternative-changes 1))))
      (= type-change changenodes.operations.Move)
      (print 'move-if-statement-alternative-affected)
      (= type-change changenodes.operations.Delete)
      (let [left-node (.getLeftParent change)
            location (.getLocationInParent left-node)]
        (if (= location org.eclipse.jdt.core.dom.IfStatement/ELSE_STATEMENT_PROPERTY)
          (def if-statement-alternative-changes (+ if-statement-alternative-changes 1)))))))

(defn field-rename [node change]
  (let [location (.getLocationInParent node)]
    (if (= location org.eclipse.jdt.core.dom.MethodInvocation/EXPRESSION_PROPERTY)
      (let  [property (.getProperty change)
             type-change (type change)]
        (if (= property org.eclipse.jdt.core.dom.SimpleName/IDENTIFIER_PROPERTY)
          (def method-invocation-rename-changes (+ 1 method-invocation-rename-changes)))
        ))))

(defn field-rename [node change]
  (inspector-jay.core/inspect (type node))
  (inspector-jay.core/inspect change))

(defn loop-over-changes [changes]
  (doseq [i changes]
    (let [affected-node (.getAffectedNode i)]
      (if-statement-condition-affected affected-node i )
      (if-statement-consequence-affected affected-node i)
      (if-statement-alternative-affected affected-node i)
      (field-rename affected-node i))
     ;(variable-declaration-affected affected-node i))
  (let []
    (print 'if-statement-condition-changes)
    (print " ")
    (print if-statement-condition-changes)
    (newline)
    (print 'if-statement-consequence-changes)
    (print " ")
    (print if-statement-consequence-changes)
    (newline)
    (print 'if-statement-alternative-changes)
    (print " ")
    (print if-statement-alternative-changes)
    (newline)
    (print 'method-invocation-rename-changes)
    (print " ")
    (print method-invocation-rename-changes)
    (newline)
    (newline))))

(defn clear-counting-vars []
  (def if-statement-condition-changes 0)
  (def if-statement-alternative-changes 0)
  (def if-statement-consequence-changes 0)
  (def variable-declaration-changes 0)
  )

(defn differences-two-files [first-filepath second-filepath]
		  (let [first-ast (. AstCreation createAstForFile first-filepath)
		        second-ast (. AstCreation createAstForFile second-filepath)
		        differencer (functionalnodes/make-differencer first-ast second-ast)]
		    (.difference differencer)
		    (let [operations (.getOperations differencer)]
        (inspector-jay.core/inspect operations)
		   (loop-over-changes operations)
		    (clear-counting-vars) 
		    )))

(defn test2 []
  (let [first-ast (. AstCreation createAstForFile "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsBefore.java")
		    second-ast (. AstCreation createAstForFile  "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsAfter.java")]
          (inspector-jay.core/inspect (functionalnodes/get-ast-changes first-ast second-ast))))
          
(def field-declaration-type-change-changes 0)


(defn field-type-change [change]
  (let [type-change (type change)]
    (if (= type-change qwalkeko.clj.functionalnodes.CUpdate)
      (let [original (:original change)
            original-parent-type (.getNodeClass (.getLocationInParent (:owner original)))
            location-original (.getNodeClass (.getLocationInParent (.getParent (:owner original))))
            right-parent (:right-parent change)
            right-parent-type (.getNodeClass (.getLocationInParent right-parent))
            location-right (.getNodeClass (.getLocationInParent (.getParent right-parent)))
            ]
        (if (and
              (= right-parent-type org.eclipse.jdt.core.dom.SimpleType)
              (= original-parent-type org.eclipse.jdt.core.dom.SimpleType)
              (= location-original org.eclipse.jdt.core.dom.FieldDeclaration)
              (= location-right org.eclipse.jdt.core.dom.FieldDeclaration))
          (print 'true))))))

(defn methoddecl-methodinvoc|calls [?methoddeclaration ?methodinvocation]
  )



;(defn change|field-type [change]
;  (logic/fresh [?before ?after]
;     (functionalnodes/change|update change)
;     (functionalnodes/change-original change ?before)
;     (functionalnodes/change-rightparent change ?after)
;     (type-of-node :SimpleType ?before)
;     (type-of-node :SimpleType ?after)
;     (parent-node :FieldDeclarartion ?before)
;     (parent-node :FieldDeclaration ?after)))

(defn change|fieldtype-fieldname-fieldidentifiers [change]
  (logic/fresh [?beforetype ?aftertype ?beforename ?aftername]
     (change|update change)
     (field-type-change :SimpleType ?beforetype ?aftertype)
     (field-name-change :VariableDeclarationFragmentation ?beforename ?aftername)
     (method-call-change :MethodInvocation ?beforename ?aftername)
     ))
     
;(parent ?str ?parent) variabledeclaration
;(has :name ?parent ?str) nameproperty

;;
(logic/run* [?update ?class]
  (logic/membero the-changes ?update)
  (change|field-type ?update)
  (== ?class :fields))


(filter (fn [change] (field-type-change change)) changes)
map
(reduce (fn [classif change] 
          (cond (field-type-change change)
            (update-in classif :fields inc)))
        #{:category 0 :fields 0 :elkecategorie 0 :unclassified}
        changes)

(ast type node)
(has prop node child)
(child property node child)


;naar csv file overschrijven per commit dat gewijzigd is


(defn test []
;if statement OK
;  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/EntityRobotPickerBefore.java"
;                         "/Users/joliendeclerck/Documents/THESIS/Files/new/EntityRobotPickerAfter.java"))
		;  ;not detectable
		;  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/DeserializeeBefore.java"
		;                         "/Users/joliendeclerck/Documents/THESIS/Files/new/DeserializeeAfter.java")

;nill file (no before)
;(differences-two-files ""
;                         "/Users/joliendeclerck/Documents/THESIS/Files/new/DownloadBuilderAfter.java")

;string aanpassen
;(differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/FileDownloadBefore.java"
;                        "/Users/joliendeclerck/Documents/THESIS/Files/new/FileDownloadAfter.java")

;not yet
;(differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/FileDownloadTestBefore.java"
;                         "/Users/joliendeclerck/Documents/THESIS/Files/new/FileDownloadTestAfter.java")

(differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsBefore.java"
                       "/Users/joliendeclerck/Documents/THESIS/Files/new/CommunityRecentSubmissionsAfter.java"))

(differences-two-files 
                       "/Users/joliendeclerck/Documents/THESIS/Files/new/MetricsServletTestAfter.java"
                       "/Users/joliendeclerck/Documents/THESIS/Files/new/MetricsServletTestBefore.java")
  
  
;  ;no idea
;  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/new/GenericContainerTestBefore.java"
;                         "/Users/joliendeclerck/Documents/THESIS/Files/new/GenericContainerTestAfter.java"))
; 

)

  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/Imports/ForgeBefore.java"
                       "/Users/joliendeclerck/Documents/THESIS/Files/Imports/ForgeAfter.java")
  (print 'new)
  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillABefore.java"
                     "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillAAfter.java")
  (print 'new)
  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillBBefore.java"
                      "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillBAfter.java")
  (print 'new)
  (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillCBefore.java"
                      "/Users/joliendeclerck/Documents/THESIS/Files/Imports/KillBillCAfter.java")
  (print 'new)
   (differences-two-files "/Users/joliendeclerck/Documents/THESIS/Files/Imports/uaaBefore.java"
                      "/Users/joliendeclerck/Documents/THESIS/Files/Imports/uaaAfter.java"))


;"/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile"
;"/Users/joliendeclerck/Documents/workspace/damp.qwalkeko.plugin/src/jolien/testfiles/testfile2"
;(ast.main/make-ast-from-file  "/Users/joliendeclerck/Documents/workspace/classification-java/testfile")

;;(def diff (new changenodes.Differencer left-compilation-unit right-compilation-unit))
;;(.difference diff)
;;(.getOperations diff)
