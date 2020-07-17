package io.cucumber.cucumberexpressions;

import io.cucumber.cucumberexpressions.Ast.AstNode;
import io.cucumber.cucumberexpressions.Ast.Located;
import io.cucumber.cucumberexpressions.Ast.Token;
import io.cucumber.cucumberexpressions.Ast.Token.Type;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.List;

@API(status = API.Status.STABLE)
public class CucumberExpressionException extends RuntimeException {

    CucumberExpressionException(String message) {
        super(message);
    }

    CucumberExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    static CucumberExpressionException createCaptureGroupParameterTypeMisMatch(TreeRegexp treeRegexp,
            List<ParameterType<?>> parameterTypes, List<Group> argGroups) {
        return new CucumberExpressionException(
                String.format("Expression /%s/ has %s capture groups (%s), but there were %s parameter types (%s)",
                        treeRegexp.pattern().pattern(),
                        argGroups.size(),
                        getGroupValues(argGroups),
                        parameterTypes.size(),
                        getParameterTypeNames(parameterTypes)
                ));
    }

    private static List<String> getParameterTypeNames(List<ParameterType<?>> parameterTypes) {
        List<String> list = new ArrayList<>();
        for (ParameterType<?> type : parameterTypes) {
            String name = type.getName();
            list.add(name);
        }
        return list;
    }

    private static List<Object> getGroupValues(List<Group> argGroups) {
        List<Object> list = new ArrayList<>();
        for (Group argGroup : argGroups) {
            String value = argGroup.getValue();
            list.add(value);
        }
        return list;
    }

    static CucumberExpressionException createMissingEndTokenException(String expression, Type beginToken, Type endToken,
            Token current) {
        return new CucumberExpressionException(message(
                current.start(),
                expression,
                pointAt(current),
                "The '" + beginToken.symbol() + "' does not have a matching '" + endToken.symbol() + "'",
                "If you did not intend to use " + beginToken.purpose() + " you can use '\\" + beginToken
                        .symbol() + "' to escape the " + beginToken.purpose()));
    }

    static CucumberExpressionException createTheEndOfLineCanNotBeEscapedException(String expression) {
        return new CucumberExpressionException(message(
                expression.length(),
                expression,
                pointAt(expression.length()),
                "The end of line can not be escaped",
                "You can use '\\\\' to escape the the '\\'"
        ));
    }

    static CucumberExpressionException createAlternativeIsEmpty(AstNode node, String expression) {
        return new CucumberExpressionException(message(
                node.start(),
                expression,
                pointAt(node),
                "Alternative may not be empty",
                "If you did not mean to use an alternative you can use '\\/' to escape the the '/'"));
    }

    static CucumberExpressionException createParameterIsNotAllowedInAlternative(AstNode node, String expression) {
        return new CucumberExpressionException(message(
                node.start(),
                expression,
                pointAt(node),
                "An alternative may not contain a parameter type",
                "If you did not mean to use an parameter type you can use '\\{' to escape the the '{'"));
    }

    static CucumberExpressionException createParameterIsNotAllowedInOptional(AstNode node, String expression) {
        return new CucumberExpressionException(message(
                node.start(),
                expression,
                pointAt(node),
                "An optional may not contain a parameter type",
                "If you did not mean to use an parameter type you can use '\\{' to escape the the '{'"));
    }

    static CucumberExpressionException createOptionalMayNotBeEmpty(AstNode node, String expression) {
        return new CucumberExpressionException(message(
                node.start(),
                expression,
                pointAt(node),
                "An optional must contain some text",
                "If you did not mean to use an optional you can use '\\(' to escape the the '('"));
    }

    static CucumberExpressionException createAlternativeMayExclusivelyContainOptionals(AstNode node,
            String expression) {
        return new CucumberExpressionException(message(
                node.start(),
                expression,
                pointAt(node),
                "An alternative may not exclusively contain optionals",
                "If you did not mean to use an optional you can use '\\(' to escape the the '('"));
    }

    private static String thisCucumberExpressionHasAProblemAt(int index) {
        return "This Cucumber Expression has a problem at column " + (index + 1) + ":" + "\n";
    }

    static CucumberExpressionException createCantEscape(String expression, int index) {
        return new CucumberExpressionException(message(
                index,
                expression,
                pointAt(index),
                "Only the characters '{', '}', '(', ')', '\\', '/' and whitespace can be escaped",
                "If you did mean to use an '\\' you can use '\\\\' to escape it"));
    }

    private static String message(int index, String expression, StringBuilder pointer, String problem,
            String solution) {
        return thisCucumberExpressionHasAProblemAt(index) +
                "\n" +
                expression + "\n" +
                pointer + "\n" +
                problem + ".\n" +
                solution;
    }

    private static StringBuilder pointAt(Located node) {
        StringBuilder pointer = pointAt(node.start());
        if (node.start() + 1 < node.end()) {
            for (int i = node.start() + 1; i < node.end() - 1; i++) {
                pointer.append("-");
            }
            pointer.append("^");
        }
        return pointer;
    }

    private static StringBuilder pointAt(int index) {
        StringBuilder pointer = new StringBuilder();
        for (int i = 0; i < index; i++) {
            pointer.append(" ");
        }
        pointer.append("^");
        return pointer;
    }


}
