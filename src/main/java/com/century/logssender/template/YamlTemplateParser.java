package com.century.logssender.template;

import com.century.logssender.model.Template;
import com.century.logssender.template.contract.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class YamlTemplateParser implements TemplateParser {

    private static final Logger logger = LoggerFactory.getLogger(YamlTemplateParser.class);

    private static final String TEMPLATE_PARSING_ERROR_MESSAGE =
            "Problem with templates parsing. Please check yaml correctness";
    private static final String FILE_ERROR_MESSAGE_POSTFIX = "with file - %s";

    private final Yaml yaml;

    public YamlTemplateParser() {
        this.yaml = new Yaml(initYamlConstructor());
    }

    public Template parseTemplate(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return yaml.load(inputStream);
        } catch (IOException e) {
            showParsingError(path, e);
        }

        return null;
    }

    private Constructor initYamlConstructor() {
        Constructor constructor = new Constructor(Template.class);
        TypeDescription typeDescription = new TypeDescription(Template.class);
        typeDescription.addPropertyParameters("recipients", String.class, Object.class);
        constructor.addTypeDescription(typeDescription);
        return constructor;
    }

    private void showParsingError(Path path, IOException e) {
        logger.error(String.format(
                String.join(" ", TEMPLATE_PARSING_ERROR_MESSAGE, FILE_ERROR_MESSAGE_POSTFIX),
                path.getFileName()), e);
    }

}
