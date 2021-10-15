// query용 json 직렬화 함수
function jsonSerialize(j) {
  var query = '';
  var prop = Object.getOwnPropertyNames(j);

  prop.forEach(k => {
    query += k + '=' + j[k] + '&';
  });
  query = query.slice(0, query.length - 1);
  return query;
}

// 일정 추가 및 수정 배경화면 클릭
function hideTodoModal(el) {
  el.onclick = function() {
    var wrap = document.querySelector('.todo-add-wrap');
    var form = wrap.querySelector('form');
    
    el.classList.remove('opacity05');
    wrap.classList.remove('top50');
    setTimeout(() => {
      el.style.display = 'none';
      form.title.value = '';
      form.color.value = '';
      form.content.value = '';
      form.beginDate.value = '';
      form.dueDate.value = '';
    }, 400);
  };
}

// 일정 추가 버튼 누를 때 모달창 출력
function showTodoAddModal(btn, bg) {
  var wrap = document.querySelector('.todo-add-wrap');
  var form = wrap.querySelector('form');
  var addSubmit = form.querySelector('.todo-add-submit');
  var modifySubmit= form.querySelector('.todo-modify-submit');

  btn.addEventListener('click', () => {
    addSubmit.type = 'submit';
    addSubmit.style.display = 'inline';
    addSubmit.removeAttribute('disabled', 'disabled');
    modifySubmit.type = 'hidden';
    modifySubmit.setAttribute('disabled', 'disabled');
    modifySubmit.removeAttribute('data-no');    
    
    bg.style.display = 'block';
    var colorDisplay = form.querySelector('.todo-add-color');
    colorDisplay.setAttribute('placeholder', '색상');
    colorDisplay.removeAttribute('data-hex');
    colorDisplay.style.backgroundColor = '';

    setTimeout(() => {
      bg.classList.add('opacity05');
      wrap.classList.add('top50');
    }, 10);
  });
}

// 일정 수정 버튼 누를 때 모달창 출력
function showTodoModifyModal(btn, bg, no) {
  var wrap = document.querySelector('.todo-add-wrap');
  var form = wrap.querySelector('form');
  var addSubmit = form.querySelector('.todo-add-submit');
  var modifySubmit= form.querySelector('.todo-modify-submit');
  var xhr = new XMLHttpRequest();
  
  xhr.onreadystatechange = function() {
    if(!(xhr.readyState === 4 && xhr.status === 200)) return;
    var mTodo = JSON.parse(xhr.response);
    form.title.value = mTodo.title;
    form.color.value = mTodo.color;
    form.content.value = mTodo.content;
    form.beginDate.value = mTodo.beginDate;
    form.dueDate.value = mTodo.dueDate;
    
    var todoAddColor = form.querySelector('.todo-add-color');
    todoAddColor.style['background-color'] = mTodo.color;
    todoAddColor.setAttribute('data-hex', mTodo.color);
    todoAddColor.setAttribute('placeholder', '');
  };

  btn.addEventListener('click', () => {
  
    xhr.open('POST', '/todo/minimalTodoInfo?no=' + no, true);
    xhr.send();
  
    modifySubmit.type = 'submit';
    modifySubmit.style.display = 'inline';
    modifySubmit.removeAttribute('disabled', 'disabled');
    modifySubmit.setAttribute('data-no', no);
    addSubmit.type = 'hidden';
    addSubmit.setAttribute('disabled', 'disabled');    
    
    bg.style.display = 'block';
    var colorDisplay = form.querySelector('.todo-add-color');
    colorDisplay.setAttribute('placeholder', '색상');
    colorDisplay.removeAttribute('data-hex');
    colorDisplay.style.backgroundColor = '';

    setTimeout(() => {
      bg.classList.add('opacity05');
      wrap.classList.add('top50');
    }, 10);
  });
}

// 일정 등록 유효성 검사
function todoFormCheck(e) {
  e.preventDefault();
  
  var form = document.querySelector('#todo-add-form'), title = form.title.value,
  content = form.content.value, color = form.querySelector('.todo-add-color').getAttribute('data-hex'),
  no = e.target.getAttribute('data-no'),
  beginDate, dueDate, todayStr, beginStr, today, dateBegin, dateDue;


  beginDate = form.beginDate.value;
  dueDate = form.dueDate.value;
  if(beginDate.trim() === '') {
    alert('시작일을 선택해주세요.');
    return;
  }
  if(dueDate.trim() === '') {
    alert('종료일을 선택해주세요.');
    return;
  }
  
  if(title.trim().length === 0) {
    alert('제목을 입력해주세요.');
    return;
  }
  
  if(color === null) {
    // alert('색상을 선택해주세요.');
    // return;
    // alert 띄우는 것 대신 랜덤한 색상 입력
    var tmpColor = '#';
    for (var i = 0; i < 3; i++) {
      tmpColor += Math.floor(Math.random() * 256).toString(16).padStart(2, '0');
    }
    color = tmpColor;
  }

  if(content.trim().length === 0) {
    if(!confirm('내용 없이 일정을 등록하시겠습니까?')) return;
  }


  todayStr = document.querySelector('div.today a').textContent.split('.');
  beginStr = beginDate.split('-');
  dueStr = dueDate.split('-');
  
  today = new Date();
  today.setFullYear(todayStr[0]);
  today.setMonth(todayStr[1]);
  today.setDate(todayStr[2]);
  
  dateBegin = new Date();
  dateBegin.setFullYear(beginStr[0]);
  dateBegin.setMonth(beginStr[1]);
  dateBegin.setDate(beginStr[2]);
  
  dateDue = new Date();
  dateDue.setFullYear(dueStr[0]);
  dateDue.setMonth(dueStr[1]);
  dateDue.setDate(dueStr[2]);

  if(dateBegin.getTime() > dateDue.getTime()) {
    alert('종료일이 시작일보다 빠를 수 없습니다.');
    return;
  } else if(today.getTime() > dateBegin.getTime()) {
    if(!confirm('오늘 날짜(' + todayStr[0] + '-' + todayStr[1] + '-' + todayStr[2] + ') 보다 등록일(' 
          + beginStr[0] + '-' + beginStr[1] + '-' + beginStr[2] + ')이 빠릅니다. 정말로 이 날짜에 등록하시겠습니까?')) return;
  }

  form.color.value = color.replace('#', 'numSign');
  
  // 이벤트 타겟의 data-no 속성이 없으면 신규 등록  
  if(no === null) {
    form.action = '/todo/addTodo';
  } else {
  // 있으면 수정
    form.action = '/todo/modifyTodo';
    var modInput = document.createElement('input');
    modInput.name = 'no';
    modInput.type = 'hidden';
    modInput.value = no;
    form.append(modInput);
  }
  form.method = 'post'
  form.submit();
}

// 등록된 일정의 색상 클릭 시 color picker modal 출력
function displayColorPickerModal(el) {
  el.addEventListener('click', () => {
    var colorPicker = document.querySelector('.todo-color-picker');

    el.classList.remove('opacity03');
    colorPicker.classList.remove('opacity1');
    setTimeout(() => {
      tcpWrap.style.display = 'none';
      colorPicker.style.display = 'none';
    }, 400);
  });
}

// 등록, 변경에 따라 color picker의 색상에 이벤트 부여
function todoColorClick(el) {
  el.addEventListener('click', () => {
    var classList = el.classList;
    // 색상 변경 시
    var todoNo = el.getAttribute('data-no'); // todoId
    var todoHEX = el.getAttribute('data-hex'); // 색상. rgb(r, b, g) 형식
    var todoRGB = el.style.backgroundColor;
    //    
    var tcpWrap = document.querySelector('.tcp-wrap'); // 모달 배경
    var picker = document.querySelector('.todo-color-picker');
    var color = picker.querySelectorAll('.color');
    var rgb = picker.querySelector('.selected-rgb');
    var hex = picker.querySelector('.selected-hex');
    var btn = picker.querySelector('.color-confirm-change-btn');
    if(classList.contains('todo-add-color')) { // 등록
      btn.value = '선택';
      btn.removeAttribute('data-no');
    } else if(classList.contains('todo-color')) { // 변경
      btn.value = '변경';
      btn.setAttribute('data-no', todoNo);
    }
      
    rgb.value = todoRGB;
    hex.value = todoHEX;

    if(todoRGB && todoHEX) { // 변경의 경우, 기본 값이 존재
      rgb.setAttribute('data-validation', 'true');
      hex.setAttribute('data-validation', 'true');
    } else { // 둘 중 하나라도 기본 값이 없는 경우
      rgb.setAttribute('data-validation', 'false');
      hex.setAttribute('data-validation', 'false');
    }

    color.forEach(c => {
      c.style.backgroundColor = c.getAttribute('data-hex');
      c.addEventListener('click', () => {
        rgb.value = c.style.backgroundColor;
        rgb.setAttribute('data-validation', 'true');
        hex.value = c.getAttribute('data-hex');
        hex.setAttribute('data-validation', 'true');
      });
    });

    tcpWrap.style.display = 'block';
    picker.style.display = 'block';
    setTimeout(() => {
      tcpWrap.classList.add('opacity03');
      picker.classList.add('opacity1');
    }, 10);
  });
}

// RGB색상 유효성 검사
function checkRGBColor(el) {
  var color = el.value;
  var regex = /rgb\(\d{1,3},\s?\d{1,3},\s?\d{1,3}\)/i;
  var hex = el.parentElement.querySelector('.selected-hex');
  var flag = false;
  var rgb;
  var tmpHEX;

  if(regex.test(color)) { // 형식이 맞는 입력 값 중
    tmpHEX = '#ddd';
    rgb = color.replace(/[^0-9,]/g, '').split(',');
    rgb.forEach((v, i) => {
      if(v < 0 || v > 255) { // 숫자 범위가 벗어나면 falg = false
        flag = false;
      }
      tmpHEX = tmpHEX.replace('d', Number(v) < 16 ? '0' + Number(v).toString(16) : Number(v).toString(16));
      if(i === 2) flag = true;
    });
  }

  if(flag) {
    hex.value = tmpHEX;
    hex.setAttribute('data-validation', 'true');
    el.setAttribute('data-validation', 'true');
  } else {
    // rgb, hex 체크 시 alert을 사용하면 크롬에서 루프에 빠짐.
    // alert('RGB 색상 형식을 확인해주세요  예) rgb(255, 128, 10) 숫자는 0 ~ 255');
    hex.value = '';
    hex.setAttribute('data-validation', 'false');
    el.setAttribute('data-validation', 'false');
  }
}

// HEX색상 유효성 검사
function checkHEXColor(el) {
  var color = el.value;
  var regex = /^#[0-9a-f]{6}$/i;
  var rgb = el.parentElement.querySelector('.selected-rgb');
  var tmpRGB;

  if(regex.test(color)) {
    tmpRGB = 'rgb(d, d, d)';
    for(var i = 0; i < 3; i++) {
      tmpRGB = tmpRGB.replace('d', parseInt(color.substring(i * 2 + 1, i * 2 + 3), 16));
    }
    rgb.value = tmpRGB;
    rgb.setAttribute('data-validation', 'true');
    el.setAttribute('data-validation', 'true');
  } else {
    // rgb, hex 체크 시 alert을 사용하면 크롬에서 루프에 빠짐.
    // alert('HEX 색상 형식을 확인해주세요.  예) #f3e3a3');
    rgb.value = '';
    rgb.setAttribute('data-validation', 'false');
    el.setAttribute('data-validation', 'false');
  }
}

// 색상 등록 / 변경 버튼 클릭
function confirmChangeTodoColor(el) {
  el.addEventListener('click', function() {
    var no;
    var color = el.parentElement.querySelector('.selected-hex').value;
    var colorValidation = el.parentElement.querySelector('.selected-hex').getAttribute('data-validation');
  
    if(colorValidation === 'false') {
      alert('유효한 색상 값을 입력해주세요.');
      return;
    }

    if(no = el.getAttribute('data-no')) { // 있으면 색상 변경
      var form = document.createElement('form');
      var inputColor = document.createElement('input');
      var inputNo = document.createElement('input');
      var inputDate = document.createElement('input');
      
      form.action = '/todo/changeTodoColor';
      form.method = 'post';
      form.style.display = 'none';
      
      inputColor.name = 'color';
      inputColor.value = color.replace('#', 'numSign');
      
      inputNo.name = 'no';
      inputNo.value = no;
      
      inputDate.name = 'date';
      inputDate.value = document.querySelector('.target-date-span').textContent;
      
      form.append(inputColor, inputNo, inputDate);
      el.append(form);
      form.submit();

    } else { // 없으면 색상 등록
      var colorDisplay = document.querySelector('.todo-add-color');
      var colorInput = document.querySelector('input[name="color"]');
      var tcpWrap = document.querySelector('.tcp-wrap');
      colorDisplay.style.backgroundColor = color;
      colorDisplay.setAttribute('data-hex', color);
      colorDisplay.removeAttribute('placeholder');
      colorInput.value = color;
      tcpWrap.click();
    }
  });  
  
}

// small calendar 표시. return xhr을 selectedSmallYearMonth()에서 사용
function smallCalendarAjax(el) {
  var elType = el.classList.contains('small-calendar-main') ? 1 : el.classList.contains('small-calendar-right') ? 0 : -1;
  var xhr = new XMLHttpRequest();
  var todoDateInput;

  if(elType === 1) {
    smallCalendarAjaxMain(el, elType, xhr);
  } else if(elType === 0) {
    todoDateInput = document.querySelectorAll('input[class^="todo-date-input-"]');
    smallCalendarAjaxRight(el, elType, todoDateInput, xhr);
  }
  
  return xhr;
}

// smallCalendarAjax에서 main의 calendar
function smallCalendarAjaxMain(el, elType, xhr) {
  var url, year, month, day, param, sYMS;

  if(el.style.display === 'none' || el.style.display === '') {

    url = '/calendar/scal';
    year = document.querySelector('#current-year').value;
    month = document.querySelector('#current-month').value;
    day = document.querySelector('#current-day').value;
    param = '?date=' + year + '-' + month + '-' + day;

    xhr.open('POST', url + param, true); // method, path, asynchronous
    xhr.setRequestHeader('Content-Type', 'application.x-www-form-urlencoded');
    xhr.send(); // send(null) 말고 다른 방법 생각
    el.style.display = 'block';
  } else {
    el.style.display = 'none';
    sYMS = el.querySelector('.s-year-month-select');
    if(sYMS.style.display === 'block') sYMS.style.display = 'none';
  }
  smallCalendarAjaxXhr(el, elType, xhr);
}

//smallCalendarAjax 에서 right의 calendar
function smallCalendarAjaxRight(el, elType, todoDateInput, xhr) {
  todoDateInput.forEach(function(tdi) {
    tdi.addEventListener('click', function() {
      var url, date, year, month, day, param, sYMS, elDateType, inputDateType;
      
      elDateType = el.getAttribute('data-date-type');
      inputDateType = tdi.getAttribute('data-date-type');
      // 날짜 선택 input 클릭 시 smallCalendar의 data-date-type이 없으면 input의 data-date-type 으로 설정
      if(elDateType === null) {
        el.setAttribute('data-date-type', inputDateType);
        elDateType = inputDateType;        
      }
      

      // 타입이 다를 때 -> 다른 곳에서 클릭 했으므로 xhr통신 함
      // 타입이 같을 때 -> 1. display 봐서 none이거나 공백이면 통신 함 2. none 아니면 통신 안 함
      if(elDateType !== inputDateType) {
        el.setAttribute('data-date-type', inputDateType);
        innerScopeAjax(el);
      } else if (el.style.display === 'none' || el.style.display === '') {
        innerScopeAjax(el);
      } else {
        el.style.display = 'none';
        sYMS = el.querySelector('.s-year-month-select');
        if(sYMS.style.display === 'block') sYMS.style.display = 'none';
      }
      
      function innerScopeAjax(el) {
        url = '/calendar/scal';
        if(tdi.value === '') {
          date = document.querySelector('.target-date-span').textContent.split('-'); // 년.월.일
        } else {
          date = tdi.value.split('-'); // 년-월-일
        }
        year = Number(date[0]);
        month = Number(date[1]);
        day = date[2] === undefined ? 1 : Number(date[2]);
        param = '?date=' + year + '-' + month + '-' + day;

        xhr.open('POST', url + param, true); // method, path, asynchronous
        xhr.setRequestHeader('Content-Type', 'application.x-www-form-urlencoded');
        xhr.send(); // send(null) 말고 다른 방법 생각

        el.style.top = tdi.offsetTop + tdi.offsetHeight + 5 + 'px';
        el.style.left = tdi.offsetLeft + 'px';
        el.style.display = 'block';
      }

      // 비동기 통신 xhr 설정
      smallCalendarAjaxXhr(el, elType, xhr, tdi);
    });
  });
}

// smallCalendarAjax...에서 xhr 처리
function smallCalendarAjaxXhr(el, elType, xhr, tdi) {
  xhr.onreadystatechange = () => {
    var result, sYear, sMonth, smallDaysDay, status, sConfirm, sCancel;

    if(!(xhr.readyState === 4 && xhr.status === 200)) return;
    result = JSON.parse(xhr.response);

    // 년, 월 textContent 추가      
    sYear = el.querySelector('.s-year');
    sMonth = el.querySelector('.s-month');
    sYear.textContent = result.year;
    sMonth.textContent = result.month;
    smallDaysDay = el.querySelectorAll('.s-days-day');
    status = result.status;

    result.days.forEach((days, row) => {
      var sDD, className;

      sDD = smallDaysDay[row].querySelectorAll('.s-d-d');
      days.forEach((day, col) => {
        // 이전 달, 이번 달, 다음 달에 따라 부여
        className = 's-d-d'
        if(status[row][col] === 0) {
          if(result.day == day) className = 's-d-d s-d-d-selected';
          sDD[col].onclick = function() {
            if(elType === 1) {
              var selected = el.querySelector('.s-d-d-selected');
              if(selected !== null) selected.classList.remove('s-d-d-selected');
              sDD[col].classList.add('s-d-d-selected');
            } else if(elType === 0) {
              tdi.value= result.year + '-' + result.month + '-' + day;
              document.querySelector('.small-calendar-right').style.display = 'none';
            }
          }
        } else if(status[row][col] === -1) {
          className = 's-d-d s-d-d-prev';
          sDD[col].onclick = function() {
            if(elType === 1) {
              var url = '/calendar/scal';
              var param = '?date=' + result.prevYear + '-' + result.prevMonth + '-' + day;
              xhr.open('GET', url + param, true);
              xhr.send();
            } else if(elType === 0) {
              tdi.value= result.prevYear + '-' + result.prevMonth + '-' + day;
              document.querySelector('.small-calendar-right').style.display = 'none';
            }
          }
        } else if(status[row][col] === 1) {
          className = 's-d-d s-d-d-next';
          sDD[col].onclick = function() {
            if(elType === 1) {
              var url = '/calendar/scal';
              var param = '?date=' + result.nextYear+ '-' + result.nextMonth + '-' + day;
              xhr.open('GET', url + param, true);
              xhr.send();
            } else if(elType === 0) {
              tdi.value= result.nextYear + '-' + result.nextMonth + '-' + day;
              document.querySelector('.small-calendar-right').style.display = 'none';
            }
          }
        }
        sDD[col].className = className;
        sDD[col].textContent = day;
      });

      if(elType === 1) {
        // 버튼에 이벤트 추가
        sConfirm = el.querySelector('.s-d-confirm');
        sCancel = el.querySelector('.s-d-cancel');
        sConfirm.onclick = function() {
          var el, sYear, sMonth, sDay, url, param;

          el = document.querySelector('.small-calendar-main');
          sYear = el.querySelector('.s-year');
          sMonth = el.querySelector('.s-month');
          sDay = el.querySelector('.s-d-d-selected');
          url = '/calendar/cal';
          param = '?date=' + sYear.textContent + '-' + sMonth.textContent + '-' + sDay.textContent;
          location.href = url + param;
        }
        sCancel.onclick = function() {
          document.querySelector('.small-calendar-main').style.display = 'none';
        }
      }
    });
  }
}

// 년. 월을 클릭 할 때 처리 smallCalendarAjax의 return xhr을 인자로 받음
function selectedSmallYearMonth(el, xhr) {
  var daySelected, year, month, day,
      sYearMonth = el.querySelector('.s-year-month');

  sYearMonth.addEventListener('click', function(e) {
    var sYear = el.querySelector('.s-year'),
        sMonth = el.querySelector('.s-month'),
        sYMS = el.querySelector('.s-year-month-select'),
        daySelected = el.querySelector('.s-d-d-selected'),
        year = el.querySelector('.s-year').textContent,
        month = el.querySelector('.s-month').textContent,
        day = daySelected === null ? '1' : daySelected.textContent,
        sYSelect, sYS, sScrollTop, sMSelect, sMs, mScrollTop, li, url, param;

    if(sYMS.style.display === 'none' || sYMS.style.display === '') { // 년, 월이 표시 되지 않는 경우, display = 'block'으로 한다.
      sYSelect = el.querySelector('.s-y-select');
      sYS = document.createElement('ul');
      sYS.className = 's-y-s';
      
      for(var i = 1970; i <= 2100; i++) {
        li = document.createElement('li');
        li.textContent = i;
        if(i == year) {
          li.className = 's-year-selected';
          sScrollTop = (i - 1970) * 30; 
        }
        li.addEventListener('click', function() {
          var selected = el.querySelector('.s-year-selected');
          if(selected !== null) {
            selected.classList.remove('s-year-selected');
          }
          this.classList.add('s-year-selected');
          sYear.textContent = this.textContent;
        });
        sYS.append(li);
      }
      sYSelect.innerHTML = '';
      sYSelect.append(sYS);

      sMSelect = el.querySelector('.s-m-select');
      sMS = document.createElement('ul');
      sMS.className = 's-m-s';
      for(var i = 1; i <= 12; i++) {
        li = document.createElement('li');
        li.textContent = i;
        if(i == month) {
          li.className = 's-month-selected';
          mScrollTop = (i - 1) * 30;
        }
        li.addEventListener('click', function() {
          var selected = el.querySelector('.s-month-selected');

          if(selected !== null) {
            selected.classList.remove('s-month-selected');
          }
          this.classList.add('s-month-selected');
          sMonth.textContent = this.textContent;
        });
        sMS.append(li);
      }
      sMSelect.innerHTML = '';
      sMSelect.append(sMS);

      sYMS.style.display = 'block';
      sYSelect.scrollTop = sScrollTop;
      sMSelect.scrollTop = mScrollTop;
    }  else { // 년, 월이 표시 되고 있는 경우, display = 'none'으로 한다

      // 선택 된 year, month값
      year = el.querySelector('.s-year-selected').textContent;
      month = el.querySelector('.s-month-selected').textContent;

      // wrap 안 보이게
      sYMS.style.display = 'none';

      // s-cal을 선택된 year, month, day에 맞게 표시 할 ajax
      url = '/calendar/scal';
      param = '?date=' + year + '-' + month + '-' + day;
      xhr.open('POST', url + param, true); // method, path, asynchronous
      xhr.setRequestHeader('Content-Type', 'application.x-www-form-urlencoded');
      xhr.send(); // send(null) 말고 다른 방법 생각
    }
  });
}

// 일정 bar를 클릭하면 마우스 위치에 따라 내용 표시
function displayTodoBrief(el, e) {  
  var no = el.getAttribute('data-no');
  var brief = document.querySelector('#bar-brief-' + no);  

  brief.style.left = (e.offsetX + 7) + 'px';
  brief.style.top = (e.offsetY + 7) + 'px';
  el.append(brief);
  brief.style.display = 'flex';
  brief.classList.remove('opacity1');
  setTimeout(() => {
    brief.classList.add('opacity1');
  }, 17);
}

// mainRight에서 일정, 검색 클릭 시 전환을 위한 함수
function optionClick(el) {

  var oDisplayed = document.querySelector('#option-displayed');
  var oText = oDisplayed.textContent;
  var cDate= document.querySelector('#content-target-date');
  var cSearch= document.querySelector('#content-todo-search');

  oDisplayed.textContent = oText === '일정' ? '검색' : '일정';
  
  switch (oText) {
    case '일정':
      oDisplayed.textContent = '검색';
      cDate.className = 'content-option-hidden';
      cSearch.className = 'content-option-displayed';
      setTimeout(() => { }, 200);
      break;
      
    case '검색':
    default:
      oDisplayed.textContent = '일정';
      cDate.className = 'content-option-displayed';
      cSearch.className = 'content-option-hidden';
      setTimeout(() => { }, 200);  
  }
}

// 일정 검색 시 종료된 일정 감추기/보이기를 전환할 checkbox 이벤트
function todoIgnoreCheck(el) {
  var label = document.querySelector('#label-todo-ignore');
  var todoSearchBtn = document.querySelector('#todo-search-btn');
  if(el.checked) {
    label.textContent = '종료된 일정 감추기';
    todoSearchBtn.setAttribute('onclick', "searchTodoWithOption(this, 'none', 'asc', 'on');");
  } else {
    label.textContent = '종료된 일정 보이기';
    todoSearchBtn.setAttribute('onclick', "searchTodoWithOption(this, 'none', 'asc', 'off');");
  }
}

// 일정 검색 버튼 및 정렬을 위한 함수
function searchTodoWithOption(el, target, sort, ignore) {
  /*
    el : 클릭되는 element, onclick 속성을 바꾸기 위해 this로 받는다.
    target : 유형 - 검색/ 제목/ 시작일/ 종료일/ D-DAY
    sort : asc, desc
    ignore : 정렬 시 종료된 일정 보이기/ 감추기 여부
  */  
  var form = document.querySelector('#form-todo-search');
  var xhr = new XMLHttpRequest();
  var json4Add = {
    'include': form.include.value,
    'ignore': ignore,
    'firstDate': form.firstDate.value,
    'lastDate': form.lastDate.value,
    'target': target,
    'sort': sort
  };
  
  var url = '/todo/getTodoWithSearch';
  var query = jsonSerialize(json4Add);

  xhr.onreadystatechange = () => {
    if(!(xhr.status === 200 && xhr.readyState === 4)) return;
    var resultBody = document.querySelector('.result-body');
    resultBody.innerHTML = xhr.response;
    
    if(el.nodeName === 'INPUT') {
      // 검색 버튼일 때
      var resultTh = document.querySelectorAll('div.result-th');
      if(ignore === 'on') {
        resultTh.forEach(function(th) {
          var dataTarget = th.getAttribute('data-target');
          var s = dataTarget === 'dday' ? 'desc' : 'asc';
          var onclick = 'searchTodoWithOption(this, "' + dataTarget + '", "' + s + '", "on");';
          th.setAttribute('data-ignore', ignore);
          th.setAttribute('onclick', onclick);
        });
      } else {      
        resultTh.forEach(function(th) {
          var dataTarget = th.getAttribute('data-target');
          var s = dataTarget === 'dday' ? 'desc' : 'asc';
          var onclick = 'searchTodoWithOption(this, "' + dataTarget + '", "' + s + '", "off");';
          th.setAttribute('data-ignore', ignore);
          th.setAttribute('onclick', onclick);
        });
      }
    } else {
      // 정렬할 때
      var resultTh = document.querySelectorAll('div.result-th');
      var dataTarget = '';
      var s = '';
      var onclick = '';
      resultTh.forEach(function(th) {
        dataTarget = th.getAttribute('data-target');
        s = dataTarget === 'dday' ? 'desc' : 'asc';
        onclick = 'searchTodoWithOption(this, "' + dataTarget + '", "' + s + '", "' + ignore + '");';
        th.setAttribute('onclick', onclick);
      });
      dataTarget = el.getAttribute('data-target');
      s = sort === 'asc' ? 'desc' : 'asc';
      el.setAttribute('onclick', 'searchTodoWithOption(this, "' + dataTarget +'", "' + s + '", "' + ignore + '");');
    }
  }
  xhr.open('post', url + '?' + query, true);
  xhr.setRequestHeader('Accept', 'text/html');
  xhr.send();
}
