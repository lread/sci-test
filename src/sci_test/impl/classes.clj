;; copied from babashka, turfed features, moved reflection.json generation to its own class
(ns sci-test.impl.classes
  {:no-doc true})

(def custom-map
  (cond->
      `{clojure.lang.LineNumberingPushbackReader {:allPublicConstructors true
                                                  :allPublicMethods true}
        java.lang.Thread
        {:allPublicConstructors true
         ;; generated with `public-declared-method-names`, see in
         ;; `comment` below
         :methods [{:name "activeCount"}
                   {:name "checkAccess"}
                   {:name "currentThread"}
                   {:name "dumpStack"}
                   {:name "enumerate"}
                   {:name "getAllStackTraces"}
                   {:name "getContextClassLoader"}
                   {:name "getDefaultUncaughtExceptionHandler"}
                   {:name "getId"}
                   {:name "getName"}
                   {:name "getPriority"}
                   {:name "getStackTrace"}
                   {:name "getState"}
                   {:name "getThreadGroup"}
                   {:name "getUncaughtExceptionHandler"}
                   {:name "holdsLock"}
                   {:name "interrupt"}
                   {:name "interrupted"}
                   {:name "isAlive"}
                   {:name "isDaemon"}
                   {:name "isInterrupted"}
                   {:name "join"}
                   {:name "run"}
                   {:name "setContextClassLoader"}
                   {:name "setDaemon"}
                   {:name "setDefaultUncaughtExceptionHandler"}
                   {:name "setName"}
                   {:name "setPriority"}
                   {:name "setUncaughtExceptionHandler"}
                   {:name "sleep"}
                   {:name "start"}
                   {:name "toString"}
                   {:name "yield"}]}
        java.net.URL
        {:allPublicConstructors true
         :allPublicFields true
         ;; generated with `public-declared-method-names`, see in
         ;; `comment` below
         :methods [{:name "equals"}
                   {:name "getAuthority"}
                   {:name "getContent"}
                   {:name "getDefaultPort"}
                   {:name "getFile"}
                   {:name "getHost"}
                   {:name "getPath"}
                   {:name "getPort"}
                   {:name "getProtocol"}
                   {:name "getQuery"}
                   {:name "getRef"}
                   {:name "getUserInfo"}
                   {:name "hashCode"}
                   {:name "openConnection"}
                   {:name "openStream"}
                   {:name "sameFile"}
                   ;; not supported: {:name "setURLStreamHandlerFactory"}
                   {:name "toExternalForm"}
                   {:name "toString"}
                   {:name "toURI"}]}
        java.util.Arrays
        {:methods [{:name "copyOf"}]}}))

(def classes
  `{:all [clojure.lang.BigInt
          clojure.lang.ExceptionInfo
          java.io.BufferedReader
          java.io.BufferedWriter
          java.io.ByteArrayInputStream
          java.io.ByteArrayOutputStream
          java.io.Console
          java.io.File
          java.io.InputStream
          java.io.IOException
          java.io.OutputStream
          java.io.FileReader
          java.io.InputStreamReader
          java.io.PushbackInputStream
          java.io.Reader
          java.io.SequenceInputStream
          java.io.StringReader
          java.io.StringWriter
          java.io.Writer
          java.lang.ArithmeticException
          java.lang.AssertionError
          java.lang.Boolean
          java.lang.Byte
          java.lang.Character
          java.lang.Class
          java.lang.ClassNotFoundException
          java.lang.Comparable
          java.lang.Double
          java.lang.Exception
          java.lang.Float
          java.lang.IndexOutOfBoundsException ;; added by Lee
          java.lang.Integer
          java.lang.Long
          java.lang.Number
          java.lang.NumberFormatException
          java.lang.Math
          java.lang.Object
          java.lang.Process
          java.lang.ProcessBuilder
          java.lang.ProcessBuilder$Redirect
          java.lang.Runtime
          java.lang.RuntimeException
          java.lang.Short
          java.lang.String
          java.lang.StringBuilder
          java.lang.System
          java.lang.Throwable
          java.math.BigDecimal
          java.math.BigInteger
          java.net.DatagramSocket
          java.net.DatagramPacket
          java.net.HttpURLConnection
          java.net.InetAddress
          java.net.ServerSocket
          java.net.Socket
          java.net.UnknownHostException
          java.net.URI
          ;; java.net.URL, see below
          java.net.URLEncoder
          java.net.URLDecoder

          java.security.MessageDigest

          java.util.concurrent.LinkedBlockingQueue
          java.util.jar.JarFile
          java.util.jar.JarEntry
          java.util.jar.JarFile$JarFileEntry
          java.util.Random
          java.util.regex.Pattern
          java.util.Base64
          java.util.Base64$Decoder
          java.util.Base64$Encoder
          java.util.Date
          java.util.Locale
          java.util.Map
          java.util.MissingResourceException
          java.util.Properties
          java.util.Set
          java.util.UUID
          java.util.concurrent.TimeUnit
          java.util.zip.InflaterInputStream
          java.util.zip.DeflaterInputStream
          java.util.zip.GZIPInputStream
          java.util.zip.GZIPOutputStream
          ~(symbol "[B")

          ]
    :constructors [clojure.lang.Delay
                   clojure.lang.MapEntry
                   clojure.lang.LineNumberingPushbackReader
                   clojure.lang.PersistentHashMap ;; added by Lee for macro test, can remove
                   java.io.EOFException
                   java.io.PrintWriter
                   java.io.PushbackReader]
    :fields [clojure.lang.PersistentQueue]
    :instance-checks [clojure.lang.IObj
                      clojure.lang.IEditableCollection
                      clojure.lang.IMapEntry
                      clojure.lang.IPersistentMap
                      clojure.lang.IPersistentSet
                      clojure.lang.IPersistentVector
                      clojure.lang.IRecord
                      clojure.lang.ISeq
                      clojure.lang.Named
                      clojure.lang.Keyword
                      clojure.lang.Symbol
                      clojure.lang.Sequential]
    :custom ~custom-map})

(defmacro gen-class-map []
  (let [classes (concat (:all classes)
                        (keys (:custom classes))
                        (:constructors classes)
                        (:methods classes)
                        (:fields classes)
                        (:instance-checks classes))
        m (apply hash-map
                 (for [c classes
                       c [(list 'quote c) c]]
                   c))]
    (assoc m :public-class
           (fn [v]
             (cond (instance? java.lang.Process v)
                   java.lang.Process
                   ;; added for calling .put on .environment from ProcessBuilder
                   (instance? java.util.Map v)
                   java.util.Map
                   ;; added for issue #239 regarding clj-http-lite
                   (instance? java.io.ByteArrayOutputStream v)
                   java.io.ByteArrayOutputStream
                   (instance? java.security.MessageDigest v)
                   java.security.MessageDigest
                   ;; streams
                   (instance? java.io.InputStream v)
                   java.io.InputStream
                   (instance? java.io.OutputStream v)
                   java.io.OutputStream
                   ;; java nio
                   (instance? java.nio.file.Path v)
                   java.nio.file.Path
                   (instance? java.nio.file.FileSystem v)
                   java.nio.file.FileSystem
                   (instance? java.nio.file.PathMatcher v)
                   java.nio.file.PathMatcher)))))

(def class-map (gen-class-map))
