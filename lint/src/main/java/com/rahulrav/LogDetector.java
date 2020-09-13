package com.rahulrav;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.Collections;
import java.util.List;

/**
 * 避免使用Log / System.out.println ,提醒使用Ln
 *
 * RoboGuice's Ln logger is similar to Log, but has the following advantages:
 *     - Debug and verbose logging are automatically disabled for release builds.
 *     - Your app name, file and line of the log message, time stamp, thread, and other useful information is automatically logged for you. (Some of this information is disabled for release builds to improve performance).
 *     - Performance of disabled logging is faster than Log due to the use of the varargs. Since your most expensive logging will often be debug or verbose logging, this can lead to a minor performance win.
 *     - You can override where the logs are written to and the format of the logging.
 * 
 * https://github.com/roboguice/roboguice/wiki/Logging-via-Ln
 *
 * Created by chentong on 18/9/15.
 */
public class LogDetector extends Detector implements SourceCodeScanner {

    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "避免使用Log/System.out.println",
            "使用Ln，防止在正式包打印log",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE)).setAndroidSpecific(true);

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        System.out.println("node = " + node +", method = " + method.toString());
        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isMemberInClass(method, "java.lang.System")) {
            System.out.println("发现了");
        }
//        if (node.toString().startsWith("System.out.println") || method.) {
//                    context.report(ISSUE, node, context.getLocation(node),
//                                       "请使用Ln，避免使用System.out.println");
//                }
        super.visitMethodCall(context, node, method);
    }

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.<String>singletonList("println");
    }
//    private fun reportUsage(
//            context: JavaContext,
//            node: UCallExpression,
//            method: PsiMethod
//    ) {
//        val quickfixData = LintFix.create()
//                .name("Use Log.e()")
//                .replace()
//                .text(method.name)
//                .with("e")
//                .robot(true) // Can be applied automatically.
//                .independent(true) // Does not conflict with other auto-fixes.
//                .build()
//
//        context.report(
//                issue = ISSUE,
//                scope = node,
//                location = context.getCallLocation(
//                        call = node,
//                        includeReceiver = false,
//                        includeArguments = false
//                ),
//                message = "Usage of `Log.wtf()` is prohibited",
//                quickfixData = quickfixData
//        )
//    }



    //    @Override
//    public List<Class<? extends Node>> getApplicableNodeTypes() {
//        return Collections.<Class<? extends Node>>singletonList(MethodInvocation.class);
//    }
//
//    @Override
//    public AstVisitor createJavaVisitor(final JavaContext context) {
//        return new ForwardingAstVisitor() {
//            @Override
//            public boolean visitMethodInvocation(MethodInvocation node) {
//
//                if (node.toString().startsWith("System.out.println")) {
//                    context.report(ISSUE, node, context.getLocation(node),
//                                       "请使用Ln，避免使用System.out.println");
//                    return true;
//                }
//
//                JavaParser.ResolvedNode resolve = context.resolve(node);
//                if (resolve instanceof JavaParser.ResolvedMethod) {
//                    JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) resolve;
//                    // 方法所在的类校验
//                    JavaParser.ResolvedClass containingClass = method.getContainingClass();
//                    if (containingClass.matches("android.util.Log")) {
//                        context.report(ISSUE, node, context.getLocation(node),
//                                       "请使用Ln，避免使用Log");
//                        return true;
//                    }
//                }
//                return super.visitMethodInvocation(node);
//            }
//        };
//    }
}