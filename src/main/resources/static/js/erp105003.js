/**
 * 입고 완료 처리 (erp105003) JavaScript
 */

// 전역 변수
var pagePerRow = 50;
var myWins;
var pendingData = []; // 입고 예정 목록 데이터 저장용
var historyData = []; // 입고 완료 내역 데이터 저장용

// 세션 변수 (Thymeleaf에서 주입됨)
var sessionCmpyCd = "A";
var sessionUserId = "admin";

// 메인 함수 객체
var fnObj = {
  // 페이징 관련 변수
  currentPage: 1,
  totalPages: 1,
  totalCount: 0,
  pageSize: 50,

  // 페이지 초기화
  initializePage: function () {
    // 오늘 날짜를 기본값으로 설정
    var today = new Date().toISOString().split("T")[0];
    $("#dateFrom").val(today);
    $("#dateTo").val(today);
    $("#historyDateFrom").val(today);
    $("#historyDateTo").val(today);

    // 초기 데이터 로드
    fnObj.loadPendingInboundList();
  },
  // 탭 관리
  tabOpen: {
    showPendingTab: function () {
      $(".tab-button").removeClass("active");
      $(".tab-content").removeClass("active");
      $(".tab-button:contains('입고 예정 목록')").addClass("active");
      $("#pendingContent").addClass("active");
    },
    showHistoryTab: function () {
      $(".tab-button").removeClass("active");
      $(".tab-content").removeClass("active");
      $(".tab-button:contains('입고 완료 내역')").addClass("active");
      $("#historyContent").addClass("active");
    },
  },
  // 입고 예정 목록 조회 (페이징)
  loadPendingInboundList: function (pageNum = 1) {
    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#agntCd").val(),
      mtrlCd: $("#productCd").val(),
      mtrlNm: $("#productNm").val(),
      dateFrom: $("#dateFrom").val(),
      dateTo: $("#dateTo").val(),
      pageNum: pageNum,
      pageSize: fnObj.pageSize,
    };

    $.ajax({
      url: "/erp105003PendingList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchVO),
      success: function (res) {
        if (res.success) {
          fnObj.currentPage = res.currentPage;
          fnObj.totalPages = res.totalPages;
          fnObj.totalCount = res.totalCount;
          // 현재 조회된 데이터 저장 (엑셀 다운로드용)
          pendingData = res.data;
          fnObj.renderPendingInboundList(res.data);
          fnObj.renderPagination();
        } else {
          alert("입고 예정 목록 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function () {
        alert("입고 예정 목록 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 예정 목록 렌더링
  renderPendingInboundList: function (data) {
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="12" style="text-align: center; padding: 20px; color: #666;">조회된 입고 예정 목록이 없습니다.</td></tr>';
    } else {
      var startNumber = (fnObj.currentPage - 1) * fnObj.pageSize + 1;
      data.forEach(function (item, index) {
        html +=
          "<tr data-id='" +
          item.tblInboundProductId +
          "' data-agnt-cd='" +
          (item.agntCd || "") +
          "' data-cmpy-cd='" +
          (item.cmpyCd || "") +
          "'>";
        html += "<td>" + (startNumber + index) + "</td>";
        html +=
          '<td><input type="checkbox" class="inbound-checkbox" value="' +
          item.tblInboundProductId +
          '" onchange="fnObj.updateCompleteButton()" /></td>';
        html += "<td>" + (item.agntNm || "") + "</td>";
        html += "<td>" + (item.mtrlCd || "") + "</td>";
        html += "<td>" + (item.mtrlNm || "") + "</td>";
        html += "<td>" + (item.inboundQuantity || 0) + "</td>";
        html += "<td>" + fnObj.formatDate(item.expectedDate) + "</td>";
        html += "<td>" + fnObj.formatDate(item.saveTime) + "</td>";
        html +=
          '<td><input type="number" class="input-field actual-quantity" min="1" value="' +
          (item.actualQuantity || item.inboundQuantity || 1) +
          '" /></td>';
        html +=
          '<td><input type="date" class="input-field date actual-date" value="' +
          (item.actualDate || new Date().toISOString().split("T")[0]) +
          '" /></td>';
        html +=
          '<td><input type="text" class="input-field remarks" placeholder="비고" value="' +
          (item.remarks || "") +
          '" /></td>';
        html +=
          '<td><button class="complete-btn" onclick="fnObj.completeSingleInbound(' +
          item.tblInboundProductId +
          ')">완료</button></td>';
        html += "</tr>";
      });
    }
    $("#pendingInboundList").html(html);
    fnObj.updateCompleteButton(); // 완료 버튼 상태 업데이트
  },

  // 입고 예정 목록 페이징 렌더링
  renderPagination: function () {
    // 페이징 정보 업데이트
    $("#paginationInfo").text("총 " + fnObj.totalCount + "건");

    // 이전/다음 버튼 상태 업데이트
    $("#prevBtn").prop("disabled", fnObj.currentPage <= 1);
    $("#nextBtn").prop("disabled", fnObj.currentPage >= fnObj.totalPages);

    // 페이지 번호 생성
    var pageNumbersHtml = "";
    var maxVisiblePages = 5;
    var startPage, endPage;

    if (fnObj.totalPages <= maxVisiblePages) {
      // 전체 페이지가 5개 이하면 모두 표시
      startPage = 1;
      endPage = fnObj.totalPages;
    } else {
      // 현재 페이지를 중심으로 앞뒤 2개씩 표시
      var halfVisible = Math.floor(maxVisiblePages / 2);

      if (fnObj.currentPage <= halfVisible + 1) {
        // 앞쪽에 있을 때: 1, 2, 3, 4, 5
        startPage = 1;
        endPage = maxVisiblePages;
      } else if (fnObj.currentPage >= fnObj.totalPages - halfVisible) {
        // 뒤쪽에 있을 때: 마지막 5개
        startPage = fnObj.totalPages - maxVisiblePages + 1;
        endPage = fnObj.totalPages;
      } else {
        // 중간에 있을 때: 현재 페이지 중심으로 앞뒤 2개씩
        startPage = fnObj.currentPage - halfVisible;
        endPage = fnObj.currentPage + halfVisible;
      }
    }

    for (var i = startPage; i <= endPage; i++) {
      var activeClass = i === fnObj.currentPage ? "active" : "";
      pageNumbersHtml +=
        '<button class="page-number ' +
        activeClass +
        '" onclick="fnObj.goToPage(' +
        i +
        ')">' +
        i +
        "</button>";
    }

    $("#pageNumbers").html(pageNumbersHtml);
  },

  // 입고 예정 목록 페이지 이동
  goToPage: function (pageNum) {
    if (pageNum < 1 || pageNum > fnObj.totalPages) {
      return;
    }
    fnObj.loadPendingInboundList(pageNum);
  },

  // 단일 입고 완료 처리
  completeSingleInbound: function (tblInboundProductId) {
    var row = $("tr[data-id='" + tblInboundProductId + "']");
    var actualQuantity = row.find(".actual-quantity").val();
    var actualDate = row.find(".actual-date").val();
    var remarks = row.find(".remarks").val();

    if (!actualQuantity || actualQuantity <= 0) {
      alert("실제 입고 수량을 입력해주세요.");
      return;
    }
    if (!actualDate) {
      alert("실제 입고 날짜를 입력해주세요.");
      return;
    }

    // 재고 업데이트에 필요한 정보를 가져오기 위해 서버에서 데이터 조회
    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#agntCd").val(),
      mtrlCd: $("#productCd").val(),
      mtrlNm: $("#productNm").val(),
      dateFrom: $("#dateFrom").val(),
      dateTo: $("#dateTo").val(),
    };

    // 현재 목록에서 해당 항목의 정보 찾기
    var currentData = null;
    $("#pendingInboundList tr").each(function () {
      if ($(this).data("id") == tblInboundProductId) {
        // 테이블에서 정보 추출
        var cells = $(this).find("td");
        currentData = {
          cmpyCd: $(this).data("cmpy-cd") || sessionCmpyCd,
          agntCd: $(this).data("agnt-cd") || "",
          mtrlCd: cells.eq(3).text().trim(),
          mtrlNm: cells.eq(4).text().trim(),
        };
        return false;
      }
    });

    var completeVO = {
      tblInboundProductId: tblInboundProductId,
      cmpyCd: currentData ? currentData.cmpyCd : sessionCmpyCd,
      agntCd: currentData ? currentData.agntCd : "",
      mtrlCd: currentData ? currentData.mtrlCd : "",
      mtrlNm: currentData ? currentData.mtrlNm : "",
      actualQuantity: parseInt(actualQuantity),
      actualDate: actualDate,
      remarks: remarks || "",
      completeUser: sessionUserId,
    };

    console.log("입고 완료 처리 데이터:", completeVO);

    $.ajax({
      url: "/erp105003CompleteInbound",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(completeVO),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.loadPendingInboundList(fnObj.currentPage); // 목록 새로고침 (현재 페이지 유지)
          // 입고 완료 내역도 새로고침 (현재 입고 완료 내역 탭이 활성화되어 있다면)
          if ($("#historyContent").hasClass("active")) {
            fnObj.loadInboundHistory(1);
          }
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("입고 완료 처리 중 오류가 발생했습니다.");
      },
    });
  },

  // 선택된 입고 완료 처리 (일괄)
  completeSelectedInbound: function () {
    var selectedItems = [];
    $(".inbound-checkbox:checked").each(function () {
      var tblInboundProductId = $(this).val();
      var row = $("tr[data-id='" + tblInboundProductId + "']");
      var actualQuantity = row.find(".actual-quantity").val();
      var actualDate = row.find(".actual-date").val();
      var remarks = row.find(".remarks").val();

      if (!actualQuantity || actualQuantity <= 0) {
        alert("실제 입고 수량을 입력해주세요.");
        return false;
      }
      if (!actualDate) {
        alert("실제 입고 날짜를 입력해주세요.");
        return false;
      }

      // 테이블에서 정보 추출
      var cells = row.find("td");
      var mtrlCd = cells.eq(3).text().trim();
      var mtrlNm = cells.eq(4).text().trim();

      selectedItems.push({
        tblInboundProductId: parseInt(tblInboundProductId),
        cmpyCd: row.data("cmpy-cd") || sessionCmpyCd,
        agntCd: row.data("agnt-cd") || "",
        mtrlCd: mtrlCd,
        mtrlNm: mtrlNm,
        actualQuantity: parseInt(actualQuantity),
        actualDate: actualDate,
        remarks: remarks || "",
        completeUser: sessionUserId,
      });
    });

    if (selectedItems.length === 0) {
      alert("완료 처리할 항목을 선택해주세요.");
      return;
    }

    if (
      !confirm(
        "선택된 " + selectedItems.length + "개의 입고를 완료 처리하시겠습니까?"
      )
    ) {
      return;
    }

    console.log("일괄 입고 완료 처리 데이터:", selectedItems);

    $.ajax({
      url: "/erp105003CompleteMultipleInbound",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedItems),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.loadPendingInboundList(fnObj.currentPage); // 목록 새로고침 (현재 페이지 유지)
          // 입고 완료 내역도 새로고침 (현재 입고 완료 내역 탭이 활성화되어 있다면)
          if ($("#historyContent").hasClass("active")) {
            fnObj.loadInboundHistory(1);
          }
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("입고 완료 처리 중 오류가 발생했습니다.");
      },
    });
  },

  // 화주사 검색 팝업
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
        srchCd: $("#agntCd").val(),
        srchNm: $("#agntNm").val(),
        srchText: $("#agntNm").val(),
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

    prodCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "prodCommSrch";
      windowText = title + " prodSrch";
      windowUrl = "/prodSrchMto";
      windowLeft = 0;
      windowTop = 0;
      windowWidth = 800;
      windowHeight = 600;
      windowsModal = true;
      windowOnClose = function (windowId) {
        return true;
      };
      windowSendObject = {
        windowId: windowId,
        cmpyCd: cmpyCd,
        srchCtgr: srchCtgr,
        srchInput: srchInput,
        srchCd: "",
        srchNm: "",
        srchText: $("#productNm").val(),
        srchArea: $("#agntCd").val(),
        agntCd: $("#agntCd").val(),
        agntNm: $("#agntNm").val(),
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
  },

  // 화주사 코드 삭제
  delAgntCode: function () {
    $("#agntCd").val("");
    $("#agntNm").val("");
  },

  // 상품 코드 삭제
  delProductCode: function () {
    $("#productCd").val("");
    $("#productNm").val("");
  },

  // 화주사명 엔터키 처리
  handleAgntNmKeypress: function (event) {
    if (event.key === "Enter") {
      var agntNm = $("#agntNm").val().trim();
      if (agntNm.length > 0) {
        fnObj.winOpen.commSrch("화주", "A", "AGENCY", "text");
      }
    }
  },

  // 상품명 엔터키 처리
  handleProductNmKeypress: function (event) {
    if (event.key === "Enter") {
      var productNm = $("#productNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.prodCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 전체 선택/해제
  toggleSelectAll: function () {
    var isChecked = $("#selectAllCheckbox").prop("checked");
    $(".inbound-checkbox").prop("checked", isChecked);
    fnObj.updateCompleteButton();
  },

  // 완료 버튼 표시/숨김 업데이트
  updateCompleteButton: function () {
    var checkedCount = $(".inbound-checkbox:checked").length;
    var completeBtn = $("#completeSelectedBtn");

    if (checkedCount > 0) {
      completeBtn.show();
      completeBtn.find("i").removeClass("fa-check").addClass("fa-check-circle");
      completeBtn.html(
        '<i class="fa fa-check-circle"></i> 선택 완료 처리 (' +
          checkedCount +
          ")"
      );
    } else {
      completeBtn.hide();
    }
  },

  // 안전한 날짜 포맷팅 함수
  formatDate: function (dateValue) {
    if (!dateValue) return "";

    // 문자열로 변환
    var dateStr = String(dateValue);

    // 이미 YYYY-MM-DD 형식인 경우
    if (dateStr.match(/^\d{4}-\d{2}-\d{2}$/)) {
      return dateStr;
    }

    // ISO 형식 (2025-01-10T15:30:00)인 경우
    if (dateStr.includes("T")) {
      return dateStr.split("T")[0];
    }

    // 8자리 숫자 형식 (20250110)인 경우
    if (dateStr.match(/^\d{8}$/)) {
      return (
        dateStr.substring(0, 4) +
        "-" +
        dateStr.substring(4, 6) +
        "-" +
        dateStr.substring(6, 8)
      );
    }

    // 배열 형식 [2025, 1, 10]인 경우
    if (Array.isArray(dateValue) && dateValue.length >= 3) {
      var year = dateValue[0];
      var month = String(dateValue[1]).padStart(2, "0");
      var day = String(dateValue[2]).padStart(2, "0");
      return year + "-" + month + "-" + day;
    }

    // Date 객체인 경우
    if (dateValue instanceof Date) {
      return dateValue.toISOString().split("T")[0];
    }

    // 기타 경우 그대로 반환
    return dateStr;
  },

  // 안전한 날짜시간 포맷팅 함수 (YYYY-MM-DD HH:MM:SS)
  formatDateTime: function (dateTimeValue) {
    if (!dateTimeValue) return "";

    // 문자열로 변환
    var dateTimeStr = String(dateTimeValue);

    // ISO 형식 (2025-01-10T15:30:00 또는 2025-01-10T15:30:00.123)인 경우
    if (dateTimeStr.includes("T")) {
      var parts = dateTimeStr.split("T");
      var datePart = parts[0];
      var timePart = parts[1] || "";

      // 시간 부분에서 소수점 제거 (밀리초 제거)
      if (timePart.includes(".")) {
        timePart = timePart.split(".")[0];
      }

      // Z 또는 +로 끝나는 경우 제거
      if (timePart.endsWith("Z")) {
        timePart = timePart.slice(0, -1);
      }
      if (timePart.includes("+")) {
        timePart = timePart.split("+")[0];
      }
      if (
        timePart.includes("-") &&
        timePart.match(/\d{2}:\d{2}:\d{2}-\d{2}:\d{2}/)
      ) {
        timePart = timePart.split("-")[0];
      }

      // 시간 부분이 있으면 결합, 없으면 날짜만 반환
      if (timePart && timePart.match(/^\d{2}:\d{2}:\d{2}$/)) {
        return datePart + " " + timePart;
      } else if (timePart && timePart.match(/^\d{2}:\d{2}$/)) {
        return datePart + " " + timePart + ":00";
      } else {
        return datePart;
      }
    }

    // YYYY-MM-DD HH:MM:SS 형식인 경우
    if (dateTimeStr.match(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}/)) {
      // 소수점이 있으면 제거
      if (dateTimeStr.includes(".")) {
        return dateTimeStr.split(".")[0];
      }
      return dateTimeStr;
    }

    // Date 객체인 경우
    if (dateTimeValue instanceof Date) {
      var year = dateTimeValue.getFullYear();
      var month = String(dateTimeValue.getMonth() + 1).padStart(2, "0");
      var day = String(dateTimeValue.getDate()).padStart(2, "0");
      var hours = String(dateTimeValue.getHours()).padStart(2, "0");
      var minutes = String(dateTimeValue.getMinutes()).padStart(2, "0");
      var seconds = String(dateTimeValue.getSeconds()).padStart(2, "0");
      return (
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds
      );
    }

    // 기타 경우 그대로 반환
    return dateTimeStr;
  },

  // 검색 폼 초기화
  clearSearchForm: function () {
    // 오늘 날짜를 기본값으로 설정
    var today = new Date().toISOString().split("T")[0];
    $("#dateFrom").val(today);
    $("#dateTo").val(today);

    $("#agntCd").val("");
    $("#agntNm").val("");
    $("#productCd").val("");
    $("#productNm").val("");
    $("#pendingInboundList").empty();
    $("#completeSelectedBtn").hide();

    // 체크박스 상태 초기화
    $("#selectAllCheckbox").prop("checked", false);
  },

  // 입고 완료 내역 조회 (페이징)
  loadInboundHistory: function (pageNum = 1) {
    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#historyAgntCd").val(),
      mtrlCd: $("#historyProductCd").val(),
      mtrlNm: $("#historyProductNm").val(),
      dateFrom: $("#historyDateFrom").val(),
      dateTo: $("#historyDateTo").val(),
      pageNum: pageNum,
      pageSize: fnObj.pageSize,
    };

    $.ajax({
      url: "/erp105003InboundHistory",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchVO),
      success: function (res) {
        if (res.success) {
          fnObj.currentPage = res.currentPage;
          fnObj.totalPages = res.totalPages;
          fnObj.totalCount = res.totalCount;
          // 현재 조회된 데이터 저장 (엑셀 다운로드용)
          historyData = res.data;
          fnObj.renderInboundHistory(res.data);
          fnObj.renderHistoryPagination();
        } else {
          alert("입고 완료 내역 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function () {
        alert("입고 완료 내역 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 내역 렌더링
  renderInboundHistory: function (data) {
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="14" style="text-align: center; padding: 20px; color: #666;">조회된 입고 내역이 없습니다.</td></tr>';
    } else {
      var startNumber = (fnObj.currentPage - 1) * fnObj.pageSize + 1;
      data.forEach(function (item, index) {
        var statusClass =
          item.inboundStatus === "COMPLETED"
            ? "status-completed"
            : "status-pending";
        var statusText =
          item.inboundStatus === "COMPLETED" ? "입고 완료" : "입고 예정";

        // 완료된 항목만 체크박스 활성화
        var isCompleted = item.inboundStatus === "COMPLETED";

        html +=
          "<tr data-id='" +
          item.tblInboundProductId +
          "' data-agnt-cd='" +
          (item.agntCd || "") +
          "' data-cmpy-cd='" +
          (item.cmpyCd || "") +
          "' data-mtrl-cd='" +
          (item.mtrlCd || "") +
          "' data-mtrl-nm='" +
          (item.mtrlNm || "") +
          "' data-actual-quantity='" +
          (item.actualQuantity || "") +
          "'>";
        html += "<td>" + (startNumber + index) + "</td>";
        html +=
          '<td><input type="checkbox" class="history-checkbox" value="' +
          item.tblInboundProductId +
          '" onchange="fnObj.updateRevertButton()" ' +
          (isCompleted ? "" : "disabled") +
          " /></td>";
        html += "<td>" + (item.agntNm || "") + "</td>";
        html += "<td>" + (item.mtrlCd || "") + "</td>";
        html += "<td>" + (item.mtrlNm || "") + "</td>";
        html += "<td>" + (item.inboundQuantity || 0) + "</td>";
        html += "<td>" + fnObj.formatDate(item.expectedDate) + "</td>";
        html += "<td>" + fnObj.formatDate(item.saveTime) + "</td>";
        html +=
          '<td><span class="status-badge ' +
          statusClass +
          '">' +
          statusText +
          "</span></td>";
        html += "<td>" + (item.actualQuantity || "") + "</td>";
        html += "<td>" + fnObj.formatDate(item.actualDate) + "</td>";
        html += "<td>" + (item.remarks || "") + "</td>";
        html += "<td>" + (item.completeUser || "") + "</td>";
        html += "<td>" + fnObj.formatDateTime(item.completeTime) + "</td>";
        html += "</tr>";
      });
    }
    $("#inboundHistoryList").html(html);
    fnObj.updateRevertButton(); // 되돌리기 버튼 상태 업데이트
  },

  // 입고 완료 내역 페이징 렌더링
  renderHistoryPagination: function () {
    // 페이징 정보 업데이트
    $("#historyPaginationInfo").text("총 " + fnObj.totalCount + "건");

    // 이전/다음 버튼 상태 업데이트
    $("#historyPrevBtn").prop("disabled", fnObj.currentPage <= 1);
    $("#historyNextBtn").prop(
      "disabled",
      fnObj.currentPage >= fnObj.totalPages
    );

    // 페이지 번호 생성
    var pageNumbersHtml = "";
    var maxVisiblePages = 5;
    var startPage, endPage;

    if (fnObj.totalPages <= maxVisiblePages) {
      // 전체 페이지가 5개 이하면 모두 표시
      startPage = 1;
      endPage = fnObj.totalPages;
    } else {
      // 현재 페이지를 중심으로 앞뒤 2개씩 표시
      var halfVisible = Math.floor(maxVisiblePages / 2);

      if (fnObj.currentPage <= halfVisible + 1) {
        // 앞쪽에 있을 때: 1, 2, 3, 4, 5
        startPage = 1;
        endPage = maxVisiblePages;
      } else if (fnObj.currentPage >= fnObj.totalPages - halfVisible) {
        // 뒤쪽에 있을 때: 마지막 5개
        startPage = fnObj.totalPages - maxVisiblePages + 1;
        endPage = fnObj.totalPages;
      } else {
        // 중간에 있을 때: 현재 페이지 중심으로 앞뒤 2개씩
        startPage = fnObj.currentPage - halfVisible;
        endPage = fnObj.currentPage + halfVisible;
      }
    }

    for (var i = startPage; i <= endPage; i++) {
      var activeClass = i === fnObj.currentPage ? "active" : "";
      pageNumbersHtml +=
        '<button class="page-number ' +
        activeClass +
        '" onclick="fnObj.goToHistoryPage(' +
        i +
        ')">' +
        i +
        "</button>";
    }

    $("#historyPageNumbers").html(pageNumbersHtml);
  },

  // 입고 완료 내역 페이지 이동
  goToHistoryPage: function (pageNum) {
    if (pageNum < 1 || pageNum > fnObj.totalPages) {
      return;
    }
    fnObj.loadInboundHistory(pageNum);
  },

  // 입고 내역 검색 폼 초기화
  clearHistorySearchForm: function () {
    // 오늘 날짜를 기본값으로 설정
    var today = new Date().toISOString().split("T")[0];
    $("#historyDateFrom").val(today);
    $("#historyDateTo").val(today);

    $("#historyAgntCd").val("");
    $("#historyAgntNm").val("");
    $("#historyProductCd").val("");
    $("#historyProductNm").val("");
    $("#inboundHistoryList").empty();
  },

  // 화주사명 엔터키 처리 (입고 내역)
  handleHistoryAgntNmKeypress: function (event) {
    if (event.key === "Enter") {
      var agntNm = $("#historyAgntNm").val().trim();
      if (agntNm.length > 0) {
        fnObj.winOpen.historyCommSrch("화주", "A", "AGENCY", "text");
      }
    }
  },

  // 상품명 엔터키 처리 (입고 내역)
  handleHistoryProductNmKeypress: function (event) {
    if (event.key === "Enter") {
      var productNm = $("#historyProductNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.historyProdCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 화주사 코드 삭제 (입고 내역)
  delHistoryAgntCode: function () {
    $("#historyAgntCd").val("");
    $("#historyAgntNm").val("");
  },

  // 상품 코드 삭제 (입고 내역)
  delHistoryProductCode: function () {
    $("#historyProductCd").val("");
    $("#historyProductNm").val("");
  },

  // 팝업 검색
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
        srchCd: $("#agntCd").val(),
        srchNm: $("#agntNm").val(),
        srchText: $("#agntNm").val(),
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

    prodCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "prodCommSrch";
      windowText = title + " prodSrch";
      windowUrl = "/prodSrchMto";
      windowLeft = 0;
      windowTop = 0;
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
        srchCd: $("#productCd").val(),
        srchNm: $("#productNm").val(),
        srchText: $("#productNm").val(),
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

    historyCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "historyCommSrch";
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
        srchCd: $("#historyAgntCd").val(),
        srchNm: $("#historyAgntNm").val(),
        srchText: $("#historyAgntNm").val(),
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

    historyProdCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "historyProdCommSrch";
      windowText = title + " prodSrch";
      windowUrl = "/prodSrchMto";
      windowLeft = 0;
      windowTop = 0;
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
        srchCd: $("#historyProductCd").val(),
        srchNm: $("#historyProductNm").val(),
        srchText: $("#historyProductNm").val(),
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
      if (windowId == "commSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#agntCd").val(rtnObj[0].commCd);
          $("#agntNm").val(rtnObj[0].commNm);
        }
      }
      if (windowId == "prodCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#productCd").val(rtnObj[0].commCd);
          $("#productNm").val(rtnObj[0].commNm);
        }
      }
      if (windowId == "historyCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#historyAgntCd").val(rtnObj[0].commCd);
          $("#historyAgntNm").val(rtnObj[0].commNm);
        }
      }
      if (windowId == "historyProdCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#historyProductCd").val(rtnObj[0].commCd);
          $("#historyProductNm").val(rtnObj[0].commNm);
        }
      }
    },
  },

  // 전체 선택/해제 (입고 완료 내역)
  toggleSelectAllHistory: function () {
    var isChecked = $("#selectAllHistoryCheckbox").prop("checked");
    $(".history-checkbox:not(:disabled)").prop("checked", isChecked);
    fnObj.updateRevertButton();
  },

  // 되돌리기 버튼 표시/숨김 업데이트
  updateRevertButton: function () {
    var checkedCount = $(".history-checkbox:checked").length;
    var revertBtn = $("#revertSelectedBtn");

    if (checkedCount > 0) {
      revertBtn.show();
      revertBtn.html(
        '<i class="fa fa-undo"></i> 선택 항목 되돌리기 (' + checkedCount + ")"
      );
    } else {
      revertBtn.hide();
    }
  },

  // 선택된 입고 되돌리기 (일괄)
  revertSelectedInbound: function () {
    var selectedItems = [];
    $(".history-checkbox:checked").each(function () {
      var tblInboundProductId = $(this).val();
      var row = $(
        "#inboundHistoryList tr[data-id='" + tblInboundProductId + "']"
      );

      selectedItems.push({
        tblInboundProductId: parseInt(tblInboundProductId),
        cmpyCd: row.data("cmpy-cd") || sessionCmpyCd,
        agntCd: row.data("agnt-cd") || "",
        mtrlCd: row.data("mtrl-cd") || "",
        mtrlNm: row.data("mtrl-nm") || "",
        actualQuantity: parseInt(row.data("actual-quantity") || 0),
        revertUser: sessionUserId,
      });
    });

    if (selectedItems.length === 0) {
      alert("되돌릴 항목을 선택해주세요.");
      return;
    }

    if (
      !confirm(
        "선택된 " +
          selectedItems.length +
          "개의 입고 완료를 되돌리시겠습니까?\n\n재고가 차감되고 입고 예정 상태로 변경됩니다."
      )
    ) {
      return;
    }

    console.log("입고 되돌리기 데이터:", selectedItems);

    $.ajax({
      url: "/erp105003RevertInbound",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedItems),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          // 체크박스 초기화
          $("#selectAllHistoryCheckbox").prop("checked", false);
          // 입고 완료 내역 새로고침
          fnObj.loadInboundHistory(fnObj.currentPage);
          // 입고 예정 목록도 새로고침 (되돌려진 항목이 나타나도록)
          if ($("#pendingContent").hasClass("active")) {
            fnObj.loadPendingInboundList(1);
          }
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("입고 되돌리기 처리 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 예정 목록 엑셀 다운로드
  downloadPendingInboundExcel: function () {
    if (pendingData.length === 0) {
      alert("엑셀 다운로드할 데이터가 없습니다.\n조회해주세요.");
      return;
    }

    // 전체 데이터 조회 (페이징 없이)
    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#agntCd").val(),
      mtrlCd: $("#productCd").val(),
      mtrlNm: $("#productNm").val(),
      dateFrom: $("#dateFrom").val(),
      dateTo: $("#dateTo").val(),
      pageNum: 1,
      pageSize: 999999, // 전체 데이터 조회
    };

    $.ajax({
      url: "/erp105003PendingList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchVO),
      success: function (res) {
        if (res.success && res.data && res.data.length > 0) {
          fnObj.exportPendingInboundToExcel(res.data);
        } else {
          alert("엑셀 다운로드할 데이터가 없습니다.");
        }
      },
      error: function () {
        alert("엑셀 다운로드 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 예정 목록 엑셀 파일 생성 및 다운로드
  exportPendingInboundToExcel: function (data) {
    var excelData = [];

    // 제목행 추가
    excelData.push(["입고 예정 목록"]);
    excelData.push([""]); // 빈 행

    // 헤더 설정
    var headers = [
      "화주사명",
      "상품코드",
      "상품명",
      "예정수량",
      "예정일",
      "등록일",
    ];
    excelData.push(headers);

    // 데이터 행 추가
    for (var i = 0; i < data.length; i++) {
      var item = data[i];
      var excelRow = [
        item.agntNm || "",
        item.mtrlCd || "",
        item.mtrlNm || "",
        item.inboundQuantity || 0,
        fnObj.formatDate(item.expectedDate) || "",
        fnObj.formatDate(item.saveTime) || "",
      ];
      excelData.push(excelRow);
    }

    // 엑셀 파일 생성 및 다운로드
    var wb = XLSX.utils.book_new();
    var ws = XLSX.utils.aoa_to_sheet(excelData);

    // 컬럼 너비 설정
    ws["!cols"] = [
      { wch: 15 }, // 화주사명
      { wch: 15 }, // 상품코드
      { wch: 30 }, // 상품명
      { wch: 12 }, // 예정수량
      { wch: 12 }, // 예정일
      { wch: 12 }, // 등록일
    ];

    XLSX.utils.book_append_sheet(wb, ws, "입고예정목록");

    var fileName =
      "입고예정목록_" + new Date().toISOString().split("T")[0] + ".xlsx";
    XLSX.writeFile(wb, fileName);
  },

  // 입고 완료 내역 엑셀 다운로드
  downloadInboundHistoryExcel: function () {
    if (historyData.length === 0) {
      alert("엑셀 다운로드할 데이터가 없습니다.\n조회해주세요.");
      return;
    }

    // 전체 데이터 조회 (페이징 없이)
    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#historyAgntCd").val(),
      mtrlCd: $("#historyProductCd").val(),
      mtrlNm: $("#historyProductNm").val(),
      dateFrom: $("#historyDateFrom").val(),
      dateTo: $("#historyDateTo").val(),
      pageNum: 1,
      pageSize: 999999, // 전체 데이터 조회
    };

    $.ajax({
      url: "/erp105003InboundHistory",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchVO),
      success: function (res) {
        if (res.success && res.data && res.data.length > 0) {
          fnObj.exportInboundHistoryToExcel(res.data);
        } else {
          alert("엑셀 다운로드할 데이터가 없습니다.");
        }
      },
      error: function () {
        alert("엑셀 다운로드 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 완료 내역 엑셀 파일 생성 및 다운로드
  exportInboundHistoryToExcel: function (data) {
    var excelData = [];

    // 제목행 추가
    excelData.push(["입고 완료 내역"]);
    excelData.push([""]); // 빈 행

    // 헤더 설정
    var headers = [
      "화주사명",
      "상품코드",
      "상품명",
      "예정수량",
      "예정일",
      "등록일",
      "상태",
      "실제수량",
      "실제일자",
      "비고",
      "완료자",
      "완료일시",
    ];
    excelData.push(headers);

    // 데이터 행 추가
    for (var i = 0; i < data.length; i++) {
      var item = data[i];
      var statusText =
        item.inboundStatus === "COMPLETED" ? "입고 완료" : "입고 예정";

      var excelRow = [
        item.agntNm || "",
        item.mtrlCd || "",
        item.mtrlNm || "",
        item.inboundQuantity || 0,
        fnObj.formatDate(item.expectedDate) || "",
        fnObj.formatDate(item.saveTime) || "",
        statusText,
        item.actualQuantity || "",
        fnObj.formatDate(item.actualDate) || "",
        item.remarks || "",
        item.completeUser || "",
        fnObj.formatDateTime(item.completeTime) || "",
      ];
      excelData.push(excelRow);
    }

    // 엑셀 파일 생성 및 다운로드
    var wb = XLSX.utils.book_new();
    var ws = XLSX.utils.aoa_to_sheet(excelData);

    // 컬럼 너비 설정
    ws["!cols"] = [
      { wch: 15 }, // 화주사명
      { wch: 15 }, // 상품코드
      { wch: 30 }, // 상품명
      { wch: 12 }, // 예정수량
      { wch: 12 }, // 예정일
      { wch: 12 }, // 등록일
      { wch: 12 }, // 상태
      { wch: 12 }, // 실제수량
      { wch: 12 }, // 실제일자
      { wch: 20 }, // 비고
      { wch: 15 }, // 완료자
      { wch: 20 }, // 완료일시
    ];

    XLSX.utils.book_append_sheet(wb, ws, "입고완료내역");

    var fileName =
      "입고완료내역_" + new Date().toISOString().split("T")[0] + ".xlsx";
    XLSX.writeFile(wb, fileName);
  },
};

// 페이지 초기화
$(document).ready(function () {
  myWins = new dhtmlXWindows();

  // 오늘 날짜를 기본값으로 설정
  var today = new Date().toISOString().split("T")[0];
  $("#dateFrom").val(today);
  $("#dateTo").val(today);
  $("#historyDateFrom").val(today);
  $("#historyDateTo").val(today);

  // 페이지 로드 시 입고 예정 목록 조회
  fnObj.loadPendingInboundList();
});

// 세션 변수 초기화 함수 (Thymeleaf에서 호출)
function initializeSessionVariables(cmpyCd, userId) {
  sessionCmpyCd = cmpyCd;
  sessionUserId = userId;
}
