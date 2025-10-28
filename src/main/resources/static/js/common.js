//ajax 수행시 비포, 완료, 에러 처리 인터셉트
$.ajaxSetup({
  beforeSend: function (xhr) {
    $(top.document).find("#loader").show();
    top.startTime = new Date(); //시간을 갱신
    xhr.setRequestHeader("AJAX", true);
  },
  complete: function () {
    $(top.document).find("#loader").hide();
  },
  error: function (request, status, error) {
    $(top.document).find("#loader").hide();
    if (request.status == "403") {
      alert("세션 종료 : " + request.status + "\n 다시 로그인하세요");
      location.href = "/exception";
    } else {
      alert(
        "로직 수행 실패 : " +
          request.status +
          ", " +
          error +
          "\n" +
          decodeURL(request.responseText)
      );
      return;
    }
  },
});

//20220112 정연호 추가 java에서 URLEncoder.encode 한것을 javascript에서 decode
function decodeURL(str) {
  var s0, i, j, s, ss, u, n, f;
  s0 = ""; // decoded str
  for (i = 0; i < str.length; i++) {
    // scan the source str
    s = str.charAt(i);
    if (s == "+") {
      s0 += " ";
    } // "+" should be changed to SP
    else {
      if (s != "%") {
        s0 += s;
      } // add an unescaped char
      else {
        // escape sequence decoding
        u = 0; // unicode of the character
        f = 1; // escape flag, zero means end of this sequence
        while (true) {
          ss = ""; // local str to parse as int
          for (j = 0; j < 2; j++) {
            // get two maximum hex characters for parse
            sss = str.charAt(++i);
            if (
              (sss >= "0" && sss <= "9") ||
              (sss >= "a" && sss <= "f") ||
              (sss >= "A" && sss <= "F")
            ) {
              ss += sss; // if hex, add the hex character
            } else {
              --i;
              break;
            } // not a hex char., exit the loop
          }
          n = parseInt(ss, 16); // parse the hex str as byte
          if (n <= 0x7f) {
            u = n;
            f = 1;
          } // single byte format
          if (n >= 0xc0 && n <= 0xdf) {
            u = n & 0x1f;
            f = 2;
          } // double byte format
          if (n >= 0xe0 && n <= 0xef) {
            u = n & 0x0f;
            f = 3;
          } // triple byte format
          if (n >= 0xf0 && n <= 0xf7) {
            u = n & 0x07;
            f = 4;
          } // quaternary byte format (extended)
          if (n >= 0x80 && n <= 0xbf) {
            u = (u << 6) + (n & 0x3f);
            --f;
          } // not a first, shift and add 6 lower bits
          if (f <= 1) {
            break;
          } // end of the utf byte sequence
          if (str.charAt(i + 1) == "%") {
            i++;
          } // test for the next shift byte
          else {
            break;
          } // abnormal, format error
        }
        s0 += String.fromCharCode(u); // add the escaped character
      }
    }
  }
  return s0;
}

window.onerror = function (errorMsg, url, lineNumber, column, errorObj) {
  alert(
    "화면 오류 메세지 :\n" +
      errorMsg +
      "\n파일위치 : " +
      url +
      "\n라인번호 : " +
      lineNumber +
      "\n위치 : " +
      column +
      "\n스텍추적 : " +
      errorObj
  );
  $(top.document).find("#loader").hide();
};

//그리드의 값을 체크한 것만 json 로 만들기
function get_chk_json(myGrid) {
  var rowArr = {};

  var newArr = [];
  var rows = myGrid.getRowsNum();
  var columns = myGrid.getColumnCount();
  var chkCoulumId;
  for (var r = 0; r < rows; r++) {
    for (var k = 0; k < columns; k++) {
      if (myGrid.getColumnId(k) == "chk") {
        chkCoulumId = k;
      }
    }

    for (var c = 0; c < columns; c++) {
      if (myGrid.cellByIndex(r, chkCoulumId).getValue() == 1) {
        //체크되어있으면
        rowArr[myGrid.getColumnId(c)] = myGrid.cellByIndex(r, c).getValue();
      }
    }

    if (Object.keys(rowArr).length > 0 && rowArr.constructor === Object) {
      newArr.push(rowArr);
      rowArr = {};
    }
  }

  return newArr;
}

//그리드의 값을 변경한 것만 json 로 만들기
function get_cud_json(myGrid) {
  var rowArr = {};
  var newArr = [];
  var rows = myGrid.getRowsNum();
  var columns = myGrid.getColumnCount();
  var cudCoulumId;
  for (var r = 0; r < rows; r++) {
    for (var k = 0; k < columns; k++) {
      if (myGrid.getColumnId(k) == "cud") {
        cudCoulumId = k;
      }
    }

    for (var c = 0; c < columns; c++) {
      if (myGrid.cellByIndex(r, cudCoulumId).getValue()) {
        //cud 값이 있으면
        rowArr[myGrid.getColumnId(c)] = myGrid.cellByIndex(r, c).getValue();
      }
    }

    if (Object.keys(rowArr).length > 0 && rowArr.constructor === Object) {
      newArr.push(rowArr);
      rowArr = {};
    }
  }
  return newArr;
}

//팝업창 열기
function myWindowsOpen(
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
) {
  myWins.createWindow({
    id: windowId,
    left: windowLeft,
    top: windowTop,
    width: windowWidth,
    height: windowHeight,
    center: false,
    onClose: windowOnClose,
  });

  myWins.window(windowId).attachURL(windowUrl, null, windowSendObject);
  myWins.window(windowId).setText(windowText); //새창의 제목
  myWins.window(windowId).setModal(windowsModal); //모달 (팝업만 되고 배경 흐리게하며 배경은 아먻도 클릭 안됨)
  myWins.window(windowId).removeUserButton("help");
  myWins.window(windowId).removeUserButton("stick");
  myWins.window(windowId).removeUserButton("park");
}

//팝업창 열기 window 객체 포함
function myWindowsOpen2(
  myWins,
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
) {
  myWins.createWindow({
    id: windowId,
    left: windowLeft,
    top: windowTop,
    width: windowWidth,
    height: windowHeight,
    center: true,
    onClose: windowOnClose,
  });

  myWins.window(windowId).attachURL(windowUrl, null, windowSendObject);
  myWins.window(windowId).setText(windowText); //새창의 제목
  myWins.window(windowId).setModal(windowsModal); //모달 (팝업만 되고 배경 흐리게하며 배경은 아먻도 클릭 안됨)
  myWins.window(windowId).removeUserButton("help");
  myWins.window(windowId).removeUserButton("stick");
  myWins.window(windowId).removeUserButton("park");
}

//그리드의 값을 ArrayList 로 만들기
function get_data(myGrid) {
  var rowArr = [];
  var newArr = [];
  var rows = myGrid.getRowsNum();
  var columns = myGrid.getColumnCount();
  for (var r = 0; r < rows; r++) {
    //iterate through the rows
    for (var c = 0; c < columns; c++) {
      //iterate through the columns
      newVal = myGrid.cellByIndex(r, c).getValue(); //get the value of the current cell
      rowArr.push(newVal); // push it to the array with the "row" data
    }
    newArr.push(rowArr); //save save the row data as an element of the array with the data
    rowArr = []; // reset the row data array
  }

  return newArr;
}

//그리드의 값을 json 로 만들기
function get_json(myGrid) {
  var rowArr = {};
  var newArr = [];
  var rows = myGrid.getRowsNum();
  var columns = myGrid.getColumnCount();
  for (var r = 0; r < rows; r++) {
    for (var c = 0; c < columns; c++) {
      rowArr[myGrid.getColumnId(c)] = myGrid.cellByIndex(r, c).getValue();
    }
    newArr.push(rowArr);
    rowArr = {};
  }

  return newArr;
}

//그리드 to json  헤더텍스트를 포함하여 표시
function get_json_for_excel(myGrid) {
  var rowArr = {};
  var newArr = [];
  var rows = myGrid.getRowsNum();
  var columns = myGrid.getColumnCount();
  for (var r = 0; r < rows; r++) {
    for (var c = 0; c < columns; c++) {
      rowArr[myGrid.getColLabel(c)] = myGrid.cellByIndex(r, c).getValue();
    }
    newArr.push(rowArr);
    rowArr = {};
  }

  return newArr;
}

//화면 url 알아내기
function pageUrl() {
  var thisfilefullname = document.URL.substring(
    document.URL.lastIndexOf("/") + 1,
    document.URL.length
  );
  var pageName = "";
  if (thisfilefullname.lastIndexOf("?") < 1) {
    //URL 뒤에 ?로 전달되는 GET방식이 아니라면
    pageName = thisfilefullname;
  } else {
    pageName = thisfilefullname.substring(0, thisfilefullname.lastIndexOf("?"));
  }
  return pageName;
}

//엑셀 다운로드
function s2ab(s) {
  var buf = new ArrayBuffer(s.length); //convert s to arrayBuffer
  var view = new Uint8Array(buf); //create uint8array as viewer
  for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xff; //convert to octet
  return buf;
}

function exportExcel() {
  // step 1. workbook 생성
  var wb = XLSX.utils.book_new();

  // step 2. 시트 만들기
  var newWorksheet = excelHandler.getWorksheet();
  // step 3. workbook에 새로만든 워크시트에 이름을 주고 붙인다.
  XLSX.utils.book_append_sheet(wb, newWorksheet, excelHandler.getSheetName());
  // step 4. 엑셀 파일 만들기
  var wbout = XLSX.write(wb, { bookType: "xlsx", type: "binary" });

  // step 5. 엑셀 파일 내보내기
  saveAs(
    new Blob([s2ab(wbout)], { type: "application/octet-stream" }),
    excelHandler.getExcelFileName()
  );
}

// 엑셀 다운로드 함수 (excelHandler 객체를 받아서 처리)
function excelDownload(handler) {
  if (!handler) {
    alert("엑셀 핸들러가 정의되지 않았습니다.");
    return;
  }

  // step 1. workbook 생성
  var wb = XLSX.utils.book_new();

  // step 2. 시트 만들기
  var newWorksheet = handler.getWorksheet();
  // step 3. workbook에 새로만든 워크시트에 이름을 주고 붙인다.
  XLSX.utils.book_append_sheet(wb, newWorksheet, handler.getSheetName());
  // step 4. 엑셀 파일 만들기
  var wbout = XLSX.write(wb, { bookType: "xlsx", type: "binary" });

  // step 5. 엑셀 파일 내보내기
  saveAs(
    new Blob([s2ab(wbout)], { type: "application/octet-stream" }),
    handler.getExcelFileName()
  );
}

// add once, make sure dhtmlxcalendar.js is loaded
dhtmlXCalendarObject.prototype.langData["ko"] = {
  // date format for inputs
  dateformat: "%Y-%m-%d",
  // header format
  hdrformat: "%Y년%F",
  // full names of months
  monthesFNames: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  // short names of months
  monthesSNames: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  // full names of days
  daysFNames: [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일",
  ],
  // short names of days
  daysSNames: ["일", "월", "화", "수", "목", "금", "토"],
  // starting day of a week. Number from 1(Monday) to 7(Sunday)
  weekstart: 7,
  // the title of the week number column
  weekname: "w",
  // name of the "Today" button
  today: "Today",
  // name of the "Clear" button
  clear: "Clear",
};

//res to json (myGrid의 눈에 보이는 컬럼값에 해당하는것)
function get_json_res(myGrid, res) {
  if (res == undefined || res == null || res.length <= 0) {
    alert("데이터를 먼저 조회해주세요");
    return;
  }

  var rowArr = {};
  var newArr = [];
  var rows = res.length;
  var columns = myGrid.getColumnCount();

  for (
    var r = 0;
    r < rows;
    r++ //조회한 결과 res 의 로우 수 만큼 반복
  ) {
    for (var c = 0; c < columns; c++) {
      var isHidden = myGrid.isColumnHidden(c);
      if (!isHidden) {
        //false 면 컬럼이 가려지지 않았다는것
        var colLabel = myGrid.getColLabel(c);
        var colId = myGrid.getColumnId(c);
        var resValue = res[r][colId];
        rowArr[myGrid.getColLabel(c)] = resValue;
      }
    }
    newArr.push(rowArr);
    rowArr = {};
  }

  return newArr;
}

//res to json2 (myGrid의 눈에 보이는 컬럼값에 해당하는것. 2nd 라인에 컬럼명이 있을때)
function get_json_res2(myGrid, res) {
  if (res == undefined || res == null || res.length <= 0) {
    alert("데이터를 먼저 조회해주세요");
    return;
  }

  var rowArr = {};
  var newArr = [];
  var rows = res.length;
  var columns = myGrid.getColumnCount();

  for (
    var r = 0;
    r < rows;
    r++ //조회한 결과 res 의 로우 수 만큼 반복
  ) {
    for (var c = 0; c < columns; c++) {
      var isHidden = myGrid.isColumnHidden(c, 1);
      if (!isHidden) {
        //false 면 컬럼이 가려지지 않았다는것
        var colLabel = myGrid.getColLabel(c, 1);
        var colId = myGrid.getColumnId(c, 1);
        var resValue = res[r][colId];
        rowArr[myGrid.getColLabel(c, 1)] = resValue;
      }
    }
    newArr.push(rowArr);
    rowArr = {};
  }

  return newArr;
}

// Byte 수 체크 제한
function fnChkByte(obj, maxByte) {
  var str = obj.value;
  var str_len = str.length;

  var rbyte = 0;
  var rlen = 0;
  var one_char = "";
  var str2 = "";

  for (var i = 0; i < str_len; i++) {
    one_char = str.charAt(i);
    if (escape(one_char).length > 4) {
      rbyte += 2; //한글2Byte
    } else {
      rbyte++; //영문 등 나머지 1Byte
    }
    if (rbyte <= maxByte) {
      rlen = i + 1; //return할 문자열 갯수
    }
  }
  if (rbyte > maxByte) {
    alert(
      "한글 " +
        maxByte / 2 +
        "자 / 영문 " +
        maxByte +
        "자를 초과 입력할 수 없습니다."
    );
    //alert("메세지는 최대 " + maxByte + "byte를 초과할 수 없습니다.")
    str2 = str.substr(0, rlen); //문자열 자르기
    obj.value = str2;
    //fnChkByte(obj, maxByte);
    return false;
  }
  return true;
}

// Byte 수 체크 제한
function fnChkStringByte(str, maxByte) {
  var str_len = str.length;

  var rbyte = 0;
  var rlen = 0;
  var one_char = "";
  var str2 = "";

  for (var i = 0; i < str_len; i++) {
    one_char = str.charAt(i);
    if (escape(one_char).length > 4) {
      rbyte += 2; //한글2Byte
    } else {
      rbyte++; //영문 등 나머지 1Byte
    }
    if (rbyte <= maxByte) {
      rlen = i + 1; //return할 문자열 갯수
    }
  }
  if (rbyte > maxByte) {
    alert(
      "한글 " +
        maxByte / 2 +
        "자 / 영문 " +
        maxByte +
        "자를 초과 입력할 수 없습니다."
    );
    //alert("메세지는 최대 " + maxByte + "byte를 초과할 수 없습니다.")
    str2 = str.substr(0, rlen); //문자열 자르기
    str = str2;
    return false;
  }
  return true;
}

//8자리 년월일을 10자리 년-월-일 로 변경하는 함수
function dateTypeChange(ymd, len) {
  if (!ymd) {
    //alert("날짜가 없거나  잘못되었습니다.");
    return "";
  }
  if (len == 8) {
    //날짜가 뭐가 들어오던 8자리 날짜형태 yyyyMMdd 로 변경
    ymd = ymd.replace(/-/g, "");
  }
  if (len == 10) {
    ymd = ymd.replace(/-/g, ""); //날짜가 뭐가 들어오던 8자리 날짜형태 yyyyMMdd 로 변경후 이걸 10자리로 변경 yyyy-MM-dd
    ymd =
      ymd.substring(0, 4) +
      "-" +
      ymd.substring(4, 6) +
      "-" +
      ymd.substring(6, 8);
  }

  if (ymd.length != len) {
    alert("날짜 형태 변환 실패");
    return fail;
  }
  return ymd;
}

/**
 * 20230329 정연호 추가. confirm, alert 를 modal로 사용하기 위한 부분
 *  alert, confirm 대용 팝업 메소드 정의 <br/>
 *  timer : 애니메이션 동작 속도 <br/>
 *  alert : 경고창 <br/>
 *  confirm : 확인창 <br/>
 *  open : 팝업 열기 <br/>
 *  close : 팝업 닫기 <br/>
 */
var action_popup = {
  //timer : 500,
  timer: 0,
  confirm: function (txt, callback) {
    if (txt == null || txt.trim() == "") {
      console.warn("confirm message is empty.");
    } else if (callback == null || typeof callback != "function") {
      console.warn("confirm callback is null or not function.");
    } else {
      $(".type-confirm .btn_ok").on("click", function () {
        $(this).unbind("click");
        callback(true);
        action_popup.close(this);
      });
      this.open("type-confirm", txt);
    }
  },

  alert: function (txt, callback) {
    if (txt == null || txt.trim() == "") {
      console.warn("alert message is empty.");
    } else if (callback == null || typeof callback != "function") {
      console.warn("alert callback is null or not function.");
      this.open("type-alert", txt);
    } else {
      $(".type-alert .modal_close").on("click", function () {
        $(this).unbind("click");
        callback(true);
        action_popup.close(this);
      });
      this.open("type-alert", txt);
    }
  },

  open: function (type, txt) {
    var popup = $("." + type);
    popup.find(".menu_msg").html(txt); //20230329 정연호 수정.text 를 html 로 변경
    $("body").append("<div class='dimLayer'></div>");
    $(".dimLayer").css("height", $(document).height()).attr("target", type);
    popup.fadeIn(this.timer);
  },

  close: function (target) {
    var modal = $(target).closest(".modal-section");
    var dimLayer;
    if (modal.hasClass("type-confirm")) {
      dimLayer = $(".dimLayer[target=type-confirm]");
      $(".type-confirm .btn_ok").unbind("click");
    } else if (modal.hasClass("type-alert")) {
      dimLayer = $(".dimLayer[target=type-alert]");
    } else {
      console.warn("close unknown target.");
      return;
    }
    modal.fadeOut(this.timer);
    setTimeout(function () {
      dimLayer != null ? dimLayer.remove() : "";
    }, this.timer);
  },
};

// ===== 레이아웃 헬퍼 함수들 =====
// 다른 페이지에서 레이아웃에 안전하게 접근할 수 있도록 하는 함수들

/**
 * 레이아웃의 C 영역 너비를 안전하게 가져오는 함수
 * @returns {number} C 영역의 너비
 */
function getLayoutCWidth() {
  try {
    // top 프레임에서 레이아웃 접근
    if (top && top.myLayout && top.myLayout.cells && top.myLayout.cells("c")) {
      return top.myLayout.cells("c").getWidth();
    }
    // 현재 프레임에서 레이아웃 접근
    else if (
      window.myLayout &&
      window.myLayout.cells &&
      window.myLayout.cells("c")
    ) {
      return window.myLayout.cells("c").getWidth();
    }
    // 레이아웃 매니저를 통한 접근
    else if (
      window.layoutManager &&
      window.layoutManager.myLayout &&
      window.layoutManager.myLayout.cells("c")
    ) {
      return window.layoutManager.myLayout.cells("c").getWidth();
    } else {
      console.warn("레이아웃에 접근할 수 없습니다. 기본값을 반환합니다.");
      return 800; // 기본 너비
    }
  } catch (error) {
    console.error("레이아웃 너비 가져오기 실패:", error);
    return 800; // 기본 너비
  }
}

/**
 * 레이아웃의 C 영역 높이를 안전하게 가져오는 함수
 * @returns {number} C 영역의 높이
 */
function getLayoutCHeight() {
  try {
    // top 프레임에서 레이아웃 접근
    if (top && top.myLayout && top.myLayout.cells && top.myLayout.cells("c")) {
      return top.myLayout.cells("c").getHeight();
    }
    // 현재 프레임에서 레이아웃 접근
    else if (
      window.myLayout &&
      window.myLayout.cells &&
      window.myLayout.cells("c")
    ) {
      return window.myLayout.cells("c").getHeight();
    }
    // 레이아웃 매니저를 통한 접근
    else if (
      window.layoutManager &&
      window.layoutManager.myLayout &&
      window.layoutManager.myLayout.cells("c")
    ) {
      return window.layoutManager.myLayout.cells("c").getHeight();
    } else {
      console.warn("레이아웃에 접근할 수 없습니다. 기본값을 반환합니다.");
      return 600; // 기본 높이
    }
  } catch (error) {
    console.error("레이아웃 높이 가져오기 실패:", error);
    return 600; // 기본 높이
  }
}

/**
 * 레이아웃의 특정 영역에 안전하게 접근하는 함수
 * @param {string} cellName - 접근할 영역 이름 (a, b, c)
 * @param {string} property - 가져올 속성 (getWidth, getHeight 등)
 * @returns {*} 해당 속성 값
 */
function getLayoutCellProperty(cellName, property) {
  try {
    // top 프레임에서 레이아웃 접근
    if (
      top &&
      top.myLayout &&
      top.myLayout.cells &&
      top.myLayout.cells(cellName)
    ) {
      return top.myLayout.cells(cellName)[property]();
    }
    // 현재 프레임에서 레이아웃 접근
    else if (
      window.myLayout &&
      window.myLayout.cells &&
      window.myLayout.cells(cellName)
    ) {
      return window.myLayout.cells(cellName)[property]();
    }
    // 레이아웃 매니저를 통한 접근
    else if (
      window.layoutManager &&
      window.layoutManager.myLayout &&
      window.layoutManager.myLayout.cells(cellName)
    ) {
      return window.layoutManager.myLayout.cells(cellName)[property]();
    } else {
      console.warn(`레이아웃 영역 ${cellName}에 접근할 수 없습니다.`);
      return null;
    }
  } catch (error) {
    console.error(
      `레이아웃 영역 ${cellName}의 ${property} 가져오기 실패:`,
      error
    );
    return null;
  }
}

/**
 * 레이아웃이 사용 가능한지 확인하는 함수
 * @returns {boolean} 레이아웃 사용 가능 여부
 */
function isLayoutAvailable() {
  return !!(top && top.myLayout) || !!window.myLayout || !!window.layoutManager;
}

// 기존 코드와의 호환성을 위한 별칭 함수들
window.getLayoutCWidth = getLayoutCWidth;
window.getLayoutCHeight = getLayoutCHeight;
window.getLayoutCellProperty = getLayoutCellProperty;
window.isLayoutAvailable = isLayoutAvailable;

// ===== 기존 코드 호환성을 위한 오버라이드 =====
// 기존 코드에서 top.myLayout.cells("c").getWidth() 호출 시 안전하게 처리

// top 객체가 없거나 myLayout이 없는 경우를 대비한 안전장치
if (typeof top !== "undefined" && top) {
  // top.myLayout이 없을 때를 대비한 안전장치
  if (!top.myLayout) {
    Object.defineProperty(top, "myLayout", {
      get: function () {
        // 레이아웃 매니저가 있으면 그것을 반환
        if (window.layoutManager && window.layoutManager.myLayout) {
          return window.layoutManager.myLayout;
        }
        // 현재 프레임의 myLayout이 있으면 그것을 반환
        if (window.myLayout) {
          return window.myLayout;
        }
        // 없으면 null 반환
        return null;
      },
      set: function (value) {
        // setter는 그대로 유지
        Object.defineProperty(top, "myLayout", {
          value: value,
          writable: true,
          configurable: true,
        });
      },
      configurable: true,
    });
  }
}

// 전역 변수 c_width를 안전하게 설정하는 함수
window.setCWidth = function () {
  try {
    return getLayoutCWidth();
  } catch (error) {
    console.error("c_width 설정 실패:", error);
    return 800; // 기본값
  }
};
