module com.eventually.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.eventually.backend;

    opens com.eventually.frontend.controller to javafx.fxml;
    exports com.eventually.frontend;
    exports com.eventually.frontend.view;
}