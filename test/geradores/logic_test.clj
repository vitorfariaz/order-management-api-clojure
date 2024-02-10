(ns geradores.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [geradores.logic :refer :all]
            [geradores.model :as t.model]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  (testing "Que cabe na fila quando fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que cabe na fila quando fila vazia E pessoa aleatória"
    ; doseq: executa pra cada elemento da lista de lista
    ; o que for passado dentro do doseq
    ;Nesse caso, vai rodar 10 testes(padrão do gen/vector)
    ; cada teste com uma lista com 0 a 4 elementos dentro
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))]
      (is (cabe-na-fila? {:espera fila}, :espera))))


  (testing "Que cabe na fila quando tem 1 na fila"
    (is (cabe-na-fila? {:espera [1]}, :espera)))

  (testing "Que cabe na fila quando menos 1 do limite"
    (is (cabe-na-fila? {:espera [1, 2, 3, 4]}, :espera)))

  (testing "Que não cabe na fila quando fila ta cheia"
    (is (not (cabe-na-fila? {:espera [1, 2, 3, 4, 5]}, :espera))))

  (testing "Que não cabe na fila quando mais 1 do limite"
    (is (not (cabe-na-fila? {:espera [1, 2, 3, 4, 5, 6]}, :espera))))

  (testing "Que não cabe quando departamento não existe"
    (is (not (cabe-na-fila? {:espera [11, 22, 33, 44]}, :raio-x))))
  )


; o doseq com 2 símbolos, múltiplica um simbol. pelo outro
; no meu caso de 20 fila e 5 pessoa, geraria 100 execuções
;(deftest chega-em-test
;  (testing "que é colocado uma pessoa em filas menores que 5"
;    ; Será gerado 20 filas/lista, cada lista com 0 a 4 elementos
;    ; Será gerado 5 pessoas
;    ; doseq múltiplica a qtd de fila por pessoa e executaria 100x
;    ;  nesse caso
;    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 20)
;            pessoa (gen/sample gen/string-alphanumeric 5)]
;        (is (= 1 1)))))

(defspec coloca-uma-pessoa-em-filas-menores-5 10
  (prop/for-all
    [fila (gen/vector gen/string-alphanumeric 0 4)
     pessoa gen/string-alphanumeric]
    ;hospital não é fila
    (is (= {:espera (conj fila pessoa)}
           (chega-em {:espera fila} :espera pessoa)))))



;ESSE TESTE NÃO EXECUTA, DEIXA SILENCIADO TODOS OS TESTES
; Gera uma lista com ?10? elementos, com 5 a 10 characteres
; gen/fmap executa a função clojure.string/join para
;cada um dos elementos gerados na função gen/vector gen/char...
;(def nome-aleatorio
;  (gen/fmap clojure.string/join
;            (gen/vector gen/char-alphanumeric 5 10)))
;
;(defn transforma-vetor-em-fila [vetor]
;  (reduce conj t.model/fila-vazia vetor))
;
; Chamaar a função transforma-vetor-em-fila
; está travando a execução de todas os testes.
; no REPL diz que está executando, mas não finaliza
;(def fila-nao-cheia-gen
;  (gen/fmap
;    transforma-vetor-em-fila
;    (gen/vector nome-aleatorio 1 4)))
;
;(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 5
;  (prop/for-all
;    ; Gera uma lista com 0 a 4 elementos com 5 a 10 characters
;    ; em cada elemento
;    [espera fila-nao-cheia-gen
;     raio-x fila-nao-cheia-gen
;     ultrasom fila-nao-cheia-gen
;     vai-para (gen/elements [:raio-x :ultrasom])]
;    (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrasom ultrasom}
;          hospital-final (transfere hospital-inicial :espera vai-para)
;          ]
;      (println hospital-inicial vai-para)
;      (= (total-de-pacientes hospital-inicial)
;         (total-de-pacientes hospital-final))
;      )
;    )
;  )

