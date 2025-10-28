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
      comboList(myComboDtType, "A", "COMM", "SO_SRCH_DT_TYPE", "", "Y", "");
      myComboDtType.selectOption(0);
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
        "#master_checkbox,CUD,ID,ID2,물류센터코드,물류센터명,AL오더,고객사주문번호,주문유형,주문상태,배송확정일,시공기사," +
          "화주코드,화주명,수취인,수취인전화,상품코드,상품명,반품수량,입고상태,용인센터입고일,입고처리자,입고비고," +
          "반출상태,반출일,반출처리자,반출비고,반출대상,우편번호,주소,저장일"
      );

      myGrid.setColumnIds(
        "chk,cud,tblReturnInboundId,tblSoMId,dcCd,dcNm,soNo,refSoNo,soTypeNm,soStatCdNm,dlvyCnfmDt,instEr," +
          "agntCd,agntNm,acptEr,acptTel1,prodCd,prodNm,returnQty,inboundYn,inboundDt,inboundUser,inboundRemark," +
          "outboundYn,outboundDt,outboundUser,outboundRemark,outboundTargetYn,postCd,addr1,saveTime"
      );

      myGrid.setInitWidths(
        "40," + // master_checkbox
          "40," + // CUD
          "80," + // ID (tblReturnInboundId)
          "80," + // ID2 (tblSoMId)
          "80," + // 물류센터코드
          "120," + // 물류센터명
          "100," + // AL오더
          "120," + // 고객사주문번호
          "100," + // 주문유형
          "100," + // 주문상태
          "100," + // 배송확정일
          "80," + // 시공기사
          "120," + // 화주코드
          "120," + // 화주명
          "80," + // 수취인
          "80," + // 수취인전화
          "60," + // 상품코드
          "200," + // 상품명
          "80," + // 반품수량
          "80," + // 입고상태
          "100," + // 용인센터입고일
          "100," + // 입고처리자
          "100," + // 입고비고
          "80," + // 반출상태
          "100," + // 반출일
          "100," + // 반출처리자
          "100," + // 반출비고
          "80," + // 반출대상
          "80," + // 우편번호
          "80," + // 주소
          "200," + // 저장일
          "160" // (마지막)
      );

      myGrid.setColAlign(
        "center,center,center,center,center,center,center,center,center,center,center," +
          "center,center,center,center,center,left,right,center,center,center,center,left," +
          "center,center,center,left,center,center,left,center"
      );

      myGrid.setColTypes(
        "ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro," +
          "ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro," +
          "ro,ro,ro,ro,ro,ro,ro,ro"
      );

      myGrid.setColSorting(
        ",,,,,str,str,str,str,str,str,str," +
          "str,str,str,str,str,str,int,str,str,str,str,str," +
          "str,str,str,str,str,str,str"
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
        "#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter"
      );

      myGrid.init();
      myGrid.enableSmartRendering(true);
      myGrid.enablePreRendering(10);

      // 뱃지 렌더링 함수
      function renderBadges() {
        for (var i = 1; i <= myGrid.getRowsNum(); i++) {
          // 입고 상태 뱃지 렌더링
          var inboundYnCell = myGrid.cellById(
            i,
            myGrid.getColIndexById("inboundYn")
          );
          if (inboundYnCell) {
            var inboundValue = inboundYnCell.getValue();
            if (inboundValue === "Y") {
              inboundYnCell.setValue(
                '<span class="badge badge-success">완료</span>'
              );
            } else {
              inboundYnCell.setValue(
                '<span class="badge badge-warning">대기</span>'
              );
            }
          }

          // 반출 상태 뱃지 렌더링
          var outboundYnCell = myGrid.cellById(
            i,
            myGrid.getColIndexById("outboundYn")
          );
          if (outboundYnCell) {
            var outboundValue = outboundYnCell.getValue();
            if (outboundValue === "Y") {
              outboundYnCell.setValue(
                '<span class="badge badge-success">완료</span>'
              );
            } else {
              outboundYnCell.setValue(
                '<span class="badge badge-warning">대기</span>'
              );
            }
          }

          // 반출 대상 뱃지 렌더링
          var outboundTargetCell = myGrid.cellById(
            i,
            myGrid.getColIndexById("outboundTargetYn")
          );
          if (outboundTargetCell) {
            var targetValue = outboundTargetCell.getValue();
            if (targetValue === "Y") {
              outboundTargetCell.setValue(
                '<span class="badge badge-primary">대상</span>'
              );
            } else {
              outboundTargetCell.setValue(
                '<span class="badge badge-secondary">제외</span>'
              );
            }
          }
        }
      }

      // 그리드 데이터 로드 후 뱃지 렌더링
      myGrid.attachEvent("onXLE", function () {
        setTimeout(renderBadges, 100);
      });

      // 행 표시 시 뱃지 렌더링
      myGrid.attachEvent("onRowDisplay", function (rowId, rowData) {
        setTimeout(function () {
          renderBadges();
        }, 50);
      });

      fnObj.gridOpen.setWidth();
      myGrid.addRow(1, "");
      myGrid.deleteRow(1);
      myGrid.enableEditEvents(true, false, false);
      myGrid.enableBlockSelection(true);
      myGrid.attachEvent("onKeyPress", fnObj.keyOpen.onKeyPressed);
      myGrid.attachEvent("onRowDblClicked", function (rowId, colIndex) {
        fnObj.winOpen.erp104001p1Open();
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
        if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200") {
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
        if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200") {
          agntListValue = sessionAgntCd; // 본인 화주사로 강제 설정
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
      console.log("search : " + (new Date().valueOf() - z) / 1000 + " 초");
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
    var jsonData = {};
    for (var c = 0; c < columns; c++) {
      jsonData[myGrid.getColumnId(c)] = searchRes[i][myGrid.getColumnId(c)];
    }
    myGrid.addRow(i + 1, "");
    myGrid.setRowData(i + 1, jsonData);

    // 용인센터 입고 상태에 따른 행 색상 설정
    var inboundYn = myGrid
      .cellById(i + 1, myGrid.getColIndexById("inboundYn"))
      .getValue();
    var outboundYn = myGrid
      .cellById(i + 1, myGrid.getColIndexById("outboundYn"))
      .getValue();
    var outboundTargetYn = myGrid
      .cellById(i + 1, myGrid.getColIndexById("outboundTargetYn"))
      .getValue();

    // 반출 대상이 아닌 경우 (N 또는 null) - 회색, 체크박스 활성화
    if (
      outboundTargetYn == "N" ||
      outboundTargetYn == null ||
      outboundTargetYn == ""
    ) {
      myGrid.setRowColor(i + 1, "#D3D3D3");
      myGrid.cellById(i + 1, myGrid.getColIndexById("chk")).setDisabled(false);
    }
    // 판별중인 경우 (P) - 연한 파란색, 체크박스 활성화
    else if (outboundTargetYn == "P") {
      myGrid.setRowColor(i + 1, "#E6F3FF");
      myGrid.cellById(i + 1, myGrid.getColIndexById("chk")).setDisabled(false);
    }
    // 반출 대상이면서 입고와 반출 모두 완료 - 연두색
    else if (inboundYn == "Y" && outboundYn == "Y") {
      myGrid.setRowColor(i + 1, "#E8F5E8");
    }
    // 반출 대상이면서 입고만 완료 - 연한 노랑색
    else if (inboundYn == "Y" && outboundYn == "N") {
      myGrid.setRowColor(i + 1, "#FFF2CC");
    }
    // 반출 대상이면서 입고도 안 된 것 - 연한 붉은색
    else if (inboundYn == "N") {
      myGrid.setRowColor(i + 1, "#FFE6E6");
    }
  }

  // 뱃지 렌더링 적용
  setTimeout(function () {
    for (var i = 1; i <= myGrid.getRowsNum(); i++) {
      // 입고 상태 뱃지 렌더링
      var inboundYnCell = myGrid.cellById(
        i,
        myGrid.getColIndexById("inboundYn")
      );
      if (inboundYnCell) {
        var inboundValue = inboundYnCell.getValue();
        if (inboundValue === "Y") {
          inboundYnCell.setValue(
            '<span class="badge badge-success">완료</span>'
          );
        } else {
          inboundYnCell.setValue(
            '<span class="badge badge-warning">대기</span>'
          );
        }
      }

      // 반출 상태 뱃지 렌더링
      var outboundYnCell = myGrid.cellById(
        i,
        myGrid.getColIndexById("outboundYn")
      );
      if (outboundYnCell) {
        var outboundValue = outboundYnCell.getValue();
        if (outboundValue === "Y") {
          outboundYnCell.setValue(
            '<span class="badge badge-success">완료</span>'
          );
        } else {
          outboundYnCell.setValue(
            '<span class="badge badge-warning">대기</span>'
          );
        }
      }

      // 반출 대상 뱃지 렌더링
      var outboundTargetCell = myGrid.cellById(
        i,
        myGrid.getColIndexById("outboundTargetYn")
      );
      if (outboundTargetCell) {
        var targetValue = outboundTargetCell.getValue();
        if (targetValue === "Y") {
          outboundTargetCell.setValue(
            '<span class="badge badge-primary">대상</span>'
          );
        } else {
          outboundTargetCell.setValue(
            '<span class="badge badge-secondary">제외</span>'
          );
        }
      }
    }
  }, 100);
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
    "물류센터코드",
    "물류센터명",
    "AL오더",
    "고객사주문번호",
    "주문유형",
    "주문상태",
    "배송확정일",
    "시공기사",
    "화주코드",
    "화주명",
    "수취인",
    "수취인전화",
    "상품코드",
    "상품명",
    "반품수량",
    "입고여부체크",
    "비고",
    "재입고일",
    "재입고처리자",
    "반출상태",
    "반출일",
    "반출처리자",
    "저장일",
  ];
  excelData.push(headers);

  // 데이터 행 추가 (요청된 순서로)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var rowData = [
      myGrid.cellById(i, myGrid.getColIndexById("dcCd")).getValue() || "", // 물류센터코드
      myGrid.cellById(i, myGrid.getColIndexById("dcNm")).getValue() || "", // 물류센터명
      myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue() || "", // 주문번호
      myGrid.cellById(i, myGrid.getColIndexById("refSoNo")).getValue() || "", // 고객사주문번호
      myGrid.cellById(i, myGrid.getColIndexById("soTypeNm")).getValue() || "", // 주문유형
      myGrid.cellById(i, myGrid.getColIndexById("soStatCdNm")).getValue() || "", // 주문상태
      myGrid.cellById(i, myGrid.getColIndexById("dlvyCnfmDt")).getValue() || "", // 배송확정일
      myGrid.cellById(i, myGrid.getColIndexById("instEr")).getValue() || "", // 시공기사
      myGrid.cellById(i, myGrid.getColIndexById("agntCd")).getValue() || "", // 화주코드
      myGrid.cellById(i, myGrid.getColIndexById("agntNm")).getValue() || "", // 화주명
      myGrid.cellById(i, myGrid.getColIndexById("acptEr")).getValue() || "", // 수취인
      myGrid.cellById(i, myGrid.getColIndexById("acptTel1")).getValue() || "", // 수취인전화
      myGrid.cellById(i, myGrid.getColIndexById("prodCd")).getValue() || "", // 상품코드
      myGrid.cellById(i, myGrid.getColIndexById("prodNm")).getValue() || "", // 상품명
      myGrid.cellById(i, myGrid.getColIndexById("returnQty")).getValue() || "", // 반품수량
      "", // 입고여부체크 (현장 체크용 빈 칸)
      "", // 비고 (현장 체크용 빈 칸)
      myGrid.cellById(i, myGrid.getColIndexById("inboundDt")).getValue() || "", // 재입고일
      myGrid.cellById(i, myGrid.getColIndexById("inboundUser")).getValue() ||
        "", // 재입고처리자
      myGrid.cellById(i, myGrid.getColIndexById("outboundYn")).getValue() || "", // 반출상태
      myGrid.cellById(i, myGrid.getColIndexById("outboundDt")).getValue() || "", // 반출일
      myGrid.cellById(i, myGrid.getColIndexById("outboundUser")).getValue() ||
        "", // 반출처리자
      myGrid.cellById(i, myGrid.getColIndexById("saveTime")).getValue() || "", // 저장일
    ];
    excelData.push(rowData);
  }

  // 엑셀 파일 생성 및 다운로드
  var wb = XLSX.utils.book_new();
  var ws = XLSX.utils.aoa_to_sheet(excelData);

  // 컬럼 너비 설정
  ws["!cols"] = [
    { wch: 12 }, // 물류센터코드
    { wch: 15 }, // 물류센터명
    { wch: 15 }, // 주문번호
    { wch: 15 }, // 고객사주문번호
    { wch: 12 }, // 주문유형
    { wch: 12 }, // 주문상태
    { wch: 12 }, // 배송확정일
    { wch: 15 }, // 시공기사
    { wch: 10 }, // 화주코드
    { wch: 15 }, // 화주명
    { wch: 15 }, // 수취인
    { wch: 15 }, // 수취인전화
    { wch: 15 }, // 상품코드
    { wch: 25 }, // 상품명
    { wch: 10 }, // 반품수량
    { wch: 12 }, // 입고여부체크
    { wch: 20 }, // 비고
    { wch: 12 }, // 재입고일
    { wch: 15 }, // 재입고처리자
    { wch: 10 }, // 반출상태
    { wch: 12 }, // 반출일
    { wch: 15 }, // 반출처리자
    { wch: 20 }, // 저장일
  ];

  XLSX.utils.book_append_sheet(wb, ws, "교환반품용인센터입고관리");

  var fileName =
    "교환반품용인센터입고관리_" +
    new Date().toISOString().split("T")[0] +
    ".xlsx";
  XLSX.writeFile(wb, fileName);
}

// 화주사별 리포트 조회
function viewReport() {
  if (!myGrid.getRowsNum()) {
    alert("리포트 조회 대상이 없습니다.\n조회해주세요");
    return;
  }

  // 화주사별 그룹핑 데이터 생성
  var reportData = {};

  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var agntNm = myGrid
      .cellById(i, myGrid.getColIndexById("agntNm"))
      .getValue();
    var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();
    var soTypeNm = myGrid
      .cellById(i, myGrid.getColIndexById("soTypeNm"))
      .getValue();
    var prodNm = myGrid
      .cellById(i, myGrid.getColIndexById("prodNm"))
      .getValue();
    var returnQty = myGrid
      .cellById(i, myGrid.getColIndexById("returnQty"))
      .getValue();
    var inboundYn = myGrid
      .cellById(i, myGrid.getColIndexById("inboundYn"))
      .getValue();
    var outboundYn = myGrid
      .cellById(i, myGrid.getColIndexById("outboundYn"))
      .getValue();
    var inboundUser = myGrid
      .cellById(i, myGrid.getColIndexById("inboundUser"))
      .getValue();
    var outboundUser = myGrid
      .cellById(i, myGrid.getColIndexById("outboundUser"))
      .getValue();

    if (!reportData[agntNm]) {
      reportData[agntNm] = [];
    }

    reportData[agntNm].push({
      soNo: soNo,
      soTypeNm: soTypeNm,
      prodNm: prodNm,
      returnQty: returnQty,
      inboundYn: inboundYn,
      outboundYn: outboundYn,
      inboundUser: inboundUser,
      outboundUser: outboundUser,
    });
  }

  // 리포트 창 열기
  openReportWindow(reportData);
}

// 리포트 창 열기
function openReportWindow(reportData) {
  var reportHtml = generateReportHtml(reportData);
  var reportWindow = window.open(
    "",
    "reportWindow",
    "width=1200,height=800,scrollbars=yes,resizable=yes"
  );

  reportWindow.document.write(reportHtml);
  reportWindow.document.close();
}

// 리포트 HTML 생성
function generateReportHtml(reportData) {
  var html = `
        <!DOCTYPE html>
        <html>
        <head>
            <title>교환/반품 용인센터 입고 관리 리포트</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; margin-bottom: 30px; }
                .agency-section { margin-bottom: 40px; page-break-inside: avoid; page-break-after: always; }
                .agency-title { background-color: #f0f0f0; padding: 10px; font-weight: bold; font-size: 16px; }
                .data-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
                .data-table th, .data-table td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                .data-table th { background-color: #f5f5f5; font-weight: bold; }
                .status-complete { color: green; font-weight: bold; }
                .status-pending { color: red; font-weight: bold; }
                .summary { margin-top: 20px; padding: 15px; background-color: #f9f9f9; }
            </style>
        </head>
        <body>
            <div class="header">
                <h1>교환/반품 용인센터 입고 관리 리포트</h1>
                <p>조회일시: ${new Date().toLocaleString()}</p>
            </div>
    `;

  var totalCount = 0;
  var completeCount = 0;

  for (var agntNm in reportData) {
    var items = reportData[agntNm];
    totalCount += items.length;

    html += `
            <div class="agency-section">
                <div class="agency-title">화주사: ${agntNm} (${items.length}건)</div>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>AL오더</th>
                            <th>주문유형</th>
                            <th>상품명</th>
                            <th>반품수량</th>
                            <th>용인센터입고</th>
                            <th>반출상태</th>
                            <th>입고처리자</th>
                            <th>반출처리자</th>
                        </tr>
                    </thead>
                    <tbody>
        `;

    for (var i = 0; i < items.length; i++) {
      var item = items[i];
      if (item.inboundYn === "Y" && item.outboundYn === "Y") {
        completeCount++;
      }

      html += `
                <tr>
                    <td>${item.soNo}</td>
                    <td>${item.soTypeNm}</td>
                    <td>${item.prodNm}</td>
                    <td>${item.returnQty}</td>
                    <td class="${
                      item.inboundYn === "Y"
                        ? "status-complete"
                        : "status-pending"
                    }">
                        ${item.inboundYn === "Y" ? "완료" : "대기"}
                    </td>
                    <td class="${
                      item.outboundYn === "Y"
                        ? "status-complete"
                        : "status-pending"
                    }">
                        ${item.outboundYn === "Y" ? "완료" : "대기"}
                    </td>
                    <td>${item.inboundUser || "-"}</td>
                    <td>${item.outboundUser || "-"}</td>
                </tr>
            `;
    }

    html += `
                    </tbody>
                </table>
            </div>
        `;
  }

  html += `
            <div class="summary">
                <h3>요약 정보</h3>
                <p>총 건수: ${totalCount}건</p>
                <p>완료 건수: ${completeCount}건</p>
                <p>진행률: ${
                  totalCount > 0
                    ? Math.round((completeCount / totalCount) * 100)
                    : 0
                }%</p>
            </div>
        </body>
        </html>
    `;

  return html;
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
    console.log("Row " + i + " chkValue:", chkValue, "type:", typeof chkValue);
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      console.log("Row " + i + " tblReturnInboundId:", tblReturnInboundId);
      if (tblReturnInboundId) {
        selectedRows.push(tblReturnInboundId);
      }
    }
  }
  console.log("Selected rows:", selectedRows);

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

  // 상태별 메시지 설정
  var statusText = "";
  switch (targetStatus) {
    case "Y":
      statusText = "반출대상";
      break;
    case "N":
      statusText = "반출대상아님";
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

// 입고완료 되돌리기 (모달 열기)
function inboundRevert() {
  // 권한 체크: 권한 9999인 사용자만 접근 가능
  if (sessionUserGrntCd != "9999") {
    alert("권한이 없습니다. 관리자만 사용할 수 있습니다.");
    return;
  }

  var selectedRows = [];
  var completedRows = [];

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var inboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("inboundYn"))
        .getValue();
      var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();

      if (tblReturnInboundId) {
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
      var inboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("inboundYn"))
        .getValue();
      var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();

      if (inboundYn !== "Y") {
        nonCompletedRows.push(soNo);
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

  // 체크된 행들 찾기
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var outboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("outboundYn"))
        .getValue();
      var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();

      if (tblReturnInboundId) {
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
      var outboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("outboundYn"))
        .getValue();
      var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();

      if (outboundYn !== "Y") {
        nonCompletedRows.push(soNo);
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

  // 체크된 행들 찾기 (입고완료된 것만)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var inboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("inboundYn"))
        .getValue();

      if (tblReturnInboundId && inboundYn === "Y") {
        selectedRows.push(tblReturnInboundId);
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

  // 체크된 행들 찾기 (반출완료된 것만)
  for (var i = 1; i <= myGrid.getRowsNum(); i++) {
    var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
    if (chkValue == "1" || chkValue == "true" || chkValue === true) {
      var tblReturnInboundId = myGrid
        .cellById(i, myGrid.getColIndexById("tblReturnInboundId"))
        .getValue();
      var outboundYn = myGrid
        .cellById(i, myGrid.getColIndexById("outboundYn"))
        .getValue();

      if (tblReturnInboundId && outboundYn === "Y") {
        selectedRows.push(tblReturnInboundId);
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

  var now = new Date();
  var year = now.getFullYear() + "";
  var month = now.getMonth() + 1 + "";
  var date = now.getDate() + "";
  if (month.length == 1) {
    month = "0" + month;
  }
  if (date.length == 1) {
    date = "0" + date;
  }

  var today = year + "-" + month + "-" + date;
  $("#dateFrom").val(today);
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

  if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200") {
    $("#srchName").val(sessionAgntNm);
    $("#srchCode").val(sessionAgntCd);
    $("#srchName").attr("readonly", true);
    $("#srchCode").attr("readonly", true);
    $("#srchName").css("color", "gray");
    $("#srchCode").css("color", "gray");
    $("#srchIcon").hide();
    $("#delIcon").hide();

    // 화주사는 본인 화주사로만 조회 강제
    console.log("화주사 권한 - 본인 화주사로만 조회: " + sessionAgntCd);
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
    console.log("sessionDcCd : " + sessionDcCd);
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

// 키보드 이벤트 처리
$(document).keydown(function (event) {
  if (event.keyCode == 113 || event.which == 113) {
    fnObj.searchOpen.sTime();
  }
});
