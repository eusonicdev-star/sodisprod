/**
 * 재고 조정 (erp105004) JavaScript
 */

// 전역 변수
var pagePerRow = 10;
var myWins;
var myGrid;
var myComboCmpyCd;
var currentTab = "adjustment";

// 세션 변수 (Thymeleaf에서 주입됨)
var sessionCmpyCd = "A";
var sessionUserId = "admin";

// 메인 함수 객체
var fnObj = {
  // 탭 전환
  tabOpen: {
    showAdjustmentTab: function () {
      currentTab = "adjustment";
      $(".tab-button").removeClass("active");
      $("#adjustmentTab").addClass("active");
      $(".tab-content").removeClass("active");
      $("#adjustmentContent").addClass("active");
    },
    showHistoryTab: function () {
      currentTab = "history";
      $(".tab-button").removeClass("active");
      $("#historyTab").addClass("active");
      $(".tab-content").removeClass("active");
      $("#historyContent").addClass("active");
      fnObj.loadAdjustmentHistory();
    },
    showInventoryTab: function () {
      currentTab = "inventory";
      $(".tab-button").removeClass("active");
      $("#inventoryTab").addClass("active");
      $(".tab-content").removeClass("active");
      $("#inventoryContent").addClass("active");
      fnObj.loadInventoryStatus();
    },
  },

  // 재고 조정 등록
  registerAdjustment: function () {
    var agntCd = $("#agntCd").val();
    var agntNm = $("#agntNm").val();
    var productCd = $("#selectedProductCd").val();
    var productNm = $("#selectedProductNm").val();
    var productId = $("#selectedProductId").val();
    var currentQty = parseInt($("#currentQty").val()) || 0;
    var adjustmentType = $("#adjustmentType").val();
    var adjustmentQty = parseInt($("#adjustmentQty").val()) || 0;
    var afterQty = parseInt($("#afterQty").val()) || 0;
    var adjustmentReason = $("#adjustmentReason").val();
    var adjustmentDate = $("#adjustmentDate").val();

    // 유효성 검사
    if (!agntCd || !agntNm) {
      alert("화주사를 선택해주세요.");
      return;
    }
    if (!productCd || !productNm) {
      alert("상품을 선택해주세요.");
      return;
    }
    if (!adjustmentType) {
      alert("조정 유형을 선택해주세요.");
      return;
    }
    if (!adjustmentQty || adjustmentQty <= 0) {
      alert("조정 수량을 입력해주세요.");
      return;
    }
    if (!adjustmentReason) {
      alert("조정 사유를 입력해주세요.");
      return;
    }
    if (!adjustmentDate) {
      alert("조정 일자를 선택해주세요.");
      return;
    }

    var adjustmentVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: agntCd,
      agntNm: agntNm,
      tblMtrlMId: productId,
      mtrlCd: productCd,
      mtrlNm: productNm,
      adjustmentType: adjustmentType,
      beforeQty: currentQty,
      adjustmentQty: adjustmentQty,
      afterQty: afterQty,
      adjustmentReason: adjustmentReason,
      adjustmentDate: adjustmentDate,
      saveUser: sessionUserId,
      updtUser: sessionUserId,
    };

    $.ajax({
      url: "/erp105004InsertAdjustment",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(adjustmentVO),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.clearAdjustmentForm();
          // 재고 현황 탭으로 이동하여 업데이트된 재고 확인
          fnObj.tabOpen.showInventoryTab();
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("재고 조정 중 오류가 발생했습니다.");
      },
    });
  },

  // 조정 폼 초기화
  clearAdjustmentForm: function () {
    $("#agntCd").val("");
    $("#agntNm").val("");
    $("#selectedProductCd").val("");
    $("#selectedProductNm").val("");
    $("#selectedProductId").val("");
    $("#currentQty").val("");
    $("#adjustmentType").val("");
    $("#adjustmentQty").val("");
    $("#afterQty").val("");
    $("#adjustmentReason").val("");
    $("#adjustmentDate").val("");
    $("#adjustmentQty").prop("disabled", true);
  },

  // 현재 재고 조회
  loadCurrentInventory: function () {
    var agntCd = $("#agntCd").val();
    var productCd = $("#selectedProductCd").val();

    if (!agntCd || !productCd) {
      $("#currentQty").val("");
      return;
    }

    var searchVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: agntCd,
      mtrlCd: productCd,
    };

    $.ajax({
      url: "/erp105004GetCurrentInventory",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchVO),
      success: function (res) {
        if (res.success && res.data) {
          $("#currentQty").val(res.data.currentQty || 0);
        } else {
          $("#currentQty").val(0);
        }
      },
      error: function () {
        $("#currentQty").val(0);
      },
    });
  },

  // 조정 수량 계산
  calculateAfterQty: function () {
    var currentQty = parseInt($("#currentQty").val()) || 0;
    var adjustmentType = $("#adjustmentType").val();
    var adjustmentQty = parseInt($("#adjustmentQty").val()) || 0;

    if (adjustmentType && adjustmentQty > 0) {
      var afterQty = 0;
      if (adjustmentType === "INCREASE") {
        afterQty = currentQty + adjustmentQty;
      } else if (adjustmentType === "DECREASE") {
        afterQty = currentQty - adjustmentQty;
        if (afterQty < 0) {
          alert("조정 후 수량이 음수가 될 수 없습니다.");
          $("#adjustmentQty").val("");
          $("#afterQty").val("");
          return;
        }
      }
      $("#afterQty").val(afterQty);
    } else {
      $("#afterQty").val("");
    }
  },

  // 페이징 관련 변수
  currentPage: 1,
  totalPages: 0,
  totalCount: 0,
  pageSize: 10,

  // 재고 현황 페이징 변수
  inventoryCurrentPage: 1,
  inventoryTotalPages: 0,
  inventoryTotalCount: 0,

  // 조정 내역 조회 (페이징)
  loadAdjustmentHistory: function (pageNum = 1) {
    var adjustmentVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#historyAgntCd").val(),
      mtrlCd: $("#historyProductCd").val(),
      mtrlNm: $("#historyProductNm").val(),
      adjustmentType: $("#historyAdjustmentType").val(),
      dateFrom: $("#historyDateFrom").val(),
      dateTo: $("#historyDateTo").val(),
      pageNum: pageNum,
      pageSize: fnObj.pageSize,
    };

    $.ajax({
      url: "/erp105004AdjustmentList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(adjustmentVO),
      success: function (res) {
        if (res.success) {
          fnObj.currentPage = res.currentPage;
          fnObj.totalPages = res.totalPages;
          fnObj.totalCount = res.totalCount;
          fnObj.renderAdjustmentHistory(res.data);
          fnObj.renderPagination();
        } else {
          alert("조정 내역 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function (xhr, status, error) {
        console.error("조정 내역 조회 오류:", error);
        alert("조정 내역 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 조정 내역 렌더링
  renderAdjustmentHistory: function (data) {
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="11" style="text-align: center; padding: 20px; color: #666;">조회된 조정 내역이 없습니다.</td></tr>';
    } else {
      data.forEach(function (item) {
        var typeClass =
          item.adjustmentType === "INCREASE"
            ? "adjustment-increase"
            : "adjustment-decrease";
        var typeText = item.adjustmentType === "INCREASE" ? "증가" : "감소";

        html += "<tr>";
        html += "<td>" + (item.agntNm || "") + "</td>";
        html += "<td>" + (item.mtrlCd || "") + "</td>";
        html += "<td>" + (item.mtrlNm || "") + "</td>";
        html += "<td>" + (item.beforeQty || 0) + "</td>";
        html +=
          '<td><span class="adjustment-badge ' +
          typeClass +
          '">' +
          typeText +
          "</span></td>";
        html += "<td>" + (item.adjustmentQty || 0) + "</td>";
        html += "<td>" + (item.afterQty || 0) + "</td>";
        html += "<td>" + (item.adjustmentReason || "") + "</td>";
        html += "<td>" + fnObj.formatDate(item.adjustmentDate) + "</td>";
        html += "<td>" + fnObj.formatDate(item.saveTime) + "</td>";
        html += "<td>" + (item.saveUser || "") + "</td>";
        html += "</tr>";
      });
    }
    $("#adjustmentHistoryList").html(html);
  },

  // 재고 현황 조회 (페이징)
  loadInventoryStatus: function (pageNum = 1) {
    var inventoryVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#inventoryAgntCd").val(),
      mtrlCd: $("#inventoryProductCd").val(),
      mtrlNm: $("#inventoryProductNm").val(),
      pageNum: pageNum,
      pageSize: fnObj.pageSize,
    };

    $.ajax({
      url: "/erp105004InventoryList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(inventoryVO),
      success: function (res) {
        if (res.success) {
          fnObj.inventoryCurrentPage = res.currentPage;
          fnObj.inventoryTotalPages = res.totalPages;
          fnObj.inventoryTotalCount = res.totalCount;
          fnObj.renderInventoryStatus(res.data);
          fnObj.renderInventoryPagination();
        } else {
          alert("재고 현황 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function (xhr, status, error) {
        console.error("재고 현황 조회 오류:", error);
        alert("재고 현황 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 재고 현황 렌더링
  renderInventoryStatus: function (data) {
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="6" style="text-align: center; padding: 20px; color: #666;">조회된 재고 현황이 없습니다.</td></tr>';
    } else {
      data.forEach(function (item) {
        html +=
          "<tr data-agnt-cd='" +
          (item.agntCd || "") +
          "' data-mtrl-cd='" +
          (item.mtrlCd || "") +
          "' data-mtrl-nm='" +
          (item.mtrlNm || "") +
          "' data-current-qty='" +
          (item.currentQty || 0) +
          "'>";
        html += "<td>" + (item.agntNm || "") + "</td>";
        html += "<td>" + (item.mtrlCd || "") + "</td>";
        html += "<td>" + (item.mtrlNm || "") + "</td>";
        html += "<td>" + (item.currentQty || 0) + "</td>";
        html +=
          "<td>" + fnObj.formatDate(item.updtTime || item.saveTime) + "</td>";
        html += "<td>" + (item.updtUser || item.saveUser || "") + "</td>";
        html += "</tr>";
      });
    }
    $("#inventoryStatusList").html(html);

    // 더블클릭 이벤트 추가
    $("#inventoryStatusList tr")
      .off("dblclick")
      .on("dblclick", function () {
        var agntCd = $(this).data("agnt-cd");
        var mtrlCd = $(this).data("mtrl-cd");
        var mtrlNm = $(this).data("mtrl-nm");
        var currentQty = $(this).data("current-qty");
        var agntNm = $(this).find("td:first").text();

        fnObj.showInventoryHistoryModal(
          agntCd,
          agntNm,
          mtrlCd,
          mtrlNm,
          currentQty
        );
      });
  },

  // 페이징 렌더링
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
      startPage = 1;
      endPage = fnObj.totalPages;
    } else {
      var halfVisible = Math.floor(maxVisiblePages / 2);

      if (fnObj.currentPage <= halfVisible + 1) {
        startPage = 1;
        endPage = maxVisiblePages;
      } else if (fnObj.currentPage >= fnObj.totalPages - halfVisible) {
        startPage = fnObj.totalPages - maxVisiblePages + 1;
        endPage = fnObj.totalPages;
      } else {
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

  // 재고 현황 페이징 렌더링
  renderInventoryPagination: function () {
    // 페이징 정보 업데이트
    $("#inventoryPaginationInfo").text(
      "총 " + fnObj.inventoryTotalCount + "건"
    );

    // 이전/다음 버튼 상태 업데이트
    $("#inventoryPrevBtn").prop("disabled", fnObj.inventoryCurrentPage <= 1);
    $("#inventoryNextBtn").prop(
      "disabled",
      fnObj.inventoryCurrentPage >= fnObj.inventoryTotalPages
    );

    // 페이지 번호 생성
    var pageNumbersHtml = "";
    var maxVisiblePages = 5;
    var startPage, endPage;

    if (fnObj.inventoryTotalPages <= maxVisiblePages) {
      startPage = 1;
      endPage = fnObj.inventoryTotalPages;
    } else {
      var halfVisible = Math.floor(maxVisiblePages / 2);

      if (fnObj.inventoryCurrentPage <= halfVisible + 1) {
        startPage = 1;
        endPage = maxVisiblePages;
      } else if (
        fnObj.inventoryCurrentPage >=
        fnObj.inventoryTotalPages - halfVisible
      ) {
        startPage = fnObj.inventoryTotalPages - maxVisiblePages + 1;
        endPage = fnObj.inventoryTotalPages;
      } else {
        startPage = fnObj.inventoryCurrentPage - halfVisible;
        endPage = fnObj.inventoryCurrentPage + halfVisible;
      }
    }

    for (var i = startPage; i <= endPage; i++) {
      var activeClass = i === fnObj.inventoryCurrentPage ? "active" : "";
      pageNumbersHtml +=
        '<button class="page-number ' +
        activeClass +
        '" onclick="fnObj.goToInventoryPage(' +
        i +
        ')">' +
        i +
        "</button>";
    }

    $("#inventoryPageNumbers").html(pageNumbersHtml);
  },

  // 페이지 이동
  goToPage: function (pageNum) {
    if (pageNum < 1 || pageNum > fnObj.totalPages) {
      return;
    }
    fnObj.loadAdjustmentHistory(pageNum);
  },

  // 재고 현황 페이지 이동
  goToInventoryPage: function (pageNum) {
    if (pageNum < 1 || pageNum > fnObj.inventoryTotalPages) {
      return;
    }
    fnObj.loadInventoryStatus(pageNum);
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
      windowWidth = 1000;
      windowHeight = 700;
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
        srchText: $("#selectedProductNm").val(),
        srchArea: $("#agntCd").val(),
        agntCd: $("#agntCd").val(),
        agntNm: $("#agntNm").val(),
      };

      var errMsg = "";
      var rtnVal = true;
      if (!$("#agntCd").val()) {
        errMsg = errMsg + "화주코드가 없습니다. 화주를 검색해주세요.\n";
        rtnVal = false;
      }
      if (!$("#agntNm").val()) {
        errMsg = errMsg + "화주명칭이 없습니다. 화주를 검색해주세요.\n";
        rtnVal = false;
      }
      if (errMsg != "" && rtnVal == false) {
        alert(errMsg);
        return;
      }
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
        windowId: windowId,
        cmpyCd: cmpyCd,
        srchCtgr: srchCtgr,
        srchInput: srchInput,
        srchCd: "",
        srchNm: "",
        srchText: $("#historyProductNm").val(),
        srchArea: $("#historyAgntCd").val(),
        agntCd: $("#historyAgntCd").val(),
        agntNm: $("#historyAgntNm").val(),
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

    inventoryCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "inventoryCommSrch";
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
        srchCd: $("#inventoryAgntCd").val(),
        srchNm: $("#inventoryAgntNm").val(),
        srchText: $("#inventoryAgntNm").val(),
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

    inventoryProdCommSrch: function (title, cmpyCd, srchCtgr, srchInput) {
      windowId = "inventoryProdCommSrch";
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
        srchText: $("#inventoryProductNm").val(),
        srchArea: $("#inventoryAgntCd").val(),
        agntCd: $("#inventoryAgntCd").val(),
        agntNm: $("#inventoryAgntNm").val(),
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
          // 화주사가 선택되면 현재 재고 조회
          fnObj.loadCurrentInventory();
        }
      }
      if (windowId == "prodCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#selectedProductCd").val(rtnObj[0].commCd);
          $("#selectedProductNm").val(rtnObj[0].commNm);
          $("#selectedProductId").val(rtnObj[0].tblMtrlMId);
          // 상품이 선택되면 현재 재고 조회
          fnObj.loadCurrentInventory();
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
      if (windowId == "inventoryCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#inventoryAgntCd").val(rtnObj[0].commCd);
          $("#inventoryAgntNm").val(rtnObj[0].commNm);
        }
      }
      if (windowId == "inventoryProdCommSrch") {
        if (rtnObj == null) {
          myWins.window(windowId).setPosition(0, 0);
          return;
        }

        if (srchInput == "text") {
          $("#inventoryProductCd").val(rtnObj[0].commCd);
          $("#inventoryProductNm").val(rtnObj[0].commNm);
        }
      }
    },
  },

  // 화주사 코드 삭제
  delAgntCode: function () {
    $("#agntCd").val("");
    $("#agntNm").val("");
    $("#currentQty").val("");
  },

  // 상품 코드 삭제
  delProductCode: function () {
    $("#selectedProductCd").val("");
    $("#selectedProductNm").val("");
    $("#selectedProductId").val("");
    $("#currentQty").val("");
    $("#adjustmentQty").val("");
    $("#afterQty").val("");
    $("#adjustmentQty").prop("disabled", true);
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
      var productNm = $("#selectedProductNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.prodCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 조정 내역 화주사명 엔터키 처리
  handleHistoryAgntNmKeypress: function (event) {
    if (event.key === "Enter") {
      var agntNm = $("#historyAgntNm").val().trim();
      if (agntNm.length > 0) {
        fnObj.winOpen.historyCommSrch("화주", "A", "AGENCY", "text");
      }
    }
  },

  // 조정 내역 상품명 엔터키 처리
  handleHistoryProductNmKeypress: function (event) {
    if (event.key === "Enter") {
      var productNm = $("#historyProductNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.historyProdCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 재고 현황 화주사명 엔터키 처리
  handleInventoryAgntNmKeypress: function (event) {
    if (event.key === "Enter") {
      var agntNm = $("#inventoryAgntNm").val().trim();
      if (agntNm.length > 0) {
        fnObj.winOpen.inventoryCommSrch("화주", "A", "AGENCY", "text");
      }
    }
  },

  // 재고 현황 상품명 엔터키 처리
  handleInventoryProductNmKeypress: function (event) {
    if (event.key === "Enter") {
      var productNm = $("#inventoryProductNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.inventoryProdCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 조정 내역 화주사 코드 삭제
  delHistoryAgntCode: function () {
    $("#historyAgntCd").val("");
    $("#historyAgntNm").val("");
  },

  // 조정 내역 상품 코드 삭제
  delHistoryProductCode: function () {
    $("#historyProductCd").val("");
    $("#historyProductNm").val("");
  },

  // 재고 현황 화주사 코드 삭제
  delInventoryAgntCode: function () {
    $("#inventoryAgntCd").val("");
    $("#inventoryAgntNm").val("");
  },

  // 재고 현황 상품 코드 삭제
  delInventoryProductCode: function () {
    $("#inventoryProductCd").val("");
    $("#inventoryProductNm").val("");
  },

  // 재고 변동 이력 모달 표시
  showInventoryHistoryModal: function (
    agntCd,
    agntNm,
    mtrlCd,
    mtrlNm,
    currentQty
  ) {
    // 모달 정보 설정
    $("#modalAgntNm").text(agntNm);
    $("#modalMtrlCd").text(mtrlCd);
    $("#modalMtrlNm").text(mtrlNm);
    $("#modalCurrentQty").text(currentQty);

    // 모달 제목 설정
    $("#inventoryHistoryTitle").text(
      "재고 변동 이력 - " + mtrlNm + " (" + mtrlCd + ")"
    );

    // 재고 변동 이력 조회
    var inventoryVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: agntCd,
      mtrlCd: mtrlCd,
    };

    $.ajax({
      url: "/erp105004GetInventoryHistory",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(inventoryVO),
      success: function (res) {
        if (res.success) {
          fnObj.renderInventoryHistory(res.data);
          $("#inventoryHistoryModal").show();
        } else {
          alert("재고 변동 이력 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function (xhr, status, error) {
        console.error("재고 변동 이력 조회 오류:", error);
        alert("재고 변동 이력 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 재고 변동 이력 렌더링
  renderInventoryHistory: function (data) {
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="7" style="text-align: center; padding: 20px; color: #666;">재고 변동 이력이 없습니다.</td></tr>';
    } else {
      data.forEach(function (item) {
        var typeClass =
          item.adjustmentType === "INCREASE"
            ? "adjustment-increase"
            : "adjustment-decrease";
        var typeText = item.adjustmentType === "INCREASE" ? "증가" : "감소";

        html += "<tr>";
        html += "<td>" + fnObj.formatDateTime(item.saveTime) + "</td>";
        html +=
          '<td><span class="adjustment-badge ' +
          typeClass +
          '">' +
          typeText +
          "</span></td>";
        html += "<td>" + (item.beforeQty || 0) + "</td>";
        html += "<td>" + (item.adjustmentQty || 0) + "</td>";
        html += "<td>" + (item.afterQty || 0) + "</td>";
        html += "<td>" + (item.adjustmentReason || "") + "</td>";
        html += "<td>" + (item.saveUser || "") + "</td>";
        html += "</tr>";
      });
    }
    $("#inventoryHistoryList").html(html);
  },

  // 재고 변동 이력 모달 닫기
  closeInventoryHistoryModal: function () {
    $("#inventoryHistoryModal").hide();
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

  // 날짜시간 포맷팅 함수 (YYYY-MM-DD HH:mm:ss)
  formatDateTime: function (dateValue) {
    if (!dateValue) return "";

    // 배열 형식 [2025, 9, 22, 9, 38, 32, 633000000]인 경우
    if (Array.isArray(dateValue) && dateValue.length >= 6) {
      var year = dateValue[0];
      var month = String(dateValue[1]).padStart(2, "0");
      var day = String(dateValue[2]).padStart(2, "0");
      var hour = String(dateValue[3]).padStart(2, "0");
      var minute = String(dateValue[4]).padStart(2, "0");
      var second = String(dateValue[5]).padStart(2, "0");
      return (
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hour +
        ":" +
        minute +
        ":" +
        second
      );
    }

    // 문자열로 변환
    var dateStr = String(dateValue);

    // ISO 형식 (2025-01-10T15:30:00)인 경우
    if (dateStr.includes("T")) {
      return dateStr.replace("T", " ").substring(0, 19);
    }

    // Date 객체인 경우
    if (dateValue instanceof Date) {
      return dateValue.toISOString().replace("T", " ").substring(0, 19);
    }

    // 기타 경우 그대로 반환
    return dateStr;
  },
};

// 페이지 초기화
$(document).ready(function () {
  myWins = new dhtmlXWindows();
  fnObj.tabOpen.showAdjustmentTab();

  // 오늘 날짜를 기본값으로 설정
  var today = new Date().toISOString().split("T")[0];
  $('input[type="date"]').val(today);

  // 조정 내역 조회 시 기본값으로 오늘 날짜 설정
  $("#historyDateFrom").val(today);
  $("#historyDateTo").val(today);

  // 조정 유형 변경 시 조정 수량 입력 필드 활성화/비활성화
  $("#adjustmentType").change(function () {
    if ($(this).val()) {
      $("#adjustmentQty").prop("disabled", false);
    } else {
      $("#adjustmentQty").prop("disabled", true);
      $("#adjustmentQty").val("");
      $("#afterQty").val("");
    }
  });

  // 조정 수량 변경 시 조정 후 수량 계산
  $("#adjustmentQty").on("input", function () {
    fnObj.calculateAfterQty();
  });

  // 조정 유형 변경 시 조정 후 수량 계산
  $("#adjustmentType").change(function () {
    fnObj.calculateAfterQty();
  });
});

// 세션 변수 초기화 함수 (Thymeleaf에서 호출)
function initializeSessionVariables(cmpyCd, userId) {
  sessionCmpyCd = cmpyCd;
  sessionUserId = userId;
}
