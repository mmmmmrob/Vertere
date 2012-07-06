/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Resource;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author RobSt
 */
public class Processor {

    public static String feetToMetres(String feetString) {
        if (feetString.length() > 0) {
            double feet = Double.parseDouble(feetString);
            DecimalFormat df = new DecimalFormat("#.#############");
            return df.format(feet * 0.3048);
        } else {
            return feetString;
        }
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
        value = value.toLowerCase();
        return WordUtils.capitalize(value);
    }

    static String trimQuotes(String newValue) {
        while (newValue.startsWith("\"") || newValue.endsWith("\"")) {
            newValue = newValue.replaceAll("^\"|\"$", "");
        }
        return newValue;
    }

    static String trim(String newValue) {
        return newValue.trim();
    }

    static String sha512(String value, Spec spec, Resource resource) {
        String salt = spec.getSalt(resource);
        return DigestUtils.sha512Hex(salt + value);
    }

    static String sql_date(String newValue) {
        String[] incomingFormats = new String[]{
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss"
        };
        try {
            for (int i = 0; i < incomingFormats.length; i++) {
                if (newValue.length() == incomingFormats[i].length()) {
                    SimpleDateFormat sqlDateFormat = new SimpleDateFormat(incomingFormats[i]);
                    Date date = sqlDateFormat.parse(newValue);
                    SimpleDateFormat xsdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return xsdDateFormat.format(date);
                }
            }
            return newValue;
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    static String urlify(String newValue) {
        newValue = trim(newValue);
        newValue = newValue.toLowerCase();
        try {
            newValue = URLEncoder.encode(newValue, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return newValue;
    }
}
