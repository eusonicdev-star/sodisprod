var pagePerRow;
var myWins;
var myGrid;
var searchRes = [];

fnObj = {
    winOpen: {
        commSrch: function (title, cmpyCd, srchCtgr, srchInput) {
            windowId = "commSrch";
            windowText = title + " commSrch";
            windowUrl = "/commSrch"
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
                srchNm: $("#srchName").val()
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
            comboList(myComboCmpyCd, 'A', 'COMM', 'CMPY_CD', '', 'Y', '');
            myComboCmpyCd.selectOption(myComboCmpyCd.getIndexByValue(cmpyCd));
            myComboCmpyCd.setFontSize("12px", "12px");
            myComboCmpyCd.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
            });
            myComboCmpyCd.disable();
        },

        comboDtType: function () {
            myComboDtType = new dhtmlXCombo("dtType");
            comboList(myComboDtType, 'A', 'COMM', 'SO_SRCH_DT_TYPE', '', 'Y', '');
            myComboDtType.selectOption(myComboDtType.getIndexByValue("2"));
            myComboDtType.readonly(true);
            myComboDtType.setFontSize("12px", "12px");
            myComboDtType.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
            });
        },

        comboDcCd: function () {
            myComboDcCd = new dhtmlXComboFromSelect("dcCd");
            comboList(myComboDcCd, 'A', 'DC_CD', '', '', 'Y', 'req');
            myComboDcCd.readonly(true);
            myComboDcCd.selectOption(0);
            myComboDcCd.setFontSize("12px", "12px");
            myComboDcCd.attachEvent("onChange", function (value, text) {
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
                    if ((myComboDcCd.getOptionsCount() - 1) == cnt) {
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
            comboList(myComboSoType, 'A', 'COMM', 'SO_ORDR_TYPE', '', 'Y', 'req');
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
                    if ((myComboSoType.getOptionsCount() - 1) == cnt) {
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

        comboOutboundStatus: function () {
            myComboOutboundStatus = new dhtmlXCombo("outboundStatus");
            myComboOutboundStatus.addOption("ALL", "전체");
            myComboOutboundStatus.addOption("WAIT", "미출고");
            myComboOutboundStatus.addOption("DONE", "출고완료");
            myComboOutboundStatus.selectOption(0);
            myComboOutboundStatus.readonly(true);
            myComboOutboundStatus.setFontSize("12px", "12px");
            myComboOutboundStatus.attachEvent("onChange", function (value, text) {
                myGrid.clearAll(false);
            });
        },

    },
    gridOpen: {
        grid: function () {
            myGrid.setImagePath("https://cdn.dhtmlx.com/edge/skins/web/imgs/");

            myGrid.setHeader("선택,출고상태,화주,주문유형,MTO,주문상태,물류센터,배송확정일,AL오더,고객사주문번호,수취인,상품코드(HAWA),상품명(HAWA),수량(HAWA),운송사,송장번호,출고일,출고자,ID");
            myGrid.setColumnIds("chk,outboundStatus,agntNm,soTypeNm,mtoYn,soStatNm,dcNm,dlvyCnfmDt,soNo,refSoNo,acptEr,cprodCd,cprodNm,cqty,courierNm,waybillNo,outboundDt,outboundUser,tblSoPId");
            myGrid.setInitWidths("50,80,100,100,60,100,100,120,120,150,120,120,200,80,100,120,100,80,0");
            myGrid.setColAlign("center,center,left,center,center,center,center,center,center,center,left,center,left,right,center,center,center,left,left");
            myGrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
            myGrid.setColSorting("na,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str");
            myGrid.attachHeader("#master_checkbox,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");

            myGrid.init();
            myGrid.enableSmartRendering(true);
            myGrid.enablePreRendering(10);
            fnObj.gridOpen.setWidth();
            myGrid.addRow(1, "");
            myGrid.deleteRow(1);
            myGrid.enableEditEvents(true, false, false);
            myGrid.enableBlockSelection(true);
            myGrid.attachEvent("onKeyPress", fnObj.keyOpen.onKeyPressed);

            // 체크박스 전체 선택/해제 시 개별 체크박스 상태 동기화
            myGrid.attachEvent("onCheckbox", function(rId, cInd, state) {
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
            myGrid.enableAutoHeight(true, window.innerHeight - searchDiv_height - searchDiv2nd_height - page_height - xhdr_height - 30);

            $("#searchDiv").width(c_width - 25);
            $("#searchDiv2nd").width(c_width - 25);
        },
    },
    searchOpen: {
        sTime: function () {
            toastr.success('잠시 기다려주세요', '조회중');
            setTimeout(function () {
                fnObj.searchOpen.search()
            }, 300);
        },

        search: function () {
            var z = (new Date()).valueOf();
            myGrid.clearAll(false);
            searchRes = null;
            searchRes = [];

            var srchData = new FormData();

            // 주문번호가 입력된 경우 주문번호만으로 조회
            var soListValue = $("#soList").val().trim();
            if (soListValue && soListValue !== "") {
                srchData.append("cmpyCd", myComboCmpyCd ? myComboCmpyCd.getSelectedValue() : "A");
                srchData.append("soList", soListValue.replace(/\n/g, ","));

                // 화주사 권한인 경우 본인 화주사로만 조회 강제
                var agntListValue = $("#srchCode").val();
                if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200" || sessionUserGrntCd == "7788") {
                    agntListValue = sessionAgntCd;
                }
                srchData.append("agntList", agntListValue);
            } else {
                // 기존 조건들로 조회
                srchData.append("cmpyCd", myComboCmpyCd ? myComboCmpyCd.getSelectedValue() : "A");
                srchData.append("dtType", myComboDtType ? myComboDtType.getSelectedValue() : "");
                srchData.append("fromDt", $("#dateFrom").val() ? $("#dateFrom").val().replace(/-/g, "") : "");
                srchData.append("toDt", $("#dateTo").val() ? $("#dateTo").val().replace(/-/g, "") : "");

                // 화주사 권한인 경우 본인 화주사로만 조회 강제
                var agntListValue = $("#srchCode").val();
                if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200" || sessionUserGrntCd == "7788") {
                    agntListValue = sessionAgntCd;
                }
                srchData.append("agntList", agntListValue);
                srchData.append("dcList", myComboDcCd ? myComboDcCd.getChecked() : "");
                srchData.append("soList", $("#soList").val() ? $("#soList").val().replace(/\n/g, ",") : "");
                srchData.append("soTypeList", myComboSoType ? myComboSoType.getChecked() : "");
                srchData.append("acptNm", $("#rcptNm").val() || "");
                srchData.append("prodList", $("#prodList").val() ? $("#prodList").val().replace(/\n/g, ",") : "");
                srchData.append("prodNm", $("#prodNm").val() || "");
                srchData.append("outboundStatus", myComboOutboundStatus ? myComboOutboundStatus.getSelectedValue() : "ALL");
            }

            $.ajax({
                url: "/erp105010List",
                processData: false,
                contentType: false,
                data: srchData,
                type: "POST",
                datatype: "json",
                async: false,
                success: function (res) {
                    if (res && res.length > 0) {
                        for (var k = 0; k < res.length; k++) {
                            searchRes.push(res[k])
                        }
                    }
                }
            });

            searchPaging(1);
            toastr.remove()
            console.log("search : " + ((new Date()).valueOf() - z) / 1000 + " 초");
        },

        delSrchCode: function () {
            $("#srchCode").val("");
            $("#srchName").val("");
        },

        searchChkVal: {
            chkVal: function (chkGubn) {
                return true; // 검증 없음
            },
        },

    },
    keyOpen: {
        enterKey: function (enterType) {
            if (window.event.keyCode == 13) {
                if (enterType == 'srchName') {
                    $("#srchName").blur();
                    $("#srchCode").blur();
                    $("#srchCode").val("");
                    fnObj.winOpen.commSrch('화주', 'A', 'AGENCY', 'text');
                }
            }
        },
        onKeyPressed: function (code, ctrl, shift) {
            if (code == 67 && ctrl) {
                myGrid.setCSVDelimiter("\t");
                myGrid.copyBlockToClipboard()
            }
            if (code == 86 && ctrl) {
                myGrid.pasteBlockFromClipboard()
            }
            return true;
        },
    },
    excelOutbound: {
        // 엑셀 템플릿 다운로드
        downloadTemplate: function () {
            var excelHandler = {
                getExcelFileName: function () {
                    return "택배출고일괄처리_양식.xlsx";
                },
                getSheetName: function () {
                    return "택배출고일괄처리";
                },
                getExcelData: function () {
                    return get_json_for_outbound_tmpl();
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

            function s2ab(s) {
                var buf = new ArrayBuffer(s.length);
                var view = new Uint8Array(buf);
                for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xff;
                return buf;
            }

            var wb = XLSX.utils.book_new();
            var newWorksheet = excelHandler.getWorksheet();
            XLSX.utils.book_append_sheet(wb, newWorksheet, excelHandler.getSheetName());
            var wbout = XLSX.write(wb, { bookType: "xlsx", type: "binary" });
            saveAs(
                new Blob([s2ab(wbout)], { type: "application/octet-stream" }),
                excelHandler.getExcelFileName()
            );
        },

        // 엑셀 파일 업로드
        uploadExcelFile: function () {
            $("#excelOutboundFileInput").click();
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

                    fnObj.excelOutbound.processExcelData(jsonData);
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

            var previewData = [];
            var validData = [];
            var errors = [];

            // AL 오더 목록 수집
            var soNoList = [];
            for (var i = 0; i < excelData.length; i++) {
                var row = excelData[i];
                if (row["AL 오더"] && soNoList.indexOf(row["AL 오더"]) === -1) {
                    soNoList.push(row["AL 오더"]);
                }
            }

            // 운송사 목록 먼저 조회
            var courierMap = {};
            var comboForm = new FormData();
            comboForm.append("cmpyCd", "A");
            comboForm.append("comboType", "COMM");
            comboForm.append("commCd", "COURIER_CD");
            comboForm.append("comdCd", "");
            comboForm.append("useYn", "Y");

            $.ajax({
                url: "/comComboList",
                processData: false,
                contentType: false,
                data: comboForm,
                type: "POST",
                async: false,
                success: function (courierRes) {
                    if (courierRes && courierRes.length > 0) {
                        for (var j = 0; j < courierRes.length; j++) {
                            courierMap[courierRes[j].comboNm] = courierRes[j].comboCd;
                        }
                    }
                }
            });

            // 서버에서 주문 정보 조회
            var srchData = new FormData();
            srchData.append("cmpyCd", myComboCmpyCd ? myComboCmpyCd.getSelectedValue() : "A");
            srchData.append("soList", soNoList.join(","));

            $.ajax({
                url: "/erp105010List",
                processData: false,
                contentType: false,
                data: srchData,
                type: "POST",
                datatype: "json",
                async: false,
                success: function (res) {
                    var orderMap = {};
                    if (res && res.length > 0) {
                        for (var k = 0; k < res.length; k++) {
                            var key = res[k]["soNo"] + "|" + res[k]["cprodCd"];
                            orderMap[key] = res[k];
                        }
                    }

                    // 엑셀 데이터 검증
                    for (var i = 0; i < excelData.length; i++) {
                        var row = excelData[i];
                        var rowNum = i + 2;
                        var isValid = true;
                        var errorMessages = [];

                        // 필수 필드 검증
                        if (!row["AL 오더"]) {
                            errorMessages.push("AL 오더 필요");
                            isValid = false;
                        }
                        if (!row["상품코드"]) {
                            errorMessages.push("상품코드 필요");
                            isValid = false;
                        }
                        if (!row["운송사"]) {
                            errorMessages.push("운송사 필요");
                            isValid = false;
                        }
                        if (!row["송장번호"]) {
                            errorMessages.push("송장번호 필요");
                            isValid = false;
                        }
                        if (!row["출고일"]) {
                            errorMessages.push("출고일 필요");
                            isValid = false;
                        }

                        // 주문 정보 검증
                        var orderKey = (row["AL 오더"] || "") + "|" + (row["상품코드"] || "");
                        var orderInfo = orderMap[orderKey];

                        if (isValid && !orderInfo) {
                            errorMessages.push("주문 정보를 찾을 수 없음");
                            isValid = false;
                        } else if (isValid && orderInfo) {
                            // 이미 출고된 건인지 확인
                            if (orderInfo["outboundDt"] && orderInfo["outboundDt"].trim() !== "") {
                                errorMessages.push("이미 출고 처리됨");
                                isValid = false;
                            }
                            // 택배 주문인지 확인
                            var soTypeNm = orderInfo["soTypeNm"] || "";
                            if (soTypeNm.indexOf("택배") < 0) {
                                errorMessages.push("택배 주문이 아님");
                                isValid = false;
                            }
                        }

                        // 출고일 형식 검증
                        if (isValid && row["출고일"]) {
                            var outboundDt = row["출고일"];
                            // YYYY-MM-DD 형식인지 확인
                            if (!outboundDt.match(/^\d{4}-\d{2}-\d{2}$/)) {
                                errorMessages.push("출고일 형식 오류 (YYYY-MM-DD)");
                                isValid = false;
                            }
                        }

                        // 운송사 코드 검증
                        var courierCd = "";
                        var courierNm = row["운송사"] || "";
                        if (isValid && courierNm) {
                            courierCd = courierMap[courierNm];
                            if (!courierCd) {
                                errorMessages.push("운송사 코드를 찾을 수 없음");
                                isValid = false;
                            }
                        }

                        previewData.push({
                            rowNum: rowNum,
                            soNo: row["AL 오더"] || "",
                            cprodCd: row["상품코드"] || "",
                            cprodNm: orderInfo ? orderInfo["cprodNm"] : "",
                            courierNm: courierNm,
                            waybillNo: row["송장번호"] || "",
                            outboundDt: row["출고일"] || "",
                            isValid: isValid,
                            errors: errorMessages,
                            orderInfo: orderInfo
                        });

                        if (isValid && orderInfo && courierCd) {
                            validData.push({
                                tblSoPId: orderInfo["tblSoPId"],
                                soNo: row["AL 오더"],
                                courierCd: courierCd,
                                courierNm: courierNm,
                                waybillNo: row["송장번호"],
                                outboundDt: row["출고일"].replace(/-/g, ""),
                                processType: "COURIER"
                            });
                        } else if (!isValid) {
                            errors.push("행 " + rowNum + ": " + errorMessages.join(", "));
                        }
                    }

                    fnObj.excelOutbound.showExcelPreview(previewData, validData, errors);
                },
                error: function () {
                    alert("주문 정보 조회 중 오류가 발생했습니다.");
                }
            });
        },

        // 엑셀 미리보기 모달 표시
        showExcelPreview: function (previewData, validData, errors) {
            fnObj.excelOutbound.excelPreviewData = validData;

            $("#excelOutboundPreviewCount").text(previewData.length);

            var tbody = $("#excelOutboundPreviewTableBody");
            tbody.empty();

            previewData.forEach(function (item, index) {
                var statusClass = item.isValid ? "status-valid" : "status-invalid";
                var statusText = item.isValid ? "유효" : "오류";
                var errorText = item.errors.length > 0 ? " (" + item.errors.join(", ") + ")" : "";

                var row = $("<tr>");
                row.append("<td style='text-align: center;'>" + (index + 1) + "</td>");
                row.append("<td>" + item.soNo + "</td>");
                row.append("<td>" + item.cprodCd + "</td>");
                row.append("<td>" + item.cprodNm + "</td>");
                row.append("<td>" + item.courierNm + "</td>");
                row.append("<td>" + item.waybillNo + "</td>");
                row.append("<td>" + item.outboundDt + "</td>");
                row.append(
                    '<td><span class="status-badge ' + statusClass + '">' + statusText + errorText + '</span></td>'
                );

                tbody.append(row);
            });

            if (errors.length > 0) {
                var errorInfo = $(
                    "<div class='preview-info' style='background: #fff3cd; border-left-color: #ffc107; padding: 10px; margin-bottom: 10px;'>"
                );
                errorInfo.append(
                    "<p style='color: #856404; margin: 0;'><strong>주의:</strong> " + errors.length + "건의 데이터에 오류가 있습니다.</p>"
                );
                errorInfo.append(
                    "<p style='color: #856404; margin: 5px 0 0 0;'>오류가 있는 행은 처리되지 않습니다.</p>"
                );
                $("#excelOutboundModal .modal-body").prepend(errorInfo);
            } else {
                var successInfo = $(
                    "<div class='preview-info' style='background: #d4edda; border-left-color: #28a745; padding: 10px; margin-bottom: 10px;'>"
                );
                successInfo.append(
                    "<p style='color: #155724; margin: 0;'><strong>성공:</strong> 모든 데이터가 유효합니다.</p>"
                );
                successInfo.append(
                    "<p style='color: #155724; margin: 5px 0 0 0;'>총 " + validData.length + "건의 데이터가 처리됩니다.</p>"
                );
                $("#excelOutboundModal .modal-body").prepend(successInfo);
            }

            $("#excelOutboundModal").show();
        },

        // 엑셀 미리보기 모달 닫기
        closeExcelPreview: function () {
            $("#excelOutboundModal").hide();
            $("#excelOutboundModal .modal-body .preview-info").remove();
            $("#excelOutboundFileInput").val("");
        },

        // 엑셀 업로드 확인
        confirmExcelUpload: function () {
            if (!fnObj.excelOutbound.excelPreviewData || fnObj.excelOutbound.excelPreviewData.length === 0) {
                alert("처리할 유효한 데이터가 없습니다.");
                return;
            }

            if (!confirm(fnObj.excelOutbound.excelPreviewData.length + "건의 택배를 출고 처리하시겠습니까?\n\n재고가 차감되며, 주문 상태가 배송완료로 변경됩니다.")) {
                return;
            }

            $.ajax({
                url: "/erp105010BatchOutbound",
                contentType: "application/json",
                data: JSON.stringify(fnObj.excelOutbound.excelPreviewData),
                type: "POST",
                datatype: "json",
                success: function (res) {
                    if (res && res.rtnYn == "Y") {
                        toastr.success(res.rtnMsg);
                        fnObj.excelOutbound.closeExcelPreview();
                        fnObj.searchOpen.search();
                    } else {
                        toastr.error(res.rtnMsg);
                    }
                },
                error: function () {
                    toastr.error("출고 처리 중 오류가 발생했습니다.");
                }
            });
        }
    },
    outboundOpen: {
        // 택배 출고 버튼 클릭
        courierOutbound: function () {
            var selectedRows = [];

            // 체크된 행 찾기
            for (var i = 1; i <= myGrid.getRowsNum(); i++) {
                var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
                if (chkValue == "1" || chkValue == "true" || chkValue === true) {
                    var tblSoPId = myGrid.cellById(i, myGrid.getColIndexById("tblSoPId")).getValue();
                    var soType = myGrid.cellById(i, myGrid.getColIndexById("soTypeNm")).getValue();
                    var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();
                    var prodNm = myGrid.cellById(i, myGrid.getColIndexById("cprodNm")).getValue();
                    var outboundDt = myGrid.cellById(i, myGrid.getColIndexById("outboundDt")).getValue();

                    // 이미 출고된 건은 제외
                    if (outboundDt && outboundDt.trim() !== "") {
                        toastr.warning("주문번호 " + soNo + "는 이미 출고 처리되었습니다.");
                        continue;
                    }

                    selectedRows.push({
                        index: i,
                        tblSoPId: tblSoPId,
                        soType: soType,
                        soNo: soNo,
                        prodNm: prodNm
                    });
                }
            }

            if (selectedRows.length == 0) {
                alert("출고 처리할 항목을 선택해주세요.");
                return;
            }

            // 택배만 선택했는지 확인 및 업체직출 혼합 체크
            var courierCount = 0;
            var directCount = 0;
            for (var i = 0; i < selectedRows.length; i++) {
                if (selectedRows[i].soType.indexOf("택배") >= 0) {
                    courierCount++;
                } else if (selectedRows[i].soType.indexOf("업체직출") >= 0) {
                    directCount++;
                }
            }

            if (courierCount == 0) {
                alert("택배 주문을 선택해주세요.");
                return;
            }

            if (directCount > 0) {
                alert("택배와 업체직출을 함께 선택할 수 없습니다.\n택배 주문만 선택해주세요.");
                return;
            }

            // 모달 표시
            fnObj.outboundOpen.showCourierModal(selectedRows);
        },

        // 택배 출고 모달 표시
        showCourierModal: function (selectedRows) {
            // 오늘 날짜를 기본값으로 설정
            var today = new Date();
            var year = today.getFullYear();
            var month = ("0" + (today.getMonth() + 1)).slice(-2);
            var date = ("0" + today.getDate()).slice(-2);
            $("#courierOutboundDate").val(year + "-" + month + "-" + date);

            // 운송사 목록 조회
            var courierOptions = "<option value=''>-- 운송사 선택 --</option>";
            var comboForm = new FormData();
            comboForm.append("cmpyCd", "A");
            comboForm.append("comboType", "COMM");
            comboForm.append("commCd", "COURIER_CD");
            comboForm.append("comdCd", "");
            comboForm.append("useYn", "Y");

            $.ajax({
                url: "/comComboList",
                processData: false,
                contentType: false,
                data: comboForm,
                type: "POST",
                async: false,
                success: function (res) {
                    console.log("운송사 목록:", res);
                    if (res && res.length > 0) {
                        for (var j = 0; j < res.length; j++) {
                            courierOptions += "<option value='" + res[j].comboCd + "'>" + res[j].comboNm + "</option>";
                        }
                    } else {
                        console.warn("운송사 목록이 비어있습니다.");
                    }
                },
                error: function (xhr, status, error) {
                    console.error("운송사 목록 조회 실패:", error);
                    toastr.error("운송사 목록을 불러올 수 없습니다.");
                }
            });

            // 테이블 생성
            var tableHtml = "<table class='outbound-table'>";
            tableHtml += "<thead><tr><th style='width: 130px;'>주문번호</th><th style='width: 200px;'>상품명</th><th style='width: 150px;'>운송사*</th><th style='width: 150px;'>송장번호*</th></tr></thead>";
            tableHtml += "<tbody>";

            for (var i = 0; i < selectedRows.length; i++) {
                var row = selectedRows[i];
                tableHtml += "<tr data-index='" + row.index + "' data-id='" + row.tblSoPId + "'>";
                tableHtml += "<td>" + row.soNo + "</td>";
                tableHtml += "<td title='" + row.prodNm + "'>" + row.prodNm + "</td>";
                tableHtml += "<td><select id='courierCd_" + i + "' class='courier-select'>" + courierOptions + "</select></td>";
                tableHtml += "<td><input type='text' id='waybillNo_" + i + "' class='waybill-input' /></td>";
                tableHtml += "</tr>";
            }

            tableHtml += "</tbody></table>";
            $("#courierOutboundTable").html(tableHtml);

            // 모달 표시 및 드래그 초기화
            $("#courierModal").show();
            fnObj.outboundOpen.initModalDrag();
        },

        // 택배 출고 확인
        confirmCourierOutbound: function () {
            var outboundList = [];
            var isValid = true;
            var errorMsg = "";

            $("#courierOutboundTable tbody tr").each(function () {
                var index = $(this).data("index");
                var tblSoPId = $(this).data("id");
                var soNo = $(this).find("td:eq(0)").text();

                // select 요소에서 값 가져오기
                var rowIndex = $(this).index();
                var courierSelect = $("#courierCd_" + rowIndex);
                var courierCd = courierSelect.val();
                var courierNm = courierSelect.find("option:selected").text();
                var waybillNo = $("#waybillNo_" + rowIndex).val();

                // 유효성 검사
                if (!courierCd || courierCd == "") {
                    isValid = false;
                    errorMsg += "주문번호 " + soNo + ": 운송사를 선택해주세요.\n";
                }
                if (!waybillNo || waybillNo.trim() == "") {
                    isValid = false;
                    errorMsg += "주문번호 " + soNo + ": 송장번호를 입력해주세요.\n";
                }

                outboundList.push({
                    tblSoPId: tblSoPId,
                    courierCd: courierCd,
                    courierNm: courierNm,
                    waybillNo: waybillNo,
                    outboundDt: $("#courierOutboundDate").val().replace(/-/g, ""),
                    processType: "COURIER"
                });
            });

            if (!isValid) {
                alert(errorMsg);
                return;
            }

            // 확인 메시지
            if (!confirm(outboundList.length + "건의 택배를 출고 처리하시겠습니까?\n\n재고가 차감되며, 주문 상태가 배송완료로 변경됩니다.")) {
                return;
            }

            // 서버로 전송
            $.ajax({
                url: "/erp105010BatchOutbound",
                contentType: "application/json",
                data: JSON.stringify(outboundList),
                type: "POST",
                datatype: "json",
                success: function (res) {
                    if (res && res.rtnYn == "Y") {
                        toastr.success(res.rtnMsg);
                        fnObj.outboundOpen.closeCourierModal();
                        fnObj.searchOpen.search();
                    } else {
                        toastr.error(res.rtnMsg);
                    }
                },
                error: function () {
                    toastr.error("출고 처리 중 오류가 발생했습니다.");
                }
            });
        },

        // 모달 닫기
        closeCourierModal: function () {
            $("#courierModal").hide();
            $("#courierOutboundTable").html("");
        },

        // 모달 드래그 기능 초기화
        initModalDrag: function () {
            var modal = document.getElementById("courierModalContent");
            var header = document.getElementById("courierModalHeader");
            var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;

            header.onmousedown = dragMouseDown;

            function dragMouseDown(e) {
                e = e || window.event;
                e.preventDefault();
                pos3 = e.clientX;
                pos4 = e.clientY;
                document.onmouseup = closeDragElement;
                document.onmousemove = elementDrag;
                header.style.cursor = 'move';
            }

            function elementDrag(e) {
                e = e || window.event;
                e.preventDefault();
                pos1 = pos3 - e.clientX;
                pos2 = pos4 - e.clientY;
                pos3 = e.clientX;
                pos4 = e.clientY;
                modal.style.top = (modal.offsetTop - pos2) + "px";
                modal.style.left = (modal.offsetLeft - pos1) + "px";
            }

            function closeDragElement() {
                document.onmouseup = null;
                document.onmousemove = null;
                header.style.cursor = 'default';
            }
        },

        // 업체직출 출고 버튼 클릭
        directOutbound: function () {
            var selectedRows = [];

            // 체크된 행 찾기
            for (var i = 1; i <= myGrid.getRowsNum(); i++) {
                var chkValue = myGrid.cellById(i, myGrid.getColIndexById("chk")).getValue();
                if (chkValue == "1" || chkValue == "true" || chkValue === true) {
                    var tblSoPId = myGrid.cellById(i, myGrid.getColIndexById("tblSoPId")).getValue();
                    var soType = myGrid.cellById(i, myGrid.getColIndexById("soTypeNm")).getValue();
                    var soNo = myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue();
                    var outboundDt = myGrid.cellById(i, myGrid.getColIndexById("outboundDt")).getValue();

                    // 이미 출고된 건은 제외
                    if (outboundDt && outboundDt.trim() !== "") {
                        toastr.warning("주문번호 " + soNo + "는 이미 출고 처리되었습니다.");
                        continue;
                    }

                    selectedRows.push({
                        tblSoPId: tblSoPId,
                        soType: soType,
                        soNo: soNo
                    });
                }
            }

            if (selectedRows.length == 0) {
                alert("출고 처리할 항목을 선택해주세요.");
                return;
            }

            // 업체직출만 선택했는지 확인 및 택배 혼합 체크
            var directCount = 0;
            var courierCount = 0;
            for (var i = 0; i < selectedRows.length; i++) {
                if (selectedRows[i].soType.indexOf("업체직출") >= 0) {
                    directCount++;
                } else if (selectedRows[i].soType.indexOf("택배") >= 0) {
                    courierCount++;
                }
            }

            if (directCount == 0) {
                alert("업체직출 주문을 선택해주세요.");
                return;
            }

            if (courierCount > 0) {
                alert("택배와 업체직출을 함께 선택할 수 없습니다.\n업체직출 주문만 선택해주세요.");
                return;
            }

            // 확인 메시지
            if (!confirm(directCount + "건의 업체직출을 출고 처리하시겠습니까?\n\n재고가 차감되며, 주문 상태가 배송완료로 변경됩니다.")) {
                return;
            }

            // 오늘 날짜
            var today = new Date();
            var year = today.getFullYear();
            var month = ("0" + (today.getMonth() + 1)).slice(-2);
            var date = ("0" + today.getDate()).slice(-2);
            var outboundDt = year + month + date;

            // 출고 데이터 생성
            var outboundList = [];
            for (var i = 0; i < selectedRows.length; i++) {
                if (selectedRows[i].soType.indexOf("업체직출") >= 0) {
                    outboundList.push({
                        tblSoPId: selectedRows[i].tblSoPId,
                        outboundDt: outboundDt,
                        processType: "DIRECT"
                    });
                }
            }

            // 서버로 전송
            $.ajax({
                url: "/erp105010BatchOutbound",
                contentType: "application/json",
                data: JSON.stringify(outboundList),
                type: "POST",
                datatype: "json",
                success: function (res) {
                    if (res && res.rtnYn == "Y") {
                        toastr.success(res.rtnMsg);
                        fnObj.searchOpen.search();
                    } else {
                        toastr.error(res.rtnMsg);
                    }
                },
                error: function () {
                    toastr.error("출고 처리 중 오류가 발생했습니다.");
                }
            });
        }
    }
}

function searchPaging(page) {
    if ($("#rowPerPage").val()) {
        pagePerRow = parseInt($("#rowPerPage").val());
    }

    var pageRange = 10;
    var totalPageCnt = searchRes.length % pagePerRow == 0 ? parseInt(searchRes.length / pagePerRow) : parseInt(searchRes.length / pagePerRow) + 1

    var a = "<div class='pagination-container'>";

    var start = (page - 1);
    var end = (page - 1) + pageRange;

    if (start % pageRange != 0) {
        start = parseInt(start / pageRange) * pageRange;
    }

    if (end % pageRange != 0 || page == 1) {
        end = parseInt(end / pageRange) * pageRange;
    }

    if (start >= pageRange) {
        a += "<input type='button' onclick='searchPaging(" + 1 + ");' value='<<' class='pagination-button'>";
        a += "<input type='button' onclick='searchPaging(" + (start - 1) + ");' value='<' class='pagination-button'>";
    }

    var r = 0;

    for (r = start; r < end; r++) {
        if (r < totalPageCnt) {
            if (r + 1 == page) {
                a += "<input type='button' onclick='searchPaging(" + (r + 1) + ");' value=" + (r + 1) + " class='pagination-button active'>";
            } else {
                a += "<input type='button' onclick='searchPaging(" + (r + 1) + ");' value=" + (r + 1) + " class='pagination-button'>";
            }
        }
    }

    if (end * pagePerRow < searchRes.length) {
        a += "<input type='button' onclick='searchPaging(" + (end + 1) + ");' value='>' class='pagination-button'>";
        a += "<input type='button' onclick='searchPaging(" + totalPageCnt + ");' value='>>' class='pagination-button'>";
    }

    a += "<div class='pagination-info'>";
    a += "<input type='number' maxlength=4 id='rowPerPage' value=" + pagePerRow + ">";
    a += "<input type='button' value='적용' onclick='searchPaging(" + 1 + ");' class='action-button'>";
    a += "&nbsp;&nbsp;<i class='fa fa-calculator fa-lg' aria-hidden='true'></i>&nbsp;" + searchRes.length;
    a += "</div></div>";
    $("#page").html(a);

    var is = (page - 1) * pagePerRow;
    var ie = (page - 1) * pagePerRow + pagePerRow;

    if (searchRes.length < ie) {
        ie = searchRes.length;
    }

    myGrid.clearAll(false);

    var columns = myGrid.getColumnCount()
    var x = (new Date()).valueOf();
    for (var i = is; i < ie; i++) {
        if (!searchRes[i]) continue;
        
        var rowId = i + 1;
        myGrid.addRow(rowId, "");
        
        // 각 컬럼별로 직접 값을 설정
        for (var c = 0; c < columns; c++) {
            var colId = myGrid.getColumnId(c);
            var colIndex = myGrid.getColIndexById(colId);
            var rawValue = searchRes[i][colId] !== undefined && searchRes[i][colId] !== null 
                ? searchRes[i][colId] 
                : "";
            
            // 출고상태 뱃지가 필요한 컬럼은 HTML로 변환
            var value = rawValue;
            if (colId === "outboundStatus") {
                var outboundDt = searchRes[i]["outboundDt"] || "";
                if (outboundDt && outboundDt.trim() !== "") {
                    value = '<span class="badge badge-primary">출고완료</span>';
                } else {
                    value = '<span class="badge badge-warning">출고대기</span>';
                }
            }
            
            try {
                var cell = myGrid.cellById(rowId, colIndex);
                if (cell) {
                    cell.setValue(value);
                }
            } catch (e) {
                console.error("Error setting cell value for " + colId + " at row " + rowId + ":", e);
            }
        }

        // 데이터 객체에서 직접 soStatCd 값 가져오기
        var soStatCdVal = searchRes[i]["soStatCd"] || searchRes[i]["SO_STAT_CD"] || "";
        if (soStatCdVal == "8000") {
            myGrid.setRowColor(rowId, "#F5A9A9");
        }
        if (soStatCdVal == "8060") {
            myGrid.setRowColor(rowId, "#FF8000");
        }
    }
}

function excelDn() {
    if (!myGrid.getRowsNum()) {
        alert("엑셀다운로드 대상이 없습니다.\n조회해주세요");
        return;
    }

    // 엑셀 다운로드 데이터 준비
    var excelData = [];

    // 제목행 추가
    excelData.push(["택배/직출 출고관리"]);
    excelData.push([""]); // 빈 행

    // 헤더 설정
    var headers = [
        "화주",
        "주문유형",
        "MTO",
        "주문상태",
        "물류센터",
        "배송확정일",
        "AL오더",
        "고객사주문번호",
        "수취인",
        "상품코드(HAWA)",
        "상품명(HAWA)",
        "수량(HAWA)",
        "운송사",
        "송장번호",
        "출고일",
        "출고자"
    ];
    excelData.push(headers);

    // 데이터 행 추가
    for (var i = 1; i <= myGrid.getRowsNum(); i++) {
        var rowData = [
            myGrid.cellById(i, myGrid.getColIndexById("agntNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("soTypeNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("mtoYn")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("soStatNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("dcNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("dlvyCnfmDt")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("soNo")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("refSoNo")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("acptEr")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("cprodCd")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("cprodNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("cqty")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("courierNm")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("waybillNo")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("outboundDt")).getValue() || "",
            myGrid.cellById(i, myGrid.getColIndexById("outboundUser")).getValue() || ""
        ];
        excelData.push(rowData);
    }

    // 엑셀 파일 생성 및 다운로드
    var wb = XLSX.utils.book_new();
    var ws = XLSX.utils.aoa_to_sheet(excelData);

    // 컬럼 너비 설정
    ws["!cols"] = [
        {wch: 15}, // 화주
        {wch: 15}, // 주문유형
        {wch: 8},  // MTO
        {wch: 12}, // 주문상태
        {wch: 15}, // 물류센터
        {wch: 12}, // 배송확정일
        {wch: 15}, // AL오더
        {wch: 18}, // 고객사주문번호
        {wch: 15}, // 수취인
        {wch: 15}, // 상품코드(HAWA)
        {wch: 25}, // 상품명(HAWA)
        {wch: 10}, // 수량(HAWA)
        {wch: 15}, // 운송사
        {wch: 18}, // 송장번호
        {wch: 12}, // 출고일
        {wch: 12}  // 출고자
    ];

    XLSX.utils.book_append_sheet(wb, ws, "택배직출출고관리");

    var fileName = "택배직출출고관리_" + new Date().toISOString().split("T")[0] + ".xlsx";
    XLSX.writeFile(wb, fileName);
}

$(document).ready(function () {
    pagePerRow = environmentPagePerRow;
    myWins = new dhtmlXWindows();
    myGrid = new dhtmlXGridObject('grid');

    // 날짜 선택 캘린더 초기화
    myCalendar = new dhtmlXCalendarObject([
        {input: "dateFrom", button: "dateFromIcon"},
        {input: "dateTo", button: "dateToIcon"}
    ]);

    // 달력 기본값을 당일로 설정
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
    $("#dateTo").val(today);

    myCalendar.loadUserLanguage("ko");

    fnObj.comboOpen.comboCmpyCd();
    fnObj.comboOpen.comboDtType();
    fnObj.comboOpen.comboDcCd();
    fnObj.comboOpen.comboSoType();
    fnObj.comboOpen.comboOutboundStatus();

    fnObj.gridOpen.grid()

    window.onresize = function (event) {
        fnObj.gridOpen.setWidth();
        fnObj.gridOpen.setWidth();
    };

    if (sessionUserGrntCd == "4100" || sessionUserGrntCd == "4200" || sessionUserGrntCd == "7788") {
        $("#srchName").val(sessionAgntNm);
        $("#srchCode").val(sessionAgntCd);
        $("#srchName").attr("readonly", true);
        $("#srchCode").attr("readonly", true);
        $("#srchName").css("color", "gray");
        $("#srchCode").css("color", "gray");
        $("#srchIcon").hide();
        $("#delIcon").hide();
    }

    toastr.options = {
        closeButton: true,
        progressBar: false,
        showMethod: 'slideDown',
        closeDuration: 0,
        positionClass: "toast-top-right",
        timeOut: 0
    };
});

$(document).keydown(function (event) {
    if (event.keyCode == 113 || event.which == 113) {
        fnObj.searchOpen.sTime()
    }
});

// 엑셀 템플릿용 JSON 데이터 생성
function get_json_for_outbound_tmpl() {
    var templateData = [
        {
            "AL 오더": "SO20250101001",
            "상품코드": "PROD001",
            "운송사": "CJ대한통운",
            "송장번호": "1234567890",
            "출고일": "2025-01-15"
        }
    ];
    return templateData;
}
