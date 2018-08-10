package com.century.logssender.template;

import com.century.logssender.model.Template;
import com.century.logssender.template.contract.TemplateParser;
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
    private final TemplateParser templateParser;

    private Disposable watchSubscription;

    @Autowired
    public TemplateManager(TemplateFilesService filesService, TemplateParser templateParser) {
        this.filesService = filesService;
        this.templateParser = templateParser;
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
                .observeOn(Schedulers.newThread())
                .map(this.templateParser::parseTemplate)
                .subscribe(this.templates::add);
    }

}
