package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.models.CashOut;
import com.sms.models.Response;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

// Controller for the popup form for adding or editing a Cashout item
public class CashOutFormController implements Initializable {

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
    private MenuButton purpose;

    @FXML
    private Text purposeErrorMessage;

    @FXML
    private Text title;

    @FXML
    private TextField nameOfReceiver;

    @FXML
    private Text nameOfReceiverErrorMessage;

    private Pane loadingIcon;

    ObservableList<Node> buttons;

    private CashOut cashOut;

    private boolean saved;

    Function<NullType, NullType> refresh;

    private ArrayList<String> purposes;

    private ArrayList<JsonNode> studentsObjs;

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

    public CashOutFormController() {
        super();
    }

    // constructor with drop down strings for drop down fields
    public CashOutFormController(ArrayList<ArrayList> dropDownLists, ArrayList<ArrayList> dropDownListsObjects,
            Function refresh) {
        super();
        if (dropDownLists != null) {
            this.purposes = (ArrayList<String>) dropDownLists.get(0);
        }
        this.refresh = refresh;
    }

    // constructor with cashOut item and with drop down strings for drop down fields
    public CashOutFormController(CashOut cashOut, ArrayList<ArrayList> dropDownLists,
            ArrayList<ArrayList> dropDownListsObjects, Function refresh) {
        super();
        if (cashOut != null) {
            this.cashOut = cashOut;
        }
        if (dropDownLists != null) {
            this.purposes = (ArrayList<String>) dropDownLists.get(0);
        }
        this.refresh = refresh;
    }

    // shows a loading icon when waiting for a response from db
    void showLoadingIcon() {
        System.out.println("Show cash in form loading icon");
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
                Tools.getStageFromNode(addItemModal));
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
                    Tools.closeStageFromNode(addItemModal);
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
                                    String currentStudentRequestBody = getCurrentCashOut().toJson();
                                    System.out.println(currentStudentRequestBody);

                                    HttpResponse<String> saveResponse;
                                    if (cashOut.getId() != null && cashOut.getId().length() > 0) {
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

    // saves a new cashOut object in the remote db
    HttpResponse<String> addCashIn(String requestBody) throws Exception {
        return RequestManager.postItem("cashout/", requestBody).get();
    }

    // updates the cashOut object in the remote db
    HttpResponse<String> updateCashIn(String requestBody) throws Exception {
        return RequestManager.updateItem("cashout/" + cashOut.getId() + "/", requestBody).get();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (cashOut != null) {
            changeTitle();
            initializeFormFields();
        }
        if(purposes != null){
            setPurposesOptions();
        }
        if(cashOut != null && purposes != null){
            initializePurpose();
        }
        initializeDateFieldFormat();
        loadingIcon = getLoadingIcon();
    }


    // sets the names of the purposes in the purpose dropdown field
    void setPurposesOptions() {
        System.out.println("init cash out purposes");
        purpose.getItems().clear();
        Tools.addDropDownItemsFromFieldAndItems(purpose, purposes);
    }

    // sets an initial value for purpose if required
    void initializePurpose() {
        String initialPurpose = purposes.get(0);
        for(String p : purposes){
            if(p.equals(cashOut.getPurpose())){
                initialPurpose = p;
                purpose.setText(initialPurpose);
                return;
            }
        }
    }

    // sets a new title
    void changeTitle() {
        title.setText("Edit Cash Out");
    }

    // sets an initial value for form fields except drop downs
    void initializeFormFields() {
        if(cashOut.getAmount() != null){
            amount.setText(cashOut.getAmount());
        }
        if(cashOut.getDate() != null){
            System.out.println("date at start");
            System.out.println(cashOut.getDate());
            LocalDate localDate = Tools.getLocalDateFromString(cashOut.getDate());
            date.setValue(localDate);
        }
        if(cashOut.getReceiverName() != null){
            nameOfReceiver.setText(cashOut.getReceiverName());
        }
    }

    // validates the cashOut form fields and shows error messages if required
    boolean validateForm() {
        return ( validateAmountField() &
                validateReceiverField() &
                validatePurposeField() &
                validateDateField());
    }

    boolean validateAmountField(){
        return Tools.digitValidation(amount, amountErrorMessage);
    }

    boolean validatePurposeField(){
        return Tools.dropDownValidation(purpose,"Select Purpose", purposeErrorMessage);
    }

    boolean validateDateField(){
        return Tools.simpleValidation(date, dateErrorMessage);
    }

    boolean validateReceiverField(){
        return Tools.simpleValidation(nameOfReceiver, nameOfReceiverErrorMessage);
    }

    // returns the current cashOut objects from the form information
    CashOut getCurrentCashOut() {
        if (cashOut == null) {
            cashOut = new CashOut();
        }
        cashOut.setAmount(amount.getText());
        cashOut.setReceiverName(nameOfReceiver.getText());
        cashOut.setDate(Tools.getUtcDateStringFromLocalDate(date.getValue()));
        cashOut.setPurpose(purpose.getText());
        return cashOut;
    }

    // sets the format for the date field to DD/MM/YYYY
    void initializeDateFieldFormat(){
        StringConverter<LocalDate> converter = Tools.getDateConverter();
        date.setConverter(converter);
    }
}
