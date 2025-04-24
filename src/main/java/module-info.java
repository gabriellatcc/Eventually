module com.eventually {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.eventually.view to javafx.fxml;
    opens com.eventually to javafx.fxml;

    exports com.eventually;
    exports com.eventually.view;
}