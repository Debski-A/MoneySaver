package com.debski.c4;

import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.ViewSet;

public class C4ModelApp {

    final static String DATABASE_TAG = "DB";

    public static void main(String[] args) throws StructurizrClientException {
        Organization organization = new Organization("MoneySaver","System model", "MoneySaver workspace");
        C1_ContextDiagram c1ContextDiagram = new C1_ContextDiagram(organization);
        c1ContextDiagram.generateView(organization);

        C2_ContainerDiagram c2ContainerDiagram = new C2_ContainerDiagram(c1ContextDiagram);
        c2ContainerDiagram.generateView(organization);

        addStructurizrStylilng(organization.views());

        StructurizrClient structurizrClient = new StructurizrClient("0bcdafdd-bf67-4744-aed3-d35f4c1192b0", "4d5616f9-25aa-4b40-8167-74eeec980f42");
        structurizrClient.putWorkspace(52818, organization.workspace());
    }

    private static void addStructurizrStylilng(ViewSet views) {
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
//        styles.addElementStyle(DATABASE_TAG).background("#1168bd").color("#ffffff").shape(Shape.Cylinder);
        styles.addElementStyle(Tags.CONTAINER).background("#bbbbbb").color("#ffffff").shape(Shape.Box);
    }
}
