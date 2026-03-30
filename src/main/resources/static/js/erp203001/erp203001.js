/**
 * erp203001.js - 시공계획 관리 메인 JavaScript
 */

// 전역 변수 선언
var addr = [];
var selectedMarker = null; // 클릭한 마커를 담을 변수
var searchRes;
var pagePerRow = 0;
var myWins;
var myGrid;
var myGrid2;
var myCalendar;
var distanceOverlay = []; // 키로수 표시하기
var drawLine = []; // 거리직선표시
var myGridOnCellChanged;

// 콤보박스 변수 (전역으로 선언)
var myComboCmpyCd;
var myComboInstCapaCtgr;
var myComboInstCapaType;
var myComboZoneType;
var myComboZone;
var myComboDcCd;
var myComboUseYn;
var myComboSeatType;

// 세션 변수 (HTML에서 주입됨)
var sessionCmpyCd;
var sessionTblUserMId;
var sessionUserGrntCd;
var sessionUserGrdCd;
var sessionDcCd;
var sessionDcNm;

// 유효성 검사 메시지
var chkValMsg = "";

var fnObj = {
    winOpen: {
        // 시공기사 찾기 팝업
        instErOpen: function (rId, cmpyCd, dcCd, instEr, useYn) {
            var windowId = "instErOpen";
            var windowText = "시공기사 찾기 팝업 erp203001p1";
            var windowUrl = "/erp203001p1";
            var windowLeft = 0;
            var windowTop = 0;
            var windowWidth = 800;
            var windowHeight = 600;
            var windowsModal = true;
            var windowOnClose = function (windowId) {
                return true;
            };
            var windowSendObject = {
                cmpyCd: cmpyCd,
                dcCd: dcCd,
                instEr: instEr,
                useYn: useYn,
                rowId: rId,
                windowId: windowId
            };

            myWindowsOpen(windowId, windowText, windowUrl, windowLeft, windowTop, windowWidth, windowHeight, windowsModal, windowOnClose, windowSendObject);

            myWins.window(windowId).attachEvent("onClose", function (win) {
                return true;
            });
        },

        // 그리드2에 있는 시공기사 찾기 팝업
        grid2InstErNameOpen: function (cmpyCd, instDt, dcCd, instEr, restYn) {
            var windowId = "grid2InstErNameOpen";
            var windowText = "시공기사 찾기 팝업2 erp203001p2";
            var windowUrl = "/erp203001p2";
            var windowLeft = -14400;
            var windowTop = -14400;
            var windowWidth = 1200;
            var windowHeight = 600;
            var windowsModal = true;
            var windowOnClose = function (windowId) {
                return true;
            };
            var windowSendObject = {
                cmpyCd: cmpyCd,
                instDt: instDt,
                dcCd: dcCd,
                instEr: instEr,
                restYn: restYn,
                windowId: windowId
            };

            myWindowsOpen(windowId, windowText, windowUrl, windowLeft, windowTop, windowWidth, windowHeight, windowsModal, windowOnClose, windowSendObject);

            myWins.window(windowId).attachEvent("onClose", function (win) {
                return true;
            });
        },

        grid2InstErStatOpen: function () {
            var windowId = "grid2InstErStatOpen";
            var windowText = "시공기사 현황 erp203001p3";
            var windowUrl = "/erp203001p3";
            var windowLeft = window.innerWidth / 2;
            var windowTop = 0;
            var windowWidth = 630;
            var windowHeight = 600;
            var windowsModal = false;
            var windowOnClose = function (windowId) {
                return true;
            };
            var windowSendObject = {
                cmpyCd: myComboCmpyCd.getSelectedValue(),
                instDt: $("#dateFrom").val().replace(/-/g, ""),
                dcCd: myComboDcCd.getSelectedValue(),
                windowId: windowId
            };

            myWindowsOpen(windowId, windowText, windowUrl, windowLeft, windowTop, windowWidth, windowHeight, windowsModal, windowOnClose, windowSendObject);

            myWins.window(windowId).attachEvent("onClose", function (win) {
                return true;
            });
        },

        rtnObject: function (windowId, rtnObj, srchInput) {
            if (rtnObj == null) {
                myWins.window(windowId).setPosition(0, 0);
                return;
            }

            if (windowId == "grid2InstErStatOpen") {
                var rtn = rtnObj[0];
                $("#instErName").val(rtnObj[0].instEr);
                $("#tblUserMId").val(rtnObj[0].tblUserMId);
            }

            if (windowId == "grid2InstErNameOpen") {
                var rtn = rtnObj[0];
                $("#instErName").val(rtnObj[0].instEr);
                $("#tblUserMId").val(rtnObj[0].tblUserMId);
            }

            if (windowId == "instErOpen") {
                var rtn = rtnObj[0];
                var paltNo = myGrid.cellById(rtn.rowId, myGrid.getColIndexById("paltNoxx")).getValue();

                if (paltNo) {
                    myGrid.forEachRow(function (eachRId) {
                        if (myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue() == paltNo) {
                            myGrid.cellById(eachRId, myGrid.getColIndexById("instEr")).setValue(rtn.userNm);
                            myGrid.cellById(eachRId, myGrid.getColIndexById("tblUserMId")).setValue(rtn.tblUserMId);
                            myGrid.cellById(eachRId, myGrid.getColIndexById("cud")).setValue("U");
                            myGrid.cellById(eachRId, myGrid.getColIndexById("chk")).setValue("1");
                        }
                    });
                } else {
                    myGrid.cellById(rtn.rowId, myGrid.getColIndexById("instEr")).setValue(rtn.userNm);
                    myGrid.cellById(rtn.rowId, myGrid.getColIndexById("tblUserMId")).setValue(rtn.tblUserMId);
                    myGrid.cellById(rtn.rowId, myGrid.getColIndexById("cud")).setValue("U");
                    myGrid.cellById(rtn.rowId, myGrid.getColIndexById("chk")).setValue("1");
                }
            }
        }
    },

    comboOpen: {
        comboCmpyCd: function () {
            myComboCmpyCd = new dhtmlXCombo("cmpyCd");
            comboList(myComboCmpyCd, 'A', 'COMM', 'CMPY_CD', '', 'Y', '');
            myComboCmpyCd.selectOption(myComboCmpyCd.getIndexByValue(sessionCmpyCd));
            myComboCmpyCd.setFontSize("12px", "12px");
            myComboCmpyCd.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
            });
            myComboCmpyCd.disable();
        },

        comboInstCapaCtgr: function () {
            myComboInstCapaCtgr = new dhtmlXComboFromSelect("instCapaCtgr");
            comboList(myComboInstCapaCtgr, 'A', 'COMM', 'INST_CAPA_CTGR', '', 'Y', 'all');
            myComboInstCapaCtgr.selectOption(0);
            myComboInstCapaCtgr.setFontSize("12px", "12px");
            myComboInstCapaCtgr.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
                if (this.getIndexByValue(value) != 0) {
                    this.setChecked(this.getIndexByValue(value), true);
                    var c = 0;
                    for (var i = 1; i < this.getOptionsCount(); i++) {
                        if (this.isChecked(i)) {
                            c++;
                        }
                    }
                    this.updateOption("", "", c + "개 선택", "");
                    this.selectOption(0);
                    this.openSelect();
                }
            });
            myComboInstCapaCtgr.attachEvent("onCheck", function (value, text) {
                if (!value) {
                    if (myComboInstCapaCtgr.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboInstCapaCtgr.getOptionsCount(); i++) {
                            myComboInstCapaCtgr.setChecked(i, true);
                            c++;
                        }
                        myComboInstCapaCtgr.updateOption("", "", c + "개 선택", "");
                        myComboInstCapaCtgr.openSelect();
                    }
                    if (!myComboInstCapaCtgr.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboInstCapaCtgr.getOptionsCount(); i++) {
                            myComboInstCapaCtgr.setChecked(i, false);
                        }
                        myComboInstCapaCtgr.updateOption("", "", c + "개 선택", "");
                        myComboInstCapaCtgr.openSelect();
                        return;
                    }
                } else {
                    var cnt = 0;
                    for (var i = 1; i < myComboInstCapaCtgr.getOptionsCount(); i++) {
                        if (myComboInstCapaCtgr.isChecked(i)) {
                            cnt++;
                        }
                    }
                    if ((myComboInstCapaCtgr.getOptionsCount() - 1) == cnt) {
                        myComboInstCapaCtgr.setChecked(0, true);
                    } else {
                        myComboInstCapaCtgr.setChecked(0, false);
                    }
                    myComboInstCapaCtgr.updateOption("", "", cnt + "개 선택", "");
                    myComboInstCapaCtgr.openSelect();
                    return;
                }
            });
            myComboInstCapaCtgr.readonly(true);
        },

        comboInstCapaType: function () {
            myComboInstCapaType = new dhtmlXComboFromSelect("instCapaType");
            comboList(myComboInstCapaType, 'A', 'COMM', 'INST_CAPA_TYPE', '', 'Y', 'all');
            myComboInstCapaType.selectOption(0);
            myComboInstCapaType.setFontSize("12px", "12px");
            myComboInstCapaType.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
                if (this.getIndexByValue(value) != 0) {
                    this.setChecked(this.getIndexByValue(value), true);
                    var c = 0;
                    for (var i = 1; i < this.getOptionsCount(); i++) {
                        if (this.isChecked(i)) {
                            c++;
                        }
                    }
                    this.updateOption("", "", c + "개 선택", "");
                    this.selectOption(0);
                    this.openSelect();
                }
            });
            myComboInstCapaType.attachEvent("onCheck", function (value, text) {
                if (!value) {
                    if (myComboInstCapaType.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboInstCapaType.getOptionsCount(); i++) {
                            myComboInstCapaType.setChecked(i, true);
                            c++;
                        }
                        myComboInstCapaType.updateOption("", "", c + "개 선택", "");
                        myComboInstCapaType.openSelect();
                    }
                    if (!myComboInstCapaType.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboInstCapaType.getOptionsCount(); i++) {
                            myComboInstCapaType.setChecked(i, false);
                        }
                        myComboInstCapaType.updateOption("", "", c + "개 선택", "");
                        myComboInstCapaType.openSelect();
                        return;
                    }
                } else {
                    var cnt = 0;
                    for (var i = 1; i < myComboInstCapaType.getOptionsCount(); i++) {
                        if (myComboInstCapaType.isChecked(i)) {
                            cnt++;
                        }
                    }
                    if ((myComboInstCapaType.getOptionsCount() - 1) == cnt) {
                        myComboInstCapaType.setChecked(0, true);
                    } else {
                        myComboInstCapaType.setChecked(0, false);
                    }
                    myComboInstCapaType.updateOption("", "", cnt + "개 선택", "");
                    myComboInstCapaType.openSelect();
                    return;
                }
            });
            myComboInstCapaType.readonly(true);
        },

        comboZoneType: function () {
            myComboZoneType = new dhtmlXCombo("zoneType");
            comboList(myComboZoneType, 'A', 'INST_ZONE_TYPE', 'ZONE_TYPE', '', 'Y', 'all');
            myComboZoneType.readonly(true);
            myComboZoneType.selectOption(0);
            myComboZoneType.setFontSize("12px", "12px");
            myComboZoneType.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myComboZone.clearAll();
                comboList(myComboZone, 'A', 'COMM', myComboZoneType.getSelectedValue(), '', 'Y', 'req');
                myComboZone.selectOption(0);
            });
        },

        comboZone: function () {
            myComboZone = new dhtmlXComboFromSelect("zone");
            comboList(myComboZone, 'A', 'COMM_LIST', '', '', 'Y', 'all');
            myComboZone.selectOption(0);
            myComboZone.setFontSize("12px", "12px");
            myComboZone.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
                if (this.getIndexByValue(value) != 0) {
                    this.setChecked(this.getIndexByValue(value), true);
                    var c = 0;
                    for (var i = 1; i < this.getOptionsCount(); i++) {
                        if (this.isChecked(i)) {
                            c++;
                        }
                    }
                    this.updateOption("", "", c + "개 선택", "");
                    this.selectOption(0);
                    this.openSelect();
                }
            });
            myComboZone.attachEvent("onCheck", function (value, text) {
                if (!value) {
                    if (myComboZone.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboZone.getOptionsCount(); i++) {
                            myComboZone.setChecked(i, true);
                            c++;
                        }
                        myComboZone.updateOption("", "", c + "개 선택", "");
                        myComboZone.openSelect();
                    }
                    if (!myComboZone.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboZone.getOptionsCount(); i++) {
                            myComboZone.setChecked(i, false);
                        }
                        myComboZone.updateOption("", "", c + "개 선택", "");
                        myComboZone.openSelect();
                        return;
                    }
                } else {
                    var cnt = 0;
                    for (var i = 1; i < myComboZone.getOptionsCount(); i++) {
                        if (myComboZone.isChecked(i)) {
                            cnt++;
                        }
                    }
                    if ((myComboZone.getOptionsCount() - 1) == cnt) {
                        myComboZone.setChecked(0, true);
                    } else {
                        myComboZone.setChecked(0, false);
                    }
                    myComboZone.updateOption("", "", cnt + "개 선택", "");
                    myComboZone.openSelect();
                    return;
                }
            });
            myComboZone.readonly(true);
        },

        comboDcCd: function () {
            myComboDcCd = new dhtmlXCombo("dcCd");
            comboList(myComboDcCd, 'A', 'DC_CD', '', '', 'Y', 'req');
            myComboDcCd.selectOption(0);
            myComboDcCd.setFontSize("12px", "12px");
            myComboDcCd.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
            });
            myComboDcCd.readonly(true);
        },

        comboUseYn: function () {
            myComboUseYn = new dhtmlXCombo("useYn");
            myComboUseYn.addOption([{value: "", text: "전체"}]);
            myComboUseYn.addOption([{value: "Y", text: "계획"}]);
            myComboUseYn.addOption([{value: "N", text: "미계획"}]);
            myComboUseYn.selectOption(0);
            myComboUseYn.setFontSize("12px", "12px");
            myComboUseYn.attachEvent("onChange", function (value, text) {
            });
            myComboUseYn.readonly(true);
        },

        comboSeatType: function () {
            myComboSeatType = new dhtmlXComboFromSelect("seatType");
            comboList(myComboSeatType, 'A', 'COMM', 'INST_SEAT_TYPE', '', 'Y', 'all');
            myComboSeatType.selectOption(0);
            myComboSeatType.setFontSize("12px", "12px");
            myComboSeatType.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
                myGrid2.clearAll(false);
                $("#totCnt").val("");
                $("#xtotCnt").val("");
                $("#cbmSumInput").val("");
                $("#costSumInput").val("");
                $("#distance").val("");
                $("#paltNoInput").val("");
                $("#instErName").val("");
                $("#tblUserMId").val("");
                if (this.getIndexByValue(value) != 0) {
                    this.setChecked(this.getIndexByValue(value), true);
                    var c = 0;
                    for (var i = 1; i < this.getOptionsCount(); i++) {
                        if (this.isChecked(i)) {
                            c++;
                        }
                    }
                    this.updateOption("", "", c + "개 선택", "");
                    this.selectOption(0);
                    this.openSelect();
                }
            });
            myComboSeatType.attachEvent("onCheck", function (value, text) {
                if (!value) {
                    if (myComboSeatType.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboSeatType.getOptionsCount(); i++) {
                            myComboSeatType.setChecked(i, true);
                            c++;
                        }
                        myComboSeatType.updateOption("", "", c + "개 선택", "");
                        myComboSeatType.openSelect();
                    }
                    if (!myComboSeatType.isChecked(0)) {
                        var c = 0;
                        for (var i = 1; i < myComboSeatType.getOptionsCount(); i++) {
                            myComboSeatType.setChecked(i, false);
                        }
                        myComboSeatType.updateOption("", "", c + "개 선택", "");
                        myComboSeatType.openSelect();
                        return;
                    }
                } else {
                    var cnt = 0;
                    for (var i = 1; i < myComboSeatType.getOptionsCount(); i++) {
                        if (myComboSeatType.isChecked(i)) {
                            cnt++;
                        }
                    }
                    if ((myComboSeatType.getOptionsCount() - 1) == cnt) {
                        myComboSeatType.setChecked(0, true);
                    } else {
                        myComboSeatType.setChecked(0, false);
                    }
                    myComboSeatType.updateOption("", "", cnt + "개 선택", "");
                    myComboSeatType.openSelect();
                    return;
                }
            });
            myComboSeatType.readonly(true);
        }
    },

    gridOpen: {
        grid: function () {
            myGrid.setImagePath("http://cdn.dhtmlx.com/edge/skins/terrace/imgs/");

            myGrid.setHeader(
                "계획" +
                ",CUD" +
                ",No" +
                ",팔렛트<br>초기화" +
                ",시공<br>계획" +
                ",모바일<br>전송유무" +
                ",타업체" +
                ",판매오더<br>유형" +
                ",시공카테<br>고리" +
                ",고객명" +
                ",AL오더" +
                ",주소" +
                ",팔렛트<br>번호" +
                ",시공기사" +
                ",배송메모" +
                ",고객요청" +
                ",CBM" +
                ",제품리스트" +
                ",시공비" +
                ",순번" +
                ",기사고유아이디" +
                ",요청일" +
                ",시공유형" +
                ",시공좌석유형" +
                ",지도<br>찾기" +
                ",mapY" +
                ",mapX" +
                ",mapIndex" +
                ",시공계획고유ID" +
                ",시공일" +
                ",회사코드" +
                ",판매오더고유ID" +
                ",화주사코드" +
                ",화주사" +
                ",판매오더유형코드" +
                ",판매오더<br>상태코드" +
                ",판매오더<br>상태" +
                ",쇼핑몰" +
                ",우편번호" +
                ",상세주소" +
                ",시공여부	" +
                ",시공카테고리코드" +
                ",시공유형코드" +
                ",시공좌석유형코드" +
                ",권역유형코드" +
                ",권역유형" +
                ",권역코드" +
                ",권역" +
                ",물류센터코드" +
                ",익일배송유무" +
                ",수량" +
                ",시공상태코드" +
                ",시공사" +
                ",시공계획시간" +
                ",확정일" +
                ",배송비" +
                ",간단주소" +
                ",총건수" +
                ",미확정건수" +
                ",제품목록"
            );

            myGrid.setColumnIds(
                "chk" +
                ",cud" +
                ",rowId" +
                ",delChk" +
                ",instPlanYn" +
                ",mobileTranYn" +
                ",outCmpyCd" +
                ",soTypeNm" +
                ",instCtgrNm" +
                ",acptEr" +
                ",soNo" +
                ",addr1" +
                ",paltNoxx" +
                ",instEr" +
                ",dlvyRqstMsg" +
                ",custSpclTxt" +
                ",cbm" +
                ",prodNm" +
                ",instCost" +
                ",seq" +
                ",tblUserMId" +
                ",rqstDt" +
                ",instTypeNm" +
                ",instSeatTypeNm" +
                ",mapFindAddr" +
                ",mapY" +
                ",mapX" +
                ",mapIndex" +
                ",instPlanId" +
                ",instDt" +
                ",cmpyCd" +
                ",tblSoMId" +
                ",agntCd" +
                ",agntNm" +
                ",soType" +
                ",soStatCd" +
                ",soStatNm" +
                ",mallCd" +
                ",postCd" +
                ",addr2" +
                ",instYn" +
                ",instCtgr" +
                ",instType" +
                ",instSeatType" +
                ",zoneType" +
                ",zoneTypeNm" +
                ",zoneCd" +
                ",zoneNm" +
                ",dcCd" +
                ",ndlvyYn" +
                ",qty" +
                ",instStatCd" +
                ",instCmpy" +
                ",instPlanDt" +
                ",cnfmTime" +
                ",dlvyCost" +
                ",smplAddr" +
                ",totCnt" +
                ",xtotCnt" +
                ",prodList"
            );

            myGrid.setInitWidths(
                "50" +
                ",40" +
                ",40" +
                ",60" +
                ",50" +
                ",70" +
                ",70" +
                ",80" +
                ",80" +
                ",80" +
                ",100" +
                ",200" +
                ",80" +
                ",120" +
                ",160" +
                ",160" +
                ",60" +
                ",120" +
                ",80" +
                ",60" +
                ",120" +
                ",80" +
                ",80" +
                ",160" +
                ",60" +
                ",120" +
                ",120" +
                ",40" +
                ",100" +
                ",80" +
                ",80" +
                ",100" +
                ",100" +
                ",120" +
                ",120" +
                ",120" +
                ",80" +
                ",120" +
                ",160" +
                ",80" +
                ",160" +
                ",80" +
                ",120" +
                ",120" +
                ",120" +
                ",120" +
                ",160" +
                ",80" +
                ",160" +
                ",120" +
                ",120" +
                ",60" +
                ",100" +
                ",120" +
                ",120" +
                ",160" +
                ",80" +
                ",160" +
                ",60" +
                ",60" +
                ",100"
            );

            myGrid.setColAlign(
                "center" +
                ",center" +
                ",right" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                "center" +
                ",center" +
                ",left" +
                ",center" +
                ",center" +
                ",left" +
                ",left" +
                ",right" +
                ",left" +
                ",right" +
                ",right" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",left" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",right" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",center" +
                ",left" +
                ",right" +
                ",right" +
                ",left"
            );

            myGrid.setColTypes(
                "ch" +
                ",ro" +
                ",ro" +
                ",ch" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",edn" +
                ",ro" +
                ",ed" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro" +
                ",ro"
            );

            myGrid.setColSorting(
                "" +
                "," +
                "," +
                "," +
                ",str" +
                ",str" +
                ",str" +
                "," +
                ",str" +
                ",str" +
                "," +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                "," +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                ",str" +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                ","
            );

            myGrid.attachHeader(
                "#master_checkbox" +
                "," +
                "," +
                ",#master_checkbox" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#numeric_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                "," +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                ",#text_filter" +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                "," +
                ","
            );

            myGrid.setColumnHidden(myGrid.getColIndexById("rowId"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("cud"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instPlanId"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instDt"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("cmpyCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("tblSoMId"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("agntCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("agntNm"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("soType"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("soStatCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("soStatNm"), false);
            myGrid.setColumnHidden(myGrid.getColIndexById("mallCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("postCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("addr2"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instYn"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instCtgr"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instType"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instSeatType"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("zoneType"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("zoneTypeNm"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("zoneCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("zoneNm"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("dcCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("ndlvyYn"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("qty"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instStatCd"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instCmpy"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("instPlanDt"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("cnfmTime"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("dlvyCost"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("mapY"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("mapX"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("mapIndex"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("smplAddr"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("totCnt"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("xtotCnt"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("prodList"), true);
            myGrid.setColumnHidden(myGrid.getColIndexById("dcNm"), true);

            myGrid.init();

            myGrid.enableSmartRendering(true);
            myGrid.enablePreRendering(10);
            fnObj.gridOpen.setWidth();
            myGrid.addRow(1, "");
            myGrid.deleteRow(1);

            myGrid.enableEditEvents(true, false, false);
            myGrid.enableBlockSelection(true);
            myGrid.attachEvent("onKeyPress", fnObj.keyOpen.onKeyPressed);

            myGrid.attachEvent("onEditCell", function (stage, rId, cInd, nValue, oValue) {
                if (distanceOverlay != undefined || distanceOverlay != null) {
                    distanceOverlay.forEach(function (distance, i) {
                        distanceOverlay[i].setMap(null);
                    });
                }

                if (drawLine != undefined || drawLine != null) {
                    drawLine.forEach(function (draw, i) {
                        drawLine[i].setMap(null);
                    });
                }

                // 순번
                if (cInd == myGrid.getColIndexById("seq")) {
                    if (stage == 1 && this.editor && this.editor.obj) {
                        this.editor.obj.select();
                    }
                    if (stage == 2 && nValue == "" && nValue != oValue) {
                        return true;
                    }

                    if (stage == 2 && !myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue()) {
                        alert("팔렛트번호가 없습니다.");
                        return false;
                    }
                    if (stage == 2 && isNaN(nValue)) {
                        alert("숫자를 넣으세요");
                        return false;
                    }

                    var selSeqPalt = myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue();
                    var errCnt = 0;
                    if (stage == 2 && !isNaN(nValue)) {
                        myGrid.forEachRow(function (eachRId) {
                            if (eachRId != rId
                                && nValue
                                && myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue() == selSeqPalt
                                && myGrid.cellById(eachRId, myGrid.getColIndexById("seq")).getValue() == nValue
                            ) {
                                errCnt++;
                            }
                        });
                    }
                    if (errCnt > 0) {
                        alert("팔렛트번호 " + selSeqPalt + " 의 순번 " + nValue + " (은)는 이미 있습니다.");
                        return false;
                    } else {
                        myGrid.cellById(rId, myGrid.getColIndexById("cud")).setValue("U");
                        myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("1");

                        addr = [];
                        myGrid.forEachRow(function (eachRId) {
                            var arrdVal = {};
                            arrdVal["seq"] = myGrid.cellById(eachRId, myGrid.getColIndexById("seq")).getValue();
                            arrdVal["addr1"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr1")).getValue();
                            arrdVal["addr2"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr2")).getValue();
                            arrdVal["instPlanYn"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instPlanYn")).getValue();
                            arrdVal["rowId"] = myGrid.cellById(eachRId, myGrid.getColIndexById("rowId")).getValue();
                            arrdVal["paltNoxx"] = myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue();
                            arrdVal["mapFindAddr"] = myGrid.cellById(eachRId, myGrid.getColIndexById("mapFindAddr")).getValue();
                            arrdVal["instSeatType"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instSeatType")).getValue();
                            arrdVal["prodList"] = myGrid.cellById(eachRId, myGrid.getColIndexById("prodList")).getValue();
                            addr.push(arrdVal);
                        });
                        onLoadMap();
                        return true;
                    }
                }
                // 팔렛트번호
                else if (cInd == myGrid.getColIndexById("paltNoxx")) {
                    if (stage == 1 && this.editor && this.editor.obj) {
                        this.editor.obj.select();
                    }
                    if (stage == 2 && nValue == "" && nValue != oValue) {
                        return true;
                    }
                    if (stage == 2 && nValue != oValue) {
                        myGrid.cellById(rId, myGrid.getColIndexById("seq")).setValue("");
                    }

                    if (stage == 2 && nValue) {
                        myGrid.cellById(rId, myGrid.getColIndexById("cud")).setValue("U");
                        myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("1");
                        var paltUserNm = "";

                        myGrid.forEachRow(function (eachRId) {
                            if (myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue() == nValue) {
                                if (paltUserNm == "") {
                                    paltUserNm = myGrid.cellById(eachRId, myGrid.getColIndexById("instEr")).getValue();
                                    myGrid.cellById(rId, myGrid.getColIndexById("instEr")).setValue(paltUserNm);
                                }
                            }
                        });

                        addr = [];
                        myGrid.forEachRow(function (eachRId) {
                            var arrdVal = {};
                            arrdVal["seq"] = myGrid.cellById(eachRId, myGrid.getColIndexById("seq")).getValue();
                            arrdVal["addr1"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr1")).getValue();
                            arrdVal["addr2"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr2")).getValue();
                            arrdVal["instPlanYn"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instPlanYn")).getValue();
                            arrdVal["rowId"] = myGrid.cellById(eachRId, myGrid.getColIndexById("rowId")).getValue();
                            arrdVal["paltNoxx"] = myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue();
                            arrdVal["mapFindAddr"] = myGrid.cellById(eachRId, myGrid.getColIndexById("mapFindAddr")).getValue();
                            arrdVal["instSeatType"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instSeatType")).getValue();
                            arrdVal["prodList"] = myGrid.cellById(eachRId, myGrid.getColIndexById("prodList")).getValue();
                            addr.push(arrdVal);
                        });
                        onLoadMap();
                        return true;
                    }
                }
                // 시공기사 변경
                else if (cInd == myGrid.getColIndexById("instEr")) {
                    var rtn = true;
                    if (stage == 1 && this.editor && this.editor.obj) {
                        this.editor.obj.select();
                    }

                    if (stage == 2 && nValue == "" && nValue != oValue) {
                        var paltNo = myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue();
                        if (paltNo) {
                            myGrid.forEachRow(function (eachRId) {
                                if (myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue() == paltNo) {
                                    myGrid.cellById(eachRId, myGrid.getColIndexById("instEr")).setValue(nValue);
                                    myGrid.cellById(eachRId, myGrid.getColIndexById("tblUserMId")).setValue(nValue);
                                    myGrid.cellById(eachRId, myGrid.getColIndexById("cud")).setValue("U");
                                    myGrid.cellById(eachRId, myGrid.getColIndexById("chk")).setValue("1");
                                }
                            });
                        } else {
                            myGrid.cellById(rId, myGrid.getColIndexById("instEr")).setValue(nValue);
                            myGrid.cellById(rId, myGrid.getColIndexById("tblUserMId")).setValue(nValue);
                            myGrid.cellById(rId, myGrid.getColIndexById("cud")).setValue("U");
                            myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("1");
                        }
                        return true;
                    }
                    if (stage == 2) {
                        var cmpyCd_value = myGrid.cellById(rId, myGrid.getColIndexById("cmpyCd")).getValue();
                        var dcCd_value = myGrid.cellById(rId, myGrid.getColIndexById("dcCd")).getValue();
                        var instEr_value = nValue;

                        var return_value = fnObj.searchOpen.instErSrch(rId, cmpyCd_value, dcCd_value, instEr_value);

                        if (return_value && return_value.userNm && return_value.tblUserMId) {
                            nValue = return_value.userNm;
                            var paltNo = myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue();
                            if (paltNo) {
                                myGrid.forEachRow(function (eachRId) {
                                    if (myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue() == paltNo) {
                                        myGrid.cellById(eachRId, myGrid.getColIndexById("instEr")).setValue(return_value.userNm);
                                        myGrid.cellById(eachRId, myGrid.getColIndexById("tblUserMId")).setValue(return_value.tblUserMId);
                                        myGrid.cellById(eachRId, myGrid.getColIndexById("cud")).setValue("U");
                                        myGrid.cellById(eachRId, myGrid.getColIndexById("chk")).setValue("1");
                                    }
                                });
                            } else {
                                myGrid.cellById(rId, myGrid.getColIndexById("instEr")).setValue(return_value.userNm);
                                myGrid.cellById(rId, myGrid.getColIndexById("tblUserMId")).setValue(return_value.tblUserMId);
                                myGrid.cellById(rId, myGrid.getColIndexById("cud")).setValue("U");
                                myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("1");
                            }
                        } else {
                            rtn = false;
                        }
                    } else if (stage == 2 && !nValue) {
                        rtn = false;
                    }
                    return rtn;
                } else {
                    return true;
                }
            });

            myGrid.attachEvent("onRowSelect", function (rId, cInd) {
                var mapIndex;

                markers.forEach(function (marker, index) {
                    if (markers[index].index == myGrid.cellById(rId, myGrid.getColIndexById("mapIndex")).getValue()) {
                        mapIndex = index;
                    }
                });
                var mapFindAddr = myGrid.cellById(rId, myGrid.getColIndexById("mapFindAddr")).getValue();

                if (mapFindAddr.trim() == "OK") {
                    var rowY = myGrid.cellById(rId, myGrid.getColIndexById("mapY")).getValue();
                    var rowX = myGrid.cellById(rId, myGrid.getColIndexById("mapX")).getValue();
                    var rowLatlng = new kakao.maps.LatLng(rowY, rowX);

                    map.setCenter(rowLatlng);
                }
            });

            myGrid.attachEvent("onCheckBox", function (rId, cInd, state) {
                if (cInd == myGrid.getColIndexById("delChk")) {
                    fnObj.searchOpen.grid2Distance();

                    if (state == false) {
                        $("#grid").find("input:eq(3)").prop("checked", false);
                    } else {
                        var delCnt1 = 0;
                        var enChk = 0;
                        myGrid.forEachRow(function (pId) {
                            if (myGrid.cellById(pId, myGrid.getColIndexById("delChk")).getValue() == "1") {
                                delCnt1++;
                            }
                            if (!myGrid.cells(pId, cInd).isDisabled()) {
                                enChk++;
                            }
                        });
                        if (enChk == delCnt1) {
                            $("#grid").find("input:eq(3)").prop("checked", true);
                        }
                    }
                }

                if (cInd == myGrid.getColIndexById("chk")) {
                    if (state == false) {
                        $("#grid").find("input:eq(0)").prop("checked", false);
                    } else {
                        if (myGrid.cellById(rId, myGrid.getColIndexById("soStatCd")).getValue() == "9999") {
                            toastr.error('이미 배송 완료입니다.');
                            $("#grid").find("input:eq(0)").prop("checked", false);
                            myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("0");
                            return;
                        }
                        if (myGrid.cellById(rId, myGrid.getColIndexById("soStatCd")).getValue() == "8000") {
                            toastr.error('이미 배송 완료 (미마감)입니다.');
                            $("#grid").find("input:eq(0)").prop("checked", false);
                            myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("0");
                            return;
                        }
                        if (myGrid.cellById(rId, myGrid.getColIndexById("mobileTranYn")).getValue() == "Y") {
                            toastr.error('이미 모바일 전송 완료입니다.');
                            $("#grid").find("input:eq(0)").prop("checked", false);
                            myGrid.cellById(rId, myGrid.getColIndexById("chk")).setValue("0");
                            return;
                        }
                        var chkCnt1 = 0;
                        myGrid.forEachRow(function (pId) {
                            if (myGrid.cellById(pId, myGrid.getColIndexById("chk")).getValue() == "1") {
                                chkCnt1++;
                            }
                        });
                        if (myGrid.getRowsNum() == chkCnt1) {
                            $("#grid").find("input:eq(0)").prop("checked", true);
                        }
                    }
                }

                var selectPaltNoxx = myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue();
                var mapIndex;

                markers.forEach(function (marker, index) {
                    if (markers[index].index == myGrid.cellById(rId, myGrid.getColIndexById("mapIndex")).getValue()) {
                        mapIndex = index;
                    }
                });
                var mapFindAddr = myGrid.cellById(rId, myGrid.getColIndexById("mapFindAddr")).getValue();

                if (mapFindAddr.trim() == "OK") {
                    var rowY = myGrid.cellById(rId, myGrid.getColIndexById("mapY")).getValue();
                    var rowX = myGrid.cellById(rId, myGrid.getColIndexById("mapX")).getValue();
                    var rowLatlng = new kakao.maps.LatLng(rowY, rowX);

                    map.setCenter(rowLatlng);
                }

                if (cInd == myGrid.getColIndexById("chk")) {
                    if (mapIndex >= 0) {
                        if (markers[mapIndex].clickYn == "N" && state == true) {
                            markers[mapIndex].clickYn = "Y";
                            markers[mapIndex].setImage(markers[mapIndex].clickImage);
                            myGrid.selectRowById(markers[mapIndex].rowId);

                            var cnt = 0;
                            myGrid2.forEachRow(function (eId) {
                                if (myGrid2.cellById(eId, myGrid2.getColIndexById("rowId")).getValue() == myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("rowId")).getValue()) {
                                    cnt++;
                                }
                            });
                            if (cnt == 0) {
                                myGrid2.addRow(markers[mapIndex].rowId, "");
                                myGrid2.setRowData(markers[mapIndex].rowId, myGrid.getRowData(markers[mapIndex].rowId));

                                myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("delChk")).setValue("0");
                                myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("delChk")).setDisabled(true);

                                if (myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("instPlanYn")).getValue() == "Y") {
                                    myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("saveGubn")).setValue("UPDT");
                                } else {
                                    myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("saveGubn")).setValue("INS");
                                }

                                var newPaltNo = 0;
                                myGrid2.forEachRow(function (pId) {
                                    if (Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue()) > newPaltNo) {
                                        newPaltNo = Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                    }
                                });
                                myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("newPaltNoSeq")).setValue((newPaltNo + 1) + "");

                                myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("chk")).setValue("");
                                fnObj.searchOpen.grid2CbmSum();
                                fnObj.searchOpen.grid2InstCostSum();
                                fnObj.searchOpen.grid2Distance();
                            }
                        } else if (markers[mapIndex].clickYn == "Y" && state == false) {
                            markers[mapIndex].clickYn = "N";
                            markers[mapIndex].setImage(markers[mapIndex].normalImage);
                            myGrid.selectRowById(markers[mapIndex].rowId);
                            myGrid2.forEachRow(function (pId) {
                                var oldP = Number(myGrid2.cellById(markers[mapIndex].rowId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                var newP = Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                if (newP > oldP) {
                                    myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).setValue((newP - 1) + "");
                                }
                            });
                            myGrid2.deleteRow(markers[mapIndex].rowId);

                            if (myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("instPlanYn")).getValue() != "Y") {
                                myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("delChk")).setDisabled(true);
                            } else {
                                myGrid.cellById(markers[mapIndex].rowId, myGrid.getColIndexById("delChk")).setDisabled(false);
                            }

                            fnObj.searchOpen.grid2CbmSum();
                            fnObj.searchOpen.grid2InstCostSum();
                            fnObj.searchOpen.grid2Distance();
                        }
                    } else {
                        if (state == true) {
                            myGrid.selectRowById(rId);
                            var cnt = 0;
                            myGrid2.forEachRow(function (eId) {
                                if (myGrid2.cellById(eId, myGrid2.getColIndexById("rowId")).getValue() == myGrid.cellById(rId, myGrid.getColIndexById("rowId")).getValue()) {
                                    cnt++;
                                }
                            });
                            if (cnt == 0) {
                                myGrid2.addRow(rId, "");
                                myGrid2.setRowData(rId, myGrid.getRowData(rId));

                                myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setValue("0");
                                myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setDisabled(true);

                                if (myGrid2.cellById(rId, myGrid2.getColIndexById("instPlanYn")).getValue() == "Y") {
                                    myGrid2.cellById(rId, myGrid2.getColIndexById("saveGubn")).setValue("UPDT");
                                } else {
                                    myGrid2.cellById(rId, myGrid2.getColIndexById("saveGubn")).setValue("INS");
                                }
                                var newPaltNo = 0;
                                myGrid2.forEachRow(function (pId) {
                                    if (Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue()) > newPaltNo) {
                                        newPaltNo = Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                    }
                                });
                                myGrid2.cellById(rId, myGrid2.getColIndexById("newPaltNoSeq")).setValue((newPaltNo + 1) + "");
                                myGrid2.cellById(rId, myGrid2.getColIndexById("chk")).setValue("");
                                fnObj.searchOpen.grid2CbmSum();
                                fnObj.searchOpen.grid2InstCostSum();
                                fnObj.searchOpen.grid2Distance();
                            }
                        } else if (state == false) {
                            myGrid.selectRowById(rId);
                            myGrid2.forEachRow(function (pId) {
                                var oldP = Number(myGrid2.cellById(rId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                var newP = Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                                if (newP > oldP) {
                                    myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).setValue((newP - 1) + "");
                                }
                            });
                            myGrid2.deleteRow(rId);

                            if (myGrid.cellById(rId, myGrid.getColIndexById("instPlanYn")).getValue() != "Y") {
                                myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setDisabled(true);
                            } else {
                                myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setDisabled(false);
                            }

                            fnObj.searchOpen.grid2CbmSum();
                            fnObj.searchOpen.grid2InstCostSum();
                            fnObj.searchOpen.grid2Distance();
                        }
                    }
                }
                var cbmSum = 0;
                var costSum = 0;
                var selPaltNo = myGrid.cellById(rId, myGrid.getColIndexById("paltNoxx")).getValue();
            });

            myGrid.attachEvent("onRowDblClicked", function (rId, cInd) {
                $("#grid1RowId").val(rId);
            });
        },

        grid2: function () {
            myGrid2.setImagePath("http://cdn.dhtmlx.com/edge/skins/terrace/imgs/");
            myGrid2.setHeader("#master_checkbox,CUD,No,saveGubn,순번," +
                "시공<br>계획,모바일<br>전송유무," +
                "타업체,판매오더<br>유형,시공카테<br>고리," +
                "고객명,물류센터,주소,팔렛트<br>번호,시공기사,배송메모,고객요청," +
                "CBM,제품리스트,시공비,순번," +
                "기사고유아이디,요청일,AL오더," +
                "시공유형,시공좌석유형,지도<br>찾기,mapY,mapX," +
                "mapIndex ,시공계획고유ID,시공일,회사코드,판매오더고유ID," +
                "화주사코드,화주사,판매오더유형코드,판매오더상태코드," +
                "판매오더상태,쇼핑몰,우편번호,상세주소," +
                "시공여부,시공카테고리코드,시공유형코드,시공좌석유형코드,권역유형코드," +
                "권역유형,권역코드,권역,물류센터코드,익일배송유무," +
                "수량,시공상태코드,시공사,시공계획시간,확정일," +
                "배송비,간단주소,총건수,미확정건수");
            myGrid2.setColumnIds("chk,cud,rowId,saveGubn,newPaltNoSeq," +
                "instPlanYn,mobileTranYn," +
                "outCmpyCd,soTypeNm,instCtgrNm," +
                "acptEr,dcNm,addr1,paltNoxx,instEr,dlvyRqstMsg,custSpclTxt," +
                "cbm,prodNm,instCost,seq," +
                "tblUserMId,rqstDt,soNo," +
                "instTypeNm,instSeatTypeNm,mapFindAddr,mapY,mapX," +
                "mapIndex,instPlanId,instDt,cmpyCd,tblSoMId," +
                "agntCd,agntNm,soType,soStatCd," +
                "soStatNm,mallCd,postCd,addr2," +
                "instYn,instCtgr,instType,instSeatType,zoneType," +
                "zoneTypeNm,zoneCd,zoneNm,dcCd,ndlvyYn," +
                "qty,instStatCd,instCmpy,instPlanDt,cnfmTime," +
                "dlvyCost,smplAddr,totCnt,xtotCnt");
            myGrid2.setInitWidths("40,40,40,40,60," +
                "50,70," +
                "70,80,80," +
                "80,100,200,80,120,160,160," +
                "60,120,80,60," +
                "120,80,100," +
                "80,160,60,120,120," +
                "40,100,80,80,100," +
                "100,120,120,120," +
                "120,160,80,160," +
                "80,120,120,120,120," +
                "160,80,160,120,120," +
                "60,100,120,120,160," +
                "80,160,60,60");
            myGrid2.setColAlign("center,center,right,center,right," +
                "center,center," +
                "center,center,center," +
                "center,center,left,center,center,left,left," +
                "right,left,right,right," +
                "center,center,center," +
                "center,center,center,center,center," +
                "center,center,center,center,center," +
                "center,center,center,center," +
                "center,center,center,left," +
                "center,center,center,center,center," +
                "center,center,center,center,center," +
                "right,center,center,center,center," +
                "center,left,right,right");
            myGrid2.setColTypes("ch,ro,ro,ro,ro," +
                "ro,ro," +
                "ro,ro,ro," +
                "ro,ro,ro,ro,ro,ro,ro," +
                "ro,ro,ro,ro," +
                "ro,ro,ro," +
                "ro,ro,ro,ro,ro," +
                "ro,ro,ro,ro,ro," +
                "ro,ro,ro,ro," +
                "ro,ro,ro,ro," +
                "edn,ro,ed,ro,ro," +
                "ro,ro,ro,ro,ro," +
                "ro,ro,ro,ro,ro," +
                "ro,ro,ro,ro");
            myGrid2.setColSorting(",,,,," +
                "str,str," +
                ",str,,str," +
                ",str,str,str,str,str,str," +
                "str,str,str,str," +
                ",str,str," +
                "str,str,str,str,str," +
                "str,,,,," +
                ",,,," +
                ",,,," +
                ",,,,," +
                ",,,,," +
                ",,,,," +
                ",,,");

            myGrid2.setColumnHidden(myGrid2.getColIndexById("seq"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("rowId"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("saveGubn"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("cud"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instPlanId"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instDt"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("cmpyCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("tblSoMId"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("agntCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("agntNm"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("soType"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("soStatCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("soStatNm"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("mallCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("postCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("addr2"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instYn"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instCtgr"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instType"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instSeatType"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("zoneType"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("zoneTypeNm"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("zoneCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("zoneNm"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("dcCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("ndlvyYn"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("qty"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instStatCd"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instCmpy"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("instPlanDt"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("cnfmTime"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("dlvyCost"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("mapY"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("mapX"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("mapIndex"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("smplAddr"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("totCnt"), true);
            myGrid2.setColumnHidden(myGrid2.getColIndexById("xtotCnt"), true);

            myGrid2.init();

            myGrid2.enableSmartRendering(true);
            myGrid2.enablePreRendering(10);
            fnObj.gridOpen.setWidth();
            myGrid2.addRow(1, "");
            myGrid2.deleteRow(1);

            myGrid2.enableEditEvents(true, false, false);

            myGrid2.attachEvent("onRowSelect", function (rId, cInd) {
                var mapIndex;

                markers.forEach(function (marker, index) {
                    if (markers[index].index == myGrid2.cellById(rId, myGrid2.getColIndexById("mapIndex")).getValue()) {
                        mapIndex = index;
                    }
                });
                var mapFindAddr = myGrid2.cellById(rId, myGrid2.getColIndexById("mapFindAddr")).getValue();

                if (mapFindAddr.trim() != "OK") {
                    // 주소로 위치를 찾을수 없습니다.
                } else {
                    var rowY = myGrid2.cellById(rId, myGrid2.getColIndexById("mapY")).getValue();
                    var rowX = myGrid2.cellById(rId, myGrid2.getColIndexById("mapX")).getValue();
                    var rowLatlng = new kakao.maps.LatLng(rowY, rowX);

                    map.setCenter(rowLatlng);
                }
            });
        },

        setWidth: function () {
            var c_width = window.innerWidth;
            var searchDiv_height = $("#searchDiv").height();
            var searchDiv2nd_height = $("#searchDiv2nd").height();
            var page_height = $("#page").height();
            var page2_height = $("#page2").height();
            var xhdr_height = $(".xhdr").height();

            myGrid.enableAutoWidth(true, (c_width) * 0.56, 100);
            myGrid2.enableAutoWidth(true, (c_width) * 0.56, 100);

            myGrid.enableAutoHeight(true, (window.innerHeight - searchDiv_height - searchDiv2nd_height - xhdr_height) * 0.5 - 30);
            myGrid2.enableAutoHeight(true, (window.innerHeight - searchDiv_height - searchDiv2nd_height - xhdr_height) * 0.3 - 30);

            $("#searchDiv").width(c_width - 25);
            $("#searchDiv2nd").width(c_width - 25);

            $("#centerLine").css("height", $("#mapdiv").height());
            $("#centerLine").css("width", (window.innerWidth - $("#mapdiv").width() - $("#gridDiv").width()) / 2);
        }
    },

    searchOpen: {
        grid2Distance: function () {
            if (distanceOverlay != undefined || distanceOverlay != null) {
                distanceOverlay.forEach(function (distance, i) {
                    distanceOverlay[i].setMap(null);
                });
            }

            if (drawLine != undefined || drawLine != null) {
                drawLine.forEach(function (draw, i) {
                    drawLine[i].setMap(null);
                });
            }

            var samePaltNoxxs = [];
            myGrid2.forEachRow(function (pId) {
                var grid2RowId = myGrid2.cellById(pId, myGrid2.getColIndexById("rowId")).getValue();
                var grid2NewPaltNoSeq = myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue();

                positions.forEach(function (position, index) {
                    if (grid2RowId == position.rowId && position.rowId) {
                        var pos = position;
                        pos.newPaltNoSeq = grid2NewPaltNoSeq;
                        samePaltNoxxs.push(pos);
                    }
                });
            });

            samePaltNoxxs.sort(function (a, b) {
                return Number(a.newPaltNoSeq) < Number(b.newPaltNoSeq) ? -1 : Number(a.newPaltNoSeq) > Number(b.newPaltNoSeq) ? 1 : 0;
            });

            var linePath;
            var lineLine = new daum.maps.Polyline();
            var distance;

            var totalDistance = 0;
            samePaltNoxxs.forEach(function (samePaltNoxx, i) {
                if (i != 0) {
                    linePath = [samePaltNoxxs[i - 1].latlng, samePaltNoxxs[i].latlng];
                }

                lineLine.setPath(linePath);

                drawLine[i] = new daum.maps.Polyline({
                    endArrow: true,
                    path: linePath,
                    strokeWeight: 3,
                    strokeColor: '#585858',
                    strokeOpacity: 0.5,
                    strokeStyle: 'solide'
                });
                drawLine[i].setMap(map);
                distance = Math.round(lineLine.getLength());
                totalDistance = totalDistance + distance;
                displayCircleDot(samePaltNoxxs[i].latlng, distance, i);
            });
            $("#distance").val(Math.round(totalDistance / 1000) + " km");
        },

        grid2Delete: function () {
            var checked = myGrid2.getCheckedRows(myGrid2.getColIndexById("chk"));
            if (!checked) {
                alert("삭제대상을 체크해주세요");
                return;
            }
            myGrid2.forEachRow(function (rId) {
                if (myGrid2.cellById(rId, myGrid2.getColIndexById("chk")).getValue() == "1") {
                    var grid2MapIndex = myGrid2.cellById(rId, myGrid2.getColIndexById("mapIndex")).getValue();
                    var grid2RowId = myGrid2.cellById(rId, myGrid2.getColIndexById("rowId")).getValue();
                    var mapIndex2;
                    markers.forEach(function (marker, index) {
                        if (markers[index].index == myGrid.cellById(rId, myGrid.getColIndexById("mapIndex")).getValue()) {
                            mapIndex2 = index;
                        }
                    });

                    if (mapIndex2 >= 0) {
                        var rowY2 = myGrid2.cellById(rId, myGrid2.getColIndexById("mapY")).getValue();
                        var rowX2 = myGrid2.cellById(rId, myGrid2.getColIndexById("mapX")).getValue();
                        var rowLatlng2 = new kakao.maps.LatLng(rowY2, rowX2);
                        map.setCenter(rowLatlng2);

                        markers[mapIndex2].clickYn = "N";
                        markers[mapIndex2].setImage(markers[mapIndex2].normalImage);
                    }
                    myGrid.forEachRow(function (eId) {
                        if (myGrid.cellById(eId, myGrid.getColIndexById("rowId")).getValue() == grid2RowId) {
                            myGrid.cellById(eId, myGrid.getColIndexById("chk")).setValue("0");
                            myGrid.selectRowById(eId);
                        }
                    });
                    myGrid2.forEachRow(function (pId) {
                        var oldP = Number(myGrid2.cellById(rId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                        var newP = Number(myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).getValue());
                        if (newP > oldP) {
                            myGrid2.cellById(pId, myGrid2.getColIndexById("newPaltNoSeq")).setValue((newP - 1) + "");
                        }
                    });
                    myGrid2.deleteRow(rId);
                    if (myGrid.cellById(rId, myGrid.getColIndexById("instPlanYn")).getValue() != "Y") {
                        myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setDisabled(true);
                    } else {
                        myGrid.cellById(rId, myGrid.getColIndexById("delChk")).setDisabled(false);
                    }
                    fnObj.searchOpen.grid2CbmSum();
                    fnObj.searchOpen.grid2InstCostSum();
                    fnObj.searchOpen.grid2Distance();
                }
            });
        },

        grid2CbmSum: function () {
            var cbmSum = 0;
            myGrid2.forEachRow(function (rId) {
                cbmSum = cbmSum + parseFloat(myGrid2.cellById(rId, myGrid2.getColIndexById("cbm")).getValue() == null || myGrid2.cellById(rId, myGrid2.getColIndexById("cbm")).getValue() == "" ? "0" : myGrid2.cellById(rId, myGrid2.getColIndexById("cbm")).getValue());
            });
            $("#cbmSumInput").val(cbmSum.toFixed(2) + "");
        },

        grid2InstCostSum: function () {
            var instCostSum = 0;
            myGrid2.forEachRow(function (rId) {
                instCostSum = instCostSum + parseFloat(myGrid2.cellById(rId, myGrid2.getColIndexById("instCost")).getValue() == null || myGrid2.cellById(rId, myGrid2.getColIndexById("instCost")).getValue() == "" ? "0" : myGrid2.cellById(rId, myGrid2.getColIndexById("instCost")).getValue());
            });
            $("#costSumInput").val(instCostSum.toFixed(0) + " 원");
        },

        instErSrch: function (rId, cmpyCd, dcCd, instEr) {
            var instErSrchData = new FormData();
            instErSrchData.append("cmpyCd", cmpyCd);
            instErSrchData.append("dcCd	", dcCd);
            instErSrchData.append("userNm", instEr);
            instErSrchData.append("useYn", "Y");
            var rtnVal = null;
            $.ajax({
                url: "/erp203001InstErSrch",
                processData: false,
                contentType: false,
                data: instErSrchData,
                type: "POST",
                dataType: "json",
                async: false,
                success: function (res) {
                    if (res && res.length == 1) {
                        rtnVal = res[0];
                    } else {
                        fnObj.winOpen.instErOpen(rId, cmpyCd, dcCd, instEr, "Y");
                        rtnVal = null;
                    }
                }
            });
            return rtnVal;
        },

        delChk: function () {
            var delData = new FormData();
            var delChkData = [];
            var delChk = {};
            var cnt = 0;
            myGrid.forEachRow(function (RowId) {
                var delChkVal = myGrid.cellById(RowId, myGrid.getColIndexById("delChk")).getValue();
                if (delChkVal == "1") {
                    cnt++;
                    delChk = myGrid.getRowData(RowId);
                    delChk.saveUser = sessionTblUserMId;
                    delChkData.push(delChk);
                    delChk = {};
                }
            });
            if (cnt == 0) {
                alert("팔렛트초기화 대상을 체크해주세요");
                return;
            }
            if (!confirm(cnt + "건을 팔렛트초기화 하시겠습니까?")) {
                return;
            }
            $.ajax({
                url: "/erp203001DelChk",
                processData: false,
                contentType: "application/json",
                data: JSON.stringify(delChkData),
                type: "POST",
                dataType: "json",
                success: function (res) {
                    if (res) {
                        var msg = "";
                        for (var i = 0; i < res.length; i++) {
                            if (res[i].rtnYn == "Y") {
                                msg = msg + "→팔렛트초기화 성공 - 오더번호 : " + res[i].soNo + "\n";
                            } else {
                                msg = msg + "▶팔렛트초기화 실패 - 오더번호 : " + res[i].soNo + "\n";
                            }
                        }
                        alert(msg);
                        fnObj.searchOpen.search();
                    } else {
                        alert("삭제 수행 결과 없음");
                    }
                }
            });
        },

        search: function () {
            myGrid.clearAll();
            myGrid2.clearAll();
            $("#totCnt").val("");
            $("#xtotCnt").val("");
            $("#cbmSumInput").val("");
            $("#costSumInput").val("");
            $("#distance").val("");
            $("#paltNoInput").val("");
            $("#instErName").val("");
            $("#tblUserMId").val("");

            if (distanceOverlay != undefined || distanceOverlay != null) {
                distanceOverlay.forEach(function (distance, i) {
                    distanceOverlay[i].setMap(null);
                });
            }

            if (drawLine != undefined || drawLine != null) {
                drawLine.forEach(function (draw, i) {
                    drawLine[i].setMap(null);
                });
            }

            if (!fnObj.searchOpen.searchChkVal.chkVal("search")) {
                alert(chkValMsg);
                return false;
            }
            var srchData = new FormData();
            srchData.append("cmpyCd", myComboCmpyCd.getSelectedValue());
            srchData.append("fromDt", $("#dateFrom").val().replace(/-/g, ""));
            srchData.append("toDt", $("#dateFrom").val().replace(/-/g, ""));
            srchData.append("instCtgrList", myComboInstCapaCtgr.getChecked());
            srchData.append("instTypeList", myComboInstCapaType.getChecked());
            srchData.append("instSeatTypeList", myComboSeatType.getChecked());
            srchData.append("instZoneTypeList", myComboZoneType.getChecked());
            srchData.append("instZoneCdList", myComboZone.getChecked());
            srchData.append("dcCd", myComboDcCd.getSelectedValue());
            srchData.append("instPlanYn", myComboUseYn.getSelectedValue());
            srchData.append("paltNoxx", $("#paltNoxx").val());
            srchData.append("instEr", $("#instEr").val());

            $.ajax({
                url: "/erp203001List",
                processData: false,
                contentType: false,
                data: srchData,
                type: "POST",
                dataType: "json",
                success: function (res) {
                    if (res && res.length > 0) {
                        $("#totCnt").val(res[0].totCnt);
                        $("#xtotCnt").val(res[0].xtotCnt);
                        var columns = myGrid.getColumnCount();
                        for (var i = 0; i < res.length; i++) {
                            var jsonData = {};
                            for (var c = 0; c < columns; c++) {
                                if (myGrid.getColumnId(c) == "rowId") {
                                    jsonData[myGrid.getColumnId(c)] = (i + 1);
                                } else {
                                    jsonData[myGrid.getColumnId(c)] = res[i][myGrid.getColumnId(c)];
                                }
                            }
                            myGrid.addRow(i + 1, "");
                            myGrid.setRowData(i + 1, jsonData);

                            if (myGrid.cellById(i + 1, myGrid.getColIndexById("instPlanYn")).getValue() != "Y") {
                                myGrid.cellById(i + 1, myGrid.getColIndexById("delChk")).setDisabled(true);
                            }
                            if (myGrid.cellById(i + 1, myGrid.getColIndexById("soStatCd")).getValue() == "9999") {
                                myGrid.cellById(i + 1, myGrid.getColIndexById("soStatNm")).setTextColor("#B100CC");
                            } else if (myGrid.cellById(i + 1, myGrid.getColIndexById("soStatCd")).getValue() == "8000") {
                                myGrid.cellById(i + 1, myGrid.getColIndexById("soStatNm")).setTextColor("#F39C12");
                            } else {
                                myGrid.cellById(i + 1, myGrid.getColIndexById("delChk")).setDisabled(false);
                            }
                        }
                    } else {
                        alert("조회 수행 결과 없음");
                    }

                    addr = [];
                    myGrid.forEachRow(function (eachRId) {
                        var arrdVal = {};
                        arrdVal["seq"] = myGrid.cellById(eachRId, myGrid.getColIndexById("seq")).getValue();
                        arrdVal["addr1"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr1")).getValue();
                        arrdVal["addr2"] = myGrid.cellById(eachRId, myGrid.getColIndexById("addr2")).getValue();
                        arrdVal["instPlanYn"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instPlanYn")).getValue();
                        arrdVal["rowId"] = myGrid.cellById(eachRId, myGrid.getColIndexById("rowId")).getValue();
                        arrdVal["paltNoxx"] = myGrid.cellById(eachRId, myGrid.getColIndexById("paltNoxx")).getValue();
                        arrdVal["mapFindAddr"] = myGrid.cellById(eachRId, myGrid.getColIndexById("mapFindAddr")).getValue();
                        arrdVal["instSeatType"] = myGrid.cellById(eachRId, myGrid.getColIndexById("instSeatType")).getValue();
                        arrdVal["prodList"] = myGrid.cellById(eachRId, myGrid.getColIndexById("prodList")).getValue();
                        arrdVal["soStatCd"] = myGrid.cellById(eachRId, myGrid.getColIndexById("soStatCd")).getValue();
                        addr.push(arrdVal);
                    });
                    onLoadMap();
                }
            });
        },

        update: function () {
            if (myGrid2.getRowsNum() == 0) {
                alert("팔렛트를 지정할 데이터를 추가해주세요.");
                return;
            }
            if (!$("#paltNoInput").val().trim()) {
                alert("팔렛트 번호가 없습니다.");
                return;
            }
            if (!$("#tblUserMId").val().trim()) {
                alert("시공기사를 검색해주세요.");
                return;
            }
            var updateData = [];
            var myGrid2Data = get_json(myGrid2);

            for (var i = 0; i < myGrid2Data.length; i++) {
                myGrid2Data[i].instDt = $("#dateFrom").val().replace(/-/g, "");
                myGrid2Data[i].tblSoPId = "";
                myGrid2Data[i].tblSoDId = "";
                myGrid2Data[i].paltNoxx = $("#paltNoInput").val().trim();
                myGrid2Data[i].seq = myGrid2Data[i].newPaltNoSeq;
                myGrid2Data[i].tblUserMId = $("#tblUserMId").val().trim();
                myGrid2Data[i].useYn = "Y";
                myGrid2Data[i].saveUser = sessionTblUserMId;
                updateData.push(myGrid2Data[i]);
            }

            $.ajax({
                url: "/erp203001Save",
                processData: false,
                contentType: "application/json",
                data: JSON.stringify(updateData),
                type: "POST",
                dataType: "json",
                success: function (res) {
                    if (res) {
                        var rtnYnCnt = 0;
                        for (var i = 0; i < res.length; i++) {
                            if (res[i].rtnYn == "Y") {
                                rtnYnCnt++;
                            }
                        }
                        $("#prePaltNoInput").val($("#paltNoInput").val());
                        alert(res.length + " 중 " + rtnYnCnt + " 건 성공");
                        fnObj.searchOpen.search();
                    } else {
                        alert("저장 수행 결과 없음");
                    }
                }
            });
        },

        searchChkVal: {
            chkVal: function (chkGubn) {
                chkValMsg = "";
                var rtnVal = true;

                if (chkGubn == "search") {
                    if (!myComboCmpyCd.getSelectedValue()) {
                        chkValMsg = chkValMsg + "회사코드를 선택해주세요\n";
                        rtnVal = false;
                    }

                    if (!$("#dateFrom").val()) {
                        chkValMsg = chkValMsg + "날짜를 입력해주세요.\n";
                    }

                    if (!myComboDcCd.getSelectedValue().trim()) {
                        chkValMsg = chkValMsg + "물류센터를 선택해주세요\n";
                        rtnVal = false;
                    }
                }
                return rtnVal;
            }
        }
    },

    keyOpen: {
        enterKey: function (enterType) {
            if (enterType == 'instErName') {
                if (window.event.keyCode == 13) {
                    $("#tblUserMId").val("");
                    fnObj.winOpen.grid2InstErNameOpen(myComboCmpyCd.getSelectedValue(),
                        $("#dateFrom").val().replace(/-/g, ""),
                        myComboDcCd.getSelectedValue(),
                        $("#instErName").val(),
                        ''
                    );
                }
            }
        },
        onKeyPressed: function (code, ctrl, shift) {
            if (code == 67 && ctrl) {
                myGrid.setCSVDelimiter("\t");
                myGrid.copyBlockToClipboard();
            }
            if (code == 86 && ctrl) {
                myGrid.pasteBlockFromClipboard();
            }
            return true;
        }
    }
};

// 엑셀 핸들러
var excelHandler = {
    getExcelFileName: function () {
        return '시공계획관리.xlsx';
    },
    getSheetName: function () {
        return '시공계획관리';
    },
    getExcelData: function () {
        return get_json_for_excel(myGrid);
    },
    getWorksheet: function () {
        return XLSX.utils.json_to_sheet(this.getExcelData());
    }
};

// Document Ready
$(document).ready(function () {
    if (pagePerRow == 0) {
        $("#page").hide();
    }

    myWins = new dhtmlXWindows();

    myGrid = new dhtmlXGridObject('grid');
    myGrid2 = new dhtmlXGridObject('grid2');

    myCalendar = new dhtmlXCalendarObject([{input: "dateFrom", button: "dateFromIcon"}]);

    var now = new Date();
    var year = now.getFullYear() + "";
    var month = (now.getMonth() + 1) + "";
    var date = now.getDate() + "";
    if (month.length == 1) {
        month = "0" + month;
    }
    if (date.length == 1) {
        date = "0" + date;
    }

    var today = year + "-" + month + "-" + date;

    $("#dateFrom").val(today);

    myCalendar.loadUserLanguage("ko");

    fnObj.comboOpen.comboCmpyCd();
    fnObj.comboOpen.comboInstCapaCtgr();
    fnObj.comboOpen.comboInstCapaType();
    fnObj.comboOpen.comboZone();
    fnObj.comboOpen.comboZoneType();
    fnObj.comboOpen.comboUseYn();
    fnObj.comboOpen.comboSeatType();
    fnObj.comboOpen.comboDcCd();

    fnObj.gridOpen.grid();
    fnObj.gridOpen.grid2();

    window.onresize = function (event) {
        fnObj.gridOpen.setWidth();
        fnObj.gridOpen.setWidth();
    };

    // 세션 변수를 사용한 물류센터 설정
    if (sessionUserGrdCd == "2000" || sessionUserGrntCd == "6000") {
        console.log("sessionUserGrdCd : " + sessionUserGrdCd);
        console.log("sessionUserGrntCd : " + sessionUserGrntCd);
        console.log("sessionDcCd : " + sessionDcCd);

        myComboDcCd.selectOption(myComboDcCd.getIndexByValue(sessionDcCd));
        myComboDcCd.disable();
    }

    $("#btnExcelDown").mouseover(function () {
        $("#btnExcelDown").val("화면에 조회된 내용을 엑셀 다운로드");
    });
    $("#btnExcelDown").mouseout(function () {
        $("#btnExcelDown").val("엑셀다운로드");
    });

    toastr.options = {
        closeButton: true,
        progressBar: true,
        showMethod: 'slideDown',
        closeDuration: 0,
        positionClass: "toast-top-right",
        timeOut: 1000,
        preventDuplicates: false
    };
});

// F2 키 누르면 조회하기
$(document).keydown(function (event) {
    if (event.keyCode == 113 || event.which == 113) {
        if ($("#btnSearch").prop("disabled") == false) {
            fnObj.searchOpen.search();
        }
    }
});
