module com.example.pick_me {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens com.example.pick_me to javafx.fxml;
    exports com.example.pick_me;
}