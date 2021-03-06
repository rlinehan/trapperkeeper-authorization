(ns puppetlabs.trapperkeeper.authorization.ring-test
  (:require [clojure.test :refer :all]
            [puppetlabs.trapperkeeper.authorization.ring :as ring]
            [puppetlabs.trapperkeeper.authorization.testutils :as testutils]
            [schema.test :as schema-test]))

(use-fixtures :once schema-test/validate-schemas)

(deftest authorized-authenticated
  (is (true? (-> (testutils/request)
                 (assoc-in [:authorization :authenticated] true)
                 (ring/authorized-authenticated))))
  (is (false? (-> (testutils/request)
                  (assoc-in [:authorization :authenticated] false)
                  (ring/authorized-authenticated)))))

(deftest set-authorized-authenticated
  (is (true? (-> (testutils/request)
                 (ring/set-authorized-authenticated true)
                 (get-in [:authorization :authenticated]))))
  (is (false? (-> (testutils/request)
                  (ring/set-authorized-authenticated false)
                  (get-in [:authorization :authenticated])))))

(deftest authorized-certificate
  (is (identical? testutils/test-domain-cert
                  (-> (testutils/request)
                      (assoc-in [:authorization :certificate]
                                testutils/test-domain-cert)
                      (ring/authorized-certificate)))))

(deftest set-authorized-certificate
  (is (identical? testutils/test-domain-cert
                  (-> (testutils/request)
                      (ring/set-authorized-certificate
                       testutils/test-domain-cert)
                      (get-in [:authorization :certificate])))))

(deftest authorized-name
  (is (= "tester" (-> (testutils/request)
                      (assoc-in [:authorization :name] "tester")
                      (ring/authorized-name))))
  (is (= "" (-> (testutils/request)
                (ring/authorized-name)))))

(deftest set-authorized-name
  (is (= "tester" (-> (testutils/request)
                      (ring/set-authorized-name "tester")
                      (get-in [:authorization :name]))))
  (is (= "" (-> (testutils/request)
                (ring/set-authorized-name nil)
                (get-in [:authorization :name])))))
