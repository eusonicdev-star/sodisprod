/**
 * 입고 관리 (erp105002) JavaScript
 */

// 전역 변수
var pagePerRow = 10;
var myWins;
var myGrid;
var myComboCmpyCd;
var currentTab = "register";
var historyData = []; // 입고 내역 데이터 저장용

// 세션 변수 (Thymeleaf에서 주입됨)
var sessionCmpyCd = "A";
var sessionUserId = "admin";

// 메인 함수 객체
var fnObj = {
  // 탭 전환
  tabOpen: {
    showRegisterTab: function () {
      currentTab = "register";
      $(".tab-button").removeClass("active");
      $("#registerTab").addClass("active");
      $(".tab-content").removeClass("active");
      $("#registerContent").addClass("active");
    },
    showHistoryTab: function () {
      currentTab = "history";
      $(".tab-button").removeClass("active");
      $("#historyTab").addClass("active");
      $(".tab-content").removeClass("active");
      $("#historyContent").addClass("active");
      fnObj.loadInboundHistory();
    },
  },

  // 회사 콤보 초기화 (사용하지 않음)
  comboOpen: {
    comboCmpyCd: function () {
      // 회사 콤보박스는 현재 사용하지 않음
      console.log("회사 콤보박스 초기화 생략");
    },
  },

  // 선택된 상품을 목록에 추가
  addProductToList: function () {
    var quantity = $("#productQuantity").val();
    var expectedDate = $("#expectedDate").val();
    var productCd = $("#selectedProductCd").val();
    var productNm = $("#selectedProductNm").val();
    var productId = $("#selectedProductId").val();

    if (!productCd || !productNm) {
      alert("상품을 선택해주세요.");
      return;
    }
    if (!quantity || quantity <= 0) {
      alert("수량을 입력해주세요.");
      return;
    }
    if (!expectedDate) {
      alert("예정일을 선택해주세요.");
      return;
    }

    // 이미 추가된 상품인지 확인
    var isDuplicate = false;
    $("#selectedProductsList tr").each(function () {
      var existingProductCd = $(this).find(".product-cd").text();
      if (existingProductCd === productCd) {
        isDuplicate = true;
        return false;
      }
    });

    if (isDuplicate) {
      alert("이미 추가된 상품입니다.");
      return;
    }

    // 선택된 상품 목록에 추가
    var productHtml = "<tr data-product-id='" + productId + "'>";
    productHtml += '<td class="product-cd">' + productCd + "</td>";
    productHtml += '<td class="product-nm">' + productNm + "</td>";
    productHtml += '<td class="product-qty">' + quantity + "</td>";
    productHtml += '<td class="product-date">' + expectedDate + "</td>";
    productHtml +=
      '<td><button class="btn btn-danger" onclick="fnObj.removeProductFromList(this)" style="padding: 4px 8px; font-size: 11px;">삭제</button></td>';
    productHtml += "</tr>";

    $("#selectedProductsList").append(productHtml);

    // 테이블 컨테이너 표시
    $("#selectedProductsContainer").show();

    // 상품 정보만 초기화 (수량과 예정일은 유지)
    $("#selectedProductCd").val("");
    $("#selectedProductNm").val("");
    $("#selectedProductId").val("");
    $("#productQuantity").prop("disabled", true);
    $("#expectedDate").prop("disabled", true);
  },

  // 상품을 목록에서 제거
  removeProductFromList: function (button) {
    $(button).closest("tr").remove();

    // 테이블이 비어있으면 컨테이너 숨기기
    if ($("#selectedProductsList tr").length === 0) {
      $("#selectedProductsContainer").hide();
    }
  },

  // 선택된 상품들을 일괄 등록
  registerSelectedProducts: function () {
    var selectedProducts = [];
    var agntCd = $("#agntCd").val();
    var agntNm = $("#agntNm").val();

    if (!agntCd || !agntNm) {
      alert("화주사를 선택해주세요.");
      return;
    }

    $("#selectedProductsList tr").each(function () {
      var productCd = $(this).find(".product-cd").text();
      var productNm = $(this).find(".product-nm").text();
      var quantity = $(this).find(".product-qty").text();
      var expectedDate = $(this).find(".product-date").text();
      var productId = $(this).data("product-id"); // 각 상품의 ID 사용

      var inboundVO = {
        cmpyCd: sessionCmpyCd,
        agntCd: agntCd,
        mtrlCd: productCd,
        mtrlNm: productNm,
        tblMtrlMId: productId, // 올바른 ID 사용
        inboundQuantity: parseInt(quantity),
        inboundStatus: "PENDING",
        expectedDate: expectedDate,
        saveUser: sessionUserId,
        updtUser: sessionUserId,
        remarks: "",
      };

      selectedProducts.push(inboundVO);
    });

    if (selectedProducts.length === 0) {
      alert("등록할 상품이 없습니다.");
      return;
    }

    // 중복 검증
    fnObj.checkDuplicateBeforeRegister(selectedProducts);
  },

  // 등록 전 중복 검증
  checkDuplicateBeforeRegister: function (selectedProducts) {
    var duplicateChecks = [];
    var completedChecks = 0;

    selectedProducts.forEach(function (product) {
      var checkData = {
        cmpyCd: product.cmpyCd,
        agntCd: product.agntCd,
        mtrlCd: product.mtrlCd,
        expectedDate: product.expectedDate,
      };

      $.ajax({
        url: "/erp105002CheckDuplicate",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(checkData),
        success: function (res) {
          if (res.success && res.isDuplicate) {
            duplicateChecks.push({
              product: product,
              message: res.message,
            });
          }
          completedChecks++;

          // 모든 검증이 완료되면 결과 처리
          if (completedChecks === selectedProducts.length) {
            fnObj.handleDuplicateCheckResult(duplicateChecks, selectedProducts);
          }
        },
        error: function () {
          completedChecks++;
          if (completedChecks === selectedProducts.length) {
            fnObj.handleDuplicateCheckResult(duplicateChecks, selectedProducts);
          }
        },
      });
    });
  },

  // 중복 검증 결과 처리
  handleDuplicateCheckResult: function (duplicateChecks, selectedProducts) {
    if (duplicateChecks.length > 0) {
      var message = "다음 상품들이 이미 같은 예정일에 등록되어 있습니다:\n\n";
      duplicateChecks.forEach(function (check) {
        message +=
          "• " +
          check.product.mtrlNm +
          " (" +
          check.product.mtrlCd +
          ") - " +
          check.product.expectedDate +
          "\n";
      });
      message += "\n등록을 계속하시겠습니까?";

      if (!confirm(message)) {
        return;
      }
    }

    // 실제 등록 진행
    fnObj.proceedWithRegistration(selectedProducts);
  },

  // 실제 등록 진행
  proceedWithRegistration: function (selectedProducts) {
    $.ajax({
      url: "/erp105002InsertMultipleInbound",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedProducts),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.clearRegisterForm();
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("입고 등록 중 오류가 발생했습니다.");
      },
    });
  },

  // 상품 입력 폼 초기화
  clearProductForm: function () {
    $("#selectedProductCd").val("");
    $("#selectedProductNm").val("");
    $("#selectedProductId").val("");
    $("#productQuantity").val("");
    // 예정일은 초기화하지 않음 (화주사별로 동일한 예정일 사용 가능)
    $("#productQuantity").prop("disabled", true);
    $("#expectedDate").prop("disabled", true);
  },

  // 전체 등록 폼 초기화
  clearRegisterForm: function () {
    $("#agntCd").val("");
    $("#agntNm").val("");
    $("#selectedProductsList").empty();
    $("#selectedProductsContainer").hide();
    fnObj.clearProductForm();
  },

  // 페이징 관련 변수
  currentPage: 1,
  totalPages: 0,
  totalCount: 0,
  pageSize: 10,

  // 입고 내역 조회 (페이징)
  loadInboundHistory: function (pageNum = 1) {
    var inboundVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#historyAgntCd").val(),
      mtrlCd: $("#historyProductCd").val(),
      mtrlNm: $("#historyProductNm").val(),
      inboundStatus: $("#historyStatus").val(),
      saveUser: sessionUserId, // 본인이 등록한 것만 조회
      dateFrom: $("#historyDateFrom").val(),
      dateTo: $("#historyDateTo").val(),
      pageNum: pageNum,
      pageSize: fnObj.pageSize,
    };

    console.log("입고 내역 조회 요청 데이터:", inboundVO);

    $.ajax({
      url: "/erp105002InboundList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(inboundVO),
      success: function (res) {
        console.log("입고 내역 조회 응답:", res);
        if (res.success) {
          fnObj.currentPage = res.currentPage;
          fnObj.totalPages = res.totalPages;
          fnObj.totalCount = res.totalCount;
          // 현재 조회된 데이터 저장 (엑셀 다운로드용)
          historyData = res.data;
          fnObj.renderInboundHistory(res.data);
          fnObj.renderPagination();
        } else {
          alert("입고 내역 조회 중 오류가 발생했습니다: " + res.message);
        }
      },
      error: function (xhr, status, error) {
        console.error("입고 내역 조회 오류:", error);
        alert("입고 내역 조회 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 내역 렌더링
  renderInboundHistory: function (data) {
    console.log("입고 내역 렌더링 데이터:", data);
    var html = "";
    if (data.length === 0) {
      html =
        '<tr><td colspan="9" style="text-align: center; padding: 20px; color: #666;">조회된 입고 내역이 없습니다.</td></tr>';
    } else {
      data.forEach(function (item) {
        var statusClass =
          item.inboundStatus === "COMPLETED"
            ? "status-completed"
            : "status-pending";
        var statusText =
          item.inboundStatus === "COMPLETED" ? "입고 완료" : "입고 예정";
        var actionHtml = "";

        if (item.inboundStatus === "PENDING") {
          actionHtml =
            '<button class="btn btn-danger" onclick="fnObj.deleteInbound(' +
            item.tblInboundProductId +
            ')" style="padding: 4px 8px; font-size: 11px;">삭제</button>';
        }

        html += "<tr>";
        var checkboxHtml = "";
        if (item.inboundStatus === "PENDING") {
          checkboxHtml =
            '<input type="checkbox" class="inbound-checkbox" value="' +
            item.tblInboundProductId +
            '" onchange="fnObj.updateDeleteButton()" />';
        }
        html += "<td>" + checkboxHtml + "</td>";
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
        html += "<td>" + actionHtml + "</td>";
        html += "</tr>";
      });
    }
    $("#inboundHistoryList").html(html);
    fnObj.updateDeleteButton(); // 삭제 버튼 상태 업데이트
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

  // 페이지 이동
  goToPage: function (pageNum) {
    if (pageNum < 1 || pageNum > fnObj.totalPages) {
      return;
    }
    fnObj.loadInboundHistory(pageNum);
  },

  // 입고 내역 삭제
  deleteInbound: function (tblInboundProductId) {
    if (!confirm("정말로 이 입고 내역을 삭제하시겠습니까?")) {
      return;
    }

    // 현재 목록에서 해당 항목의 상태 확인
    var inboundStatus = "";
    $("#inboundHistoryList tr").each(function () {
      var checkbox = $(this).find('input[value="' + tblInboundProductId + '"]');
      if (checkbox.length > 0) {
        var row = $(this);
        var statusBadge = row.find(".status-badge");
        if (statusBadge.hasClass("status-completed")) {
          inboundStatus = "COMPLETED";
        } else {
          inboundStatus = "PENDING";
        }
        return false;
      }
    });

    var deleteData = {
      tblInboundProductId: tblInboundProductId,
      inboundStatus: inboundStatus,
      updtUser: sessionUserId,
    };

    $.ajax({
      url: "/erp105002DeleteInbound",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(deleteData),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.loadInboundHistory(fnObj.currentPage);
        } else {
          alert(res.message);
        }
      },
      error: function () {
        alert("입고 내역 삭제 중 오류가 발생했습니다.");
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
        srchText: $("#agntNm").val(), // 입력된 화주사명을 검색어로 전달
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
      //상품 찾기 팝업
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
        srchText: $("#selectedProductNm").val(), // 입력된 상품명을 검색어로 전달
        srchArea: $("#agntCd").val(), //화주코드
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
          $("#selectedProductCd").val(rtnObj[0].commCd);
          $("#selectedProductNm").val(rtnObj[0].commNm);
          $("#selectedProductId").val(rtnObj[0].tblMtrlMId);
          // 상품이 선택되면 수량과 날짜 입력 필드를 활성화
          $("#productQuantity").prop("disabled", false);
          $("#expectedDate").prop("disabled", false);
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

  // 화주사 코드 삭제
  delAgntCode: function () {
    $("#agntCd").val("");
    $("#agntNm").val("");
  },

  // 상품 코드 삭제
  delProductCode: function () {
    $("#selectedProductCd").val("");
    $("#selectedProductNm").val("");
    $("#selectedProductId").val("");
    $("#productQuantity").val("");
    $("#expectedDate").val("");
    $("#productQuantity").prop("disabled", true);
    $("#expectedDate").prop("disabled", true);
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

  // 입고 내역 화주사명 엔터키 처리
  handleHistoryAgntNmKeypress: function (event) {
    if (event.key === "Enter") {
      var agntNm = $("#historyAgntNm").val().trim();
      if (agntNm.length > 0) {
        fnObj.winOpen.historyCommSrch("화주", "A", "AGENCY", "text");
      }
    }
  },

  // 입고 내역 상품명 엔터키 처리
  handleHistoryProductNmKeypress: function (event) {
    if (event.key === "Enter") {
      var productNm = $("#historyProductNm").val().trim();
      if (productNm.length > 0) {
        fnObj.winOpen.historyProdCommSrch("상품", "A", "MTRL_SO", "text");
      }
    }
  },

  // 입고 내역 화주사 코드 삭제
  delHistoryAgntCode: function () {
    $("#historyAgntCd").val("");
    $("#historyAgntNm").val("");
  },

  // 입고 내역 상품 코드 삭제
  delHistoryProductCode: function () {
    $("#historyProductCd").val("");
    $("#historyProductNm").val("");
  },

  // 전체 선택/해제
  toggleSelectAll: function () {
    var isChecked = $("#selectAllCheckbox").prop("checked");
    $(".inbound-checkbox").prop("checked", isChecked);
    fnObj.updateDeleteButton();
  },

  // 삭제 버튼 표시/숨김 업데이트
  updateDeleteButton: function () {
    var checkedCount = $(".inbound-checkbox:checked").length;
    if (checkedCount > 0) {
      $("#deleteSelectedBtn").show();
    } else {
      $("#deleteSelectedBtn").hide();
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

  // 선택된 입고 내역 일괄 삭제
  deleteSelectedInbound: function () {
    var selectedIds = [];
    $(".inbound-checkbox:checked").each(function () {
      selectedIds.push($(this).val());
    });

    if (selectedIds.length === 0) {
      alert("삭제할 항목을 선택해주세요.");
      return;
    }

    if (
      !confirm(
        "선택된 " + selectedIds.length + "개의 입고 내역을 삭제하시겠습니까?"
      )
    ) {
      return;
    }

    // 각 항목을 개별적으로 삭제
    var deletePromises = selectedIds.map(function (id) {
      // 해당 항목의 상태 확인
      var inboundStatus = "PENDING"; // 체크박스가 있는 항목은 모두 PENDING 상태

      var deleteData = {
        tblInboundProductId: parseInt(id),
        inboundStatus: inboundStatus,
        updtUser: sessionUserId,
      };

      return $.ajax({
        url: "/erp105002DeleteInbound",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(deleteData),
      });
    });

    Promise.all(deletePromises)
      .then(function (responses) {
        alert("선택된 입고 내역이 삭제되었습니다.");
        fnObj.loadInboundHistory(fnObj.currentPage);
      })
      .catch(function (error) {
        alert("삭제 중 오류가 발생했습니다.");
        console.error(error);
      });
  },

  // 엑셀 템플릿 다운로드
  downloadExcelTemplate: function () {
    // 엑셀 헨들러
    var excelHandler = {
      getExcelFileName: function () {
        return "입고일괄등록_양식.xlsx";
      },
      getSheetName: function () {
        return "입고일괄등록";
      },
      getExcelData: function () {
        return get_json_for_tmpl();
      },
      getWorksheet: function () {
        var worksheet = XLSX.utils.json_to_sheet(this.getExcelData());
        var range = XLSX.utils.decode_range(worksheet["!ref"]);
        for (var r = range.s.r; r <= range.e.r; r++) {
          for (var c = range.s.c; c <= range.e.c; c++) {
            var cellName = XLSX.utils.encode_cell({ c: c, r: r });
            worksheet[cellName].z = "@";
          }
        }
        return worksheet;
      },
    };

    // 엑셀 다운로드
    function s2ab(s) {
      var buf = new ArrayBuffer(s.length);
      var view = new Uint8Array(buf);
      for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xff;
      return buf;
    }

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
  },

  // 엑셀 파일 업로드
  uploadExcelFile: function () {
    $("#excelFileInput").click();
  },

  // 엑셀 파일 선택 처리
  handleExcelFileSelect: function (event) {
    var file = event.target.files[0];
    if (!file) return;

    var reader = new FileReader();
    reader.onload = function (e) {
      try {
        var data = e.target.result;
        var workbook = XLSX.read(data, { type: "binary" });
        var sheetName = workbook.SheetNames[0];
        var worksheet = workbook.Sheets[sheetName];
        var jsonData = XLSX.utils.sheet_to_json(worksheet);

        fnObj.processExcelData(jsonData);
      } catch (error) {
        alert("엑셀 파일을 읽는 중 오류가 발생했습니다: " + error.message);
      }
    };
    reader.readAsBinaryString(file);
  },

  // 엑셀 데이터 처리
  processExcelData: function (excelData) {
    if (!excelData || excelData.length === 0) {
      alert("엑셀 파일에 데이터가 없습니다.");
      return;
    }

    // 데이터 검증 및 미리보기 데이터 준비
    var previewData = [];
    var validData = [];
    var errors = [];
    var duplicateCheck = {}; // 중복 검증용 객체

    for (var i = 0; i < excelData.length; i++) {
      var row = excelData[i];
      var rowNum = i + 2; // 엑셀 행 번호 (헤더 제외)
      var isValid = true;
      var errorMessages = [];

      // 필수 필드 검증
      if (!row["화주사코드"]) {
        errorMessages.push("화주사코드 필요");
        isValid = false;
      }
      if (!row["상품코드"]) {
        errorMessages.push("상품코드 필요");
        isValid = false;
      }
      if (!row["수량"] || isNaN(row["수량"]) || row["수량"] <= 0) {
        errorMessages.push("유효한 수량 필요");
        isValid = false;
      }
      if (!row["예정일"]) {
        errorMessages.push("예정일 필요");
        isValid = false;
      }

      // 중복 검증 (같은 예정일 + 같은 상품코드)
      if (isValid && row["화주사코드"] && row["상품코드"] && row["예정일"]) {
        var duplicateKey =
          row["화주사코드"] + "|" + row["상품코드"] + "|" + row["예정일"];

        if (duplicateCheck[duplicateKey]) {
          errorMessages.push(
            "중복: 같은 예정일에 같은 상품이 이미 등록됨 (행 " +
              duplicateCheck[duplicateKey] +
              ")"
          );
          isValid = false;
        } else {
          duplicateCheck[duplicateKey] = rowNum;
        }
      }

      // 미리보기 데이터 추가
      previewData.push({
        rowNum: rowNum,
        agntCd: row["화주사코드"] || "",
        mtrlCd: row["상품코드"] || "",
        mtrlNm: row["상품명"] || "",
        quantity: row["수량"] || "",
        expectedDate: row["예정일"] || "",
        isValid: isValid,
        errors: errorMessages,
        originalData: row,
      });

      // 유효한 데이터만 validData에 추가
      if (isValid) {
        validData.push({
          cmpyCd: sessionCmpyCd,
          agntCd: row["화주사코드"],
          mtrlCd: row["상품코드"],
          mtrlNm: row["상품명"] || "",
          inboundQuantity: parseInt(row["수량"]),
          expectedDate: row["예정일"],
          inboundStatus: "PENDING",
          saveUser: sessionUserId,
          updtUser: sessionUserId,
          remarks: "", // 입고 등록 시에는 비고 없음
        });
      } else {
        errors.push("행 " + rowNum + ": " + errorMessages.join(", "));
      }
    }

    // 미리보기 모달 표시
    fnObj.showExcelPreview(previewData, validData, errors);
  },

  // 엑셀 미리보기 모달 표시
  showExcelPreview: function (previewData, validData, errors) {
    // 미리보기 데이터 저장
    fnObj.excelPreviewData = validData;

    // 총 건수 표시
    $("#previewCount").text(previewData.length);

    // 기존 오류 정보 제거
    $(".modal-body .preview-info").remove();

    // 테이블 내용 생성
    var tbody = $("#excelPreviewTableBody");
    tbody.empty();

    previewData.forEach(function (item) {
      var statusClass = item.isValid ? "status-valid" : "status-invalid";
      var statusText = item.isValid ? "유효" : "오류";
      var errorText =
        item.errors.length > 0 ? " (" + item.errors.join(", ") + ")" : "";

      var row = $("<tr>");
      row.append("<td>" + item.agntCd + "</td>");
      row.append("<td>" + item.mtrlCd + "</td>");
      row.append("<td>" + item.mtrlNm + "</td>");
      row.append("<td>" + item.quantity + "</td>");
      row.append("<td>" + item.expectedDate + "</td>");
      row.append(
        '<td><span class="status-badge ' +
          statusClass +
          '">' +
          statusText +
          errorText +
          "</span></td>"
      );

      tbody.append(row);
    });

    // 오류가 있는 경우에만 경고 표시
    if (errors.length > 0) {
      var errorInfo = $(
        "<div class='preview-info' style='background: #fff3cd; border-left-color: #ffc107;'>"
      );
      errorInfo.append(
        "<p style='color: #856404;'><strong>주의:</strong> " +
          errors.length +
          "건의 데이터에 오류가 있습니다.</p>"
      );
      errorInfo.append(
        "<p style='color: #856404;'>오류가 있는 행은 등록되지 않습니다.</p>"
      );
      $(".modal-body").prepend(errorInfo);
    } else {
      // 오류가 없는 경우 성공 메시지 표시
      var successInfo = $(
        "<div class='preview-info' style='background: #d4edda; border-left-color: #28a745;'>"
      );
      successInfo.append(
        "<p style='color: #155724;'><strong>성공:</strong> 모든 데이터가 유효합니다.</p>"
      );
      successInfo.append(
        "<p style='color: #155724;'>총 " +
          validData.length +
          "건의 데이터가 등록됩니다.</p>"
      );
      $(".modal-body").prepend(successInfo);
    }

    // 모달 표시
    $("#excelPreviewModal").show();
  },

  // 엑셀 미리보기 모달 닫기
  closeExcelPreview: function () {
    $("#excelPreviewModal").hide();
    $(".modal-body .preview-info").remove(); // 모든 오류/성공 정보 제거
    $("#excelFileInput").val(""); // 파일 입력 초기화
  },

  // 엑셀 업로드 확인
  confirmExcelUpload: function () {
    if (!fnObj.excelPreviewData || fnObj.excelPreviewData.length === 0) {
      alert("등록할 유효한 데이터가 없습니다.");
      return;
    }

    // 서버로 전송
    fnObj.uploadExcelData(fnObj.excelPreviewData);

    // 모달 닫기
    fnObj.closeExcelPreview();
  },

  // 엑셀 데이터 서버 업로드
  uploadExcelData: function (data) {
    $.ajax({
      url: "/erp105002ExcelUpload",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        if (res.success) {
          alert(res.message);
          fnObj.loadInboundHistory(fnObj.currentPage); // 입고 내역 새로고침
        } else {
          alert("업로드 실패: " + res.message);
        }
      },
      error: function () {
        alert("엑셀 업로드 중 오류가 발생했습니다.");
      },
    });
  },

  // 입고 내역 엑셀 다운로드
  downloadInboundHistoryExcel: function () {
    if (historyData.length === 0) {
      alert("엑셀 다운로드할 데이터가 없습니다.\n조회해주세요.");
      return;
    }

    // 전체 데이터 조회 (페이징 없이)
    var inboundVO = {
      cmpyCd: sessionCmpyCd,
      agntCd: $("#historyAgntCd").val(),
      mtrlCd: $("#historyProductCd").val(),
      mtrlNm: $("#historyProductNm").val(),
      inboundStatus: $("#historyStatus").val(),
      saveUser: sessionUserId,
      dateFrom: $("#historyDateFrom").val(),
      dateTo: $("#historyDateTo").val(),
      pageNum: 1,
      pageSize: 999999, // 전체 데이터 조회
    };

    $.ajax({
      url: "/erp105002InboundList",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(inboundVO),
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

  // 입고 내역 엑셀 파일 생성 및 다운로드
  exportInboundHistoryToExcel: function (data) {
    var excelData = [];

    // 제목행 추가
    excelData.push(["입고 내역"]);
    excelData.push([""]); // 빈 행

    // 헤더 설정
    var headers = [
      "화주사명",
      "상품코드",
      "상품명",
      "수량",
      "예정일",
      "등록일",
      "상태",
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
      { wch: 10 }, // 수량
      { wch: 12 }, // 예정일
      { wch: 12 }, // 등록일
      { wch: 12 }, // 상태
    ];

    XLSX.utils.book_append_sheet(wb, ws, "입고내역");

    var fileName =
      "입고내역_" + new Date().toISOString().split("T")[0] + ".xlsx";
    XLSX.writeFile(wb, fileName);
  },
};

// 엑셀 템플릿용 JSON 데이터 생성
function get_json_for_tmpl() {
  var templateData = [
    {
      화주사코드: "AG001",
      상품코드: "M001",
      상품명: "상품명 예시",
      수량: "10",
      예정일: "2025-01-15",
    },
  ];
  return templateData;
}

// 페이지 초기화
$(document).ready(function () {
  myWins = new dhtmlXWindows();
  fnObj.tabOpen.showRegisterTab();

  // 오늘 날짜를 기본값으로 설정
  var today = new Date().toISOString().split("T")[0];
  $('input[type="date"]').val(today);

  // 입고 내역 조회 시 기본값으로 오늘 날짜 설정
  $("#historyDateFrom").val(today);
  $("#historyDateTo").val(today);
});

// 세션 변수 초기화 함수 (Thymeleaf에서 호출)
function initializeSessionVariables(cmpyCd, userId) {
  sessionCmpyCd = cmpyCd;
  sessionUserId = userId;
}
