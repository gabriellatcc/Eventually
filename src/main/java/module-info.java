module com.eventually {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.mail;
    requires java.sql;
    requires activation;
    requires org.slf4j;
    requires java.net.http;

    opens com.eventually to javafx.fxml;

    exports com.eventually;
}