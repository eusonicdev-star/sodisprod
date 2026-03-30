// erp105005.js - 교환/반품 용인센터 입고 관리 페이지 JavaScript

// 전역 변수
var pagePerRow = pagePerRowConfig || 50;
var myWins;
var myGrid;
var searchRes = [];
var myCalendar;

// 메인 함수 객체
fnObj = {
  winOpen: {
    commSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "commSrch";
      windowText = title + " commSrch";
      windowUrl = "/commSrch";
      windowLeft = -14400;
      windowTop = -14400;
      windowWidth = 800;
      windowHeight = 600;
      windowsModal = true;
      windowOnClose = function (windowId) {
        return true;
      };
      windowSendObject = {
        cmpyCd: cmpyCd,
        srchCtgr: srchCtgr,
        srchInput: srchInput,
        windowId: windowId,
        srchCd: $("#srchCode").val(),
        srchNm: $("#srchName").val(),
      };

      myWindowsOpen(
        windowId,
        windowText,
        windowUrl,
        windowLeft,
        windowTop,
        windowWidth,
        windowHeight,
        windowsModal,
        windowOnClose,
        windowSendObject
      );

      myWins.window(windowId).attachEvent("onClose", function (win) {
        return true;
      });
    },

    erp104001p1Open: function () {
      windowId = "erp104001p1";
      windowText = "주문상세보기 erp104001p1";
      windowUrl = "/erp104001p1";
      windowLeft = 0;
      windowTop = 0;
      windowWidth = window.innerWidth;
      windowHeight = window.innerHeight;
      windowsModal = true;
      windowOnClose = function (windowId) {
        return true;
      };
      windowSendObject = {
        cmpyCd: sessionCmpyCd,
        windowId: windowId,
        tblSoMId: myGrid
          .cellById(
            myGrid.getSelectedRowId(),
            myGrid.getColIndexById("tblSoMId")
          )
          .getValue(),
      };
      myWindowsOpen(
        windowId,
        windowText,
        windowUrl,
        windowLeft,
        windowTop,
        windowWidth,
        windowHeight,
        windowsModal,
        windowOnClose,
        windowSendObject
      );
      myWins.window(windowId).attachEvent("onClose", function (win) {
        return true;
      });
    },

    rtnObject: function (windowId, rtnObj, srchInput) {
      if (rtnObj == null) {
        myWins.window(windowId).setPosition(0, 0);
        return;
      }
      if (windowId == "commSrch") {
        if (srchInput == "text") {
          $("#srchCode").val(rtnObj[0].commCd);
          $("#srchName").val(rtnObj[0].commNm);
        }
      }
    },
  },
  comboOpen: {
    comboCmpyCd: function () {
      myComboCmpyCd = new dhtmlXCombo("cmpyCd");
      comboList(myComboCmpyCd, "A", "COMM", "CMPY_CD", "", "Y", "");
      myComboCmpyCd.selectOption(myComboCmpyCd.getIndexByValue(sessionCmpyCd));
      myComboCmpyCd.setFontSize("12px", "12px");
      myComboCmpyCd.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
      });
      myComboCmpyCd.disable();
    },

    comboDtType: function () {
      myComboDtType = new dhtmlXCombo("dtType");
      // erp105005 전용 공통코드 사용 (SO_SRCH_DT_TYPE_105005)
      // 이 코드에는 기존 SO_SRCH_DT_TYPE 옵션 + 반출일 옵션이 포함됨
      comboList(
        myComboDtType,
        "A",
        "COMM",
        "SO_SRCH_DT_TYPE_105005",
        "",
        "Y",
        ""
      );
      // 배송확정일(코드값 3)을 기본값으로 설정
      setTimeout(function () {
        var index = myComboDtType.getIndexByValue("3");
        if (index >= 0) myComboDtType.selectOption(index);
      }, 100);
      myComboDtType.setFontSize("12px", "12px");
      myComboDtType.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
      });
    },

    comboDcCd: function () {
      myComboDcCd = new dhtmlXComboFromSelect("dcCd");
      comboList(myComboDcCd, "A", "DC_CD", "", "", "Y", "req");
      myComboDcCd.readonly(true);
      myComboDcCd.selectOption(0);
      myComboDcCd.setFontSize("12px", "12px");
      myComboDcCd.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
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
      myComboDcCd.attachEvent("onCheck", function (value, text) {
        if (!value) {
          if (myComboDcCd.isChecked(0)) {
            var c = 0;
            for (var i = 1; i < myComboDcCd.getOptionsCount(); i++) {
              myComboDcCd.setChecked(i, true);
              c++;
            }
            myComboDcCd.updateOption("", "", c + "개 선택", "");
            myComboDcCd.openSelect();
          }
          if (!myComboDcCd.isChecked(0)) {
            var c = 0;
            for (var i = 1; i < myComboDcCd.getOptionsCount(); i++) {
              myComboDcCd.setChecked(i, false);
            }
            myComboDcCd.updateOption("", "", c + "개 선택", "");
            myComboDcCd.openSelect();
            return;
          }
        } else {
          var cnt = 0;
          for (var i = 1; i < myComboDcCd.getOptionsCount(); i++) {
            if (myComboDcCd.isChecked(i)) {
              cnt++;
            }
          }
          if (myComboDcCd.getOptionsCount() - 1 == cnt) {
            myComboDcCd.setChecked(0, true);
          } else {
            myComboDcCd.setChecked(0, false);
          }
          myComboDcCd.updateOption("", "", cnt + "개 선택", "");
          myComboDcCd.openSelect();
          return;
        }
      });
    },

    comboSoType: function () {
      myComboSoType = new dhtmlXComboFromSelect("soType");
      comboList(myComboSoType, "A", "COMM", "SO_ORDR_TYPE", "", "Y", "req");
      myComboSoType.selectOption(0);
      myComboSoType.readonly(true);
      myComboSoType.setFontSize("12px", "12px");
      myComboSoType.attachEvent("onChange", function (value, text) {
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
      myComboSoType.attachEvent("onCheck", function (value, text) {
        if (!value) {
          if (myComboSoType.isChecked(0)) {
            var c = 0;
            for (var i = 1; i < myComboSoType.getOptionsCount(); i++) {
              myComboSoType.setChecked(i, true);
              c++;
            }
            myComboSoType.updateOption("", "", c + "개 선택", "");
            myComboSoType.openSelect();
          }
          if (!myComboSoType.isChecked(0)) {
            var c = 0;
            for (var i = 1; i < myComboSoType.getOptionsCount(); i++) {
              myComboSoType.setChecked(i, false);
            }
            myComboSoType.updateOption("", "", c + "개 선택", "");
            myComboSoType.openSelect();
            return;
          }
        } else {
          var cnt = 0;
          for (var i = 1; i < myComboSoType.getOptionsCount(); i++) {
            if (myComboSoType.isChecked(i)) {
              cnt++;
            }
          }
          if (myComboSoType.getOptionsCount() - 1 == cnt) {
            myComboSoType.setChecked(0, true);
          } else {
            myComboSoType.setChecked(0, false);
          }
          myComboSoType.updateOption("", "", cnt + "개 선택", "");
          myComboSoType.openSelect();
          return;
        }
      });
    },

    comboInboundStatus: function () {
      myComboInboundStatus = new dhtmlXCombo("inboundStatus");
      myComboInboundStatus.addOption("", "전체");
      myComboInboundStatus.addOption("Y", "용인센터입고완료");
      myComboInboundStatus.addOption("N", "용인센터입고대기");
      myComboInboundStatus.selectOption(0);
      myComboInboundStatus.setFontSize("12px", "12px");
      myComboInboundStatus.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
      });
    },

    comboOutboundStatus: function () {
      myComboOutboundStatus = new dhtmlXCombo("outboundStatus");
      myComboOutboundStatus.addOption("", "전체");
      myComboOutboundStatus.addOption("Y", "반출완료");
      myComboOutboundStatus.addOption("N", "반출대기");
      myComboOutboundStatus.selectOption(0);
      myComboOutboundStatus.setFontSize("12px", "12px");
      myComboOutboundStatus.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
      });
    },

    comboOutboundTarget: function () {
      myComboOutboundTarget = new dhtmlXCombo("outboundTarget");
      myComboOutboundTarget.addOption("", "전체");
      myComboOutboundTarget.addOption("Y", "반출대상");
      myComboOutboundTarget.addOption("N", "반출대상아님");
      myComboOutboundTarget.addOption("P", "판별중");
      myComboOutboundTarget.selectOption(0);
      myComboOutboundTarget.setFontSize("12px", "12px");
      myComboOutboundTarget.attachEvent("onChange", function (value, text) {
        myGrid.clearAll(false);
      });
    },
  },
  gridOpen: {
    grid: function () {
      myGrid.setImagePath("https://cdn.dhtmlx.com/edge/skins/web/imgs/");

      myGrid.setHeader(
        "#master_checkbox,CUD,ID,ID2,반출대상,입고상태,용인센터입고일,입고비고,반출상태,반출일,반출비고,기타비고," +
          "물류센터명,AL오더,고객사주문번호,주문유형,주문상태,배송확정일,시공기사,화주명,수취인," +
          "상품코드,상품명,반품수량,입고처리자,반출처리자,우편번호,주소,수취인전화,배송요청사항,고객사요청사항,저장일," +
          "물류센터코드,화주코드"
      );

      myGrid.setColumnIds(
        "chk,cud,tblReturnInboundId,tblSoMId,outboundTargetYn,inboundYn,inboundDt,inboundRemark," +
          "outboundYn,outboundDt,outboundRemark,etcRemark,dcNm,soNo,refSoNo,soTypeNm,soStatCdNm,dlvyCnfmDt,instEr," +
          "agntNm,acptEr,prodCd,prodNm,returnQty,inboundUser,outboundUser,postCd,addr1,acptTel1,dlvyRqstMsg,custSpclTxt,saveTime," +
          "dcCd,agntCd"
      );

      myGrid.setInitWidths(
        "40," + // master_checkbox
          "40," + // CUD
          "80," + // ID (tblReturnInboundId)
          "80," + // ID2 (tblSoMId)
          "80," + // 반출대상
          "80," + // 입고상태
          "100," + // 용인센터입고일
          "100," + // 입고비고
          "80," + // 반출상태
          "100," + // 반출일
          "100," + // 반출비고
          "150," + // 기타비고
          "120," + // 물류센터명
          "100," + // AL오더
          "120," + // 고객사주문번호
          "100," + // 주문유형
          "100," + // 주문상태
          "100," + // 배송확정일
          "80," + // 시공기사
          "120," + // 화주명
          "80," + // 수취인
          "60," + // 상품코드
          "200," + // 상품명
          "80," + // 반품수량
          "100," + // 입고처리자
          "100," + // 반출처리자
          "80," + // 우편번호
          "80," + // 주소
          "80," + // 수취인전화
          "200," + // 배송요청사항
          "200," + // 고객사요청사항
          "200," + // 저장일
          "80," + // 물류센터코드
          "120" // 화주코드
      );

      myGrid.setColAlign(
        "center,center,center,center,center,center,center,left," +
          "center,center,left,left,center,center,center,center,center,center,center," +
          "center,center,left,right,center,center,center,center,left,center,left,left," +
          "center,center,center"
      );

      myGrid.setColTypes(
        "ch,ro,ro,ro,ro,ro,ro,ro," +
          "ro,ro,ro,ed,ro,ro,ro,ro,ro,ro,ro," +
          "ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro," +
          "ro,ro,ro"
      );

      myGrid.setColSorting(
        ",,,,,str,str,str," +
          "str,str,str,str,str,str,str,str,str,str,str," +
          "str,str,str,str,str,str,int,str,str,str,str,str,str,str," +
          "str,str"
      );

      myGrid.setColumnHidden(myGrid.getColIndexById("chk"), false);
      myGrid.setColumnHidden(myGrid.getColIndexById("cud"), true);
      myGrid.setColumnHidden(
        myGrid.getColIndexById("tblReturnInboundId"),
        true
      );
      myGrid.setColumnHidden(myGrid.getColIndexById("tblSoMId"), true);
      myGrid.setColumnHidden(myGrid.getColIndexById("agntCd"), true);
      myGrid.setColumnHidden(myGrid.getColIndexById("dcCd"), true);

      // 텍스트 필터 추가
      myGrid.attachHeader(
        "#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter"
      );

      myGrid.init();
      myGrid.enableSmartRendering(true);
      myGrid.enablePreRendering(10);

      // 뱃지 렌더링 헬퍼 함수
      function getBadgeHtml(value, type) {
        if (type === "inbound") {
          if (value === "Y") {
            return '<span class="badge badge-success">완료</span>';
          } else {
            return '<span class="badge badge-warning">대기</span>';
          }
        } else if (type === "outbound") {
          if (value === "Y") {
            return '<span class="badge badge-success">완료</span>';
          } else {
            return '<span class="badge badge-warning">대기</span>';
          }
        } else if (type === "target") {
          if (value === "Y") {
            return '<span class="badge badge-primary">대상</span>';
          } else {
            return '<span class="badge badge-secondary">제외</span>';
          }
        }
        return value || "";
      }

      fnObj.gridOpen.setWidth();
      myGrid.addRow(1, "");
      myGrid.deleteRow(1);
      myGrid.enableEditEvents(true, false, false);
      myGrid.enableBlockSelection(true);
      myGrid.attachEvent("onKeyPress", fnObj.keyOpen.onKeyPressed);
      myGrid.attachEvent("onRowDblClicked", function (rowId, colIndex) {
        fnObj.winOpen.erp104001p1Open();
      });

      // 기타비고 셀 편집 완료 시 자동 저장
      myGrid.attachEvent("onEditCell", function (stage, rowId, colIndex, newValue, oldValue) {
        // stage: 0=편집 시작, 1=편집 중, 2=편집 완료
        if (stage == 2) {
          var colId = myGrid.getColumnId(colIndex);
          if (colId === "etcRemark" && newValue !== oldValue) {
            var tblReturnInboundId = myGrid
              .cellById(rowId, myGrid.getColIndexById("tblReturnInboundId"))
              .getValue();

            if (tblReturnInboundId) {
              saveEtcRemark(tblReturnInboundId, newValue);
            }
          }
        }
        return true;
      });
    },
    setWidth: function () {
      c_width = top.myLayout.cells("c").getWidth();
      searchDiv_height = $("#searchDiv").height();
      searchDiv2nd_height = $("#searchDiv2nd").height();
      page_height = $("#page").height();
      xhdr_height = $(".xhdr").height();

      myGrid.enableAutoWidth(true, c_width - 25, 100);
      myGrid.enableAutoHeight(
        true,
        window.innerHeight -
          searchDiv_height -
          searchDiv2nd_height -
          page_height -
          xhdr_height -
          30
      );

      $("#searchDiv").width(c_width - 25);
      $("#searchDiv2nd").width(c_width - 25);
    },
  },
  searchOpen: {
    sTime: function () {
      toastr.success("잠시 기다려주세요", "조회중");
      setTimeout(function () {
        fnObj.searchOpen.search();
      }, 300);
    },

    search: function () {
      var z = new Date().valueOf();
      myGrid.clearAll(false);
      searchRes = null;
      searchRes = [];

      if (!fnObj.searchOpen.searchChkVal.chkVal("search")) {
        alert(chkValMsg);
        return false;
      }

      var srchData = new FormData();

      // 주문번호가 입력된 경우 주문번호만으로 조회
      var soListValue = $("#soList").val().trim();
      if (soListValue && soListValue !== "") {
        srchData.append("cmpyCd", myComboCmpyCd.getSelectedValue());
        srchData.append("soList", soListValue.replace(/\n/g, ","));

        // 화주사 권한인 경우 본인 화주사로만 조회 강제
        var agntListValue = $("#srchCode").val();
        if (
          sessionUserGrntCd == "4100" ||
          sessionUserGrntCd == "4200" ||
          sessionUserGrntCd == "7788"
        ) {
          agntListValue = sessionAgntCd;
        }
        srchData.append("agntList", agntListValue);
      } else {
        // 기존 조건들로 조회
        srchData.append("cmpyCd", myComboCmpyCd.getSelectedValue());
        srchData.append("dtType", myComboDtType.getSelectedValue());
        srchData.append("fromDt", $("#dateFrom").val().replace(/-/g, ""));
        srchData.append("toDt", $("#dateTo").val().replace(/-/g, ""));

        // 화주사 권한인 경우 본인 화주사로만 조회 강제
        var agntListValue = $("#srchCode").val();
        if (
          sessionUserGrntCd == "4100" ||
          sessionUserGrntCd == "4200" ||
          sessionUserGrntCd == "7788"
        ) {
          agntListValue = sessionAgntCd;
        }
        srchData.append("agntList", agntListValue);
        srchData.append("dcList", myComboDcCd.getChecked());
        srchData.append("soList", $("#soList").val().replace(/\n/g, ","));
        srchData.append("soTypeList", myComboSoType.getChecked());
        srchData.append("acptNm", $("#rcptNm").val());
        srchData.append("acptTel", $("#rcptTel").val());
        srchData.append("prodList", $("#prodList").val().replace(/\n/g, ","));
        srchData.append("prodNmSrch", $("#prodNm").val());
        srchData.append("srchAddr", $("#srchAddr").val());
        srchData.append(
          "inboundStatus",
          myComboInboundStatus.getSelectedValue()
        );
        srchData.append(
          "outboundStatus",
          myComboOutboundStatus.getSelectedValue()
        );
        srchData.append(
          "outboundTarget",
          myComboOutboundTarget.getSelectedValue()
        );
      }

      $.ajax({
        url: "/erp105005List",
        processData: false,
        contentType: false,
        data: srchData,
        type: "POST",
        datatype: "json",
        async: false,
        success: function (res) {
          if (res && res.length > 0) {
            for (var k = 0; k < res.length; k++) {
              searchRes.push(res[k]);
            }
          }
        },
      });

      searchPaging(1);
      toastr.remove();
    },

    delSrchCode: function () {
      $("#srchCode").val("");
      $("#srchName").val("");
    },

    searchChkVal: {
      chkVal: function (chkGubn) {
        chkValMsg = "";
        var rtnVal = true;

        if (chkGubn == "search") {
          // 주문번호가 입력된 경우 기존 조건 무시
          var soListValue = $("#soList").val().trim();
          if (soListValue && soListValue !== "") {
            return true;
          }

          if (!myComboCmpyCd.getSelectedValue()) {
            chkValMsg = chkValMsg + "회사코드가 없습니다\n";
            rtnVal = false;
          }
          if (!myComboDtType.getSelectedValue()) {
            chkValMsg = chkValMsg + "달력유형을 선택해주세요\n";
            rtnVal = false;
          }
          if (!$("#dateFrom").val()) {
            chkValMsg = chkValMsg + "검색시작일을 입력해주세요\n";
            rtnVal = false;
          }
          if (!$("#dateTo").val()) {
            chkValMsg = chkValMsg + "검색종료일을 입력해주세요\n";
            rtnVal = false;
          }
          if ($("#dateFrom").val() && $("#dateTo").val()) {
            var date1 = new Date($("#dateFrom").val());
            var date2 = new Date($("#dateTo").val());
            if (date1 > date2) {
              chkValMsg = chkValMsg + "검색시작일이 검색종료일보다 작습니다.\n";
              rtnVal = false;
            }
          }

          // 배송확정일 기준 2025년 10월 2일 이전 날짜 검색 불가
          var minDate = new Date("2025-10-02");
          if ($("#dateFrom").val()) {
            var fromDate = new Date($("#dateFrom").val());
            if (fromDate < minDate) {
              chkValMsg =
                chkValMsg +
                "배송확정일 기준 2025년 10월 2일 이전 날짜는 검색할 수 없습니다.\n";
              rtnVal = false;
            }
          }
          if ($("#dateTo").val()) {
            var toDate = new Date($("#dateTo").val());
            if (toDate < minDate) {
              chkValMsg =
                chkValMsg +
                "배송확정일 기준 2025년 10월 2일 이전 날짜는 검색할 수 없습니다.\n";
              rtnVal = false;
            }
          }
        }
        return rtnVal;
      },
    },
  },
  keyOpen: {
    enterKey: function (enterType) {
      if (window.event.keyCode == 13) {
        if (enterType == "srchName") {
          $("#srchName").blur();
          $("#srchCode").blur();
          $("#srchCode").val("");
          fnObj.winOpen.commSrch("화주", "A", "AGENCY", "text");
        }
      }
    },
    onKeyPressed: function (code, ctrl) {
      if (code == 67 && ctrl) {
        myGrid.setCSVDelimiter("\t");
        myGrid.copyBlockToClipboard();
      }
      if (code == 86 && ctrl) {
        myGrid.pasteBlockFromClipboard();
      }
      return true;
    },
  },
};

// 페이징 처리
function searchPaging(page) {
  if ($("#rowPerPage").val()) {
    pagePerRow = parseInt($("#rowPerPage").val());
  }

  var pageRange = 10;
  var totalPageCnt =
    searchRes.length % pagePerRow == 0
      ? parseInt(searchRes.length / pagePerRow)
      : parseInt(searchRes.length / pagePerRow) + 1;

  var a = "<div class='pagination-container'>";

  var start = page - 1;
  var end = page - 1 + pageRange;

  if (start % pageRange != 0) {
    start = parseInt(start / pageRange) * pageRange;
  }

  if (end % pageRange != 0 || page == 1) {
    end = parseInt(end / pageRange) * pageRange;
  }

  if (start >= pageRange) {
    a +=
      "<input type='button' onclick='searchPaging(" +
      1 +
      ");' value='<<' class='pagination-button'>";
    a +=
      "<input type='button' onclick='searchPaging(" +
      (start - 1) +
      ");' value='<' class='pagination-button'>";
  }

  var r = 0;
  for (r = start; r < end; r++) {
    if (r < totalPageCnt) {
      if (r + 1 == page) {
        a +=
          "<input type='button' onclick='searchPaging(" +
          (r + 1) +
          ");' value=" +
          (r + 1) +
          " class='pagination-button active'>";
      } else {
        a +=
          "<input type='button' onclick='searchPaging(" +
          (r + 1) +
          ");' value=" +
          (r + 1) +
          " class='pagination-button'>";
      }
    }
  }

  if (end * pagePerRow < searchRes.length) {
    a +=
      "<input type='button' onclick='searchPaging(" +
      (end + 1) +
      ");' value='>' class='pagination-button'>";
    a +=
      "<input type='button' onclick='searchPaging(" +
      totalPageCnt +
      ");' value='>>' class='pagination-button'>";
  }

  a += "<div class='pagination-info'>";
  a +=
    "<input type='number' maxlength=4 id='rowPerPage' value=" +
    pagePerRow +
    ">";
  a +=
    "<input type='button' value='적용' onclick='searchPaging(" +
    1 +
    ");' class='action-button'>";
  a +=
    "&nbsp;&nbsp;<i class='fa fa-calculator fa-lg' aria-hidden='true'></i>&nbsp;" +
    searchRes.length;
  a += "</div></div>";
  $("#page").html(a);

  var is = (page - 1) * pagePerRow;
  var ie = (page - 1) * pagePerRow + pagePerRow;

  if (searchRes.length < ie) {
    ie = searchRes.length;
  }

  myGrid.clearAll(false);
  var columns = myGrid.getColumnCount();
  var x = new Date().valueOf();
  for (var i = is; i < ie; i++) {
    if (!searchRes[i]) continue;

    var rowId = i + 1;
    myGrid.addRow(rowId, "");

    // 각 컬럼별로 직접 값을 설정
    for (var c = 0; c < columns; c++) {
      var colId = myGrid.getColumnId(c);
      var colIndex = myGrid.getColIndexById(colId);
      var rawValue =
        searchRes[i][colId] !== undefined && searchRes[i][colId] !== null
          ? searchRes[i][colId]
          : "";

      // 뱃지가 필요한 컬럼은 HTML로 변환
      var value = rawValue;
      if (colId === "inboundYn") {
        if (rawValue === "Y") {
          value = '<span class="badge badge-success">완료</span>';
        } else {
          value = '<span class="badge badge-warning">대기</span>';
        }
      } else if (colId === "outboundYn") {
        if (rawValue === "Y") {
          value = '<span class="badge badge-success">완료</span>';
        } else {
          value = '<span class="badge badge-warning">대기</span>';
        }
      } else if (colId === "outboundTargetYn") {
        if (rawValue === "Y") {
          value = '<span class="badge badge-primary">대상</span>';
        } else {
          value = '<span class="badge badge-secondary">제외</span>';
        }
      }

      try {
        var cell = myGrid.cellById(rowId, colIndex);
        if (cell) {
          cell.setValue(value);
        }
      } catch (e) {
        // Error setting cell value
      }
    }

    // 용인센터 입고 상태에 따른 행 색상 설정 (원본 값 사용)
    var inboundYn = searchRes[i]["inboundYn"] || "";
    var outboundYn = searchRes[i]["outboundYn"] || "";
    var outboundTargetYn = searchRes[i]["outboundTargetYn"] || "";

    // 반출 대상이 아닌 경우 (N 또는 null) - 회색, 체크박스 활성화
    if (
      outboundTargetYn == "N" ||
      outboundTargetYn == null ||
      outboundTargetYn == ""
    ) {
      myGrid.setRowColor(rowId, "#D3D3D3");
      myGrid.cellById(rowId, myGrid.getColIndexById("chk")).setDisabled(false);
    }
    // 판별중인 경우 (P) - 연한 파란색, 체크박스 활성화
    else if (outboundTargetYn == "P") {
      myGrid.setRowColor(rowId, "#E6F3FF");
      myGrid.cellById(rowId, myGrid.getColIndexById("chk")).setDisabled(false);
    }
    // 반출 대상이면서 입고와 반출 모두 완료 - 연두색
    else if (inboundYn == "Y" && outboundYn == "Y") {
      myGrid.setRowColor(rowId, "#E8F5E8");
    }
    // 반출 대상이면서 입고만 완료 - 연한 노랑색
    else if (inboundYn == "Y" && outboundYn == "N") {
      myGrid.setRowColor(rowId, "#FFF2CC");
    }
    // 반출 대상이면서 입고도 안 된 것 - 연한 붉은색
    else if (inboundYn == "N") {
      myGrid.setRowColor(rowId, "#FFE6E6");
    }
  }
}

// 엑셀 다운로드
function excelDn() {
  if (!myGrid.getRowsNum()) {
    alert("엑셀다운로드 대상이 없습니다.\n조회해주세요");
    return;
  }

  // 엑셀 다운로드 데이터 준비
  var excelData = [];

  // 제목행 추가
  excelData.push(["교환/반품 용인센터 입고 관리"]);
  excelData.push([""]); // 빈 행

  // 요청된 순서로 헤더 설정
  var headers = [
    "반출대상",
    "입고상태",
    "용인센터입고일",
    "입고비고",
    "반출상태",
    "반출일",
    "반출비고",
    "기타비고",
    "물류센터명",
    "AL오더",
    "고객사주문번호",
    "주문유형",
    "주문상태",
    "배송확정일",
    "시공기사",
    "화주명",
    "수취인",
    "상품코드",
    "상품명",
    "반품수량",
    "입고처리자",
    "반출처리자",
    "우편번호",
    "주소",
    "수취인전화",
    "배송요청사항",
    "고객사요청사항",
    "저장일",
    "입고여부체크",
    "비고",
  ];
  excelData.push(headers);

  // 데이터 행 추가 (요청된 순서로) - searchRes의 모든 데이터 사용
  for (var i = 0; i < searchRes.length; i++) {
    var rowData = searchRes[i];
    if (!rowData) continue;

    // 원본 데이터에서 직접 가져오기 (HTML 뱃지가 아닌 원본 값 사용)
    var outboundTargetValue = rowData["outboundTargetYn"] || "";
    if (outboundTargetValue === "Y") outboundTargetValue = "대상";
    else if (outboundTargetValue === "N") outboundTargetValue = "제외";
    else if (outboundTargetValue === "P") outboundTargetValue = "판별중";

    var inboundValue = rowData["inboundYn"] || "";
    if (inboundValue === "Y") inboundValue = "완료";
    else if (inboundValue === "N") inboundValue = "대기";

    var outboundValue = rowData["outboundYn"] || "";
    if (outboundValue === "Y") outboundValue = "완료";
    else if (outboundValue === "N") outboundValue = "대기";

    var excelRow = [
      outboundTargetValue,
      inboundValue,
      rowData["inboundDt"] || "",
      rowData["inboundRemark"] || "",
      outboundValue,
      rowData["outboundDt"] || "",
      rowData["outboundRemark"] || "",
      rowData["etcRemark"] || "",
      rowData["dcNm"] || "",
      rowData["soNo"] || "",
      rowData["refSoNo"] || "",
      rowData["soTypeNm"] || "",
      rowData["soStatCdNm"] || "",
      rowData["dlvyCnfmDt"] || "",
      rowData["instEr"] || "",
      rowData["agntNm"] || "",
      rowData["acptEr"] || "",
      rowData["prodCd"] || "",
      rowData["prodNm"] || "",
      rowData["returnQty"] || "",
      rowData["inboundUser"] || "",
      rowData["outboundUser"] || "",
      rowData["postCd"] || "",
      rowData["addr1"] || "",
      rowData["acptTel1"] || "",
      rowData["dlvyRqstMsg"] || "",
      rowData["custSpclTxt"] || "",
      rowData["saveTime"] || "",
      "", // 입고여부체크
      "", // 비고
    ];
    excelData.push(excelRow);
  }

  // 엑셀 파일 생성 및 다운로드
  var wb = XLSX.utils.book_new();
  var ws = XLSX.utils.aoa_to_sheet(excelData);

  // 컬럼 너비 설정
  ws["!cols"] = [
    { wch: 10 }, // 반출대상
    { wch: 10 }, // 입고상태
    { wch: 12 }, // 용인센터입고일
    { wch: 20 }, // 입고비고
    { wch: 10 }, // 반출상태
    { wch: 12 }, // 반출일
    { wch: 20 }, // 반출비고
    { wch: 20 }, // 기타비고
    { wch: 15 }, // 물류센터명
    { wch: 15 }, // AL오더
    { wch: 15 }, // 고객사주문번호
    { wch: 12 }, // 주문유형
    { wch: 12 }, // 주문상태
    { wch: 12 }, // 배송확정일
    { wch: 15 }, // 시공기사
    { wch: 15 }, // 화주명
    { wch: 15 }, // 수취인
    { wch: 15 }, // 상품코드
    { wch: 25 }, // 상품명
    { wch: 10 }, // 반품수량
    { wch: 15 }, // 입고처리자
    { wch: 15 }, // 반출처리자
    { wch: 12 }, // 우편번호
    { wch: 20 }, // 주소
    { wch: 15 }, // 수취인전화
    { wch: 30 }, // 배송요청사항
    { wch: 30 }, // 고객사요청사항
    { wch: 20 }, // 저장일
    { wch: 12 }, // 입고여부체크
    { wch: 20 }, // 비고
  ];

  XLSX.utils.book_append_sheet(wb, ws, "교환반품용인센터입고관리");

  var fileName =
    "교환반품용인센터입고관리_" +
    new Date().toISOString().split("T")[0] +
    ".xlsx";
  XLSX.writeFile(wb, fileName);
}

// 용인센터 입고 완료 처리 (모달 열기)
function inboundComplete() {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      if (tblReturnInboundId) {
        selectedRows.push(tblReturnInboundId);
      }
    }
  }

  if (selectedRows.length == 0) {
    alert("처리할 항목을 선택해주세요.");
    return;
  }

  // 모달 열기
  openInboundModal();
}

// 반출 완료 처리 (모달 열기)
function outboundComplete() {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];
  var nonTargetRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var outboundTargetYn = myGrid
        .cellById(i, myGrid.getColIndexById("outboundTargetYn"))
        .getValue();
      var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();

      if (tblReturnInboundId) {
        if (outboundTargetYn == "N") {
          nonTargetRows.push(soNo);
        } else if (outboundTargetYn == "P") {
          // 판별중인 경우는 처리하지 않음
        } else {
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  if (selectedRows.length == 0 && nonTargetRows.length == 0) {
    alert("처리할 항목을 선택해주세요.");
    return;
  }

  // 반출 대상이 아닌 상품이 포함된 경우 경고
  if (nonTargetRows.length > 0) {
    var message =
      "다음 주문번호는 반출 대상 상품이 아닙니다:\n" +
      nonTargetRows.join(", ") +
      "\n\n";
    if (selectedRows.length > 0) {
      message +=
        "반출 대상 상품 " + selectedRows.length + "개만 처리하시겠습니까?";
    } else {
      message += "반출 대상 상품이 없습니다.";
      alert(message);
      return;
    }

    if (!confirm(message)) {
      return;
    }
  }

  // 모달 열기
  openOutboundModal();
}

// 입고일 선택 모달 열기
function openInboundModal() {
  // 오늘 날짜를 기본값으로 설정
  var today = new Date();
  var year = today.getFullYear();
  var month = String(today.getMonth() + 1).padStart(2, "0");
  var day = String(today.getDate()).padStart(2, "0");
  var todayStr = year + "-" + month + "-" + day;

  $("#inboundDate").val(todayStr);
  $("#inboundModal").show();
}

// 입고일 선택 모달 닫기
function closeInboundModal() {
  $("#inboundModal").hide();
  $("#inboundRemark").val(""); // 비고 필드 초기화
}

// 입고일 선택 후 실제 처리 수행
function confirmInboundComplete() {
  var selectedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      if (tblReturnInboundId) {
        selectedRows.push(tblReturnInboundId);
      }
    }
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("processType", "INBOUND");
  processData.append("processUser", sessionUserName);
  processData.append("processDt", $("#inboundDate").val().replace(/-/g, ""));
  processData.append("inboundRemark", $("#inboundRemark").val());

  $.ajax({
    url: "/erp105005BatchInboundComplete",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("용인센터 입고 완료 처리되었습니다.");
        closeInboundModal();
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 반출일 선택 모달 열기
function openOutboundModal() {
  // 오늘 날짜를 기본값으로 설정
  var today = new Date();
  var year = today.getFullYear();
  var month = String(today.getMonth() + 1).padStart(2, "0");
  var day = String(today.getDate()).padStart(2, "0");
  var todayStr = year + "-" + month + "-" + day;

  $("#outboundDate").val(todayStr);
  $("#outboundModal").show();
}

// 반출일 선택 모달 닫기
function closeOutboundModal() {
  $("#outboundModal").hide();
  $("#outboundRemark").val(""); // 비고 필드 초기화
}

// 반출일 선택 후 실제 처리 수행
function confirmOutboundComplete() {
  var selectedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var outboundTargetYn = myGrid
        .cellById(i, myGrid.getColIndexById("outboundTargetYn"))
        .getValue();

      if (tblReturnInboundId) {
        if (outboundTargetYn != "N" && outboundTargetYn != "P") {
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("processType", "OUTBOUND");
  processData.append("processUser", sessionUserName);
  processData.append("processDt", $("#outboundDate").val().replace(/-/g, ""));
  processData.append("outboundRemark", $("#outboundRemark").val());

  $.ajax({
    url: "/erp105005BatchOutboundComplete",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("반출 완료 처리되었습니다.");
        closeOutboundModal();
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 반출 대상 여부를 특정 상태로 변경
function setOutboundTarget(targetStatus) {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      if (tblReturnInboundId) {
        selectedRows.push(tblReturnInboundId);
      }
    }
  }

  if (selectedRows.length == 0) {
    alert("처리할 항목을 선택해주세요.");
    return;
  }

  // 반출대상아님('N')으로 변경할 때는 모달 열기
  if (targetStatus == "N") {
    openOutboundTargetNonModal();
    return;
  }

  // 상태별 메시지 설정
  var statusText = "";
  switch (targetStatus) {
    case "Y":
      statusText = "반출대상";
      break;
    case "P":
      statusText = "판별중";
      break;
  }

  if (
    !confirm(
      "선택한 " +
        selectedRows.length +
        "개 항목을 " +
        statusText +
        "으로 변경하시겠습니까?"
    )
  ) {
    return;
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("outboundTargetYn", targetStatus);
  processData.append("processUser", sessionUserName);

  $.ajax({
    url: "/erp105005ToggleOutboundTarget",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("선택한 항목이 " + statusText + "으로 변경되었습니다.");
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 반출대상아님으로 변경 모달 열기
function openOutboundTargetNonModal() {
  $("#outboundTargetNonModal").show();
  $("#outboundTargetNonRemark").val(""); // 비고 필드 초기화
}

// 반출대상아님으로 변경 모달 닫기
function closeOutboundTargetNonModal() {
  $("#outboundTargetNonModal").hide();
  $("#outboundTargetNonRemark").val(""); // 비고 필드 초기화
}

// 반출대상아님으로 변경 확인 처리
function confirmOutboundTargetNon() {
  var selectedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      if (tblReturnInboundId) {
        selectedRows.push(tblReturnInboundId);
      }
    }
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("outboundTargetYn", "N");
  processData.append("processUser", sessionUserName);
  processData.append("outboundRemark", $("#outboundTargetNonRemark").val());

  $.ajax({
    url: "/erp105005ToggleOutboundTarget",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("선택한 항목이 반출대상아님으로 변경되었습니다.");
        closeOutboundTargetNonModal();
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 입고완료 되돌리기 (모달 열기)
function inboundRevert() {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];
  var completedRows = [];

  // 체크된 행들 찾기 (searchRes에서 원본 데이터 사용)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData && tblReturnInboundId) {
        var inboundYn = rowData["inboundYn"] || "";
        var soNo = rowData["soNo"] || "";

        if (inboundYn === "Y") {
          completedRows.push(soNo);
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  if (selectedRows.length == 0) {
    alert("입고완료된 항목을 선택해주세요.");
    return;
  }

  // 입고완료되지 않은 항목이 포함된 경우 경고
  var nonCompletedRows = [];
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData) {
        var inboundYn = rowData["inboundYn"] || "";
        var soNo = rowData["soNo"] || "";

        if (inboundYn !== "Y") {
          nonCompletedRows.push(soNo);
        }
      }
    }
  }

  if (nonCompletedRows.length > 0) {
    var message =
      "다음 주문번호는 입고완료되지 않은 항목입니다:\n" +
      nonCompletedRows.join(", ") +
      "\n\n";
    if (completedRows.length > 0) {
      message +=
        "입고완료된 항목 " + completedRows.length + "개만 되돌리시겠습니까?";
    } else {
      message += "되돌릴 수 있는 항목이 없습니다.";
      alert(message);
      return;
    }

    if (!confirm(message)) {
      return;
    }
  }

  // 모달 열기
  openInboundRevertModal();
}

// 반출완료 되돌리기 (모달 열기)
function outboundRevert() {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];
  var completedRows = [];

  // 체크된 행들 찾기 (searchRes에서 원본 데이터 사용)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData && tblReturnInboundId) {
        var outboundYn = rowData["outboundYn"] || "";
        var soNo = rowData["soNo"] || "";

        if (outboundYn === "Y") {
          completedRows.push(soNo);
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  if (selectedRows.length == 0) {
    alert("반출완료된 항목을 선택해주세요.");
    return;
  }

  // 반출완료되지 않은 항목이 포함된 경우 경고
  var nonCompletedRows = [];
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData) {
        var outboundYn = rowData["outboundYn"] || "";
        var soNo = rowData["soNo"] || "";

        if (outboundYn !== "Y") {
          nonCompletedRows.push(soNo);
        }
      }
    }
  }

  if (nonCompletedRows.length > 0) {
    var message =
      "다음 주문번호는 반출완료되지 않은 항목입니다:\n" +
      nonCompletedRows.join(", ") +
      "\n\n";
    if (completedRows.length > 0) {
      message +=
        "반출완료된 항목 " + completedRows.length + "개만 되돌리시겠습니까?";
    } else {
      message += "되돌릴 수 있는 항목이 없습니다.";
      alert(message);
      return;
    }

    if (!confirm(message)) {
      return;
    }
  }

  // 모달 열기
  openOutboundRevertModal();
}

// 입고완료 되돌리기 모달 열기
function openInboundRevertModal() {
  $("#inboundRevertRemark").val("");
  $("#inboundRevertModal").show();
}

// 입고완료 되돌리기 모달 닫기
function closeInboundRevertModal() {
  $("#inboundRevertModal").hide();
  $("#inboundRevertRemark").val("");
}

// 입고완료 되돌리기 확인
function confirmInboundRevert() {
  var remark = $("#inboundRevertRemark").val().trim();
  if (!remark) {
    alert("되돌리기 사유를 입력해주세요.");
    $("#inboundRevertRemark").focus();
    return;
  }

  var selectedRows = [];

  // 체크된 행들 찾기 (입고완료된 것만, searchRes에서 원본 데이터 사용)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData && tblReturnInboundId) {
        var inboundYn = rowData["inboundYn"] || "";
        if (inboundYn === "Y") {
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("processType", "INBOUND_REVERT");
  processData.append("processUser", sessionUserName);
  processData.append("revertRemark", remark);

  $.ajax({
    url: "/erp105005RevertInboundComplete",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("입고완료가 되돌려졌습니다.");
        closeInboundRevertModal();
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 반출완료 되돌리기 모달 열기
function openOutboundRevertModal() {
  $("#outboundRevertRemark").val("");
  $("#outboundRevertModal").show();
}

// 반출완료 되돌리기 모달 닫기
function closeOutboundRevertModal() {
  $("#outboundRevertModal").hide();
  $("#outboundRevertRemark").val("");
}

// 반출완료 되돌리기 확인
function confirmOutboundRevert() {
  var remark = $("#outboundRevertRemark").val().trim();
  if (!remark) {
    alert("되돌리기 사유를 입력해주세요.");
    $("#outboundRevertRemark").focus();
    return;
  }

  var selectedRows = [];

  // 체크된 행들 찾기 (반출완료된 것만, searchRes에서 원본 데이터 사용)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();

      // searchRes에서 원본 데이터 찾기
      var rowData = null;
      for (var j = 0; j < searchRes.length; j++) {
        if (
          searchRes[j] &&
          searchRes[j]["tblReturnInboundId"] == tblReturnInboundId
        ) {
          rowData = searchRes[j];
          break;
        }
      }

      if (rowData && tblReturnInboundId) {
        var outboundYn = rowData["outboundYn"] || "";
        if (outboundYn === "Y") {
          selectedRows.push(tblReturnInboundId);
        }
      }
    }
  }

  var processData = new FormData();
  processData.append("selectedIds", selectedRows.join(","));
  processData.append("processType", "OUTBOUND_REVERT");
  processData.append("processUser", sessionUserName);
  processData.append("revertRemark", remark);

  $.ajax({
    url: "/erp105005RevertOutboundComplete",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("반출완료가 되돌려졌습니다.");
        closeOutboundRevertModal();
        fnObj.searchOpen.search();
      } else {
        toastr.error("처리 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("처리 중 오류가 발생했습니다.");
    },
  });
}

// 문서 준비 완료 시 초기화
$(document).ready(function () {
  myWins = new dhtmlXWindows();
  myGrid = new dhtmlXGridObject("grid");
  myCalendar = new dhtmlXCalendarObject([
    { input: "dateFrom", button: "dateFromIcon" },
    {
      input: "dateTo",
      button: "dateToIcon",
    },
  ]);

  // 배송확정일 기준 2025년 10월 2일부터 조회 가능하므로, 시작일은 2025-10-02로 설정
  var today = new Date().toISOString().split("T")[0];
  $("#dateFrom").val("2025-10-02");
  $("#dateTo").val(today);

  myCalendar.loadUserLanguage("ko");

  fnObj.comboOpen.comboCmpyCd();
  fnObj.comboOpen.comboDtType();
  fnObj.comboOpen.comboDcCd();
  fnObj.comboOpen.comboSoType();
  fnObj.comboOpen.comboInboundStatus();
  fnObj.comboOpen.comboOutboundStatus();
  fnObj.comboOpen.comboOutboundTarget();

  fnObj.gridOpen.grid();

  window.onresize = function (event) {
    fnObj.gridOpen.setWidth();
    fnObj.gridOpen.setWidth();
  };

  if (
    sessionUserGrntCd == "4100" ||
    sessionUserGrntCd == "4200" ||
    sessionUserGrntCd == "7788"
  ) {
    $("#srchName").val(sessionAgntNm);
    $("#srchCode").val(sessionAgntCd);
    $("#srchName").attr("readonly", true);
    $("#srchCode").attr("readonly", true);
    $("#srchName").css("color", "gray");
    $("#srchCode").css("color", "gray");
    $("#srchIcon").hide();
    $("#delIcon").hide();
  }

  // 권한 9999인 사용자만 특정 버튼 표시
  if (sessionUserGrntCd != "9999") {
    // 용인센터입고완료 버튼 숨기기
    $("#btnInboundComplete").hide();

    // 입고완료 되돌리기 버튼 숨기기
    $("#btnInboundRevert").hide();

    // 반출완료 버튼 숨기기
    $("#btnOutboundComplete").hide();

    // 반출완료 되돌리기 버튼 숨기기
    $("#btnOutboundRevert").hide();

    // 반출대상으로변경 버튼 숨기기
    $("#btnSetOutboundTarget").hide();

    // 반출대상아님으로변경 버튼 숨기기
    $("#btnSetNonTarget").hide();

    // 판별중으로변경 버튼 숨기기
    $("#btnSetPending").hide();
  }

  if (sessionUserGrdCd == "2000" || sessionUserGrntCd == "6000") {
    myComboDcCd.setChecked(myComboDcCd.getIndexByValue(sessionDcCd), true);
    myComboDcCd.updateOption("", "", sessionDcNm, "");
    myComboDcCd.disable();
  }

  toastr.options = {
    closeButton: true,
    progressBar: false,
    showMethod: "slideDown",
    closeDuration: 0,
    positionClass: "toast-top-right",
    timeOut: 0,
  };
});

// 기타비고 저장
function saveEtcRemark(tblReturnInboundId, etcRemark) {
  var processData = new FormData();
  processData.append("tblReturnInboundId", tblReturnInboundId);
  processData.append("etcRemark", etcRemark);
  processData.append("processUser", sessionUserName);

  $.ajax({
    url: "/erp105005UpdateEtcRemark",
    processData: false,
    contentType: false,
    data: processData,
    type: "POST",
    datatype: "json",
    success: function (res) {
      if (res && res.rtnYn == "Y") {
        toastr.success("기타비고가 저장되었습니다.");
      } else {
        toastr.error("저장 중 오류가 발생했습니다.");
      }
    },
    error: function () {
      toastr.error("저장 중 오류가 발생했습니다.");
    },
  });
}

// 키보드 이벤트 처리
$(document).keydown(function (event) {
  if (event.keyCode == 113 || event.which == 113) {
    fnObj.searchOpen.sTime();
  }
});
