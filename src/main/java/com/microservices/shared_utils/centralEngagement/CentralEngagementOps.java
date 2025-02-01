package com.microservices.shared_utils.centralEngagement;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CentralEngagementOps {
    public static final String MSG91_WHATSAPP_API_URL = "https://api.msg91.com/api/v5/whatsapp/whatsapp-outbound-message/bulk/";
    public static final String MSG91_SECRET_KEY = "438018ADgkbd5Z1VN679e2adfP1";
    public static final String INTEGRATED_NUMBER = "15557084773";

    public static HttpResponse<String> getTriggerAPIBuilder(String requestBody) throws UnirestException {
        return Unirest.post(MSG91_WHATSAPP_API_URL)
                .header("Content-Type", "application/json")
                .header("authkey", MSG91_SECRET_KEY)
                .body(requestBody)
                .asString();
    }

    public static String getWhatsAppRequestBody(ArrayList<String> variables, String templateName, String imageUrl, String contactNo) {
        try {
            // Create components for the message
            JSONObject components = new JSONObject();
            for (int i = 0; i < variables.size(); i++) {
                JSONObject body = new JSONObject();
                body.put("type", "text");
                body.put("value", variables.get(i));
                components.put("body_" + (i + 1), body);
            }

            if (imageUrl != null && !imageUrl.isEmpty()) {
                JSONObject header = new JSONObject();
                header.put("type", "image");
                header.put("value", imageUrl);
                components.put("header_1", header);
            }

            // Create to_and_components array
            JSONArray toAndComponentsArray = new JSONArray();
            JSONObject toAndComponents = new JSONObject();
            toAndComponents.put("to", new JSONArray().put(contactNo));
            toAndComponents.put("components", components);
            toAndComponentsArray.put(toAndComponents);

            // Create the payload
            JSONObject payload = new JSONObject();
            payload.put("messaging_product", "whatsapp");
            payload.put("type", "template");
            JSONObject template = new JSONObject();
            template.put("name", templateName);
            JSONObject language = new JSONObject();
            language.put("code", "en_US");
            language.put("policy", "deterministic");
            template.put("language", language);
            template.put("namespace", JSONObject.NULL);
            template.put("to_and_components", toAndComponentsArray);
            payload.put("template", template);

            // Create the main JSON object
            JSONObject mainJson = new JSONObject();
            mainJson.put("integrated_number", INTEGRATED_NUMBER);
            mainJson.put("content_type", "template");
            mainJson.put("payload", payload);

            return mainJson.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
