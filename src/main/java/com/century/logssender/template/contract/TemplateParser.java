package com.century.logssender.template.contract;

import com.century.logssender.model.Template;

import java.nio.file.Path;

/**
    Contract for any template parsers.
    @see com.century.logssender.template.YamlTemplateParser
 */
public interface TemplateParser {
    Template parseTemplate(Path path);
}
