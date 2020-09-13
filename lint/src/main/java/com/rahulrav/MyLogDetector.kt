package com.rahulrav

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class MyLogDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? =
            listOf(
                    "d"
            )

    override fun visitMethodCall(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            reportUsage(context, node, method)
        }
    }

    private fun reportUsage(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
        val quickfixData = LintFix.create()
                .name("Use Android.log()")
                .replace()
                .text(method.name)
                .with("e")
                .robot(true) // Can be applied automatically.
                .independent(true) // Does not conflict with other auto-fixes.
                .build()

        context.report(
                issue = ISSUE,
                scope = node,
                location = context.getCallLocation(
                        call = node,
                        includeReceiver = false,
                        includeArguments = false
                ),
                message = "Usage of Android自带的",
                quickfixData = quickfixData
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
                MyLogDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                id = "MyError",
                briefDescription = "搜狗加油111",
                explanation = """
                   我就不让
                """.trimIndent(),
                category = Category.CORRECTNESS,
                priority = 5,
                severity = Severity.ERROR,
                implementation = IMPLEMENTATION
        ).setAndroidSpecific(true)
    }
}

