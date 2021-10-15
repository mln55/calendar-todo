// mycalandar.html
var currYearMonth = document.querySelector('.curr-year-month');
currYearMonth.addEventListener('click', function () {
  var smallCalendarMain = document.querySelector('.small-calendar-main');
  selectedSmallYearMonth(smallCalendarMain, smallCalendarAjax(smallCalendarMain));
});

var dayBar = document.querySelectorAll('.day-bar');
dayBar.forEach(bar => {
  bar.addEventListener('click', function(e) {
    e.stopPropagation();
    displayTodoBrief(this, e);
  })
});

var brief = document.querySelectorAll('div[id^="bar-brief-"]');
brief.forEach(b => {
  b.addEventListener('click', function (e) {
  e.stopPropagation();
  });
  var closeBtn = b.querySelector('.brief-close-btn');
  closeBtn.addEventListener('click', function (e) {
    e.stopPropagation();
    b.classList.remove('opacity1');
    setTimeout(() => {
      b.style.display = 'none';
    }, 200);
  });
});


//window.addEventListener('DOMContentLoaded', function() {
// mainRight.html
var todoDeleteBtn = document.querySelectorAll('.todo-delete-btn');
// 미리 필요한 값을 얻어와 사용자가 값을 변경하더라도 대응할 수 있도록 한다.
var targetDate = document.querySelector('.target-date').textContent;
for (var i = 0; i < todoDeleteBtn.length; i++) {
  const no = todoDeleteBtn[i].getAttribute('data-no');

  todoDeleteBtn[i].addEventListener('click', function() {
    if(!confirm('일정을 삭제하시겠습니까?')) return;

    var dateSplit = targetDate.split('-');
    var data = {
      'no': no,
      'date': targetDate
    };
    var deleteForm = document.createElement('form');

    deleteForm.style.display = 'none';
    deleteForm.action = "/todo/deleteTodo?" + jsonSerialize(data);
    deleteForm.method = 'post';
    this.append(deleteForm);
    deleteForm.submit();
  });
}
