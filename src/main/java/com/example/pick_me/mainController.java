package com.example.pick_me;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;


import java.io.IOException;
import java.net.URL;
import java.util.*;

public class mainController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    JFXListView<String> memberList;

    @FXML
    JFXPopup copyPopup;

    JFXButton copy;

    @FXML
    public void onEnter(ActionEvent e){
        onInputButtonClick();
    }

    List<String> nameList = null;
    ArrayList<String> arraynameList = new ArrayList<>();
    ArrayList<String> shufflednameList = null;
    ArrayList<String> shuffledThemeList = null;
    ArrayList<String> randomColorList = new ArrayList<>();
    ArrayList<String> pickTheme = new ArrayList<>();

    StringBuilder clipboardString = new StringBuilder();

    int flag = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberList.setItems(FXCollections.observableArrayList());
        memberList.setCellFactory(stringListView -> new CenteredListViewCell());


        //여기서 테마 추가
        pickTheme.add("A");
        pickTheme.add("B");
        pickTheme.add("C");


        //클립보드 생성
        for (int i = 0; i < pickTheme.size(); i++) {
            clipboardString.append(pickTheme.get(i) + " : " + getTheme(pickTheme.get(i))+"  ");
        }
        clipboardString.append("\n");
        clipboardString.append("\n");

        copy = new JFXButton("복사");
        copy.setStyle("-fx-font-size: 20;");
        copy.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent e) ->{
            if(e.getButton() == MouseButton.PRIMARY){
                //클립보드를 시스템 클립보드로 이동
                final ClipboardContent content = new ClipboardContent();
                content.putString(clipboardString.toString());
                Clipboard.getSystemClipboard().setContent(content);
                copyPopup.hide();
            }
        }
        );

    }

    @FXML
    protected void onInputButtonClick() {
            String tmp = nameTextField.getText();
            nameTextField.clear();

            if(tmp.isBlank()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("오류");
                alert.setHeaderText("올바르지 않은 형식");
                alert.setContentText("이름을 입력해주세요!");
                alert.showAndWait();
                return;
            }

            memberList.getItems().add(tmp);
            nameTextField.requestFocus();
    }


    private String getTheme(String alpa){

        //각 테마에 대한 이름 여기서 추가
        if(alpa.equals("A")){
            return "개발자MBTI";
        }else if(alpa.equals("B")){
            return "오늘의 운세";
        }else if(alpa.equals("C")){
            return "깨알 개발 퀴즈";
        }

        return "";
    }

    @FXML
    protected void onsuffleButtonClick(){

        if(flag==0){
            nameList = memberList.getItems();
            arraynameList.addAll(nameList);
        }

        shufflednameList = new ArrayList<>(arraynameList);
        Collections.shuffle(shufflednameList);
        shuffledThemeList = new ArrayList<>();
        randomColorList = new ArrayList<>();


        for (int i = 0; i < shufflednameList.size(); i=i+3) {
            //여기 고쳐야함
            shufflednameList.add(i,pickTheme.get(0) +":"+getTheme(pickTheme.get(0))+"   " +pickTheme.get(1) +":"+ getTheme(pickTheme.get(1)));
            shuffledThemeList.add(pickTheme.get(0));
            shuffledThemeList.add(pickTheme.get(1));


            //랜덤 칼라 값
            Random random = new Random();
            int nextInt = random.nextInt(0xffffff + 1);
            String colorCode = String.format("#%06x", nextInt);
            randomColorList.add(colorCode);

            Collections.shuffle(pickTheme);
        }


        memberList.setItems(FXCollections.observableArrayList());
        for(String x:shufflednameList){
            memberList.getItems().add(x);
        }

        flag = 1;
        memberList.setCellFactory(stringListView -> new CenteredListViewCell());
    }



    @FXML
    protected void onListViewClicked(MouseEvent e) throws IOException {
        if(e.getEventType() == MouseEvent.MOUSE_CLICKED){
            if(e.getButton() == MouseButton.SECONDARY){

                copyPopup = new JFXPopup(copy);
                Scene scene = mainPane.getScene();
                Window window = scene.getWindow();
                copyPopup.show(memberList,JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,e.getX(),e.getY());
            }
        }
    }

    class CenteredListViewCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setStyle(null);
            } else {
                // Create the HBox
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);

                // Create centered Label
                Label label = new Label(item);
                label.setStyle("-fx-font-size:15;");
                label.setAlignment(Pos.CENTER);

                hBox.getChildren().add(label);
                setGraphic(hBox);

                if(flag==1){
                    //섞인 이름 중에 자신의 위치 판별
                    int idx = shufflednameList.indexOf(item);

                    String myTheme = "";
                    //찾지 못했을 경우 찾지 않음
                    if(idx!=-1){
                        if(idx%3!=0){ //정해진 테마가 있는 셀이면 실행하지 않음
                            int classification = idx / 3; //2명씩 + 정해진 테마 칸(1칸) 씩 분류함
                            try{
                                myTheme = shuffledThemeList.get(idx - classification -1 );
                            }catch(Exception e){}

                            String cellText = idx - classification + " : " + item + "  (" +myTheme + ")";
                            //번호를 붙이는 것
                            label.setText(cellText); //테마가 있는 칸의 인덱스를 제외함

                            //각 칸마다 글자 크기와 색상을 지정
                            label.setStyle("-fx-text-fill: white; -fx-font-size:15;");

                            //분류된 것들 끼리 색상 랜덤 지정
                            setStyle("-fx-background-color:"+ randomColorList.get(classification) + ";");
                            setGraphic(hBox);

                            if(clipboardString.lastIndexOf(cellText)==-1){
                                clipboardString.append(cellText);
                                clipboardString.append("\n");
                                if(idx%3==2){
                                    clipboardString.append("-------------");
                                    clipboardString.append("\n");
                                }
                            }

                        }

                    }

                }


            }
        }
    }



}