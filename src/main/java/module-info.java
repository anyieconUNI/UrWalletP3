module co.urwallet {
    requires javafx.controls;
    requires javafx.fxml;
//    requires org.mapstruct;
    requires static lombok;
    requires org.mapstruct;


    opens co.urwallet to javafx.fxml;
    exports co.urwallet;
    exports co.urwallet.viewController;
    opens co.urwallet.viewController to javafx.fxml;
    opens co.urwallet.controller;
    opens co.urwallet.mapping.dto;
    opens co.urwallet.mapping.mappers;
    exports co.urwallet.mapping.mappers;
    opens co.urwallet.model;

}