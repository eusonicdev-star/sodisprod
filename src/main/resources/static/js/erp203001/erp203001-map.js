/**
 * erp203001-map.js - 시공계획 관리 카카오 맵 관련 JavaScript
 */

// 지도 관련 전역 변수
var mapContainer;
var mapOption;
var map;
var dots = {};
var mapTypeControl;
var mapTypes;
var zoomControl;
var locPosition, message;
var lat, lon;
var markers = [];
var positions = [];

/**
 * 지도 초기화 함수 - DOM 로드 후 호출
 */
function initKakaoMap() {
  mapContainer = document.getElementById("map");
  mapOption = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 10,
  };

  // 지도 생성
  map = new kakao.maps.Map(mapContainer, mapOption);

  // 지도 타입 컨트롤
  mapTypeControl = new kakao.maps.MapTypeControl();

  // 지도 유형
  mapTypes = {
    terrain: kakao.maps.MapTypeId.TERRAIN,
    traffic: kakao.maps.MapTypeId.TRAFFIC,
    bicycle: kakao.maps.MapTypeId.BICYCLE,
    useDistrict: kakao.maps.MapTypeId.USE_DISTRICT,
  };

  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
  zoomControl = new kakao.maps.ZoomControl();
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

  // 현재 위치 처리
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
      lat = position.coords.latitude;
      lon = position.coords.longitude;

      locPosition = new kakao.maps.LatLng(lat, lon);
      message = null;

      map.setCenter(locPosition);
    });
  } else {
    locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
    message = "현재위치를 사용할수 없습니다";
    map.setCenter(locPosition);
  }
}

/**
 * 지도에 마커와 인포윈도우를 표시하는 함수
 */
function displayMarker(locPosition, message) {
  var marker = new kakao.maps.Marker({
    map: map,
    position: locPosition,
    clickable: true,
  });

  var iwContent = message;
  var iwRemoveable = true;

  var infowindow = new kakao.maps.InfoWindow({
    content: iwContent,
    removable: iwRemoveable,
  });

  if (message) {
    infowindow.open(map, marker);
  }
  map.setCenter(locPosition);
}

/**
 * 인포윈도우를 표시하는 클로저를 만드는 함수
 */
function makeOverListener(map, marker, infowindow) {
  return function () {
    infowindow.open(map, marker);
  };
}

/**
 * 인포윈도우를 닫는 클로저를 만드는 함수
 */
function makeOutListener(infowindow) {
  return function () {
    infowindow.close();
  };
}

/**
 * 지도에 마커 로드하는 메인 함수
 */
function onLoadMap() {
  positions = [];

  var inner = {};
  var geocoder = new kakao.maps.services.Geocoder();

  // 기존 마커 제거
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];

  var infowindow;
  var marker;

  var checkIndex = 0;

  $("#btnSearch").prop("disabled", true);
  addr.forEach(function (address, index) {
    setTimeout(function () {
      geocoder.addressSearch(addr[index].addr1, function (result, status) {
        myGrid
          .cellById(addr[index].rowId, myGrid.getColIndexById("mapFindAddr"))
          .setValue(status);
        myGrid
          .cellById(addr[index].rowId, myGrid.getColIndexById("mapIndex"))
          .setValue(index);

        if (status === kakao.maps.services.Status.OK) {
          if (result.length == 1) {
            var resultValue = result[0];
            myGrid
              .cellById(addr[index].rowId, myGrid.getColIndexById("mapY"))
              .setValue(resultValue.y);
            myGrid
              .cellById(addr[index].rowId, myGrid.getColIndexById("mapX"))
              .setValue(resultValue.x);
            inner.latlng = new kakao.maps.LatLng(resultValue.y, resultValue.x);

            var soStat = "";
            if (addr[index].soStatCd == "9999") {
              soStat = "<b>[배송완료]</b><br><br>";
            } else if (addr[index].soStatCd == "8000") {
              soStat = "<b>[배송완료(미마감)]</b><br><br>";
            }

            inner.content =
              "<div id='content" +
              index +
              "'style='width:300px;text-align:left;padding:6px 0;font-size:0.8em;'>" +
              soStat +
              addr[index].prodList.replace(/\개\//g, "개<br>") +
              "</div>";
            inner.zIndex = 15;

            inner.paltNoxx = addr[index].paltNoxx;
            inner.mapFindAddr = addr[index].mapFindAddr;
            inner.seq = addr[index].seq;
            inner.rowId = addr[index].rowId;
            var imageUrl = "";

            if (addr[index].instSeatType == "1111") {
              if (addr[index].instPlanYn == "N") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker/gray/marker_completed_others.png";
              } else if (addr[index].instPlanYn == "Y") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker/green/marker_ing_others.png";
              }

              if (addr[index].soStatCd == "9999") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker/lavender/marker_9999_others.png";
              } else if (addr[index].soStatCd == "8000") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker/orange/marker_8000_others.png";
              }

              var imageSrc = imageUrl;
              var imageSize = new kakao.maps.Size(20, 24);
              var imageOption = { offset: new kakao.maps.Point(10, 24) };
              var markerImage = new kakao.maps.MarkerImage(
                imageSrc,
                imageSize,
                imageOption,
              );

              inner.markerImage = markerImage;

              var clickImageSrc =
                "https://sodisprod.cdn1.cafe24.com/marker/red/marker_return_others.png";
              var clickImageSize = new kakao.maps.Size(20, 24);
              var clickImageOption = { offset: new kakao.maps.Point(10, 24) };
            } else if (addr[index].instSeatType == "2222") {
              if (addr[index].instPlanYn == "N") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker2/marker_2_551401.png";
              } else if (addr[index].instPlanYn == "Y") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker2/marker_2_blue.png";
              }

              if (addr[index].soStatCd == "9999") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker2/marker_2_lavender.png";
              } else if (addr[index].soStatCd == "8000") {
                imageUrl =
                  "https://sodisprod.cdn1.cafe24.com/marker2/marker_2_orange2.png";
              }

              var imageSrc = imageUrl;
              var imageSize = new kakao.maps.Size(24, 24);
              var imageOption = { offset: new kakao.maps.Point(12, 24) };

              var markerImage = new kakao.maps.MarkerImage(
                imageSrc,
                imageSize,
                imageOption,
              );
              inner.markerImage = markerImage;

              var clickImageSrc =
                "https://sodisprod.cdn1.cafe24.com/marker2/marker_2_red.png";
              var clickImageSize = new kakao.maps.Size(24, 24);
              var clickImageOption = { offset: new kakao.maps.Point(12, 24) };
            }

            var clickImage = new kakao.maps.MarkerImage(
              clickImageSrc,
              clickImageSize,
              clickImageOption,
            );

            marker = new kakao.maps.Marker({
              map: map,
              position: inner.latlng,
              image: inner.markerImage,
            });
            marker.index = index;
            marker.rowId = addr[index].rowId;
            marker.normalImage = inner.markerImage;
            marker.clickImage = clickImage;
            marker.clickYn = "N";

            markers.push(marker);

            infowindow = new kakao.maps.InfoWindow({
              content: inner.content,
              zIndex: inner.zIndex,
            });

            kakao.maps.event.addListener(
              marker,
              "mouseover",
              makeOverListener(map, marker, infowindow),
            );
            kakao.maps.event.addListener(
              marker,
              "mouseout",
              makeOutListener(infowindow),
            );

            markers.forEach(function (markerInfo, u) {
              if (markerInfo.index == index) {
                kakao.maps.event.addListener(markers[u], "click", function () {
                  if (markers[u].clickYn == "N") {
                    if (
                      myGrid
                        .cellById(
                          markers[u].rowId,
                          myGrid.getColIndexById("instPlanYn"),
                        )
                        .getValue() == "Y"
                    ) {
                    }
                    if (
                      myGrid
                        .cellById(
                          markers[u].rowId,
                          myGrid.getColIndexById("mobileTranYn"),
                        )
                        .getValue() == "Y"
                    ) {
                      if (
                        myGrid
                          .cellById(
                            markers[u].rowId,
                            myGrid.getColIndexById("soStatCd"),
                          )
                          .getValue() == "9999"
                      ) {
                        toastr.error("이미 배송 완료입니다.(지도 마커 클릭)");
                      } else if (
                        myGrid
                          .cellById(
                            markers[u].rowId,
                            myGrid.getColIndexById("soStatCd"),
                          )
                          .getValue() == "8000"
                      ) {
                        toastr.error(
                          "이미 배송 완료 (미마감)입니다.(지도 마커 클릭)",
                        );
                      } else {
                        toastr.error(
                          "이미 모바일 전송 완료입니다.(지도 마커 클릭)",
                        );
                      }
                      myGrid.selectRowById(markers[u].rowId);
                      return;
                    }
                    markers[u].clickYn = "Y";
                    markers[u].setImage(markers[u].clickImage);
                    myGrid
                      .cellById(markers[u].rowId, myGrid.getColIndexById("chk"))
                      .setValue("1");
                    myGrid.selectRowById(markers[u].rowId);
                    myGrid2.addRow(markers[u].rowId, "");
                    myGrid2.setRowData(
                      markers[u].rowId,
                      myGrid.getRowData(markers[u].rowId),
                    );
                    myGrid
                      .cellById(
                        markers[u].rowId,
                        myGrid.getColIndexById("delChk"),
                      )
                      .setValue("0");
                    myGrid
                      .cellById(
                        markers[u].rowId,
                        myGrid.getColIndexById("delChk"),
                      )
                      .setDisabled(true);
                    if (
                      myGrid2
                        .cellById(
                          markers[u].rowId,
                          myGrid2.getColIndexById("instPlanYn"),
                        )
                        .getValue() == "Y"
                    ) {
                      myGrid2
                        .cellById(
                          markers[u].rowId,
                          myGrid2.getColIndexById("saveGubn"),
                        )
                        .setValue("UPDT");
                    } else {
                      myGrid2
                        .cellById(
                          markers[u].rowId,
                          myGrid2.getColIndexById("saveGubn"),
                        )
                        .setValue("INS");
                    }
                    var newPaltNo = 0;
                    myGrid2.forEachRow(function (pId) {
                      if (
                        Number(
                          myGrid2
                            .cellById(
                              pId,
                              myGrid2.getColIndexById("newPaltNoSeq"),
                            )
                            .getValue(),
                        ) > newPaltNo
                      ) {
                        newPaltNo = Number(
                          myGrid2
                            .cellById(
                              pId,
                              myGrid2.getColIndexById("newPaltNoSeq"),
                            )
                            .getValue(),
                        );
                      }
                    });
                    myGrid2
                      .cellById(
                        markers[u].rowId,
                        myGrid2.getColIndexById("newPaltNoSeq"),
                      )
                      .setValue(newPaltNo + 1 + "");
                    myGrid2
                      .cellById(
                        markers[u].rowId,
                        myGrid2.getColIndexById("chk"),
                      )
                      .setValue("");
                    fnObj.searchOpen.grid2CbmSum();
                    fnObj.searchOpen.grid2InstCostSum();
                    fnObj.searchOpen.grid2Distance();
                  } else if (markers[u].clickYn == "Y") {
                    markers[u].clickYn = "N";
                    markers[u].setImage(markers[u].normalImage);
                    myGrid
                      .cellById(markers[u].rowId, myGrid.getColIndexById("chk"))
                      .setValue("0");
                    myGrid.selectRowById(markers[u].rowId);

                    myGrid2.forEachRow(function (pId) {
                      var oldP = Number(
                        myGrid2
                          .cellById(
                            markers[u].rowId,
                            myGrid2.getColIndexById("newPaltNoSeq"),
                          )
                          .getValue(),
                      );
                      var newP = Number(
                        myGrid2
                          .cellById(
                            pId,
                            myGrid2.getColIndexById("newPaltNoSeq"),
                          )
                          .getValue(),
                      );
                      if (newP > oldP) {
                        myGrid2
                          .cellById(
                            pId,
                            myGrid2.getColIndexById("newPaltNoSeq"),
                          )
                          .setValue(newP - 1 + "");
                      }
                    });
                    myGrid2.deleteRow(markers[u].rowId);
                    if (
                      myGrid
                        .cellById(
                          markers[u].rowId,
                          myGrid.getColIndexById("instPlanYn"),
                        )
                        .getValue() != "Y"
                    ) {
                      myGrid
                        .cellById(
                          markers[u].rowId,
                          myGrid.getColIndexById("delChk"),
                        )
                        .setDisabled(true);
                    } else {
                      myGrid
                        .cellById(
                          markers[u].rowId,
                          myGrid.getColIndexById("delChk"),
                        )
                        .setDisabled(false);
                    }

                    fnObj.searchOpen.grid2CbmSum();
                    fnObj.searchOpen.grid2InstCostSum();
                    fnObj.searchOpen.grid2Distance();
                  }
                });
              }
            });
            if (index == 0) {
              map.setCenter(inner.latlng);
            }
            positions.push(inner);
            inner = {};
          } else {
            myGrid
              .cellById(
                addr[index].rowId,
                myGrid.getColIndexById("mapFindAddr"),
              )
              .setValue(result.length + "개");
          }
        }
      });
      checkIndex++;
      if (checkIndex == addr.length) {
        $("#btnSearch").prop("disabled", false);
      }
    }, index * 15);
  });
  if (addr.length == 0) {
    $("#btnSearch").prop("disabled", false);
  }
}

/**
 * 거리 표시 오버레이 생성 함수
 */
function displayCircleDot(position, distance, i) {
  if (distance > 0) {
    distanceOverlay[i] = new daum.maps.CustomOverlay({
      content:
        '<div class="dotOverlay" style="background:white;padding: 5px;background: white;background-color: rgba(255, 255, 255, 0.6); border-radius: 20px;">' +
        '<span class="number" style="font-size: larger; font-weight: bold;">' +
        Math.round(distance / 1000) +
        " km" +
        "</span>" +
        "</div>",
      position: position,
      xAnchor: 1.3,
      yAnchor: 1.3,
      zIndex: 1,
    });
    distanceOverlay[i].setMap(map);
  }
}
