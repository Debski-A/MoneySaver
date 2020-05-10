package com.debski.c4;

import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
class C1_ContextDiagram implements ViewGenerator {

    Person user;

    SoftwareSystem moneySaver;

    SoftwareSystem exchangeRatesAPI;

    C1_ContextDiagram(Organization organization) {
        Model model = organization.model();
        user = user(model);
        moneySaver = moneySaver(model);
        exchangeRatesAPI = exchangeRatesAPI(model);
        defineSystemDependencies();
    }

    private Person user(Model model) {
        return model.addPerson(
                Location.External,
                "User",
                "User interacting with MoneySaver"
        );
    }

    private SoftwareSystem moneySaver(Model model) {
        return model.addSoftwareSystem(
                Location.Internal,
                "MoneySaver",
                "Application for managing home budget by registered users"
        );
    }

    private SoftwareSystem exchangeRatesAPI(Model model) {
        return model.addSoftwareSystem(
                Location.External,
                "Exchange rates API",
                "Exchange rates calculation service"
        );
    }

    private void defineSystemDependencies() {
        user.uses(moneySaver, "Uses");
        moneySaver.uses(exchangeRatesAPI, "Uses", "JSON/HTTPS");
    }

    @Override
    public void generateView(Organization organization) {
        SystemContextView systemContextView = organization.views().createSystemContextView(
                moneySaver,
                "SystemContext",
                "Context Diagram of MoneySaver architecture");
        systemContextView.addNearestNeighbours(moneySaver);
        systemContextView.addAnimation(
                moneySaver,
                user,
                exchangeRatesAPI
        );
    }
}
