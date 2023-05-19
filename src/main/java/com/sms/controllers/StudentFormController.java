package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.sms.models.Student;
import com.sms.tools.Config;
import com.sms.tools.Tools;

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

    private ArrayList<String> classes;

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

    public StudentFormController() {
        super();
    }

    public StudentFormController(ArrayList<ArrayList> dropDownLists) {
        super();
        if (dropDownLists != null) {
            this.classes = (ArrayList<String>) dropDownLists.get(0);
        }
    }

    public StudentFormController(Student student, ArrayList<ArrayList> dropDownLists) {
        super();
        if (student != null) {
            this.student = student;
        }
        if (dropDownLists != null) {
            this.classes = (ArrayList<String>) dropDownLists.get(0);
        }
    }

    void showLoadingIcon() {
        System.out.println("Show student form loading icon");
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
        Initializable errorInSavingPageController = new ErrorInSavingPageController();
        Tools.showModal(errorInSavingPageController, "/pages/ErrorInSavingPage.fxml",
                Tools.getStageFromNode(addStudentModal));
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
                    Tools.closeStageFromNode(addStudentModal);
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
                                    for (int i = 0; i <= 10; i++) {
                                        if (i * 10 == 100) {
                                            saved = true;
                                        }
                                        updateProgress(i * 10, 100);
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (student != null) {
            changeTitle();
            initializeFormFields();
        }
        if (classes != null) {
            setClassesOptions();
            initializeClass();
        }
        loadingIcon = getLoadingIcon();
    }

    void setClassesOptions() {
        System.out.println("init student classes");
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for (String clazz : classes) {
            menuItems.add(getMenuItem(clazz));
        }
        pupilClass.getItems().clear();
        pupilClass.getItems().addAll(menuItems);
    }

    void changeTitle() {
        title.setText("Edit student");
    }

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

    void initializeClass() {
        pupilClass.setText(classes.get(0));
    }

    boolean validateForm() {
        return (validatePupilNameField() &
                validateClassField() &
                validateParentNameField() &
                validateParentPhoneField());
    }

    boolean validatePupilNameField() {
        return Tools.simpleValidation(pupilName, pupilNameErrorMessage);
    }

    boolean validateClassField() {
        return Tools.simpleValidation(pupilClass, pupilClassErrorMessage);
    }

    boolean validateParentNameField() {
        return Tools.simpleValidation(parentName, pupilParentNameErrorMessage);
    }

    boolean validateParentPhoneField() {
        return Tools.simpleValidation(phoneNumber, pupilParentPhoneErrorMessage) &&
                Tools.digitValidation(phoneNumber, pupilParentPhoneErrorMessage);
    }
}
