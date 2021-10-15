// mainRight.jsp
// 일정 입력 모달
var bg = document.querySelector('.todo-add-bg');
if(bg) {
  hideTodoModal(bg);
  var todoAddBtn = document.querySelector('.todo-add-btn');
  showTodoAddModal(todoAddBtn, bg);
  
  var todoModifyBtn = document.querySelectorAll('.todo-modify-btn');
  for(var i = 0; i < todoModifyBtn.length; i++) {
    const no = todoModifyBtn[i].getAttribute('data-no');
    
    showTodoModifyModal(todoModifyBtn[i], bg, todoModifyBtn[i].getAttribute('data-no'));
  }
}

var rgb = document.querySelector('.selected-rgb');
if(rgb) {
  rgb.addEventListener('blur', function () {
    checkRGBColor(this);
  });
}

var hex = document.querySelector('.selected-hex');
if(hex) {
  hex.addEventListener('blur', function () {
    checkHEXColor(this);
  });
}

var smallCalendarRight = document.querySelector('.small-calendar-right');
if(smallCalendarRight) {
  selectedSmallYearMonth(smallCalendarRight, smallCalendarAjax(smallCalendarRight));
}

var todoAddSubmit = document.querySelector('.todo-add-submit');
if(todoAddSubmit) {
  todoAddSubmit.addEventListener('click', todoFormCheck);
}

var todoModifySubmit = document.querySelector('.todo-modify-submit');
if(todoModifySubmit) {
  todoModifySubmit.addEventListener('click', todoFormCheck);
}

var todoAddCancel = document.querySelector('.todo-add-cancel');
if(todoAddCancel) {
  todoAddCancel.addEventListener('click', () => {
    document.querySelector('.todo-add-bg').click();
  });
}

// 새로운 일정 색상 등록
var colorInput = document.querySelector('.todo-add-color');
if(colorInput) {
  todoColorClick(colorInput);
}

// 등록된 일정의 색상 변경
var todoColors = document.querySelectorAll('.todo-color');
if(todoColors) {
  todoColors.forEach(tc => {
    todoColorClick(tc)
  });
}


// 색상 모달 제거
var tcpWrap = document.querySelector('.tcp-wrap');
if(tcpWrap) {
  displayColorPickerModal(tcpWrap);
}


// 새로운 일정 색상 등록 및 등록된 일정 색상 변경
var colorConfirmChangeBtn = document.querySelector('.color-confirm-change-btn');
if(colorConfirmChangeBtn) {
  confirmChangeTodoColor(colorConfirmChangeBtn);
}
