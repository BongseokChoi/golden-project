package com.iandna.project.util;

import org.springframework.stereotype.Component;

@Component
public class ExcelDataUtil {
  public String convertPayMethod(int payMethod) {
    String res = "";

    switch (payMethod) {
      case 1:
        res = "신용카드";
        break;
      case 2:
        res = "계좌이체";
        break;
    }

    return res;
  }

  public String convertCustState(int custState) {
    String res = "";

    switch (custState) {
      case 1:
        res = "입실";
        break;
      case 2:
        res = "입실예약확정";
        break;
      case 3:
        res = "입실예약";
        break;
      case 4:
        res = "퇴실";
        break;
      case 5:
        res = "방문상담예약";
        break;
      case 6:
        res = "방문상담취소";
        break;
      case 9:
        res = "예약취소";
        break;
      case 12:
        res = "임시저장";
        break;
      case 99:
        res = "중도퇴실";
        break;
    }

    return res;
  }

  public String convertPhoneNumber(String custPhone) {
    if(custPhone == null || custPhone.length() == 0) {
      return custPhone;
    } else {
      return custPhone.substring(0, 3) + "-" + custPhone.substring(3, 7) + "-" + custPhone.substring(7);
    }
  }
}
