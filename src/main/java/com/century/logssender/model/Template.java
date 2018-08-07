package com.century.logssender.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "recipients")
public class Template {

    private String tagName;
    private Set<String> recipients;

    public boolean isFor(LogEvent logEvent) {
        return getTagName().toLowerCase().contains(logEvent.getSysLogTag().toLowerCase());
    }
}
