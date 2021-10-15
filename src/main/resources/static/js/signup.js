var signupForm = document.querySelector('#signup-form');

let id = signupForm.id;
let pw = signupForm.pw;
let pw2 = signupForm.pw2;
let errId = document.querySelector('#err-id');
let errPw = document.querySelector('#err-pw');
let errPw2 = document.querySelector('#err-pw2');
let idPattern = /^[a-zA-Z][0-9a-zA-Z_]{5,19}$/
let pwPattern = /[a-zA-Z0-9`~!@#$%^&*]{8,19}$/

const xhrOverlapId = new XMLHttpRequest();
xhrOverlapId.onreadystatechange = function(form) {
  if (xhrOverlapId.readyState !== 4 || xhrOverlapId.status !== 200) return;

  if (xhrOverlapId.response === 'true') {
    errId.innerHTML = '이미 사용중인 아이디입니다.';
    errId.style.display = 'block';
    errPw.innerHTML = '';
    errPw.style.display = 'none';
    errPw2.innerHTML = '';
    errPw2.style.display = 'none';
  } else {
    form.submit();
  }
}


signupForm.id.addEventListener('focusout', function() {
  if (!idPattern.test(id.value)) {
    errId.innerHTML = '아이디는 영문자로 시작해야 하며 공백 없이 영문자, 숫자, _가 허용 된 6~20자 이내여야 합니다.';
    errId.style.display = 'block';
  } else {
    errId.innerHTML = '';
    errId.style.display = 'none';
  }
  errPw.innerHTML = '';
  errPw.style.display = 'none';
  errPw2.innerHTML = '';
  errPw2.style.display = 'none';
});


signupForm.addEventListener('submit', function(e) {

  if (!idPattern.test(id.value)) {
    errId.innerHTML = '아이디는 영문자로 시작해야 하며 공백 없이 영문자, 숫자, _가 허용 된 6~20자 이내여야 합니다.';
    errId.style.display = 'block';
    errPw.innerHTML = '';
    errPw.style.display = 'none';
    errPw2.innerHTML = '';
    errPw2.style.display = 'none';
    e.preventDefault();
    id.focus();
    return;
  }

  if (!pwPattern.test(pw.value)) {
    errId.innerHTML = '';
    errId.style.display = 'none';
    errPw.innerHTML = '비밀번호는 8 ~ 20자리이내여야 합니다.';
    errPw.style.display = 'block';
    errPw2.innerHTML = '';
    errPw2.style.display = 'none';
    e.preventDefault();
    pw.focus();
    return;
  }

  if (pw.value !== pw2.value) {
    errId.innerHTML = '';
    errId.style.display = 'none';
    errPw.innerHTML = '';
    errPw.style.display = 'none';
    errPw2.innerHTML = '비밀번호를 확인해주세요.';
    errPw2.style.display = 'block';
    e.preventDefault();
    pw2.focus();
    return;
  }
});
