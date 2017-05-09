(ns jolien.classification
  (:require [qwalkeko.clj.functionalnodes :as functionalnodes])
  (:require [jolien.toplevelcategorize :as categorize])
  (:require [clojure.core.logic :as logic])
  (:require [qwalkeko.clj.functionalnodes :as changes])
  (:require [damp.ekeko.jdt
             [ast :as jdt]])
  (:import [jolien.ast AstCreation]))

; GENERAL
(defn classify-change [classification change]
;  (let [categories (set (logic/run* [?classifications] ;set om duplicaten te filteren
;                         (change-classification change ?classifications)))]
 (let [categories  (logic/run* [?classifications]
                     (change-classification change ?classifications))]
    (reduce
      (fn [classification category]
        (update-in classification 
          [category (:operation change)]
          (fn [x] (if (nil? x) 1 (inc x)))))
      classification
      categories)))


(defn classify-changes [changes]
 (let [list (reduce
              classify-change
              {}
              changes)]
   list))

(defn differences-two-files [first-filepath second-filepath]
  (let [first-ast (. AstCreation createAstForFile first-filepath)
		    second-ast (. AstCreation createAstForFile second-filepath)
        differencer (functionalnodes/get-ast-changes first-ast second-ast)
        changes (:changes differencer)
                number-of-uncassified-changes (count changes)];not correct yet
    (inspector-jay.core/inspect changes)
    (print (classify-changes changes))
    (newline)
    (newline)))

(defn change-node|affects [change ?node]
  (logic/condu
    [(changes/change|insert change)
     (changes/change-rightparent change ?node)]
    [(changes/change|update change)
     (changes/change-rightparent change ?node)]
    [(changes/change|delete change)
     (changes/change-original change ?node)]
    [(changes/change|move change)
     (changes/change-rightparent change ?node)]))

(defn change-classification [change ?classtype]
  (logic/all
    (logic/conde
      [(change-exception change )
      (logic/== ?classtype [:exception])]
      [ (change-field-name change ) 
       ;[(logic/onceo (change|test change)) ; filter duplicaten
       (logic/== ?classtype [:field-name-change ])]
      [ (change-field-type change) ;;;;ok
       (logic/== ?classtype [:field-type-change])]
      [ (change-import-statement change ) 
       (logic/== ?classtype [:import-statement-change])]
      [ (change-test-annotation change ) 
       (logic/== ?classtype [:annotation-test-added])]
      [ (change-assert-statement change)
       (logic/== ?classtype [:change|asserte-statement])]
      [ (change-modifier change ) 
       (logic/== ?classtype [:change|modifier])]      
      [(change-method-invocation-parameter change )
       (logic/== ?classtype [:change-method-invocation-parameter])]
      [(change-if-statement-expression change )
       (logic/== ?classtype [:change-if-statement-expression])]
      [(change-if-statement-consequence change )
      (logic/== ?classtype [:change-if-statement-consequence])]
      [(change-if-statement-alternative change )
      (logic/== ?classtype [:change-if-statement-alternative])]
      [ (ast|switch change )
       (logic/== ?classtype [:switch-statement])]


      ;...
      )))

; TEST ANNOTATION ADDED
(defn ast|annotation [ast ]
  (logic/fresh [?parent ?prop]
    (jdt/ast :MarkerAnnotation ast)
    (jdt/ast-parent ast ?parent)
    (jdt/ast :MethodDeclaration ?parent)))

(defn annotation|test-annotation [annotation]
  (logic/fresh [?name ?str]
    (jdt/has :typeName annotation ?name)
    (jdt/name|simple-string ?name ?str)
     (logic/project [?str]
      (logic/== true (.startsWith ?str "Test")))))

(defn change-test-annotation [change]
  (logic/fresh [?node]
    (logic/all 
       (change-node|affects change ?node)
       (ast|annotation ?node )
       (annotation|test-annotation ?node))))

; ASSERT STATEMENT CHANGED
(defn ast|method-invocation [node]
  (logic/fresh [?parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :MethodInvocation ?parent)))

(defn method-invocation|assert [node]
  (logic/fresh [?name ?parent ?str]
    (jdt/ast-parent node ?parent)
    (jdt/has :name ?parent ?name)
    (jdt/name|simple-string ?name ?str)
    (logic/project [?str]
      (logic/== true (.startsWith ?str "assert")))))

(defn change-assert-statement [change]
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (ast|method-invocation ?node)
      (method-invocation|assert ?node))))

; MODIFIER
(defn modifier [node]
  (logic/fresh []
    (jdt/ast :Modifier node)))

(defn change-modifier [change]
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (modifier ?node))))

; IMPORT STATEMENT
(defn import-statement [node]
  (logic/fresh [?parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :ImportDeclaration ?parent)))

(defn change-import-statement [change]
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (import-statement ?node))))
  
; FIELD TYPE CHANGE
(defn field-declaration [node]
  (logic/fresh [?parent ?parent-parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :FieldDeclaration ?parent)))

(defn field-type-change [node]
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (jdt/ast :SimpleType ?parent)))

(defn change-field-type [change]
  (logic/fresh [?node ?field]
    (logic/all 
      (change-node|affects change ?node)
      (field-declaration ?node)
      (field-type-change ?node))))

; FIELD NAME CHANGE
(defn field-name-change [node ?name]
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (jdt/ast :VariableDeclarationFragment ?parent)))

(defn change-field-name [change ]
  (logic/fresh [?node ?name]
    (logic/all
      (change-node|affects change ?node)
      (field-declaration ?node)
      (field-name-change ?node ?name))))

; METHOD INVOCATION REMOVE PARAM
(defn param-in-method-invocation [node]
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (ast|method-invocation ?parent)))

(defn change-method-invocation-parameter [change ]
  (logic/fresh [?node ?parent ?parent-parent ]
    (change-node|affects change ?node)
    (param-in-method-invocation ?node)
    (changes/change-property change :arguments)))

; IF STATEMENT
(defn ast|if-statement [node ?parent]
  (logic/fresh []
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :IfStatement ?parent)))

(defn if-statement|expression [node statement]
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :expression statement node)))

(defn if-statement|consequence [node statement]
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :thenStatement statement node)))

(defn if-statement|alternative [node statement]
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :elseStatement statement node)))


(defn change-if-statement-expression [change ]
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|expression ?node ?statement)))

(defn change-if-statement-consequence [change ]
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|consequence ?node ?statement)))

(defn change-if-statement-alternative [change ]
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|alternative ?node ?statement)))

; nog testen
(defn ast|switch [node]
  (logic/fresh []
    (jdt/ast :SwitchCase node)))
 
(defn change-exception [change ]
  (logic/fresh [?node ?parent]
    (change-node|affects change ?node)
    (jdt/ast-parent+ ?node ?parent)
    (jdt/ast :CatchClause ?parent)))
  

