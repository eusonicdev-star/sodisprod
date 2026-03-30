/**
 * 레이아웃 관리 JavaScript 모듈
 * 유진소닉 TMS 시스템의 메인 레이아웃을 관리합니다.
 */

// ===== 전역 변수 선언 =====
let sessionUserId; // 사용자 ID
let sessionUserNm; // 사용자 이름
let sessionCmpyNm; // 회사명
let sessionUserGrntNm; // 사용자 권한명
let sessionUserIP; // 사용자 IP
let menuTreeList; // 메뉴 트리 데이터

// DHTMLX 컴포넌트 전역 변수 (다른 페이지에서 접근 가능하도록 window 객체에 할당)
let myTreeView; // 좌측 메뉴 트리
let myLayout; // 전체 페이지 레이아웃
let myTabbar; // 탭바 컴포넌트

// 세션 타이머 관련 변수
let defaultTime = 360 * 60; // 세션아웃 타임 (분*초)
let startTime = new Date(); // 페이지 접속시의 시간
let varPopup;
let tid; // 타이머 ID

/**
 * 레이아웃 관리 클래스
 */
class LayoutManager {
  constructor() {
    this.init();
  }

  /**
   * 초기화 함수
   */
  init() {
    this.setSessionData();
    this.createLayout();
    this.setupTreeView();
    this.setupTabbar();
    this.loadMenuList();
    this.setupEventListeners();
    this.startSessionTimer();
    this.exposeToGlobal(); // 전역 변수 노출
  }

  /**
   * 세션 데이터 설정
   */
  setSessionData() {
    if (window.sessionData) {
      sessionUserId = window.sessionData.userId || "";
      sessionUserNm = window.sessionData.userNm || "";
      sessionCmpyNm = window.sessionData.cmpyNm || "";
      sessionUserGrntNm = window.sessionData.userGrntNm || "";
      sessionUserIP = window.sessionData.userIp || "";
    }
  }

  /**
   * DHTMLX 레이아웃 생성
   */
  createLayout() {
    // DHTMLX 레이아웃 초기화
    myLayout = new dhtmlXLayoutObject({
      iconset: "font_awesome",
      parent: document.body,
      pattern: "3T",
    });

    // 상단 영역(a) 설정
    myLayout.cells("a").hideHeader();
    myLayout.cells("a").setHeight(40);
    myLayout.cells("a").fixSize(false, true);

    // 상단 메뉴바 HTML 구성
    this.createTopMenuBar();

    // 좌측 영역(b) 설정
    myLayout.cells("b").setText(sessionUserGrntNm);
    myLayout.cells("b").setWidth(240);
    myLayout.cells("b").fixSize(true, false);

    // 메인 영역(c) 설정
    myLayout.cells("c").hideHeader();
    myLayout.cells("c").fixSize(true, false);
    myLayout.cells("c").showInnerScroll();
  }

  /**
   * 상단 메뉴바 생성
   */
  createTopMenuBar() {
    const profileDisplay = document.getElementById("profileDisplay");
    const profileHTML = profileDisplay ? profileDisplay.innerHTML : "";

    const menuBarHTML = `
            <div id="menuInfo">
                <img src="/img/new-logo.png" alt="로고">
                <a href="#" class="menu-action" onclick="layoutManager.menuOpen();">
                    <i class="fa fa-plus" aria-hidden="true"></i><span>메뉴 열기</span>
                </a>
                <a href="#" class="menu-action" onclick="layoutManager.menuClose();">
                    <i class="fa fa-minus" aria-hidden="true"></i><span>메뉴 닫기</span>
                </a>
                <a href="#" onclick="layoutManager.clearAllTabs();">
                    <i class="fa fa-times fa-1x ml-2" aria-hidden="true"></i><span>모든 탭 닫기</span>
                </a>
                <span id="userInfo" style="font-size:12px;margin-left:10px;"></span>
                <div class="pull-right">
                    ${profileHTML}
                    <a href="#" onclick="layoutManager.btnLogOut();">
                        <i class="fa fa-sign-out fa-1x" aria-hidden="true"></i><span>로그아웃</span>
                    </a>
                    <a href="#" onclick="layoutManager.showMyInfo();">
                        <i class="fa fa-user fa-1x" aria-hidden="true"></i><span>내 정보</span>
                    </a>
                    <a href="#">
                        <i class="fa fa-history fa-1x" style="color:black;"></i>
                        <span id="m" style="font-size:1.1em;font-weight:lighter;" onclick="layoutManager.sessionTimePlus();">000:00</span>
                    </a>
                </div>
            </div>
        `;

    myLayout.cells("a").attachHTMLString(menuBarHTML);
  }

  /**
   * 트리뷰 설정
   */
  setupTreeView() {
    myTreeView = myLayout.cells("b").attachTreeView({
      skin: "material",
      iconset: "font_awesome",
      multiselect: false,
      checkboxes: false,
      dnd: false,
      context_menu: false,
      onload: function () {
        // 트리뷰 로드 완료 시 실행
      },
    });

    myTreeView.setSizes();

    // 트리뷰 클릭 이벤트
    myTreeView.attachEvent("onClick", (id) => {
      this.sessionTimePlus();
      this.handleTreeItemClick(id);
    });
  }

  /**
   * 탭바 설정
   */
  setupTabbar() {
    myTabbar = myLayout.cells("c").attachTabbar({
      parent: "tabbarObj",
      skin: "dhx_Material",
      mode: "top",
      align: "left",
      close_button: true,
      content_zone: true,
      iconset: "font_awesome",
      onload: function () {
        // 탭바 로드 완료 시 실행
      },
      arrows_mode: "auto",
    });

    // 기본 환영 탭 추가
    this.addWelcomeTab();
  }

  /**
   * 환영 탭 추가
   */
  addWelcomeTab() {
    myTabbar.addTab("welcome", "환영합니다");
    myTabbar.tabs("welcome").attachHTMLString(`
            <div class="welcome-content">
                <h2>얼라이언스 시스템에 오신 것을 환영합니다</h2>
                <p>좌측 메뉴에서 원하시는 기능을 선택해주세요.</p>
            </div>
        `);
    myTabbar.tabs("welcome").setActive();
  }

  /**
   * 메뉴 리스트 로드
   */
  loadMenuList() {
    const cmpyCd = window.sessionData?.cmpyCd || "";
    const userGrntCd = window.sessionData?.userGrntCd || "";

    if (!cmpyCd || !userGrntCd) {
      console.warn("회사코드 또는 권한코드가 없습니다.");
      return;
    }

    const menuListForm = new FormData();
    menuListForm.append("cmpyCd", cmpyCd);
    menuListForm.append("sysCd", "ERP");
    menuListForm.append("userGrntCd", userGrntCd);

    $.ajax({
      url: "/menuList",
      processData: false,
      contentType: false,
      data: menuListForm,
      type: "POST",
      datatype: "json",
      async: true,
      success: (res) => {
        if (!res || res.length === 0) {
          console.warn("서버에서 받은 메뉴 데이터가 없습니다.");
          return;
        }

        menuTreeList = res;
        this.buildMenuTree(res);
        this.menuOpen();
      },
      error: (xhr, status, error) => {
        console.error("메뉴 로드 실패:", error);
      },
    });
  }

  /**
   * 메뉴 트리 구성
   */
  buildMenuTree(menuData) {
    let parentIdMap = {};

    menuData.forEach((menu, index) => {
      let parentId = parentIdMap[menu.menuLvl - 1] || "";
      myTreeView.addItem(
        menu.tblMenuMId,
        menu[`menuNm${menu.menuLvl}`],
        parentId,
        index + 1
      );

      if (menu.url) {
        myTreeView.setUserData(menu.tblMenuMId, "url", menu.url);
      }

      parentIdMap[menu.menuLvl] = menu.tblMenuMId;
    });
  }

  /**
   * 이벤트 리스너 설정
   */
  setupEventListeners() {
    // 페이지 로드 완료 시 로더 숨기기
    $(document).ready(() => {
      $("#loader").fadeOut();
    });
  }

  /**
   * 전역 변수 노출 (다른 페이지와의 호환성을 위해)
   */
  exposeToGlobal() {
    // DHTMLX 컴포넌트들을 전역 변수로 노출
    window.myLayout = myLayout;
    window.myTreeView = myTreeView;
    window.myTabbar = myTabbar;

    // 세션 변수들도 노출
    window.sessionUserId = sessionUserId;
    window.sessionUserNm = sessionUserNm;
    window.sessionCmpyNm = sessionCmpyNm;
    window.sessionUserGrntNm = sessionUserGrntNm;
    window.sessionUserIP = sessionUserIP;
    window.menuTreeList = menuTreeList;

    // top 프레임에도 노출 (iframe 내에서 접근할 수 있도록)
    if (typeof top !== "undefined" && top && top !== window) {
      try {
        top.myLayout = myLayout;
        top.myTreeView = myTreeView;
        top.myTabbar = myTabbar;
        top.sessionUserId = sessionUserId;
        top.sessionUserNm = sessionUserNm;
        top.sessionCmpyNm = sessionCmpyNm;
        top.sessionUserGrntNm = sessionUserGrntNm;
        top.sessionUserIP = sessionUserIP;
        top.menuTreeList = menuTreeList;
      } catch (error) {
        console.warn("top 프레임에 변수 노출 실패 (동일 출처 정책):", error);
      }
    }

    // 레이아웃 매니저 인스턴스도 전역으로 노출
    window.layoutManager = this;
    if (typeof top !== "undefined" && top && top !== window) {
      try {
        top.layoutManager = this;
      } catch (error) {
        console.warn("top 프레임에 layoutManager 노출 실패:", error);
      }
    }
  }

  /**
   * 세션 타이머 시작
   */
  startSessionTimer() {
    tid = setInterval(() => this.updateSessionTimer(), 1000);
  }

  /**
   * 세션 타이머 업데이트
   */
  updateSessionTimer() {
    const endTime = new Date();
    const gapTime = parseInt(Math.floor(endTime - startTime) / 1000);
    const sTime = defaultTime - gapTime;

    if (sTime < 0) {
      clearInterval(tid);
      if (varPopup !== undefined) {
        varPopup.close();
      }
    } else {
      const h = Math.floor(sTime / 60)
        .toString()
        .padStart(2, "0");
      const mi = (sTime % 60).toString().padStart(2, "0");
      const timeString = h + ":" + mi;

      const $timer = $("#m");
      $timer.html(timeString);

      // 시간에 따른 색상 변경
      if (sTime < 60) {
        $timer.css("color", "red");
      } else if (sTime < 180) {
        $timer.css("color", "blue");
      } else {
        $timer.css("color", "#8C8C8C");
      }
    }
  }

  /**
   * 트리 아이템 클릭 처리
   */
  handleTreeItemClick(id) {
    const ids = myTabbar.getAllTabs();

    if (ids.length >= 15) {
      alert("더 이상 창을 열 수 없습니다. (최대 15개)");
      return;
    }

    // 이미 열린 탭인지 확인
    for (let i = 0; i < ids.length; i++) {
      if (ids[i] === id.toString()) {
        myTabbar.setTabInActive();
        myTabbar.tabs(id.toString()).setActive();
        return;
      }
    }

    // 새 탭 열기
    if (myTreeView.getUserData(id, "url")) {
      myTabbar.setTabInActive();
      myTabbar.addTab(id.toString(), myTreeView.getItemText(id));
      myTabbar.tabs(id.toString()).attachURL(myTreeView.getUserData(id, "url"));
      myTabbar.tabs(id.toString()).setActive();
    }
  }

  /**
   * 메뉴 열기
   */
  menuOpen() {
    if (!menuTreeList || menuTreeList.length === 0) {
      return;
    }

    menuTreeList.forEach((menu) => {
      if (menu && menu.tblMenuMId) {
        myTreeView.openItem(menu.tblMenuMId);
      }
    });
  }

  /**
   * 메뉴 닫기
   */
  menuClose() {
    if (!menuTreeList || menuTreeList.length === 0) {
      return;
    }

    menuTreeList.forEach((menu) => {
      if (menu && menu.tblMenuMId) {
        myTreeView.closeItem(menu.tblMenuMId);
      }
    });
  }

  /**
   * 모든 탭 닫기
   */
  clearAllTabs() {
    myTabbar.clearAll();
    this.addWelcomeTab();
  }

  /**
   * 로그아웃 처리
   */
  btnLogOut() {
    const resultConfirm = confirm("로그아웃 하시겠습니까?");
    if (resultConfirm) {
      top.location.href = "./logout";
    }
  }

  /**
   * 세션 시간 갱신
   */
  sessionTimePlus() {
    $.ajax({
      url: "/sessionTimePlus",
      method: "post",
      success: () => {
        startTime = new Date();
      },
      error: (xhr, status, error) => {
        console.error("세션 갱신 실패:", error);
      },
    });
  }

  /**
   * 내 정보 표시
   */
  showMyInfo() {
    const ids = myTabbar.getAllTabs();

    for (let i = 0; i < ids.length; i++) {
      if (ids[i] === "myInfo") {
        myTabbar.setTabInActive();
        myTabbar.tabs("myInfo").setActive();
        return;
      }
    }

    myTabbar.setTabInActive();
    myTabbar.addTab("myInfo", "내 정보");
    myTabbar.tabs("myInfo").attachURL("/myInfo");
    myTabbar.tabs("myInfo").setActive();
  }
}

// ===== 전역 인스턴스 생성 =====
let layoutManager;

// DOM 로드 완료 시 레이아웃 매니저 초기화
$(document).ready(() => {
  layoutManager = new LayoutManager();
});

// 전역 함수들 (기존 코드와의 호환성을 위해)
window.menuOpen = () => layoutManager.menuOpen();
window.menuClose = () => layoutManager.menuClose();
window.btnLogOut = () => layoutManager.btnLogOut();
window.showMyInfo = () => layoutManager.showMyInfo();
window.sessionTimePlus = () => layoutManager.sessionTimePlus();

// 추가 호환성 함수들
window.doOnLoad = () => {
  // 기존 코드와의 호환성을 위한 빈 함수
  console.log(
    "doOnLoad 함수가 호출되었습니다. 레이아웃 매니저가 이미 초기화되었습니다."
  );
};

// 레이아웃 관련 전역 함수들
window.menuList = (cmpyCd, sysCd, userGrntCd) => {
  if (layoutManager) {
    // 기존 코드와의 호환성을 위한 함수
    console.log(
      "menuList 함수가 호출되었습니다. 레이아웃 매니저가 이미 초기화되었습니다."
    );
  }
};

// 전역 변수들도 노출 (기존 코드 호환성)
window.myWins = null; // 기존 코드에서 사용하는 변수
window.myGrid = null; // 기존 코드에서 사용하는 변수
window.myCalendar = null; // 기존 코드에서 사용하는 변수
