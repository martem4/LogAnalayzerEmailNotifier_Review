package com.century.logssender.template;

import com.century.logssender.model.Template;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

@Service
public class TemplateManager {

    private final Set<Template> templates = new HashSet<>();

    private final TemplateFilesService filesService;
    private final YamlTemplateParser yamlParser;

    private Disposable watchSubscription;

    @Autowired
    public TemplateManager(TemplateFilesService filesService, YamlTemplateParser yamlParser) {
        this.filesService = filesService;
        this.yamlParser = yamlParser;
    }

    @PostConstruct
    public void onInit() {
        subscribeOnTemplates();
    }

    @PreDestroy
    public void onDestroy() {
        if (watchSubscription != null) {
            this.watchSubscription.dispose();
        }
    }

    public Set<Template> getTemplates() {
        return templates;
    }

    private void subscribeOnTemplates() {
        watchSubscription = Observable.merge(filesService.observeChangedTemplates(),
                                             filesService.observeInitialTemplates())
                .map(this.yamlParser::parseTemplate)
                .observeOn(Schedulers.newThread())
                .subscribe(this.templates::add);
    }

}
