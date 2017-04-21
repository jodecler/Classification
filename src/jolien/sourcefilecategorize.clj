(ns jolien.sourcefilecategorize
    (:require [jolien.changes :as changes])
    )

;(defn type-of-source-file-change [file-name-before file-name-after]
;  (let [changes (changes/get-changes file-name-before file-name-after)]
;    (doseq [change changes]
;      (control-flow change))))
;
;(defn control-flow [change]
;  (if-statement change)
;  (switch-statement change)
;  )
;
;(defn inconsequential [change]
;  (rename-variable change))
;
;(defn field-rename [node change]
;  (let [location (.getLocationInParent node)]
;    (if (= location org.eclipse.jdt.core.dom.MethodInvocation/EXPRESSION_PROPERTY)
;      (let  [property (.getProperty change)
;             type-change (type change)]
;        (if (= property org.eclipse.jdt.core.dom.SimpleName/IDENTIFIER_PROPERTY)
;          (def method-invocation-rename-changes (+ 1 method-invocation-rename-changes)))
;        ))))
;
;          
;
;(defn field-name-change [change]
;  (logic/fresh [?before ?after]
;               (functionalnodes/change|update change)
;               (functionalnodes/change-original change ?before)
;               (functionalnodes/change-rightparent change ?after)
;               (type-of-node :SimpleType ?before)
;               (type-of-node :SimpleType ?after)))
;               
;               
;             
                 
                 ;(defn change|field-type [change]
;  (logic/fresh [?before ?after]
;     (functionalnodes/change|update change)
;     (functionalnodes/change-original change ?before)
;     (functionalnodes/change-rightparent change ?after)
;     (type-of-node :SimpleType ?before)
;     (type-of-node :SimpleType ?after)
;     (parent-node :FieldDeclarartion ?before)
;     (parent-node :FieldDeclaration ?after)))

;control flow
;   else, break, switch, if

;data flow

;exception flow
;   catch, add remove, type exception change, throw

;inconsequential
;   rename var

;call relation

