package com.abs.notification.application;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateRenderer {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{\\s*([\\w.]+)\\s*}}");

    public String render(String template, Map<String, ?> vars) {
        if (template == null || template.isEmpty()) return template;
        Matcher m = PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            Object value = vars == null ? null : vars.get(m.group(1));
            m.appendReplacement(sb, Matcher.quoteReplacement(value == null ? "" : value.toString()));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
