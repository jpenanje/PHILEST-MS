package com.sms.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.tools.Tools;

// static data of the application
public class Repository {
    // returns the menu items
    public static JsonNode getMenuItems() {
        String menuString = '['+
        "{\"title\": \"Dashboard\",\"selectedImgPath\": \"/images/dashboard_icon_light.png\", \"unselectedImgPath\": \"/images/dashboard_icon_dark.png\", \"controller\":\"com.sms.controllers.DashboardController\",\"view\":\"/sections/Dashboard.fxml\"},"+
        "{\"title\": \"Pupils\",\"add_button_title\": \"Add Pupil\",\"selectedImgPath\": \"/images/student_icon_light.png\", \"unselectedImgPath\": \"/images/student_icon_dark.png\", \"controller\":\"com.sms.controllers.StudentsController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.StudentFormController\",\"form_view\":\"/pages/StudentForm.fxml\",\"class_path\":\"com.sms.models.Student\"},"+
        "{\"title\": \"Cash Ins\",\"add_button_title\": \"Add Cash In\",\"selectedImgPath\": \"/images/cash_in_icon_light.png\", \"unselectedImgPath\": \"/images/cash_in_icon_dark.png\", \"controller\":\"com.sms.controllers.CashInController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.CashInFormController\",\"form_view\":\"/pages/CashInForm.fxml\",\"class_path\":\"com.sms.models.CashIn\"},"+
        "{\"title\": \"Cash Out\",\"add_button_title\": \"Add Cash out\",\"selectedImgPath\": \"/images/cash_out_icon_light.png\", \"unselectedImgPath\": \"/images/cash_out_icon_dark.png\", \"controller\":\"com.sms.controllers.CashOutController\",\"view\":\"/sections/TableSection.fxml\", \"headers\":[\"Header1\",\"Header2\"], \"form_controller\":\"com.sms.controllers.CashOutFormController\",\"form_view\":\"/pages/CashOutForm.fxml\",\"class_path\":\"com.sms.models.CashOut\"},"+
        "{\"title\": \"logout\",\"unselectedImgPath\": \"/images/logout_icon_dark.png\"}"+
        "]";
        return Tools.getJsonNodeFromString(menuString);
    }
}