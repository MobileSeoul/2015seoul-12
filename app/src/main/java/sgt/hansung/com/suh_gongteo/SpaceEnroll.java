package sgt.hansung.com.suh_gongteo;

import java.util.Date;

/**
 * Created by user on 2015-10-19.
 */
public class SpaceEnroll {
    private int SetNum, Price; //공간등록번호, 가격
    private String SeName, SeAddress1, SeAddress, SeMaxUser, SeProperty; //공간명, 주소, 상세주소, 수용인원, 공간속성
    private char SeOption1, SeOption2, SeOption3, SeOption4, SeOption5; //옵션1, 옵션2...
    private Date SeTimestart,SeTimeend, SeTime; //사용시작시간, 사용종료시간, 사용시간
    private String SePic, Other, Userid, SeSize; //공간사진, 상세내용, 공급자 아이디, 공간크기

    /*
    public SpaceEnroll(int setNum, int price, String seName, String seAddress1,
              String seAddress, String seMaxUser, String seProperty,
              char seOption1, char seOption2, char seOption3, char seOption4,
              char seOption5, Date seTimestart, Date seTimeend, Date seTime,
              String sePic, String other, String userid, String seSize) {
        SetNum = setNum;
        Price = price;
        SeName = seName;
        SeAddress1 = seAddress1;
        SeAddress = seAddress;
        SeMaxUser = seMaxUser;
        SeProperty = seProperty;
        SeOption1 = seOption1;
        SeOption2 = seOption2;
        SeOption3 = seOption3;
        SeOption4 = seOption4;
        SeOption5 = seOption5;
        SeTimestart = seTimestart;
        SeTimeend = seTimeend;
        SeTime = seTime;
        SePic = sePic;
        Other = other;
        Userid = userid;
        SeSize = seSize;
    }*/
    public SpaceEnroll(){

    }
    public int getSetNum() {
        return SetNum;
    }

    public void setSetNum(int setNum) {
        SetNum = setNum;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getSeName() {
        return SeName;
    }

    public void setSeName(String seName) {
        SeName = seName;
    }

    public String getSeAddress1() {
        return SeAddress1;
    }

    public void setSeAddress1(String seAddress1) {
        SeAddress1 = seAddress1;
    }

    public String getSeAddress() {
        return SeAddress;
    }

    public void setSeAddress(String seAddress) {
        SeAddress = seAddress;
    }

    public String getSeMaxUser() {
        return SeMaxUser;
    }

    public void setSeMaxUser(String seMaxUser) {
        SeMaxUser = seMaxUser;
    }

    public String getSeProperty() {
        return SeProperty;
    }

    public void setSeProperty(String seProperty) {
        SeProperty = seProperty;
    }

    public char getSeOption1() {
        return SeOption1;
    }

    public void setSeOption1(char seOption1) {
        SeOption1 = seOption1;
    }

    public char getSeOption2() {
        return SeOption2;
    }

    public void setSeOption2(char seOption2) {
        SeOption2 = seOption2;
    }

    public char getSeOption3() {
        return SeOption3;
    }

    public void setSeOption3(char seOption3) {
        SeOption3 = seOption3;
    }

    public char getSeOption4() {
        return SeOption4;
    }

    public void setSeOption4(char seOption4) {
        SeOption4 = seOption4;
    }

    public char getSeOption5() {
        return SeOption5;
    }

    public void setSeOption5(char seOption5) {
        SeOption5 = seOption5;
    }

    public Date getSeTimestart() {
        return SeTimestart;
    }

    public void setSeTimestart(Date seTimestart) {
        SeTimestart = seTimestart;
    }

    public Date getSeTimeend() {
        return SeTimeend;
    }

    public void setSeTimeend(Date seTimeend) {
        SeTimeend = seTimeend;
    }

    public Date getSeTime() {
        return SeTime;
    }

    public void setSeTime(Date seTime) {
        SeTime = seTime;
    }

    public String getSePic() {
        return SePic;
    }

    public void setSePic(String sePic) {
        SePic = sePic;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getSeSize() {
        return SeSize;
    }

    public void setSeSize(String seSize) {
        SeSize = seSize;
    }
}
