package demo;

import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.DMNServicesClient;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

public class App {

    private static final String URL = "http://localhost:8230/kie-server/services/rest/server";
    private static final String USER = "kieserver";
    private static final String PASSWORD = "kieserver";

    private static final MarshallingFormat FORMAT = MarshallingFormat.JAXB;

    private KieServicesConfiguration conf;
    private KieServicesClient kieServicesClient;

    public void initialize() {
        conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
        conf.setMarshallingFormat(FORMAT);
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
    }

    private void demo() {
        initialize();

        DMNServicesClient dmnClient = kieServicesClient.getServicesClient(DMNServicesClient.class);

        DMNContext dmnContext = dmnClient.newContext();
        dmnContext.set("Age", 23);
        dmnContext.set("had previous incidents", false);

        String containerId = "demo1";
        ServiceResponse<DMNResult> serverResp = dmnClient.evaluateAll(containerId, dmnContext);

        DMNResult dmnResult = serverResp.getResult();

        for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {
            System.out.println("Decision name: " + dr.getDecisionName());
            System.out.println("Decision status: " + dr.getEvaluationStatus());
            System.out.println("Decision result: " + dr.getResult());
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.demo();
    }
}
