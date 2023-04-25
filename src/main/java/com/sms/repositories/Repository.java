package com.sms.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.tools.Tools;

public class Repository {
    public static JsonNode getMenuItems() {
        String menuString = '['+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"},"+
        "{\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/pages/Dashboard.fxml\"}"+
        "]";
        return Tools.getJsonNodeFromString(menuString);
    }
}