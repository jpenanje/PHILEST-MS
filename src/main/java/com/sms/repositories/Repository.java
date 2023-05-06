package com.sms.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.tools.Tools;

public class Repository {
    public static JsonNode getMenuItems() {
        String menuString = '['+
        "{\"title\": \"Dashboard\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Students\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.StudentsController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"]},"+
        "{\"title\": \"Teachers\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Subjects\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Classes\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Marks\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Results\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Payments\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"}"+
        "]";
        return Tools.getJsonNodeFromString(menuString);
    }
}