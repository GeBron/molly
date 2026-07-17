function checkLogin(callback) {
  ajaxRequest({
    path: '/auth/info',
    type: 'GET'
  }).done(function (res) {
    if (res && res.code === 200 && res.data) {
      setUserInfo(res.data);
      if (typeof callback === 'function') {
        callback(res.data);
      }
    } else {
      clearUserInfo();
      window.location.href = '/login';
    }
  }).fail(function () {
    clearUserInfo();
    window.location.href = '/login';
  });
}

function logout() {
  ajaxRequest({
    path: '/auth/logout',
    type: 'POST'
  }).done(function () {
    clearUserInfo();
    window.location.href = '/login';
  });
}

$(function () {
  const loginForm = $('#login-form');
  if (loginForm.length > 0) {
    loginForm.on('submit', function (e) {
      e.preventDefault();
      const username = $('#username').val().trim();
      const password = $('#password').val();
      const btn = loginForm.find('button[type="submit"]');
      if (!username || !password) {
        showToast('请输入用户名和密码', 'error');
        return;
      }
      setButtonLoading(btn, true);
      ajaxRequest({
        path: '/auth/login',
        type: 'POST',
        data: { username: username, password: password }
      }).done(function (res) {
        if (res && res.code === 200 && res.data) {
          setUserInfo(res.data);
          showToast('登录成功', 'success');
          setTimeout(function () {
            window.location.href = '/dashboard';
          }, 500);
        }
      }).always(function () {
        setButtonLoading(btn, false);
      });
    });
  }
});
