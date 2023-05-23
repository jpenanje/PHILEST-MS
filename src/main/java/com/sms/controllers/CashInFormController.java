package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.models.CashIn;
import com.sms.models.Response;
import com.sms.models.Student;
import com.sms.tools.Config;
import com.sms.tools.RequestManager;
import com.sms.tools.Tools;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CashInFormController implements Initializable {

    @FXML
    private MenuButton academicYear;

    @FXML
    private Text academicYearErrorMessage;

    @FXML
    private StackPane addItemModal;

    @FXML
    private TextField amount;

    @FXML
    private Text amountErrorMessage;

    @FXML
    private HBox buttonsPane;

    @FXML
    private DatePicker date;

    @FXML
    private Text dateErrorMessage;

    @FXML
    private VBox formPane;

    @FXML
    private VBox mainPanePupil;

    @FXML
    private MenuButton pupil;

    @FXML
    private Text pupilErrorMessage;

    @FXML
    private MenuButton purpose;

    @FXML
    private Text purposeErrorMessage;

    @FXML
    private Text title;

    private Pane loadingIcon;

    ObservableList<Node> buttons;

    private CashIn cashIn;

    private boolean saved;

    Function<NullType, NullType> refresh;

    private ArrayList<String> students;

    private ArrayList<String> purposes;

    private ArrayList<String> academicYears;

    private ArrayList<JsonNode> studentsObjs;

    @FXML
    void cancel(ActionEvent event) {
        Tools.closeStageFromNode(formPane);
    }

    @FXML
    void save(ActionEvent event) {
        boolean isValidForm = validateForm();
        if (isValidForm) {
            showLoadingIcon();
            Service saveService = getSaveService();
            bindSaveServiceWithDisplay(saveService);
            saveService.start();
        }
    }

    public CashInFormController() {
        super();
    }

    public CashInFormController(ArrayList<ArrayList> dropDownLists, ArrayList<ArrayList> dropDownListsObjects,
            Function refresh) {
        super();
        if (dropDownLists != null) {
            this.students = (ArrayList<String>) dropDownLists.get(0);
            this.studentsObjs = (ArrayList<JsonNode>) dropDownListsObjects.get(0);
            this.academicYears = (ArrayList<String>) dropDownLists.get(1);
            this.purposes = (ArrayList<String>) dropDownLists.get(2);
        }
        this.refresh = refresh;
    }

    public CashInFormController(CashIn cashIn, ArrayList<ArrayList> dropDownLists,
            ArrayList<ArrayList> dropDownListsObjects, Function refresh) {
        super();
        if (cashIn != null) {
            this.cashIn = cashIn;
        }
        if (dropDownLists != null) {
            this.students = (ArrayList<String>) dropDownLists.get(0);
            this.studentsObjs = (ArrayList<JsonNode>) dropDownListsObjects.get(0);
            this.academicYears = (ArrayList<String>) dropDownLists.get(1);
            this.purposes = (ArrayList<String>) dropDownLists.get(2);
        }
        this.refresh = refresh;
    }

    void showLoadingIcon() {
        System.out.println("Show cash in form loading icon");
        buttons = FXCollections.observableArrayList(buttonsPane.getChildren());
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(loadingIcon);
    }

    void removeLoadingIcon() {
        System.out.println("Remove loading icon");
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().addAll(buttons);
    }

    void showErrorMessage() {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(
                "There was an issue while trying to save this item. Check your internet connection and try again.");
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                Tools.getStageFromNode(addItemModal));
    }

    void showSuccessIconForSeconds() {
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(getSuccessPane());
        waitAndGoBack();
    }

    void waitAndGoBack() {
        Service service = getWaitService();
        bindWaitServiceWithDisplay(service);
        service.start();
    }

    void bindWaitServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    Tools.closeStageFromNode(addItemModal);
                }
            };
        });
    }

    void bindSaveServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (saved) {
                        System.out.println("saved");
                        removeLoadingIcon();
                        showSuccessIconForSeconds();
                        // closePane();
                    } else {
                        System.out.println("not saved");
                        removeLoadingIcon();
                        showErrorMessage();
                    }
                }
            };
        });
    }

    Service getWaitService() {
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(600);
                                        updateProgress(100, 100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();

                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    saved = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                            saved = true;
                        } catch (Exception e) {
                            System.out.println("future error");
                            saved = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    Pane getSuccessPane() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/SuccessPane.fxml");
    }

    Pane getLoadingIcon() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/CircleLoadingIcon.fxml");
    }

    Service getSaveService() {
        System.out.println("get Save Cash In Service");
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String currentStudentRequestBody = getCurrentCashIn().toJson();

                                    HttpResponse<String> saveResponse;
                                    if (cashIn.getId() != null && cashIn.getId().length() > 0) {
                                        saveResponse = updateCashIn(currentStudentRequestBody);
                                    } else {
                                        saveResponse = addCashIn(currentStudentRequestBody);
                                    }
                                    Response customResponse = Tools.getCustomResponseFromResponse(saveResponse);

                                    if (customResponse.getCode() < 300 && customResponse.getCode() >= 200) {
                                        saved = true;
                                        updateProgress(100, 100);
                                        Platform.runLater(() -> {
                                            refresh.apply(null);
                                        });

                                    }

                                    else {
                                        updateProgress(100, 100);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    saved = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            saved = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    HttpResponse<String> addCashIn(String requestBody) throws Exception {
        return RequestManager.postItem("cashin/", requestBody).get();
    }

    HttpResponse<String> updateCashIn(String requestBody) throws Exception {
        return RequestManager.updateItem("cashin/" + cashIn.getId() + "/", requestBody).get();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (cashIn != null) {
            changeTitle();
            initializeFormFields();
        }
        if (students != null) {
            setStudentsOptions();
        }
        if(cashIn != null && students != null){
            initializeStudent();
        }
        if(academicYears != null){
            setYearsOptions();
        }
        if(cashIn != null && academicYears != null){
            initializeYear();
        }
        if(purposes != null){
            setPurposesOptions();
        }
        if(cashIn != null && purposes != null){
            initializePurpose();
        }
        loadingIcon = getLoadingIcon();
    }

    void setStudentsOptions() {
        System.out.println("init cashIn students");
        Tools.addDropDownItemsFromFieldAndItems(pupil, students);
    }

    void setYearsOptions() {
        System.out.println("init cashIn years");
        academicYear.getItems().clear();
        Tools.addDropDownItemsFromFieldAndItems(academicYear, academicYears);
        // ArrayList<MenuItem> menuItems = new ArrayList<>();
        // for (String academicYear : academicYears) {
        //     menuItems.add(getMenuItem(clazz));
        // }
        // pupilClass.getItems().addAll(menuItems);
    }

    void setPurposesOptions() {
        System.out.println("init cash in purposes");
        purpose.getItems().clear();
        Tools.addDropDownItemsFromFieldAndItems(purpose, purposes);
    }

    void initializePurpose() {
        String initialPurpose = purposes.get(0);
        for(String p : purposes){
            if(p.equals(cashIn.getPurpose())){
                initialPurpose = p;
                purpose.setText(initialPurpose);
                return;
            }
        }
    }

    void changeTitle() {
        title.setText("Edit Cash In");
    }

    MenuItem getMenuItem(String text) {
        MenuItem toBeReturned = new MenuItem();
        toBeReturned.setText(text);
        final String finalText = text;
        toBeReturned.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                pupil.setText(finalText);
            }
        });

        return toBeReturned;
    }

    void initializeFormFields() {
        if(cashIn.getAmount() != null){
            amount.setText(cashIn.getAmount());
        }
        if(cashIn.getDate() != null){
            LocalDate localDate = LocalDate.now();
            date.setValue(localDate);
        }
    }

    void initializeStudent() {
        String initialStudent = students.get(0);
        for(JsonNode student : studentsObjs){
            if(student.get("id").asInt() == cashIn.getStudentId()){
                initialStudent = student.get("full_name").asText();
                pupil.setText(initialStudent);
                return;
            }
        }
        
    }

    void initializeYear() {
        String initialYear = academicYears.get(0);
        for(String ay : academicYears){
            if(ay.equals(cashIn.getAcademicYear())){
                initialYear = ay;
                academicYear.setText(initialYear);
                return;
            }
        }
    }

    boolean validateForm() {
        return ( validateAmountField() &
                validatePupilField() &
                validatePurposeField() &
                validateAcademicYearField() &
                validateDateField());
    }

    boolean validateAmountField(){
        return Tools.digitValidation(amount, amountErrorMessage);
    }

    boolean validatePupilField(){
        return Tools.dropDownValidation(pupil,"Select Pupil", pupilErrorMessage);
    }

    boolean validatePurposeField(){
        return Tools.dropDownValidation(purpose,"Select Purpose", purposeErrorMessage);
    }

    boolean validateAcademicYearField(){
        return Tools.dropDownValidation(academicYear,"Academic year", academicYearErrorMessage);
    }

    boolean validateDateField(){
        return Tools.simpleValidation(date, dateErrorMessage);
    }

    CashIn getCurrentCashIn() {
        if (cashIn == null) {
            cashIn = new CashIn();
        }
        cashIn.setAmount(amount.getText());
        cashIn.setAcademicYear(academicYear.getText());
        cashIn.setDate(Tools.getUtcDateStringFromLocalDate(date.getValue()));
        cashIn.setPurpose(purpose.getText());
        cashIn.setStudentId(getPupilIdFromName(pupil.getText()));
        // student.setPupilName(pupilName.getText());
        // student.setParentName(parentName.getText());
        // student.setPhoneNumber(phoneNumber.getText());
        // student.setClassId(getClassIdFromName(pupilClass.getText()));
        // student.setCurrentYear(currentYear.getText());
        return cashIn;
    }

    int getPupilIdFromName(String studentName) {
        for (JsonNode node : this.studentsObjs) {
            if ((node.get("full_name").asText()).equals(studentName)) {
                return node.get("id").asInt();
            }
        }
        return 1;
    }
}
