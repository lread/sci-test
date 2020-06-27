#!/usr/bin/env bash

set -eou pipefail

NATIVE_IMAGE_XMX="3500m"
TARGET_EXE=testing123
ALIAS="-A:graal"

status-line() {
    script/status-line "$1" "$2"
}

status-line info "Locate Graal native-image"
GRAAL_NATIVE_IMAGE="$(script/graal-find-native-image.sh)"
echo "GraalVM native-image program: ${GRAAL_NATIVE_IMAGE}"

rm -rf .cpcache
rm -rf classes

mkdir -p classes
mkdir -p target

status-line info "Compute classpath"
CLASSPATH="$(clojure ${ALIAS} -Spath)"

status-line info "AOT compile sources"
java -cp "${CLASSPATH}" \
     clojure.main \
     -e "(compile 'sci-test.main)"

status-line info "Generate reflection.json for Graal native-image"
clojure -A:reflection

status-line info "Graal native-image compile AOT"
rm -rf "${TARGET_EXE}"

# an array for args makes fiddling with args easier (can comment out a line during testing)
native_image_args=(
    "-H:Name=${TARGET_EXE}"
    -H:+ReportExceptionStackTraces
    -J-Dclojure.spec.skip-macros=true
    -J-Dclojure.compiler.direct-linking=true
    --initialize-at-run-time=java.lang.Math\$RandomNumberGeneratorHolder
    --initialize-at-build-time
    -H:Log=registerResource:
    --enable-all-security-services
    --verbose
    --no-fallback
    --no-server
    --report-unsupported-elements-at-runtime
    -cp "${CLASSPATH}:classes"
    "-J-Xmx${NATIVE_IMAGE_XMX}"
    sci_test.main)

if [[ "$OSTYPE" == "darwin"* ]]; then
    TIME_CMD="command time -l"
else
    TIME_CMD="command time -v"
fi

# shellcheck disable=SC2086
${TIME_CMD} \
    ${GRAAL_NATIVE_IMAGE} "${native_image_args[@]}"

status-line info "Your executable, sir or madam."
ls -lh ${TARGET_EXE}
