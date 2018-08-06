package com.century.logssender.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
public class Template {

    private String tagName;
    private Set<String> recipients;

    public Template(Set<String> recipients) {
        this.recipients = recipients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return Objects.equals(tagName, template.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }

    public boolean isFor(LogEvent logEvent) {
        return getTagName().toLowerCase().contains(logEvent.getSysLogTag().toLowerCase());
    }
}
