package com.sms.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.tools.Tools;

public class Repository {
    public static JsonNode getMenuItems() {
        String menuString = '['+
        "{\"title\": \"Dashboard\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Pupils\",\"add_button_title\": \"Add Pupil\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.StudentsController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.StudentFormController\",\"form_view\":\"/pages/StudentForm.fxml\",\"class_path\":\"com.sms.models.Student\"},"+
        "{\"title\": \"Cash Ins\",\"add_button_title\": \"Add Cash In\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.CashInController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.CashInFormController\",\"form_view\":\"/pages/CashInForm.fxml\",\"class_path\":\"com.sms.models.CashIn\"},"+
        "{\"title\": \"Cash Out\",\"add_button_title\": \"Add Cash out\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.CashOutController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.CashOutFormController\",\"form_view\":\"/pages/CashOutForm.fxml\",\"class_path\":\"com.sms.models.CashOut\"},"+
        "{\"title\": \"logout\",\"unselectedImgPath\": \"/images/dashboard_icon_dark.png\"}"+
        "]";
        return Tools.getJsonNodeFromString(menuString);
    }
}