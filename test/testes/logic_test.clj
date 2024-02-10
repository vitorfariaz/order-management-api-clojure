(ns testes.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [testes.logic :refer :all]
            [testes.model :as t.model]
            [schema.core :as s]))

(s/set-fn-validation! true)

; 'is' é o que faz o assert de fato
; sem ele, não é entendido que é um test válido
(deftest cabe-na-fila?-test
  (testing "Que cabe na fila quando fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

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

(deftest chega-em-test
  (testing "aceita pessoas enquanto cabem pessoas na fila"
    (is (= {:espera (conj t.model/fila-vazia "1" "2" "3" "4" "5")}
           (chega-em {:espera (conj t.model/fila-vazia "1" "2" "3" "4")}, :espera, "5"))))

  (is (= {:espera (conj t.model/fila-vazia "60" "5" "59")}
         (chega-em {:espera (conj t.model/fila-vazia "60" "5")}, :espera, "59")))

  (testing "Não aceita quando não cabe na fila"
    ; Verifica se a exception foi jogada
    ; classico codigo ruim
    ; pois qq outro erro generico lançado vai dar falso positivo
    ;(is (thrown? clojure.lang.ExceptionInfo
    ;             (chega-em {:espera [11,22,33,44,55]}, :espera 76)))
    ;
    ; Mesmo que escolha uma exeption do genero, outro lugar
    ; pode lançar essa exception
    ;(is (thrown? IllegalStateException
    ;                         (chega-em {:espera [11,22,33,44,55]}, :espera 76)))

    ; O perigo do swap!, teriamos que trabalhar em outro ponto
    ;(is (nil? (chega-em {:espera [11, 22, 33, 44, 55]}, :espera 76)))


    ; desvantagem: não mostra qual foi o erro
    ; desvantagem: trabalhosa
    ;(is (try
    ;      (chega-em {:espera [1,2,,4,5,6,90]}, :espera, 78)
    ;      false
    ;      (catch clojure.lang.ExceptionInfo e
    ;        (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e)))
    ;        )
    ;      ))

    ;(is (= {:hospital {:espera [11, 33, 55, 66, 77, 99]}, :resultado :fila-cheia}
    ;        (chega-em {:espera [11, 33, 55, 66, 77, 99]}, :espera 176)))

    (is (try
          (chega-em {:espera (conj t.model/fila-vazia "1", "2" "43" "9" "12" "123")}, :espera, "78")
          false
          (catch clojure.lang.ExceptionInfo e
            (= :fila-cheia (:erro (ex-data e)))))))
  )


(deftest transfere-test
  (testing "aceita pessoas se cabe"
    (let [hospital-original {:espera (conj t.model/fila-vazia "5"),
                             :raio-x t.model/fila-vazia}
          ]
      (is (= {:espera []
              :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x)))
      )

    (let [hospital-original {:espera (conj t.model/fila-vazia "51" "5"),
                             :raio-x (conj t.model/fila-vazia "13")}
          hospital-esperado {:espera (conj t.model/fila-vazia "5")
                             :raio-x (conj t.model/fila-vazia "13" "51")}
          ]
      (is (= hospital-esperado
             (transfere hospital-original :espera :raio-x)))
      )
    )

  (testing "recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj t.model/fila-vazia "5"),
                          :raio-x (conj t.model/fila-vazia "2" "43" "41" "1" "10")}]

      (is (try
            (transfere hospital-cheio :espera :raio-x)
            false
            (catch clojure.lang.ExceptionInfo e
              (= :fila-cheia (:erro (ex-data e))))))))

  ;JEITO retornando objeto com resultado ao
  ;invés de lançar exception
  ; Trouxe algumas complexidades e dificuldades pra lidar com schemas
  ;(is (= {:hospital  hospital-cheio
  ;        :resultado :fila-cheia}
  ;       (transfere hospital-cheio :espera :raio-x)))


  ; caso alguém apague alguma restrição no schema, pode NÃO falhar algum teste
  ; e depois alguém usar uma função de forma errada e estourar em PRD
  ; Como pro meu domínio algumas informações do schema são cruciais,
  ; quero evitar que isso aconteça
  (testing "Não pode invocar transfere sem passar hospital"
    (is (thrown? clojure.lang.ExceptionInfo
                 (transfere nil :espera :raio-x))))

  (testing "Parametros obrigatórios"
    (let [hospital-cheio {:espera (conj t.model/fila-vazia "5"),
                          :raio-x (conj t.model/fila-vazia "1" "10")}]
      (is (thrown? AssertionError
                   (transfere hospital-cheio :nao-existe :raio-x)))

      (is (thrown? AssertionError
                   (transfere hospital-cheio :raio-x :nao-existe)))
      )))