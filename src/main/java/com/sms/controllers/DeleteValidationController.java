package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.models.Response;
import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DeleteValidationController implements Initializable {

    Stage stage;
    Function delete;
    boolean deleted;

    ObservableList<Node> buttons;
    private Pane loadingIcon;
    private Function refresh;

    public DeleteValidationController(Stage stage, Function delete, Function refresh) {
        super();
        this.stage = stage;
        this.delete = delete;
        this.refresh = refresh;
    }

    @FXML
    StackPane basePane;

    @FXML
    private HBox buttonsPane;

    @FXML
    void cancel(ActionEvent event) {
        Tools.closeStageFromNode(basePane);
    }

    @FXML
    void delete(ActionEvent event) {
        Function<NullType, CompletableFuture<HttpResponse<String>>> onDelete = (Function<NullType, CompletableFuture<HttpResponse<String>>>) delete;
        showLoadingIcon();
        Service deleteService = getDeleteService(onDelete);
        bindDeleteServiceWithDisplay(deleteService);
        deleteService.start();
    }

    Service getDeleteService(Function<NullType, CompletableFuture<HttpResponse<String>>> onDelete) {
        System.out.println("get Delete Student Service");
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
                                    HttpResponse<String> deleteResponse = null;
                                    try {
                                        deleteResponse = onDelete.apply(null).get();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    
                                    
                                    Response customResponse = Tools.getCustomResponseFromResponse(deleteResponse);
                                    if (customResponse.getCode() < 300 && customResponse.getCode() >= 200) {
                                        deleted = true;
                                        updateProgress(100, 100);
                                        refresh.apply(null);
                                    } else {
                                        updateProgress(100, 100);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    deleted = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.deleteItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            deleted = false;
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
        loadingIcon = getLoadingIcon();
        // bindStageHeight();
    }

    void bindStageHeight() {
        basePane.heightProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                stage.sizeToScene();
                stage.centerOnScreen();
            };
        });
    }

    void bindDeleteServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (deleted) {
                        System.out.println("deleted");
                        removeLoadingIcon();
                        showSuccessIconForSeconds();
                        // closePane();
                    } else {
                        System.out.println("not deleted");
                        removeLoadingIcon();
                        showErrorMessage();
                        closeCurrentWindow();

                    }
                }
            };
        });
    }

    void closeCurrentWindow() {
        Tools.closeStageFromNode(basePane);
    }

    void showLoadingIcon() {
        System.out.println("Show student form loading icon");
        buttons = FXCollections.observableArrayList(buttonsPane.getChildren());
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(loadingIcon);
    }

    Pane getLoadingIcon() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/CircleLoadingIcon.fxml");
    }

    void removeLoadingIcon() {
        System.out.println("Remove loading icon");
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().addAll(buttons);
    }

    void showErrorMessage() {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(
                "There was an issue while trying to delete this item. Check your internet connection and try again.");
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                stage);
    }

    void showSuccessIconForSeconds() {
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(getSuccessPane());
        waitAndGoBack();
    }

    Pane getSuccessPane() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/SuccessPane.fxml");
    }

    void waitAndGoBack() {
        Service service = getWaitService();
        bindWaitServiceWithDisplay(service);
        service.start();
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
                                    deleted = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.deleteItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            deleted = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    void bindWaitServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    Tools.closeStageFromNode(buttonsPane);
                }
            };
        });
    }
}
