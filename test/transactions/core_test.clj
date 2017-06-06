(ns transactions.core-test
  (:require [expectations :refer :all]
            [transactions.core :refer :all]
            [datomic.api :as d]))

(defn create-empty-in-memory-db []
  (let [uri "datomic:mem:/transactions_model_db"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
          (d/transact conn schema)
          conn)))

(expect #{["mrx" "0"] ["mrx1" "1"]}
  (with-redefs [conn (create-empty-in-memory-db)]
    (do
      (add-user "678d88b2-87b0-403b-b63d-5da7465aecc3" "mrx" "0")
      (add-user "ba550d0e-adac-4864-b88b-407cab5e76af" "mrx1" "1")
      (find-all-users))))

(expect "0"
  (with-redefs [conn (create-empty-in-memory-db)]
    (do
      (add-user "678d88b2-87b0-403b-b63d-5da7465aecc3" "mrx" "0")
      (add-user "ba550d0e-adac-4864-b88b-407cab5e76af" "mrx1" "1")
      (find-user-by-gid "678d88b2-87b0-403b-b63d-5da7465aecc3"))))

(expect "mrx"
  (with-redefs [conn (create-empty-in-memory-db)]
    (do
      (add-user "678d88b2-87b0-403b-b63d-5da7465aecc3" "mrx" "0")
      (add-user "ba550d0e-adac-4864-b88b-407cab5e76af" "mrx1" "1")
      (find-user-by-name "mrx"))))
