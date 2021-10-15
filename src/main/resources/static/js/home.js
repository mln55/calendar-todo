var loginForm = document.querySelector('#login-form');
if (loginForm) {
  loginForm.addEventListener('submit', function(e) {
    var id = loginForm.id.value;
    var pw = loginForm.pw.value;

    errId = document.querySelector('#err-empty-id');
    errPw = document.querySelector('#err-empty-pw');
    loginFail = document.querySelector('#login-fail');

    if(id === '') {
      errId.innerHTML = '아이디를 입력해주세요';
      errId.style.display = 'block';
      errPw.innerHTML = '';
      errPw.style.display = 'none';
      loginFail.style.display = 'none';
      e.preventDefault();
      return;
    }

    if(pw === '') {
      errId.innerHTML = '';
      errId.style.display = 'none';
      errPw.innerHTML = '비밀번호를 입력해주세요';
      errPw.style.display = 'block';
      loginFail.style.display = 'none';
      e.preventDefault();
      return;
    }
  });
}
