#!/bin/sh

# Runs clean and install on all dependencies.
# Must be run from within the cmr-dev-system folder


cd ../cmr-common-lib
lein do clean, install, clean

cd ../cmr-vdd-spatial-viz
lein do clean, install, clean
# Run as a separate step since it fails in CI but works locally. It's not necessary for CI.
lein compile-coffeescript

cd ../cmr-spatial-lib
lein do clean, install, clean

cd ../cmr-umm-lib
lein do clean, install, clean

cd ../cmr-system-trace-lib
lein do clean, install, clean

cd ../cmr-oracle-lib
lein do clean, install, clean

cd ../cmr-elastic-utils-lib
lein do clean, install, clean

cd ../cmr-transmit-lib
lein do clean, install, clean

cd ../cmr-acl-lib
lein do clean, install, clean

cd ../cmr-es-spatial-plugin
lein do clean, install, clean

cd ../cmr-mock-echo-app
lein do clean, install, clean

cd ../cmr-metadata-db-app
lein do clean, install, clean

cd ../cmr-indexer-app
lein do clean, install, clean

cd ../cmr-index-set-app
lein do clean, install, clean

cd ../cmr-ingest-app
lein do clean, install, clean

cd ../cmr-search-app

# Generate search docs unless skip-docs is passed in as an argument.
if [ "$1" != "skip-docs" ]
  then
  lein generate-docs
fi

lein do clean, install, clean

cd ../cmr-bootstrap-app
lein do clean, install, clean

cd ../cmr-system-int-test
lein do clean, install, clean

cd ../cmr-dev-system
lein clean