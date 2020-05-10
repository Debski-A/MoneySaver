package com.debski.c4;

import com.structurizr.model.Container;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import lombok.Value;
import lombok.experimental.Accessors;

import static com.debski.c4.C4ModelApp.DATABASE_TAG;

@Value
@Accessors(fluent = true)
public class C2_ContainerDiagram implements ViewGenerator {

    Container accountService;

    Container calculationService;

    Container authService;

    Container configServer;

    Container eurekaServer;

    Container gatewayAPI;

    Container accountsDatabase;

    Container authTokensDatabase;

    Container reactUI;

    C1_ContextDiagram c1ContextDiagram;

    C2_ContainerDiagram(C1_ContextDiagram c1ContextDiagram) {
        this.c1ContextDiagram = c1ContextDiagram;
        SoftwareSystem moneySaver = c1ContextDiagram.moneySaver();
        Person user = c1ContextDiagram.user();
        SoftwareSystem exchangeRatesAPI = c1ContextDiagram.exchangeRatesAPI();

        accountService = accountService(moneySaver);
        calculationService = calculationService(moneySaver);
        authService = authService(moneySaver);
        configServer = configServer(moneySaver);
        eurekaServer = eurekaServer(moneySaver);
        gatewayAPI = gatewayAPI(moneySaver);
        accountsDatabase = accountsDatabase(moneySaver);
        authTokensDatabase = authTokensDatabase(moneySaver);
        reactUI = reactUI(moneySaver);

        defineContainerDependencies();
    }

    private Container accountService(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("Account Service", "Creating and managing account", "Java, Spring Cloud, Spring Web");
    }

    private Container calculationService(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("Calculation Service", "Provides calculations based on account incomes and outcomes data", "Java, Spring Cloud, Spring Web");
    }

    private Container authService(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("Authentication/Authorization Service", "Provides OAuth2 protocol for MoneySaver", "Java, Spring Cloud, OAuth2");
    }

    private Container configServer(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("Configuration Server", "Provides centralized configuration storage for MoneySaver", "Java, Spring Cloud Config Server");
    }

    private Container eurekaServer(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("Eureka Server", "Provides service discovery for MoneySaver", "Java, Spring Cloud Eureka");
    }

    private Container gatewayAPI(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("API Gateway", "Backend services entry point", "Java, Spring Cloud Gateway");
    }

    private Container accountsDatabase(SoftwareSystem moneySaver) {
        Container container = moneySaver.addContainer("Accounts Database", "Accounts data, hashed passwords, incomes, outcomes, etc.", "MySQL");
        moneySaver.addTags(DATABASE_TAG);
        return container;
    }

    private Container authTokensDatabase(SoftwareSystem moneySaver) {
        Container container = moneySaver.addContainer("Tokens Database", "Authentication/Authorization tokens database", "MySQL");
        moneySaver.addTags(DATABASE_TAG);
        return container;
    }

    private Container reactUI(SoftwareSystem moneySaver) {
        return moneySaver.addContainer("UI", "User Interface", "React.js");
    }

    private void defineContainerDependencies() {
        SoftwareSystem moneySaver = c1ContextDiagram.moneySaver();
        SoftwareSystem exchangeRatesAPI = c1ContextDiagram.exchangeRatesAPI();
        Person user = c1ContextDiagram.user();

        user.uses(reactUI, "Uses", "browser/HTTPS");

        reactUI.uses(gatewayAPI, "Accounts/Auth/Calculations", "JSON/HTTP");

        gatewayAPI.uses(configServer, "Uses", "YAML/HTTP");
        gatewayAPI.uses(eurekaServer, "Subscribes", "HTTP");
        gatewayAPI.uses(accountService, "Redirects", "JSON/HTTP");
        gatewayAPI.uses(calculationService, "Redirects", "JSON/HTTP");
        gatewayAPI.uses(authService, "Redirects", "JSON/HTTP");

        eurekaServer.uses(configServer, "Uses", "YAML/HTTP");
//        eurekaServer.uses(accountService, "Health checks", "HTTP");
//        eurekaServer.uses(calculationService, "Health checks", "HTTP");
//        eurekaServer.uses(authService, "Health checks", "HTTP");
//        eurekaServer.uses(gatewayAPI, "Health checks", "HTTP");

        accountService.uses(configServer, "Uses", "YAML/HTTP");
        accountService.uses(eurekaServer, "Subscribes", "HTTP");
        accountService.uses(authService, "Auth", "JSON/HTTP");
        accountService.uses(accountsDatabase, "Calls", "JDBC");

        calculationService.uses(configServer, "Uses", "YAML/HTTP");
        calculationService.uses(eurekaServer, "Subscribes", "HTTP");
        calculationService.uses(authService, "Auth", "JSON/HTTP");
        calculationService.uses(accountService, "Calls", "JSON/HTTP");
        calculationService.uses(exchangeRatesAPI, "Calls", "JSON/HTTP");

        authService.uses(configServer, "Uses", "YAML/HTTP");
        authService.uses(eurekaServer, "Subscribes", "HTTP");
        authService.uses(accountsDatabase, "Calls", "JDBC");
        authService.uses(authTokensDatabase, "Calls", "JDBC");
    }


    @Override
    public void generateView(Organization organization) {
        ContainerView containerView = organization.views().createContainerView(
                c1ContextDiagram.moneySaver(),
                "Containers",
                "Containers Diagram of MoneySaver architecture"
        );
        containerView.add(c1ContextDiagram.user());
        containerView.addAllContainers();
        containerView.add(c1ContextDiagram.exchangeRatesAPI());

        containerView.addAnimation(
                c1ContextDiagram.user(),
                c1ContextDiagram.exchangeRatesAPI(),
                reactUI,
                accountService,
                calculationService,
                authService,
                gatewayAPI,
                configServer,
                eurekaServer,
                accountsDatabase,
                authTokensDatabase
        );
    }
}
