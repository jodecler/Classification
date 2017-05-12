(ns jolien.classification
  (:require [jolien.toplevelcategorize :as categorize])
  (:require [clojure.core.logic :as logic])
  (:require [qwalkeko.clj.functionalnodes :as changes])
  (:require [damp.ekeko.jdt [ast :as jdt]])
  (:import [jolien.ast AstCreation]))

(defn classify-change [classification change]
  "Classify one change"
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
  "loop over changes and make classification"
 (let [list (reduce
              classify-change
              {}
              changes)]
   list))

(defn change-node|affects [change ?node]
  "Get the node affected by a change"
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
  "Actual classification"
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
       (logic/== ?classtype [:switch-statement])])))


(defn ast|annotation [ast ]
  "Annotation change of a method declaration"
  (logic/fresh [?parent ?prop]
    (jdt/ast :MarkerAnnotation ast)
    (jdt/ast-parent ast ?parent)
    (jdt/ast :MethodDeclaration ?parent)))

(defn annotation|test-annotation [annotation]
  "Annotation changed, of a test"
  (logic/fresh [?name ?str]
    (jdt/has :typeName annotation ?name)
    (jdt/name|simple-string ?name ?str)
     (logic/project [?str]
      (logic/== true (.startsWith ?str "Test")))))

(defn change-test-annotation [change]
  "Test annotation changed, must be a test annotation and a change of annotation"
  (logic/fresh [?node]
    (logic/all 
       (change-node|affects change ?node)
       (ast|annotation ?node )
       (annotation|test-annotation ?node))))

(defn ast|method-invocation [node]
  "node must be a method invocation"
  (logic/fresh [?parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :MethodInvocation ?parent)))

(defn method-invocation|assert [node]
  "Method invocation of an assert-statement"
  (logic/fresh [?name ?parent ?str]
    (jdt/ast-parent node ?parent)
    (jdt/has :name ?parent ?name)
    (jdt/name|simple-string ?name ?str)
    (logic/project [?str]
      (logic/== true (.startsWith ?str "assert")))))

(defn change-assert-statement [change]
  "Change assert statement, must be a method invocation of an assert"
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (ast|method-invocation ?node)
      (method-invocation|assert ?node))))

(defn modifier [node]
  "node must be a modifier"
  (logic/fresh []
    (jdt/ast :Modifier node)))

(defn change-modifier [change]
  "changed node is a modifier"
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (modifier ?node))))

(defn import-statement [node]
  "node is import declaration"
  (logic/fresh [?parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :ImportDeclaration ?parent)))

(defn change-import-statement [change]
  "change is import statement"
  (logic/fresh [?node]
    (logic/all
      (change-node|affects change ?node)
      (import-statement ?node))))

(defn field-declaration [node]
  "node is a fielddeclaration"
  (logic/fresh [?parent ?parent-parent]
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :FieldDeclaration ?parent)))

(defn field-type-change [node]
  "change of a type"
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (jdt/ast :SimpleType ?parent)))

(defn change-field-type [change]
  "field type change, change of type and field"
  (logic/fresh [?node ?field]
    (logic/all 
      (change-node|affects change ?node)
      (field-declaration ?node)
      (field-type-change ?node))))

; FIELD NAME CHANGE
(defn field-name-change [node ?name]
  "name of a field changed"
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (jdt/ast :VariableDeclarationFragment ?parent)))

(defn change-field-name [change ]
  "field name change, change of field and name"
  (logic/fresh [?node ?name]
    (logic/all
      (change-node|affects change ?node)
      (field-declaration ?node)
      (field-name-change ?node ?name))))


(defn param-in-method-invocation [node]
  "parameter in method invocation"
  (logic/fresh [?parent]
    (jdt/ast-parent node ?parent)
    (ast|method-invocation ?parent)))

(defn change-method-invocation-parameter [change ]
  "change of method invocation, parameter"
  (logic/fresh [?node ?parent ?parent-parent ]
    (change-node|affects change ?node)
    (param-in-method-invocation ?node)
    (changes/change-property change :arguments)))

(defn ast|if-statement [node ?parent]
  "if-statement"
  (logic/fresh []
    (jdt/ast-parent+ node ?parent)
    (jdt/ast :IfStatement ?parent)))

(defn if-statement|expression [node statement]
  "expression of if-statement"
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :expression statement node)))

(defn if-statement|consequence [node statement]
  "consequence in if-statement"
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :thenStatement statement node)))

(defn if-statement|alternative [node statement]
  "alternative in if-statement"
  (logic/fresh []
    (jdt/ast-parent node statement)
    (jdt/has :elseStatement statement node)))

(defn change-if-statement-expression [change ]
  "change if statement expression"
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|expression ?node ?statement)))

(defn change-if-statement-consequence [change ]
  "change in consequence of if-statement"
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|consequence ?node ?statement)))

(defn change-if-statement-alternative [change ]
  "change in alternative of if-statement"
  (logic/fresh [?node ?statement ?value ]
    (change-node|affects change ?node)
    (ast|if-statement ?node ?statement)
    (if-statement|alternative ?node ?statement)))

(defn ast|switch [change]
  "switch statement"
  (logic/fresh [?node]
    (change-node|affects change ?node)
    (jdt/ast :SwitchCase ?node)))
 
(defn change-exception [change]
  "Catch exception"
  (logic/fresh [?node ?parent]
    (change-node|affects change ?node)
    (jdt/ast-parent+ ?node ?parent)
    (jdt/ast :CatchClause ?parent)))
  

