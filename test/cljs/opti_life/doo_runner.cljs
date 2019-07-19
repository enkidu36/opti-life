(ns opti-life.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [opti-life.core-test]))

(doo-tests 'opti-life.core-test)

