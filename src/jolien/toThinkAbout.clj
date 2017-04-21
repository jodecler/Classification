(ns jolien.toThinkAbout)


(defn change|assertfalse-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertFalse")))

(defn change|assertnotnull-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertNotNull")))

(defn change|assertnull-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertNull")))

(defn change|assertnotsame-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertNotSame")))

(defn change|assertsame-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertSame")))

(defn change|asserttrue-statement [change ?iets]
  (logic/fresh [?left-parent ?lpp ?name]
    (changes/change|update change)
    (changes/change-leftparent change ?left-parent)
    (jdt/ast-parent ?left-parent ?lpp)
    (jdt/ast :MethodInvocation ?lpp)
    (jdt/has :name ?lpp ?name)
    (jdt/name|simple-string ?name "assertTrue")))
