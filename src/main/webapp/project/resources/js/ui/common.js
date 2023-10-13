const commonFn = {

    /** 현재 날자 기준 parameter로 전달받은 year, month, day 더한 날짜 리턴해주는 함수 */
    getDate : (year, month, day) => {
        const today = new Date();
        const date = new Date(today.getFullYear() + year , today.getMonth() + month , today.getDate() + day);

        const returnYear = date.getFullYear();
        let returnMonth = date.getMonth() + 1;
        let returnDate = date.getDate();

        const checkMonth = String(returnMonth);
        if(checkMonth.length != 2) returnMonth = '0' + checkMonth;
        const checkDate = String(returnDate);
        if(checkDate.length != 2) returnDate = '0' + checkDate;

        return returnYear + '-' + returnMonth + '-' + returnDate;
    },

    getTime : (date) => {

        var hours = ('0' + date.getHours()).slice(-2);
        var minutes = ('0' + date.getMinutes()).slice(-2);
        var seconds = ('0' + date.getSeconds()).slice(-2);

        var timeString = hours + ':' + minutes;
        return timeString;
    },

    getDateTime : (reqDate) => {
        const year = reqDate.getFullYear();
        let month = reqDate.getMonth() + 1;
        let date = reqDate.getDate();

        if (month < 10) {
            month = "0" + month;
        }
        if (date < 10) {
            date = "0" + date;
        }

        const hours = ('0' + reqDate.getHours()).slice(-2);
        const minutes = ('0' + reqDate.getMinutes()).slice(-2);
        const seconds = ('0' + reqDate.getSeconds()).slice(-2);

        return year + '-' + month + '-' + date + ' ' + hours + ':' + minutes;
    },

    getDateDiff: (d1, d2) => {
        const date1 = new Date(d1);
        const date2 = new Date(d2);
        const diffDate = date1.getTime() - date2.getTime();
        // return Math.abs(diffDate / (1000 * 60 * 60 * 24)); // 밀리세컨 * 초 * 분 * 시 = 일
        return (diffDate / (1000 * 60 * 60 * 24)); // 밀리세컨 * 초 * 분 * 시 = 일
    },


    /** 가격 콤마 찍어주는 함수 */
    getPrice: (price) => {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '원';
    },

    /** 가족 관계 구분 코드 정의 함수 */
    getRelGbn : (regGbnCd) => {
        switch(regGbnCd) {
            case "0" : return "엄마"; break;
            case "1" : return "아빠"; break;
            case "2" : return "할머니"; break;
            case "3" : return "할아버지"; break;
            case "4" : return "친지"; break;
            case "5" : return "친할머니"; break;
            case "6" : return "친할아버지"; break;
            case "7" : return "외할머니"; break;
            case "8" : return "외할아버지"; break;
            case "9" : return "미입력"; break;
            default : return ""; break;
        }
    },

    /** 핸드폰 번호 '-' 추가 함수 */
    getPhoneFormat: (phone) => {
        let result = "";
        if(phone.length > 10) {
            const p = phone.replaceAll('-', '');
            result = p.substring(0, 3) +  '-' + p.substring(3, 7) + '-' + p.substring(7);
        }
        return result;
    },

    /** 고객 성별 정의 함수 */
    getCustGender: (gender) => {

        /* 0 : 남, 1: 여  */
        switch(gender) {
            case '0' : return '남'; break;
            case '1' : return '여'; break;
            default : return ''; break;
        }
    },

    /** 아기 성별 정의 함수 */
    getBabyGender: (gender) => {
        switch(gender) {
            case '0' : return '여아'; break;
            case '1' : return '남아'; break;
            case '2' : return '태아'; break;
            default : return ''; break;
        }
    },

    getPrice: (price) => {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '원';
    },

    /**  모달창 열어주는 함수 */
    openModal : (modalId) => {
        $("#" + modalId).modal("show");
        // document.body.style.overflow = 'hidden';
    },

    closeModal : (modalId) => {
        $("#" + modalId).modal("hide");
        // document.body.style.overflow = 'auto';
    },

    /** null check return replace data*/
    ifNull: (data, replaceData) => {

        if(typeof data == "undefined" || data == null || data == "") {
            return replaceData;
        } else {
            return data;
        }
    },

    /** null check return boolean*/
    isNull: (data) => {
        if(typeof data == "undefined" || data == null || data == "") {
            return true;
        } else {
            return false;
        }
    },


    callAPI : (req) => {
        // let data = req.data;
        if(req.type.toUpperCase() !== 'GET') {
            req.data = JSON.stringify(req.data);
        }

        let result = '';
        $.ajax({
            url: req.url,
            type: req.type,
            data: req.data,
            dataType: 'json',
            contentType: 'application/json',
            async : false,
            success: (res) => {
                result = res;
            }
        })
        return result;
    },
    callFormAPI : (req) => {
        let result = '';
        $.ajax({
            type: req.type,
            enctype: req.enctype,
            url: req.url,
            data: req.data,
            processData: false,
            contentType: false,
            timeout: 60000,
            cache: false,
            dataType: 'json',
            async : false,
            success: (res) => {
                result = res;
            },
            error : (xhr, status, error) => {
                console.log(xhr);
            }
        })
        return result;
    },

    openModal : (modalId) => {
        $("#" + modalId).modal("show");
    },


    /** 엑셀 비밀번호 검증 함수 */
    excelPwdValid : () => {

        let excelPwd = $("#excelPwd").val().trim();
        let excelPwdChk = $("#excelPwdChk").val().trim();
        let pwdReg = /^(?=.[\dA-Za-z])((?=.\d)|(?=.*\W)).{8,16}$/;

        if(excelPwd.length == 0) {
            alert("비밀번호를 입력해 주세요");
            return false;
        } else if(excelPwdChk.length == 0) {
            alert("비밀번호 확인을 입력해 주세요");
            return false;
        } else if(excelPwd.length > 0 && !pwdReg.test(excelPwd)) {
            alert("비밀번호는 특수문자 혹은 숫자를 포함한 8자 이상 16자 이하로 입력해주세요.");
            return false;
        } else if(excelPwd != excelPwdChk) {
            alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
            return false;
        } else {
            return true;
        }
    },

}
