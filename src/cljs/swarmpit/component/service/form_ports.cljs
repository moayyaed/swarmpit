(ns swarmpit.component.service.form-ports
  (:require [material.component :as comp]
            [swarmpit.component.state :as state]
            [rum.core :as rum]))

(enable-console-print!)

(def cursor [:page :service :wizard :ports])

(def headers ["Container port" "Protocol" "Host port"])

(defn- format-port-value
  [value]
  (if (zero? value) "" value))

(defn- form-container [value index]
  (comp/table-row-column
    {:name (str "form-container-" index)
     :key  (str "form-container-" index)}
    (comp/form-list-textfield
      {:name     (str "form-container-text-" index)
       :key      (str "form-container-text-" index)
       :type     "number"
       :min      1
       :max      65535
       :value    (format-port-value value)
       :onChange (fn [_ v]
                   (state/update-item index :containerPort (js/parseInt v) cursor))})))

(defn- form-protocol [value index]
  (comp/table-row-column
    {:name (str "form-protocol-" index)
     :key  (str "form-protocol-" index)}
    (comp/form-list-selectfield
      {:name     (str "form-protocol-select-" index)
       :key      (str "form-protocol-select-" index)
       :value    value
       :onChange (fn [_ _ v]
                   (state/update-item index :protocol v cursor))}
      (comp/menu-item
        {:name        (str "form-protocol-tcp-" index)
         :key         (str "form-protocol-tcp-" index)
         :value       "tcp"
         :primaryText "TCP"})
      (comp/menu-item
        {:name        (str "form-protocol-udp-" index)
         :key         (str "form-protocol-udp-" index)
         :value       "udp"
         :primaryText "UDP"}))))

(defn- form-host [value index]
  (comp/table-row-column
    {:name (str "form-host-" index)
     :key  (str "form-host-" index)}
    (comp/form-list-textfield
      {:name     (str "form-host-text-" index)
       :key      (str "form-host-text-" index)
       :type     "number"
       :min      1
       :max      65535
       :value    (format-port-value value)
       :onChange (fn [_ v]
                   (state/update-item index :hostPort (js/parseInt v) cursor))})))

(defn- render-ports
  [item index]
  (let [{:keys [containerPort
                protocol
                hostPort]} item]
    [(form-container containerPort index)
     (form-protocol protocol index)
     (form-host hostPort index)]))

(rum/defc form < rum/reactive []
  (let [ports (state/react cursor)]
    (comp/form-table headers
                     ports
                     render-ports
                     (fn [] (state/add-item {:containerPort 0
                                             :protocol      "tcp"
                                             :hostPort      0} cursor))
                     (fn [index] (state/remove-item index cursor)))))