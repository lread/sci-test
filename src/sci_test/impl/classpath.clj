(ns sci-test.impl.classpath
  {:no-doc true}
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [java.util.jar JarFile]))

(set! *warn-on-reflection* true)

(defprotocol IResourceResolver
  (getResource [this paths opts])
  (getResources [this paths opts]))

(deftype DirectoryResolver [path]
  IResourceResolver
  (getResource [this resource-paths {:keys [:url?]}]
    (some
     (fn [resource-path]
       (let [f (io/file path resource-path)]
         (when (.exists f)
           (if url?
             ;; manual conversion, faster than going through .toURI
             (java.net.URL. "file" nil (.getCanonicalPath f))
             {:file (.getCanonicalPath f)
              :source (slurp f)}))))
     resource-paths)))

(defn path-from-jar
  [^java.io.File jar-file resource-paths {:keys [:url?]}]
  (with-open [jar (JarFile. jar-file)]
    (some (fn [path]
            (when-let [entry (.getEntry jar path)]
              (if url?
                ;; manual conversion, faster than going through .toURI
                (java.net.URL. "jar" nil
                 (str "file:" (.getCanonicalPath jar-file) "!/" path))
                {:file path
                 :source (slurp (.getInputStream jar entry))})))
          resource-paths)))

(deftype JarFileResolver [jar-file]
  IResourceResolver
  (getResource [this resource-paths opts]
    (path-from-jar jar-file resource-paths opts)))

(defn part->entry [part]
  (if (str/ends-with? part ".jar")
    (JarFileResolver. (io/file part))
    (DirectoryResolver. (io/file part))))

(deftype Loader [entries]
  IResourceResolver
  (getResource [this resource-paths opts]
    (some #(getResource % resource-paths opts) entries))
  (getResources [this resource-paths opts]
    (keep #(getResource % resource-paths opts) entries)))

(defn loader [^String classpath]
  (let [parts (.split classpath (System/getProperty "path.separator"))
        entries (map part->entry parts)]
    (Loader. entries)))

(defn source-for-namespace [loader namespace opts]
  (let [ns-str (name namespace)
        ^String ns-str (munge ns-str)
        ;; do NOT pick the platform specific file separator here, since that doesn't work for searching in .jar files
        ;; (io/file "foo" "bar/baz") does work on Windows, despite the forward slash
        base-path (.replace ns-str "." "/")
        resource-paths (mapv #(str base-path %) [".bb" ".clj" ".cljc"])]
    (getResource loader resource-paths opts)))
