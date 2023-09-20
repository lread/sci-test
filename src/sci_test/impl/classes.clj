;; copied from babashka, turfed features, moved reflection.json generation to its own namespace

(ns sci-test.impl.classes
  {:no-doc true})

(defn on-jdk11-plus? []
  (let [major (->> (System/getProperty "java.version")
                   (re-matches #"^(\d+).*")
                   last
                   Long/parseLong)]
    (>= major 11)))

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
        {:methods [{:name "copyOf"}]}}

        ;; added by Lee
        (on-jdk11-plus?)
        (assoc `java.lang.reflect.AccessibleObject
               {:methods [{:name "canAccess"}]})))

(def classes
  `{:all [clojure.lang.ArityException
          clojure.lang.BigInt
          clojure.lang.ExceptionInfo
          java.io.BufferedReader
          java.io.BufferedWriter
          java.io.ByteArrayInputStream
          java.io.ByteArrayOutputStream
          java.io.Console
          java.io.File
          java.io.FileFilter
          java.io.FilenameFilter
          java.io.FileNotFoundException
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
          java.lang.Iterable
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
          java.net.ConnectException
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
          java.security.DigestInputStream
          java.util.concurrent.LinkedBlockingQueue
          java.util.jar.JarFile
          java.util.jar.JarEntry
          java.util.jar.JarFile$JarFileEntry
          java.util.stream.Stream
          java.util.Random
          ;; java.util.regex.Matcher
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
          java.util.zip.ZipInputStream
          java.util.zip.ZipEntry
          ~(symbol "[B")]
    :constructors [clojure.lang.Delay
                   clojure.lang.MapEntry
                   clojure.lang.LineNumberingPushbackReader
                   java.io.EOFException
                   java.io.PrintWriter
                   java.io.PushbackReader]

    :fields [clojure.lang.PersistentQueue]
    :instance-checks [clojure.lang.Cons
                      clojure.lang.Cycle
                      clojure.lang.IObj
                      clojure.lang.IFn
                      clojure.lang.IPending
                      ;; clojure.lang.IDeref
                      ;; clojure.lang.IAtom
                      clojure.lang.IEditableCollection
                      clojure.lang.IMapEntry
                      clojure.lang.IPersistentCollection
                      clojure.lang.IPersistentMap
                      clojure.lang.IPersistentSet
                      ;;clojure.lang.PersistentHashSet ;; temp for meander
                      clojure.lang.IPersistentVector
                      clojure.lang.IRecord
                      clojure.lang.IRef
                      clojure.lang.ISeq
                      clojure.lang.Iterate
                      clojure.lang.LazySeq
                      clojure.lang.Named
                      clojure.lang.Keyword
                      clojure.lang.Repeat
                      clojure.lang.Symbol
                      clojure.lang.Sequential
                      clojure.lang.Seqable
                      java.util.List]
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
                   java.nio.file.PathMatcher
                   (instance? java.util.stream.BaseStream v)
                   java.util.stream.BaseStream)))))

(def class-map (gen-class-map))

;; copied from babashka
(def imports
  '{ArithmeticException java.lang.ArithmeticException
    AssertionError java.lang.AssertionError
    BigDecimal java.math.BigDecimal
    BigInteger java.math.BigInteger
    Boolean java.lang.Boolean
    Byte java.lang.Byte
    Character java.lang.Character
    Class java.lang.Class
    ClassNotFoundException java.lang.ClassNotFoundException
    Comparable java.lang.Comparable
    Double java.lang.Double
    Exception java.lang.Exception
    IllegalArgumentException java.lang.IllegalArgumentException
    IndexOutOfBoundsException java.lang.IndexOutOfBoundsException
    Integer java.lang.Integer
    File java.io.File
    Float java.lang.Float
    Long java.lang.Long
    Math java.lang.Math
    Number java.lang.Number
    NumberFormatException java.lang.NumberFormatException
    Object java.lang.Object
    Runtime java.lang.Runtime
    RuntimeException java.lang.RuntimeException
    Process        java.lang.Process
    ProcessBuilder java.lang.ProcessBuilder
    Short java.lang.Short
    String java.lang.String
    StringBuilder java.lang.StringBuilder
    System java.lang.System
    Thread java.lang.Thread
    Throwable java.lang.Throwable})
