(ns transactions.core
  (:require [datomic.api :as d]))

(def conn nil)

(defn add-user [gid name age]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :user/gid gid
                      :user/name name
                      :user/age age}]))

(defn find-all-users []
  (d/q '[:find ?name ?age
         :where [?u :user/name ?name]
                [?u :user/age ?age]]
    (d/db conn)))

(defn eid []
  (ffirst (d/q '[:find ?gid
                 :where [_ :user/name "mrx"]
                        [_ :user/gid ?gid]]
            (d/db conn))))

(defn ent []
  [:user/gid (eid)])

(defn find-user-by-name [name]
  (ffirst (d/q '[:find ?user
                 :in $ ?name
                 :where [ ?user :user/name ?name]]
            (d/db conn)
            name)))

(defn find-user-by-gid [gid]
  (d/pull (d/db conn) '[*] [:user/gid gid]))
