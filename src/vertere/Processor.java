/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Resource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Normalizer;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author RobSt
 */
public class Processor {

    public static String feetToMetres(String feetString) {
        double feet = Double.parseDouble(feetString);
        DecimalFormat df = new DecimalFormat("#.#############");
        return df.format(feet * 0.3048);
    }

    static String flattenUtf8(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized;
    }

    static String normalise(String value) {
        value = value.trim();
        value = value.replaceAll(" ", "_");
        value = value.toLowerCase();
        return value;
    }

    static String regex(String value, Spec spec, Resource resource) {
        String regexMatch = spec.getRegexMatch(resource);
        String regexOutput = spec.getRegexOutput(resource);

//        throw new RuntimeException("\n\nRegex\n**-" + regexMatch + "-**\n**-" + regexOutput + "-**\n\n");
        return value.replaceAll(regexMatch, regexOutput);
    }

    static String round(String value) {
        double number = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(number);
    }

    static String substr(String value, Spec spec, Resource resource, Resource processStep) {
        Resource whereToFindSettings;
        if (processStep.equals(Vertere.Processes.substr)) {
            whereToFindSettings = resource;
        } else {
            whereToFindSettings = processStep;
        }
        int start = spec.getSubstrStart(whereToFindSettings);
        int length = spec.getSubstrLength(whereToFindSettings);
        if (start >= 0 && length >= 0) {
            return value.substring(start, start + length);
        } else {
            return value;
        }
    }

    static String titleCase(String value) {
        return StringUtils.capitalize(value);
    }

    static String trimQuotes(String newValue) {
        while (newValue.startsWith("\"") || newValue.endsWith("\"")) {
            newValue = newValue.replaceAll("^\"|\"$", "");
        }
        return newValue;
    }
}
