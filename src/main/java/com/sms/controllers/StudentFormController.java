package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// Controller for the popup form for adding or editing a Student item
public class StudentFormController implements Initializable {

    @FXML
    private StackPane addStudentModal;

    @FXML
    private VBox formPane;

    @FXML
    private VBox mainPanePupil;

    @FXML
    private TextField parentName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private MenuButton pupilClass;

    @FXML
    private Text pupilClassErrorMessage;

    @FXML
    private MenuButton currentYear;

    @FXML
    private Text currentYearErrorMessage;

    @FXML
    private TextField pupilName;

    @FXML
    private Text pupilNameErrorMessage;

    @FXML
    private Text pupilParentNameErrorMessage;

    @FXML
    private Text pupilParentPhoneErrorMessage;

    @FXML
    private Text title;

    @FXML
    private HBox buttonsPane;

    private Pane loadingIcon;

    ObservableList<Node> buttons;

    private Student student;

    private boolean saved;

    Function<NullType, NullType> refresh;

    private ArrayList<String> classes;

    private ArrayList<String> academicYears;

    private ArrayList<JsonNode> classesObjs;

    // cancels the addition or editing of an item
    @FXML
    void cancel(ActionEvent event) {
        Tools.closeStageFromNode(formPane);
    }

    // confirms the addition or editing of an item
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

    public StudentFormController() {
        super();
    }

    // constructor with drop down strings for drop down fields
    public StudentFormController(ArrayList<ArrayList> dropDownLists, ArrayList<ArrayList> dropDownListsObjects,
            Function refresh) {
        super();
        if (dropDownLists != null) {
            this.classes = (ArrayList<String>) dropDownLists.get(0);
            this.classesObjs = (ArrayList<JsonNode>) dropDownListsObjects.get(0);
            this.academicYears = (ArrayList<String>) dropDownLists.get(1);
        }
        this.refresh = refresh;
    }

    // constructor with student item and with drop down strings for drop down fields
    public StudentFormController(Student student, ArrayList<ArrayList> dropDownLists,
            ArrayList<ArrayList> dropDownListsObjects, Function refresh) {
        super();
        if (student != null) {
            this.student = student;
        }
        if (dropDownLists != null) {
            this.classes = (ArrayList<String>) dropDownLists.get(0);
            this.classesObjs = (ArrayList<JsonNode>) dropDownListsObjects.get(0);
            this.academicYears = (ArrayList<String>) dropDownLists.get(1);
        }
        this.refresh = refresh;
    }

    // shows a loading icon when waiting for a response from db
    void showLoadingIcon() {
        System.out.println("Show student form loading icon");
        buttons = FXCollections.observableArrayList(buttonsPane.getChildren());
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(loadingIcon);
    }

    // removes the loading icon
    void removeLoadingIcon() {
        System.out.println("Remove loading icon");
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().addAll(buttons);
    }

    // shows a dialogue error message
    void showErrorMessage() {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(
                "There was an issue while trying to save this item. Check your internet connection and try again.");
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                Tools.getStageFromNode(addStudentModal));
    }

    // shows the success icon for a few seconds
    void showSuccessIconForSeconds() {
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(getSuccessPane());
        waitAndGoBack();
    }

    // freezes the app for some time before closing the popup
    void waitAndGoBack() {
        Service service = getWaitService();
        bindWaitServiceWithDisplay(service);
        service.start();
    }

    // ensures that after waiting, the popup is closed
    void bindWaitServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    Tools.closeStageFromNode(addStudentModal);
                }
            };
        });
    }

    // ensures that after saving the loading icon is removed and appropriate action is taken
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

    // a service for freezing the popup for sometime
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

    // returns a success pane
    Pane getSuccessPane() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/SuccessPane.fxml");
    }

    // returns a loading icon
    Pane getLoadingIcon() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/CircleLoadingIcon.fxml");
    }

    // returns a service for saving the item information in the remote db
    // in an asynchronous way(without blocking the whole app)
    Service getSaveService() {
        System.out.println("get Save Student Service");
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
                                    String currentStudentRequestBody = getCurrentStudent().toJson();

                                    HttpResponse<String> saveResponse;
                                    if (student.getId() != null && student.getId().length() > 0) {
                                        saveResponse = updateStudent(currentStudentRequestBody);
                                    } else {
                                        saveResponse = addStudent(currentStudentRequestBody);
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

    // saves a new student object in the remote db
    HttpResponse<String> addStudent(String requestBody) throws Exception {
        return RequestManager.postItem("students/", requestBody).get();
    }

    // updates the cashin object in the remote db
    HttpResponse<String> updateStudent(String requestBody) throws Exception {
        return RequestManager.updateItem("students/" + student.getId() + "/", requestBody).get();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (student != null) {
            changeTitle();
            initializeFormFields();
        }
        if (classes != null) {
            setClassesOptions();
        }
        if(student != null && classes != null){
            initializeClass();
        }
        if(academicYears != null){
            setYearsOptions();
        }
        if(student != null && academicYears != null){
            initializeYear();
        }
        loadingIcon = getLoadingIcon();
    }

    // sets the names of the classes in the class dropdown field
    void setClassesOptions() {
        System.out.println("init student classes");
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for (String clazz : classes) {
            menuItems.add(getMenuItem(clazz));
        }
        pupilClass.getItems().clear();
        pupilClass.getItems().addAll(menuItems);
    }

    // sets the years in the academic year dropdown field
    void setYearsOptions() {
        System.out.println("init student years");
        currentYear.getItems().clear();
        Tools.addDropDownItemsFromFieldAndItems(currentYear, academicYears);
    }

    // sets a new title
    void changeTitle() {
        title.setText("Edit pupil");
    }

    // returns a menuItem object from a text for a dropdown
    MenuItem getMenuItem(String text) {
        MenuItem toBeReturned = new MenuItem();
        toBeReturned.setText(text);
        final String finalText = text;
        toBeReturned.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                pupilClass.setText(finalText);
            }
        });

        return toBeReturned;
    }

    // sets an initial value for form fields except drop downs
    void initializeFormFields() {
        if (student.getPupilName() != null) {
            pupilName.setText(student.getPupilName());
        }
        if (student.getParentName() != null) {
            parentName.setText(student.getParentName());
        }
        if (student.getPhoneNumber() != null) {
            phoneNumber.setText(student.getPhoneNumber());
        }
    }

    // sets an initial value for the class drop down field
    // if required.
    void initializeClass() {
        String initialClass = classes.get(0);
        for(JsonNode clazz : classesObjs){
            if(clazz.get("id").asInt() == student.getClassId()){
                initialClass = clazz.get("name").asText();
                pupilClass.setText(initialClass);
                return;
            }
        }
        
    }

    // sets an initial value for the year drop down field
    // if required.
    void initializeYear() {
        String initialYear = academicYears.get(0);
        for(String academicYear : academicYears){
            if(academicYear.equals(student.getCurrentYear())){
                initialYear = academicYear;
                currentYear.setText(initialYear);
                return;
            }
        }
    }

    // validates the cashIn form fields and shows error messages if required
    boolean validateForm() {
        return (validatePupilNameField() &
                validateClassField() &
                validateParentNameField() &
                validateParentPhoneField()&
                validateYearField());
    }

    boolean validatePupilNameField() {
        return Tools.simpleValidation(pupilName, pupilNameErrorMessage);
    }

    boolean validateClassField() {
        return Tools.dropDownValidation(pupilClass,"Class", pupilClassErrorMessage);
    }

    boolean validateParentNameField() {
        return Tools.simpleValidation(parentName, pupilParentNameErrorMessage);
    }

    boolean validateParentPhoneField() {
        return Tools.digitValidation(phoneNumber, pupilParentPhoneErrorMessage);
    }

    boolean validateYearField() {
        return Tools.dropDownValidation(currentYear,"Current Year", currentYearErrorMessage);
    }

    // returns the current student objects from the form information
    Student getCurrentStudent() {
        if (student == null) {
            student = new Student();
        }
        student.setPupilName(pupilName.getText());
        student.setParentName(parentName.getText());
        student.setPhoneNumber(phoneNumber.getText());
        student.setClassId(getClassIdFromName(pupilClass.getText()));
        student.setCurrentYear(currentYear.getText());
        return student;
    }

    // returns the student id from his/her name
    int getClassIdFromName(String className) {
        for (JsonNode node : this.classesObjs) {
            if ((node.get("name").asText()).equals(className)) {
                return node.get("id").asInt();
            }
        }
        return 1;
    }
}
