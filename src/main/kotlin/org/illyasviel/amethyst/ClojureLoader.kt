/*
 * Copyright (c) 2019 the original author or authors.
 * Licensed under the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-2.0
 */

package org.illyasviel.amethyst

import clojure.lang.RT
import clojure.lang.Symbol
import clojure.lang.Var
import com.intellij.openapi.diagnostic.Logger
import java.io.StringWriter

class ClojureLoader {
    companion object {
        private val logger = Logger.getInstance(ClojureLoader::class.java)
        private const val INIT_CLOJURE = "org.illyasviel.amethyst.init-clojure"
    }

    init {
        val oldLoader = Thread.currentThread().contextClassLoader
        try {
            val loader = ClojureLoader::class.java.classLoader
            Thread.currentThread().contextClassLoader = loader

            val writer = StringWriter()

            Class.forName("clojure.lang.RT")

            Var.pushThreadBindings(RT.map(clojure.lang.Compiler.LOADER, loader,
                    RT.`var`("clojure.core", "*warn-on-reflection*"), true,
                    RT.ERR, writer))

            RT.`var`("clojure.core", "require").invoke(Symbol.intern(INIT_CLOJURE))
            Var.find(Symbol.intern("$INIT_CLOJURE/init")).invoke()

            val result = writer.toString()
            if (result.isNotEmpty()) {
                logger.error("Reflection warnings:\n$result")
            }
            initCaller()
        } catch (e: Exception) {
            logger.error(e.message, e)
        } finally {
            Var.popThreadBindings()
            Thread.currentThread().contextClassLoader = oldLoader
        }
    }

    private fun initCaller() {
        CleanNsUtils.initCaller()
    }
}
