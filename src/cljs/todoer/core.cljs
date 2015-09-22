(ns ^:figwheel-always todoer.core
  (:require [cljs.core.async :as async :refer [<! >!]]
            [om.core :as om :include-macros true]
            [sablono.core :as sab :include-macros true :refer-macros [html]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defonce app-state
         (atom {:showing    :all
                :todo-items [{:category  nil
                              :text      "Clean the apartment"
                              :complete? false}
                             {:category  nil
                              :text      "Buy some groceries"
                              :complete? false}]}))

(defn enter? [e] (= (.. e -keyCode) 13))



;; Add an item in category ctg with text t
(defn add-item! [ctg txt]
  (swap! app-state
         #(assoc % :todo-items
                   (conj (:todo-items %) {:category ctg,
                                          :text txt,
                                          :complete? false}))))


(defn filter-items [showing items]
  (filter #(or
            (= showing :all)
            (and (= showing :complete) (:complete? %))
            (and (= showing :incomplete) (not (:complete? %))))
          items))




;; Renders a single to-do item
(defn todo-item
  [{:keys [complete? text remove-fn] :as item}]
  (om/component
    (html [:div.todo-item
           [:span.todo-item-checkbox
            [:input
             {:type      "checkbox"
              :checked   complete?
              :on-change (fn [_] (om/transact! item :complete? #(not %)))}]]
           [:span.todo-item-text text]
           [:button.remove-item {:on-click #(remove-fn)} "×"]])))




;; Renders a list of to-do items
(defn todo-list
  [{:keys [todo-items add-item remove-item]} owner]
  (reify
    om/IInitState
    (init-state [_] {:new-item ""})
    om/IRenderState
    (render-state [_ {:keys [new-item]}]
      (html [:div.todo-item-list
             [:div.new-item
              [:span new-item]
              [:input
               {:value       new-item
                :placeholder "What needs to be done?"
                :on-change   #(om/set-state! owner :new-item (.. % -target -value))
                :on-key-down #(when (enter? %)
                               (add-item {:text new-item :complete? false})
                               (om/set-state! owner :new-item ""))}]
              (om/build-all
                todo-item
                (map #(assoc % :remove-fn (partial remove-item %)) todo-items))]]))))




;; Shows a category of TOOD items
(defn categorized-todo-items
  [{:keys [showing, category-name, add-item, remove-item, items] :as todo-items}]
  (let [filtered-items (filter-items showing items)
        ;; When adding a new item, check to see if it starts with a label,
        ;; e.g. "Automotive:" -- if so, file that item in the labeled category
        add-item (fn [i] (if-let [[_ c t] (re-find #"^(\S+):\s*(.+)" (:text i))]
                           (add-item (assoc i :text t :category c))
                           (add-item (assoc i :category category-name))))]
    (om/component
      (html [:div.todo-item-category
             [:h3.todo-category-label (or category-name "Uncategorized")]
             (om/build todo-list {:add-item add-item :remove-item remove-item :todo-items items})]))))






;; Root TO-DO app component:
(defn todo-app [{:keys [todo-items showing] :as data}]
  (om/component
    (let [add-item (fn [i] (om/transact! data :todo-items #(conj % i)))
          remove-item (fn [i] (om/transact! data :todo-items (fn [v] (vec (remove #(= i %) v)))))
          filtered-items (filter-items showing todo-items)
          categorized-items (group-by :category todo-items)]
      (html [:div.todo-app
             #_ (om/build todo-list (assoc data :add-item add-item
                                             :remove-item remove-item
                                             :todo-items filtered-items))

             (om/build-all categorized-todo-items (map (fn [[c i]] {:category-name c
                                                                      :items         i
                                                                      :showing       showing
                                                                      :add-item      add-item
                                                                      :remove-item   remove-item})
                                                         categorized-items))
            #_ [:div.completion-select
              [:button.filter {:on-click #(om/update! data :showing :all)
                               :class    (if (= showing :all) "active")}
               "All"]
              [:button.filter {:on-click #(om/update! data :showing :complete)
                               :class    (if (= showing :complete) "active")}
               "Complete"]
              [:button.filter {:on-click #(om/update! data :showing :incomplete)
                               :class    (if (= showingq :incomplete) "active")}
               "Incomplete"]]]))))



(defn start! []
  (om/root todo-app app-state {:target (js/document.querySelector "#app")}))

(defn on-js-reload [] (start!))

(start!)
