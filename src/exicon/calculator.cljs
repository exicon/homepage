(ns exicon.calculator
  (:require-macros
    [tailrecursion.javelin :refer [defc defc= cell=]]))

(defn tbl [keys & vals]
  (map (partial zipmap keys) (partition (count keys) vals)))

(def app-name-item {:id :app-name :type "text"})

(def size-items
  (tbl [:value :title :description :image]
       :small "Small" "(1-10 pages)" "size-small.png"
       :medium "Medium" "(11-20 pages)" "size-medium.png"
       :large "Large" "(20+ pages)" "size-large.png"))

(def login-items
  (tbl [:value :title :image]
       :social-login "Social media and email" "login-social.png"
       :email "Email only" "login-email.png"
       :no "They won't sign in" "login-no.png"))

(def features-items
  (tbl [:value :title :image]
       :ar "Augmen\u00adted Reality" "augmented-reality.png"
       :commerce "Commerce" "commerce.png"
       :communication "Communi\u00adcation" "communication.png"
       :game "Gami\u00adfication" "gamification.png"
       :location "Location" "location.png"
       :news "News\u00adfeed" "newsfeed.png"
       :ratings "Ratings" "ratings.png"
       :social "Social Media" "social-media.png"
       :sync ["Synch\u00adroni\u00adzation"] "sync.png"))

(def platforms-items
  (tbl [:value :title]
       :iphone "iPhone"
       :wearables "Apple Watch"
       :ipad "iPad"
       :android-phone "Android Phone"
       :android-tablet "Android Tablet"
       :desktop "PC/Mac"
       :windows-phone "Windows Phone"
       :windows-tablet "Windows Tablet"
       :blackberry "Blackberry"))

(def stage-items
  (tbl [:value :title :image]
       :idea "Idea" "idea.png"
       :sketch "Sketches" "sketches.png"
       :project "Full Project Specifi\u00adcation"
       "full-project-spec.png"
       :prototype "Prototype" "prototype.png"))

(def $ "$")

(def weights {:platforms {
                          :ios 1.3
                          :android 1
                          :desktop 2
                          :blackberry 1.1
                          :windows 1.1
                          :wearables 0.6}

              :size {
                     :small 1.3
                     :medium 1.7
                     :large 2.7}

              :login {
                      :social-login 12000
                      :email 10000
                      :no 8000}

              :features {
                         :ar 20000
                         :commerce 10000
                         :communication 10000
                         :game 5000
                         :location 4000
                         :news 1500
                         :ratings 3500
                         :social 4000
                         :sync 8000}

              :stage {
                      :idea 1.2
                      :sketch 1.1
                      :project 0.95
                      :prototype 0.85}

              :range 1500})

(defc current-submission nil)
(defc selected-platforms #{})
(defc selected-size nil)
(defc selected-login nil)
(defc selected-features #{})
(defc selected-languages 1)
(defc selected-stage nil)

(defn make-hash [obj]
  (if (coll? obj) obj #{obj}))

(defn value-title [value items]
  (let [restruct {}]
    (map
      (apply assoc restruct
             (flatten (map #(vector (:value %) (:title %)) items)))
      (make-hash value))))

(defc= result
  (let [weight (fn [& keys] (get-in weights keys))
        ios-platform (if
                       (or (selected-platforms :iphone)
                           (selected-platforms :ipad))
                       (weight :platforms :ios) 0)
        wearables-platform (if (selected-platforms :wearables)
                             (weight :platforms :wearables) 0)
        android-platform (if
                           (or (selected-platforms :android-phone)
                               (selected-platforms :android-tablet))
                           (weight :platforms :android) 0)
        windows-platform (if
                           (or (selected-platforms :windows-phone)
                               (selected-platforms :windows-tablet))
                           (weight :platforms :windows) 0)
        blackberry-platform (if (selected-platforms :blackberry)
                              (weight :platforms :blackberry) 0)
        desktop-platform (if (selected-platforms :desktop)
                           (weight :platforms :desktop) 0)
        platforms (+ ios-platform
                     android-platform
                     windows-platform
                     blackberry-platform
                     desktop-platform
                     wearables-platform)
        size (weight :size selected-size)
        login (weight :login selected-login)
        languages (+ 1 (* 0.3 (- selected-languages 1)))
        features (apply + (map #(weight :features %)
                               selected-features))
        stage (weight :stage selected-stage)
        cost-range (weight :range)
        cost (int (* (+ features login) platforms languages size stage))
        ;lower-cost (if (>= cost cost-range) (- cost cost-range) 0)

        u-cost (if (zero? cost) 0 (int (+ cost cost-range)))
        upper-cost (if (< u-cost 25000) 25000 (int (+ cost cost-range)))
        l-cost (if (zero? cost) 0 (int (/ upper-cost 2)))
        lower-cost (if (< l-cost 20000) 20000 l-cost)]
    ; For fine tuning the parameters
    #_(prn "Sub results"
           features login platforms languages size stage lower-cost upper-cost)
    [lower-cost upper-cost]))

(defc= cost-calculator
  {:platforms selected-platforms
   :size selected-size
   :login selected-login
   :features selected-features
   :languages selected-languages
   :stage selected-stage
   :result result})
