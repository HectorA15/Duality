
package duality.util;

import duality.model.Restriction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class EquationParser {

        private static final Pattern FULL_EQUATION_PATTERN = Pattern.compile(
                "(-?[0-9]*\\.?[0-9]*)x([+-][0-9]*\\.?[0-9]*)y=(-?[0-9]*\\.?[0-9]*)"
        );
        private static final Pattern SIMPLE_X_FUNCTION_PATTERN = Pattern.compile(
                "(-?[0-9]*\\.?[0-9]*)x"
        );
        private static final Pattern SIMPLE_Y_FUNCTION_PATTERN = Pattern.compile(
                "(-?[0-9]*\\.?[0-9]*)y"
        );
        private static final Pattern HORIZONTAL_LINE_PATTERN = Pattern.compile("y=(-?[0-9]*\\.?[0-9]*)");
        private static final Pattern VERTICAL_LINE_PATTERN = Pattern.compile("x=(-?[0-9]*\\.?[0-9]*)");

        public static Restriction parse(String input) {
            String sanitizedInput = input.trim().toLowerCase().replace(" ", "");
            if (sanitizedInput.isEmpty()) return null;

            Matcher fullMatcher = FULL_EQUATION_PATTERN.matcher(sanitizedInput);
            if (fullMatcher.matches()) {
                try {
                    double xCoeff = parseCoeff(fullMatcher.group(1));
                    double yCoeff = parseCoeff(fullMatcher.group(2));
                    double limit = Double.parseDouble(fullMatcher.group(3));
                    return new Restriction(xCoeff, yCoeff, limit);
                } catch (NumberFormatException e) { return null; }
            }

            Matcher simpleXMatcher = SIMPLE_X_FUNCTION_PATTERN.matcher(sanitizedInput);
            if (simpleXMatcher.matches()) {
                try {
                    double xCoeff = parseCoeff(simpleXMatcher.group(1));
                    return new Restriction(xCoeff, -1, 0);
                } catch (NumberFormatException e) { return null; }
            }

            Matcher simpleYMatcher = SIMPLE_Y_FUNCTION_PATTERN.matcher(sanitizedInput);
            if (simpleYMatcher.matches()) {
                try {
                    double yCoeff = parseCoeff(simpleYMatcher.group(1));
                    return new Restriction(1, -yCoeff, 0);
                } catch (NumberFormatException e) { return null; }
            }

            Matcher horizontalMatcher = HORIZONTAL_LINE_PATTERN.matcher(sanitizedInput);
            if (horizontalMatcher.matches()) {
                try {
                    double limit = Double.parseDouble(horizontalMatcher.group(1));
                    return new Restriction(0, 1, limit);
                } catch (NumberFormatException e) { return null; }
            }

            Matcher verticalMatcher = VERTICAL_LINE_PATTERN.matcher(sanitizedInput);
            if (verticalMatcher.matches()) {
                try {
                    double limit = Double.parseDouble(verticalMatcher.group(1));
                    return new Restriction(1, 0, limit);
                } catch (NumberFormatException e) { return null; }
            }

            return null;
        }

        private static double parseCoeff(String coeffStr) {
            if (coeffStr == null || coeffStr.isEmpty() || coeffStr.equals("+")) return 1.0;
            if (coeffStr.equals("-")) return -1.0;
            return Double.parseDouble(coeffStr);
        }
    }